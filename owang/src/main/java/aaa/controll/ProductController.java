package aaa.controll;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import aaa.model.MCompanyDTO;
import aaa.model.PaymentDTO;
import aaa.model.PaymentResponseMember.Payment;
import aaa.model.ProductDTO;
import aaa.model.SoloDTO;
import aaa.service.MCompanyMapper;
import aaa.service.PayMapper;
import aaa.service.PayService;
import aaa.service.ProductMapper;
import aaa.service.RecruitMapper;
import aaa.service.ReviewMapper;
import aaa.service.SoloMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("product")
public class ProductController {

	@Resource
	SoloMapper sm;
	@Resource
	ReviewMapper rvm;
	@Resource
	MCompanyMapper mcm;
	@Resource
	ProductMapper pm;

	// 상품안내
	@RequestMapping("/intro")
	String product_intro() {
		return "product/product_intro";
	}
	
	// 상품신청
	@RequestMapping("/notice")
	String product_notice(Model mm, HttpSession session) {
		String alertMsg = "로그인이 필요합니다";
		String goUrl = "/login/main";

		// 개인회원
		if (session.getAttribute("sid") != null) {
			String sid = (String) session.getAttribute("sid");
			SoloDTO soloinfo = sm.detailSolo(sid);
			// sid가 작성한 리뷰개수가 0보다 크면
			if (rvm.ccnt(sid) > 0) {
				alertMsg = "프리미엄회원은 결제가 불필요합니다";
				goUrl = "/product/notice";
			} else {
				String pay = "solo";
				mm.addAttribute("pay", pay);
			}
			session.setAttribute("solosession", soloinfo);
		}

		// 기업회원
		if (session.getAttribute("cid") != null) {
			String cid = (String) session.getAttribute("cid");
			MCompanyDTO compinfo = mcm.deatilCompany(cid);
			// cid가 인증받았다면
			if (compinfo.isCapproval()) {
				String pay = "comp";
				mm.addAttribute("pay", pay);
			} else {
				alertMsg = "기업인증이 필요합니다";
				goUrl = "/product/notice";
			}
			session.setAttribute("companysession", compinfo);
		}
		// 전체 상품 목록
		List<ProductDTO> data = pm.list();
		for (ProductDTO payment : data) {
			payment.setFormatPrice(String.format("%,d", payment.getProductPrice())+"원");
		}

		mm.addAttribute("msg", alertMsg);
		mm.addAttribute("goUrl", goUrl);
		mm.addAttribute("data", data);
		return "product/product_notice";
	}

	// 상품 결제 폼
	@RequestMapping("/{productId}")
	String product_form(@PathVariable String productId, Model mm, HttpSession session) {
		// 사용자정보 채워주기
		String name = null, tel = null, email = null;
		Date today = new Date();
		if (session.getAttribute("sid") != null) {
			// 세션에서 id만 가져와서 db에서 정보 불러오기
			String sid = (String) session.getAttribute("sid");
			SoloDTO soloinfo = sm.detailSolo(sid);
			name = soloinfo.getSname();
			tel = soloinfo.getSphone();
			email = soloinfo.getSemail();
			Date sdate = soloinfo.getSdate();
			// 유효기간이 오늘이전이 아닌 경우 보내줌
			if (sdate != null && !sdate.before(today)) {
				mm.addAttribute("date", sdate);
			}
			session.setAttribute("solosession", soloinfo);
		} else if (session.getAttribute("cid") != null) {
			String cid = (String) session.getAttribute("cid");
			MCompanyDTO compinfo = mcm.deatilCompany(cid);
			name = compinfo.getCname();
			tel = compinfo.getCcall();
			email = compinfo.getCemail();
			Date cdate = compinfo.getCdate();
			if (cdate != null && !cdate.before(today)) {
				mm.addAttribute("date", cdate);
			}
			session.setAttribute("companysession", compinfo);
		} else {
			String errorMsg = "세션정보가 없습니다. 로그인 후 다시 시도해주세요";
			mm.addAttribute("errorMsg", errorMsg);
		}
		// 상품정보 채워주기
		ProductDTO pdto = pm.detail(productId);
		pdto.setFormatPrice(String.format("%,d", pdto.getProductPrice())+"원");

		mm.addAttribute("name", name);
		mm.addAttribute("tel", tel);
		mm.addAttribute("email", email);

		mm.addAttribute("pdto", pdto);
		return "product/product_form";
	}

