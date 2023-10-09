package aaa.controll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import aaa.model.MCompanyDTO;
import aaa.model.PaymentResponseAll;
import aaa.model.PaymentResponseAll.Pagedpayment;
import aaa.model.PaymentResponseMember.Payment;
import aaa.model.ProductDTO;
import aaa.model.SoloDTO;
import aaa.service.AdminCompanyMapper;
import aaa.service.AdminSolo;
import aaa.service.AskMapper;
import aaa.service.MCompanyMapper;
import aaa.service.PayMapper;
import aaa.service.PayService;
import aaa.service.ProductMapper;
import aaa.service.SoloMapper;
import jakarta.annotation.Resource;

@Controller
@RequestMapping("admin_product")
public class AdminProductController {

	@Resource
	ProductMapper pm;

	// 상품관리
	@RequestMapping("/modify")
	String admin_product_modify(Model mm) {
		// 상품 목록
		List<ProductDTO> data = pm.list();
		for (ProductDTO payment : data) {
			payment.setFormatPrice(String.format("%,d", payment.getProductPrice())+"원");
		}
		mm.addAttribute("data", data);
		// 상품 추가 위한 DTO
		mm.addAttribute("ProductDTO", new ProductDTO());
		return "admin/product/modify";
	}

	// 상품추가
	@PostMapping("/insertProduct")
	public String addProduct(@ModelAttribute ProductDTO productDTO) {
		// DTO에 담긴 정보를 db에 저장(상품추가)
		pm.insert(productDTO);
		return "redirect:/admin_product/modify"; // 목록 페이지로 리다이렉트(새로고침)
	}

	// 상품삭제
	@RequestMapping("/deleteProduct")
	public String deleteProduct(@RequestParam("productId") String productId) {
		// 파라미터로 전달받은 상품 id를 통해 db 삭제(상품삭제)
		pm.delete(productId);
		return "redirect:/admin_product/modify";
	}

	@Resource
	PayMapper paym;
	@Autowired
	PayService payS;

	// 결제정산
	@RequestMapping("/graph")
	String admin_product_graph(Model mm, @RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) {

		// 오늘날짜(날짜 선택시 Max값 목적) string으로 변환해서 html로 보내줌
		String today = payS.dateformat(new Date());

		Date dateStartDate = null;
		Date dateEndDate = null;
		// 날짜 입력한 경우
		if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
			// 날짜비교를 위해 date로 변환
			dateStartDate = payS.stringToDate(startDate);
			dateEndDate = payS.stringToDate(endDate);
			// 시작일이 종료일보다 후인 경우 - 예외처리
			if (dateStartDate.after(dateEndDate)) {
				String errorMsg = "기간을 확인하세요";
				mm.addAttribute("errorMsg", errorMsg);
			}
			// 날짜 미입력한 경우
		} else {
			startDate = "2022-09-01";
			endDate = today;
			// date변수가 null인 경우 db 첫날부터 마지막날까지 데이터를 표시함
			// 따로 시작일과 종료일 초기값을 date로 형변환하지 않아도 됨
		}

		// 그래프 데이타
		// db에서 일매출 합산
		List<Map<String, Object>> dailytotal = paym.dailytotal(dateStartDate, dateEndDate);

		// 리스트 데이타
		// db에서 회원종류별 매출액 순위리스트
		List<Map<String, Object>> totalbys = paym.totalbys(dateStartDate, dateEndDate);
		List<Map<String, Object>> totalbyc = paym.totalbyc(dateStartDate, dateEndDate);
		// 회원자료 중 sid나 cid가 없는 자료 제외하기 - sid가 없는 자료면 cid자료고, cid가 없는 자료면 sid자료임
		totalbys = totalbys.stream().filter(map -> map.containsKey("sid")).collect(Collectors.toList());
		totalbyc = totalbyc.stream().filter(map -> map.containsKey("cid")).collect(Collectors.toList());

		// db에서 상품별 매출액 순위리스트
		List<Map<String, Object>> totalbyp = paym.totalbyp(dateStartDate, dateEndDate);

		// 기간내 값들 합산(원본맵,합산할 키)
		int totalSum = mapSum(dailytotal, "totalAmount");
		int sSum = mapSum(totalbys, "total");
		int cSum = mapSum(totalbyc, "total");
		int pSum = mapSum(totalbyp, "total");

		mm.addAttribute("today", today);
		mm.addAttribute("startDate", startDate);
		mm.addAttribute("endDate", endDate);

