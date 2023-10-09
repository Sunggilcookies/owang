package aaa.controll;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.mysql.cj.Session;

import aaa.model.MCompanyDTO;
import aaa.model.PageData;
import aaa.model.PaymentResponseMember.Payment;
import aaa.model.SoloDTO;
import aaa.service.AdminCompanyMapper;
import aaa.service.EndSoloMapper;
import aaa.service.PayMapper;
import aaa.service.PayService;
import aaa.service.SoloMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("solo")
public class SoloController {

	@Resource
	SoloMapper sssmapper;

	@Resource
	AdminCompanyMapper acmapper;

	@Resource
	EndSoloMapper endSoloMapper;

	// 회원관리

	// 개인정보상세보기
	@RequestMapping("solo_info")
	String detail(Model mm, HttpSession session, PageData pd) {
		String sid = (String) session.getAttribute("sid");
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		pd.setMsg("개인회원만 이용가능합니다");
		pd.setGoUrl("/");

		if (sid != null) { // 세선에서 값을 받으면
			SoloDTO dto = sssmapper.detailSolo(sid);
			System.out.println(dto);
			mm.addAttribute("dto", dto);

			return "solo_info/detail";

		} else {

			return "join/join_alert";
		}
	}

	// 개인정보수정

	@GetMapping("modify")
	String modify(Model mm, HttpSession session, MCompanyDTO company) {
		String sid = (String) session.getAttribute("sid");
		mm.addAttribute("dto", sssmapper.detailSolo(sid));
		List<MCompanyDTO> data = acmapper.join(company);
		mm.addAttribute("mainData", data);
		return "solo_info/modify";
	}

	@PostMapping("modify")
	String modifyReg(SoloDTO dto, PageData pd, 
			HttpServletRequest request, HttpSession session) {

		pd.setMsg("수정실패");
		pd.setGoUrl("/solo/modify");

		if (dto.getScompanyFile() == null) { // 파일 없을때만 파일저장

			fileSavesolo(dto, request);
		}

		int cnt = sssmapper.modifffy(dto); // 메서드 안의 값이 들어와서 cnt 값이 1이 됩니다.
		System.out.println(dto.cid + "보고싶다 너의 cid");
		if (cnt > 0) {
			pd.setMsg("수정되었습니다. 다시로그인 부탁드립니다.");
			pd.setGoUrl("/login/main");
			// 세션수정
			SoloDTO solosession = sssmapper.resumeSolo(dto.getSid());

			session.setAttribute("solosession", solosession);
			session.invalidate();
		}

		return "join/join_alert";
	}

	// 개인정보삭제

	@GetMapping("delete")
	String delete(HttpSession session, Model mm) {

		String sid = (String) session.getAttribute("sid");
		mm.addAttribute("sid", sid);

		return "solo_info/delete";
	}

	@PostMapping("delete")
	String deleteReg(SoloDTO dto, PageData pd, HttpSession session) {

		pd.setMsg("삭제실패");
		pd.setGoUrl("/solo/delete");

		SoloDTO byedto = sssmapper.detailSolo(dto.sid);
		// 결제내역에 탈퇴일 추가
		paym.endMem(dto.getSid());
		int cnt = sssmapper.delettt(dto); // 메서드안의 값이 들어와서 cnt 값이 1이됌

		System.out.println("deleteReg:" + cnt);
		if (cnt > 0) {
			endSoloMapper.endSoloInsert(byedto);
			pd.setMsg("삭제되었습니다.");
			pd.setGoUrl("/");
			session.invalidate();
		}

		return "join/join_alert";
	}
	// 파일수정삭제
	@PostMapping("fileDelete")
		String fileDelete(SoloDTO dto,
				PageData pd, 
				HttpServletRequest request
				) {

			SoloDTO delDTO = sssmapper.detailSolo(dto.getSid());
			pd.setMsg("파일 삭제실패");
			pd.setGoUrl("/solo/modify");

			int cnt = sssmapper.fileDelete(dto);
			System.out.println("modifyReg:" + cnt);
			if (cnt > 0) {
				fileDeleteModule(delDTO, request);
				pd.setMsg("파일 삭제되었습니다.");
			}

			return "join/join_alert";
		}


	void fileSavesolo(SoloDTO dto, HttpServletRequest request) {
		// 파일 업로드 유무 확인
		if (dto.getMmff() == null || dto.getMmff().isEmpty()) {
			return;
		}

		String path = request.getServletContext().getRealPath("soloup");
		// path = "C:\\Final_Team\\owang\\src\\main\\webapp\\member\\soloup";

		int dot = dto.getMmff().getOriginalFilename().lastIndexOf(".");
		String fDomain = dto.getMmff().getOriginalFilename().substring(0, dot);
		String ext = dto.getMmff().getOriginalFilename().substring(dot);

		dto.setScompanyFile(fDomain + ext);
		File ff = new File(path + "\\" + dto.getScompanyFile());
		int cnt = 1;
		while (ff.exists()) {

			dto.setScompanyFile(fDomain + "_" + cnt + ext);
			ff = new File(path + "\\" + dto.getScompanyFile());
			cnt++;
		}

		try {
			FileOutputStream fos = new FileOutputStream(ff);

			fos.write(dto.getMmff().getBytes());

			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	// 파일지우기
	void fileDeleteModule(SoloDTO delDTO, HttpServletRequest request) {
		if (delDTO.getScompanyFile() != null) {
			String path = request.getServletContext().getRealPath("soloup");
			/*
			 * path = "E:\\BackEnd_hakwon\\N_SpringWorks\\exboard2\\src\\main\\webapp\\up";
			 */

			new File(path + "\\" + delDTO.getScompanyFile()).delete();
		}
	}

	// 상품
	@Resource
	PayMapper paym;
	@Autowired
	PayService payS;

	@RequestMapping("/product")
	String product(Model mm, HttpSession session,PageData pd) {
		// 세션에서 id 가져옴
		String sid = (String) session.getAttribute("sid");
		// db정보 가져옴
		SoloDTO soloinfo = sssmapper.detailSolo(sid);
		
		
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		if (sid == null || solosession == null) {
			pd.setMsg("개인회원만 이용가능합니다");
			pd.setGoUrl("/");
			return "solo_resume/alert";
		}
		
		
		// db정보 가져온 김에 세션 업데이트
		session.setAttribute("solosession", soloinfo);

		// sdate가 오늘이전이 아닌 경우 유효기간을 보내줌
		Date sdate = soloinfo.getSdate();
		Date today = new Date();
		if (sdate != null && !sdate.before(today)) {
			mm.addAttribute("date", sdate);
		}

		// 아이디로 db의 impuid로 리스트를 만들어 가져오고, 서버에 보내 결제내역을 가져옴
		List<String> impuidList = paym.impuids(sid);
		if (!impuidList.isEmpty()) {
			List<Payment> paymentData = payS.getPaymentData(impuidList);
			mm.addAttribute("paymentData", paymentData);
		}

		return "product/payment";
	}
}