	@Resource
	PayMapper paym;
	@Autowired
	PayService payS;
	@Resource
	RecruitMapper rcm;

	// 주문정보 전달받음
	@PostMapping("/complete")
	@ResponseBody
	public int paymentComplete(@RequestBody PaymentDTO payDTO, HttpSession session) {

		// 결제정보 검증(주문정보와 금액 비교)
		// 서비스에서 토큰 가져옴
		String token = payS.getToken();
		// 결제된 금액
		String amount = payS.paymentInfo(payDTO.getImpUid(), token);
		int res = 1;
		// 주문정보 금액과 결제된 금액/1000이 다를 경우 - 0(검증실패)
		if (payDTO.getAmount() / 1000 != Integer.parseInt(amount)) {
			res = 0;
			// 결제 취소
			payS.paymentCancel(token, payDTO.getImpUid(), "결제 금액 오류");
			return res;
		}

		// 결제 회원 정보 변경하기
		// 상품 유효기간 가져오기
		int valid = pm.period(payDTO.getProductId());
		Date today = new Date();
		// 개인회원 세션이 존재할 경우
		if (session.getAttribute("sid") != null) {
			String sid = (String) session.getAttribute("sid");
			SoloDTO soloinfo = sm.detailSolo(sid);
			// 주문정보에 담기
			payDTO.setSid(sid);
			// 회원 유효기간 가져오기
			Date sdate = soloinfo.getSdate();
			// 리뷰작성회원인 경우 - 유효기간 변경 x
			if (sdate == null && soloinfo.getStype() == 2) {
				// 유효기간이 null이거나 오늘 이전인 경우 - 오늘날짜에 유효기간 더함
			} else if (sdate == null || sdate.before(today)) {
				sdate = payS.dateAddValid(today, valid);
				// 유효기간이 오늘 이후거나 오늘인 경우 - 유효기간에 유효기간 더함
			} else {
				sdate = payS.dateAddValid(sdate, valid);
			}
			// 회원 유효기간 세팅
			soloinfo.setSdate(sdate);
			// 타입을 2로 바꾸기
			soloinfo.setStype(2);
			// db에 stype과 sdate 변경
			sm.paysmember(soloinfo);
			// 세션 업데이트
			session.setAttribute("solosession", soloinfo);
			
		// 기업회원 세션이 존재할 경우
		} else if (session.getAttribute("cid") != null) {
			;
			String cid = (String) session.getAttribute("cid");
			MCompanyDTO compinfo = mcm.deatilCompany(cid);
			payDTO.setCid(cid);
			compinfo.setCtype(2);
			Date cdate = compinfo.getCdate();
			if (cdate == null || cdate.before(today)) {
				cdate = payS.dateAddValid(today, valid);
			} else {
				cdate = payS.dateAddValid(cdate, valid);
			}
			compinfo.setCdate(cdate);
			mcm.paycmember(compinfo);
			session.setAttribute("companysession", compinfo);
			// 결제기업의 채용공고를 모두 유효하도록 타입을 바꿈
			rcm.payRTypeByCid(cid);
		} else {
			payS.paymentCancel(token, payDTO.getImpUid(), "만료된 세션");
			return -1;
		}
		// 주문정보를 db에 저장
		paym.insert(payDTO);
		return res;
	}

	// 결제 금액 검증까지 성공했을 때 리다이렉트
	@RequestMapping("/result/{impUid}")
	String result(@PathVariable String impUid, Model mm, HttpSession session) {
		// 유효기간정보
		Date date = null;
		if (session.getAttribute("sid") != null) {
			String sid = (String) session.getAttribute("sid");
			SoloDTO soloinfo = sm.detailSolo(sid);
			date = soloinfo.getSdate();
			session.setAttribute("solosession", soloinfo);
		}
		if (session.getAttribute("cid") != null) {
			String cid = (String) session.getAttribute("cid");
			MCompanyDTO compinfo = mcm.deatilCompany(cid);;
			date = compinfo.getCdate();
			session.setAttribute("companysession", compinfo);
		}
		// 결제시 부여된 impUid로 db내용 가져옴
		List<String> impuidList = new ArrayList<>();
		impuidList.add(impUid);
		List<Payment> paymentData = payS.getPaymentData(impuidList);
		
		mm.addAttribute("date", date);
		mm.addAttribute("paymentData", paymentData);
		
		return "product/product_result";
	}

}