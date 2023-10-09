package aaa.controll;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import aaa.model.ApplicantDTO;
import aaa.model.MCompanyDTO;
import aaa.model.PageData;
import aaa.model.RecruitDTO;
import aaa.model.ReviewDTO;
import aaa.model.ScrabDTO;
import aaa.model.SoloDTO;
import aaa.model.SoloResumeDTO;
import aaa.service.MCompanyMapper;
import aaa.service.RecruitMapper;
import aaa.service.ReviewMapper;
import aaa.service.SoloMapper;
import aaa.service.SoloApplicantMapper;
import aaa.service.SoloResumeMapper;
import aaa.service.SoloScrabMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("solo_review")
public class SoloReviewController {

	// 리뷰
	@Resource
	ReviewMapper rvmapper;

	@Resource
	SoloMapper srMapper;
	
	// 기업
	@Resource
	MCompanyMapper mcmapper;
	
	
	// 리뷰 리스트
	@RequestMapping("home")
	String solo_review(Model mm, ReviewDTO dto, HttpSession session,PageData pd) {
		String sid = (String) session.getAttribute("sid");
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		if (sid == null || solosession == null) {
			pd.setMsg("개인회원만 이용가능합니다");
			pd.setGoUrl("/");
			return "solo_resume/alert";
		}
		
		
		dto.setSid(sid);
		System.out.println(dto);
		List<ReviewDTO> mainData = rvmapper.soloreviewList(dto);
		mm.addAttribute("mainData", mainData);

		System.out.println(mainData);
		return "solo_review/home";
	}
	
	
	// 리뷰수정페이지
	@GetMapping("/modify/{cid}/{page}/{sid}")
	String modify(Model mm, @PathVariable String cid, ReviewDTO dto, @PathVariable String sid) {

		System.out.println(cid + "널이냐?");
		dto.setCid(cid);
		dto.setSid(sid);
	    MCompanyDTO companyDTO = mcmapper.deatilCompany(cid);
		System.out.println(dto);
		ReviewDTO rdto = rvmapper.reviewDetail(dto);
		System.out.println(rdto);
		System.out.println();

		mm.addAttribute("mainData", rdto);
		return "solo_review/review_modify";
	}

	// 리뷰 데이서 수정 페이지
	@PostMapping("/modify/{cid}/{page}/{sid}")
	String modifyReg(ReviewDTO dto, @PathVariable String cid, @PathVariable String sid) {

		int cnt = rvmapper.reviewModify(dto);

		if (cnt == 0) {
			dto.setMsg("수정실패");
			dto.setGoUrl("/solo_review/home");
		}
		dto.setMsg("수정성공");
		dto.setGoUrl("/solo_review/home");

		return "solo_review/review_alert";
	}
	
	@RequestMapping("/delete/{cid}/{page}/{sid}")
	String delete(ReviewDTO dto,
			@PathVariable String cid,
			@PathVariable String sid,
			HttpSession session,
			SoloDTO sdto) {
		
		int cnt = rvmapper.reviewDelete(dto);
		dto.setMsg("삭제실패");
		dto.setGoUrl("/review/modify/" + dto.getCid() + "/" + dto.getPage() + "/" + dto.getSid());
		if (cnt == 1) {
			dto.setMsg("삭제성공");
			
			
		// 세션 가져옴
		String ssid = (String) session.getAttribute("sid");
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		
		// solosession의 stype 속성을 2로 설정
		sdto.setSid(sid);
		sdto.setStype(1);
		sdto.setSdate(null);
		solosession.setStype(1);
		solosession.setSdate(null);
		// 수정된 solosession을 다시 세션에 설정
		session.setAttribute("sid", sdto.getSid());
		session.setAttribute("solosession", solosession);

		// 타입 변경
		srMapper.loginsmember(sdto);
		System.out.println("내가 sdto야");
		System.out.println(sdto);
		
		dto.setGoUrl("/solo_review/home");
		
	}
	
		return "solo_review/review_alert";
	}

}
  