		mm.addAttribute("graphData", dailytotal);
		mm.addAttribute("slist", totalbys);
		mm.addAttribute("clist", totalbyc);
		mm.addAttribute("plist", totalbyp);

		mm.addAttribute("totalSum", totalSum);
		mm.addAttribute("sSum", sSum);
		mm.addAttribute("cSum", cSum);
		mm.addAttribute("pSum", pSum);

		return "admin/product/graph";
	}

	// 맵리스트에서 키 값들을 합하는 메서드
	public int mapSum(List<Map<String, Object>> list, String key) {
		return list.stream().mapToInt(entry -> ((BigDecimal) entry.get(key)).intValue()).sum();
	}

	// 전체 매출내역
	@RequestMapping("/payment/{page}")
	public String getPayments(Model mm, @PathVariable int page,
			@RequestParam(value = "status", defaultValue = "paid") String status,
			@RequestParam(value = "limit", defaultValue = "15") int limit,
			@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to,
			@RequestParam(value = "sorting", defaultValue = "-started") String sorting) {
		// 날짜 변환
		String today = payS.dateformat(new Date());
		String range = null;
		if (from != null && !from.isEmpty() && to != null && !to.isEmpty()) {
			// Unix타임스탬프로 변환
			long fromunix = payS.stringToUnix(from);
			// to가 날짜의 0시까지의 결과만 나와서 하루를 더해줌
			long tounix = payS.stringToUnix(to) + 24 * 60 * 60; // 24시간 * 60분 * 60초
			if (fromunix > tounix) {
				String errorMsg = "기간을 확인하세요";
				mm.addAttribute("errorMsg", errorMsg);
				from = null;
				to = null;
			} else if ((tounix - fromunix) > 90 * 24 * 60 * 60) {
				String errorMsg = "결제내역은 최대 90일까지 조회 가능합니다";
				mm.addAttribute("errorMsg", errorMsg);
				from = null;
				to = null;
			} else {
				range = "&from=" + fromunix + "&to=" + tounix;
			}
		}
		// API 통신
		String token = payS.getToken(); // PayService를 통해 토큰을 가져옴
		// URL 생성
		String apiUrl = "https://api.iamport.kr/payments/status/" + status + "?page=" + page + "&limit=" + limit + range
				+ "&sorting=" + sorting + "&_token=" + token;
		// rest템플릿
		RestTemplate restTemplate = new RestTemplate();
		// PaymentResponse 형태로 정보받음
		ResponseEntity<PaymentResponseAll> responseEntity = restTemplate.getForEntity(apiUrl, PaymentResponseAll.class);
		PaymentResponseAll responseBody = responseEntity.getBody();
		if (responseBody.getCode() != 0 || responseBody.getResponse() == null) {
			mm.addAttribute("errorMsg", responseBody.getMessage());
		} else {
			// 받아온 정보로 페이지처리
			int total = responseBody.getResponse().getTotal();
			int totalPages = (int) Math.ceil((double) total / limit);
//			System.out.println("page " + page + " totalPages " + totalPages);

			// 앞뒤로 몇페이지씩 보일건지
			int ranged = 2;
			// 둘 중에 큰 숫자
			int startPage = Math.max(1, page - ranged);
			// 둘 중에 작은 숫자
			int endPage = Math.min(totalPages, page + ranged);
			// 이전버튼 눌렀을 때의 페이지
			int prevPage = Math.max(1, page - ranged - 1);
			// 다음버튼 눌렀을 때의 페이지
			int nextPage = Math.min(totalPages, page + ranged + 1);

			List<Pagedpayment> paymentData = responseBody.getResponse().getList();
			for (Pagedpayment payment : paymentData) {
				payS.dataformat(payment);
			}

			mm.addAttribute("page", page);
			mm.addAttribute("status", status);
			mm.addAttribute("limit", limit);
			mm.addAttribute("from", from);
			mm.addAttribute("to", to);
			mm.addAttribute("sorting", sorting);

			mm.addAttribute("today", today);
			mm.addAttribute("total", total);
			mm.addAttribute("totalPages", totalPages);
			mm.addAttribute("startPage", startPage);
			mm.addAttribute("endPage", endPage);
			mm.addAttribute("prevPage", prevPage);
			mm.addAttribute("nextPage", nextPage);

			mm.addAttribute("paymentData", paymentData);
		}
		return "admin/product/payment_list";
	}

	// 매출내역상세(단건 조회)
	@RequestMapping("/payment/detail/{impuid}")
	String paymentByImpuid(Model mm, @PathVariable String impuid) {
		// impuid를 List에 추가
		List<String> impuidList = new ArrayList<>();
		impuidList.add(impuid);
		List<Payment> paymentData = payS.getPaymentData(impuidList);
		mm.addAttribute("paymentData", paymentData);
		return "admin/product/payment_detail";
	}
	
	// 아이디로 검색
	@RequestMapping("/payment/sch")
	String search(Model mm, @RequestParam("searchKeyword") String id) {
		// 아이디로 db의 impuid로 리스트를 만들어 가져오고, 서버에 보내 결제내역을 가져옴
		List<String> impuidList = paym.impuids(id);
		if (!impuidList.isEmpty()) {
			List<Payment> paymentData = payS.getPaymentData(impuidList);
			mm.addAttribute("paymentData", paymentData);
		}else {
			String errorMsg = "검색결과가 없습니다";
			mm.addAttribute("errorMsg", errorMsg);
		}
		mm.addAttribute("id", id);
		return "admin/product/payment_schlist";
	}
	

	@Resource
	SoloMapper sm;
	@Resource
	MCompanyMapper mcm;

	// 결제취소
	@RequestMapping("/payment/cancle")
	String paymentCancle(@RequestParam("impUid") String impUid, @RequestParam("id") String id,
			@RequestParam("name") String pname) {
		// 취소진행
		String token = payS.getToken();
		payS.paymentCancel(token, impUid, "취소사유: 관리자에 의한 취소");
		paym.payCancel(impUid);

		// db변경
		// 유효기간: 상품명에서 숫자 추출, 없으면 0일
		Date today = new Date();
		int valid = 0;
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(pname);
		if (matcher.find()) {
			String num = matcher.group();
			valid = Integer.parseInt(num);
		}
		// 유효기간, 타입 변경
		// 상품명으로 기업회원인지 개인회원인지
		if (pname.contains("채용")) {
			// 기업회원
			// 모든 정보 불러오기
			MCompanyDTO compinfo = mcm.deatilCompany(id);
			if (compinfo != null) {
				// 유효기간 확인
				Date cdate = compinfo.getCdate();
				// 결제취소 기업회원이 cdate가 없을 확률은 없지만 그래도
				if (cdate != null) {
					// 취소결제의 유효기간을 뺌
					Date mcdate = payS.dateAddValid(cdate, -valid);
					// cdate 변경
					compinfo.setCdate(mcdate);
					// ctype이 2(결제유효회원)이고, 변경된 유효기간이 오늘 이전이라면
					if (compinfo.getCtype() == 2 && mcdate.before(today)) { //
						// ctype 변경
						compinfo.setCtype(1);
					}
				}
				// 변경된 cdate와 ctype을 db에 저장
				mcm.paycmember(compinfo);
			}

		} else {
			// 개인회원
			SoloDTO soloinfo = sm.detailSolo(id);
			if (soloinfo != null) {
				Date sdate = soloinfo.getSdate();
				// 리뷰를 쓰지않은 결제회원
				if (sdate != null) {
					Date msdate = payS.dateAddValid(sdate, -valid);
					// sdate 변경
					soloinfo.setSdate(msdate);
					// stype이 2(결제유효회원)이고, 변경된 유효기간이 오늘 이전이라면
					if (soloinfo.getStype() == 2 && msdate.before(today)) {
						// stype 변경
						soloinfo.setStype(1);
					}
					// 변경된 sdate와 stype을 db에 저장
					sm.paysmember(soloinfo);
				}
				// 리뷰를 쓴 결제회원 - db변경 없음
			}
		}
		return "redirect:/admin_product/payment/detail/" + impUid + "?alert";
	}

	@Resource
	AdminSolo asm;
	@Resource
	AdminCompanyMapper acm;
	@Resource
	AskMapper am;

	@RequestMapping("/index")
	String index(Model mm) {
		String today = payS.dateformat(new Date());
		int soloChk = asm.solocomListCnt();
		int compChk = acm.adminAddMiCont();
		int askChk = am.noReCnt();
		int payChk = paym.dayPayCnt(payS.dateformat(new Date()));

		mm.addAttribute("today", today);
		mm.addAttribute("soloChk", soloChk);
		mm.addAttribute("compChk", compChk);
		mm.addAttribute("askChk", askChk);
		mm.addAttribute("payChk", payChk);

		return "admin/product/index";
	}
}