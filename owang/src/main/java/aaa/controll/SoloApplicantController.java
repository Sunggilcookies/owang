package aaa.controll;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
import aaa.model.SoloDTO;
import aaa.model.SoloResumeDTO;
import aaa.service.MCompanyMapper;
import aaa.service.MailService;
import aaa.service.RecruitMapper;
import aaa.service.SoloMapper;
import aaa.service.SoloApplicantMapper;
import aaa.service.SoloResumeMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("solo_applicant")
public class SoloApplicantController {

	
	
	@Autowired
    MailService mailService;
	
	
	// 개인 지원서
	@Resource
	SoloApplicantMapper samapper;

	// 개인 회원
	@Resource
	SoloMapper smapper;

	// 지원하기때문에 넣었음
	@Resource
	SoloResumeMapper rsmapper;

	// solo_recruit >> home에 기업 디테일
	// 채용공고
	@Resource
	RecruitMapper recruitMapper;

	// 기업
	@Resource
	MCompanyMapper mcmapper;

	// 지원서 리스트
	@RequestMapping("home/{page}")
	String solo_recruit(Model mm, ApplicantDTO adto, PageData pd, 
			 @PathVariable int page,HttpSession session) {
		String sid = (String) session.getAttribute("sid");
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		if (sid == null || solosession == null) {
			pd.setMsg("개인회원만 이용가능합니다");
			pd.setGoUrl("/");
			return "solo_resume/alert";
		}
		
		
		adto.setSid(sid);
		System.out.println(adto);
		List<ApplicantDTO> appdata = samapper.applist(adto);
		adto.calc(appdata.size());
		adto.setPage(page);
		mm.addAttribute("appdata", appdata);
		System.out.println("나오렴" + appdata.size());

		System.out.println("나오란망ㄹ야 : " + adto.getStart() + ", " +  adto.getLimit());
		//System.out.println(appdata);
		
		
		return "solo_applicant/home";
	}

	// 지원서 디테일
	@RequestMapping("detail/{page}/{ano}") 
	String solo_recruit_detail(Model mm, HttpSession session
			, ApplicantDTO adto,  SoloResumeDTO rdto
			, @PathVariable int page, @PathVariable int ano) {

		// 세션
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		mm.addAttribute("solosession", solosession);
		int rsid = adto.getRsid();
		// 
		adto.setPage(page);
		mm.addAttribute("adto", samapper.appdetail(adto.ano, solosession.sid));
		System.out.println(solosession);
		System.out.println(adto);
		return "solo_applicant/detail";
	}
	
	// 지원서 제출 페이지
	@GetMapping("submit/{page}/{cid}/{id}")
	String submit(Model mm, SoloResumeDTO rdto, @PathVariable int page, @PathVariable String cid, HttpSession session,
			@PathVariable int id, PageData pd) {
		String sid = (String) session.getAttribute("sid");
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		MCompanyDTO companyDTO = mcmapper.deatilCompany(cid);

		// 공고 불러오기
		RecruitDTO rcdto = recruitMapper.recruitDetail(id);
		String recruitTitle = rcdto.getRecruitTitle();
		String cname = companyDTO.getCname();
		
		// 내가 쓴 이력서 불러오기
		List<SoloResumeDTO> data = rsmapper.resumelist(sid);

	    if (data.isEmpty()) {
	        pd.setMsg("이력서 등록후 이용해주세요");
	        pd.setGoUrl("/solo_resume/home");
	        
	        return "solo_applicant/alert";
	    }

	    mm.addAttribute("mainData", data);
	    mm.addAttribute("recruitTitle", recruitTitle);
	    mm.addAttribute("cname", cname);
	    session.setAttribute("selectedResume", rdto);
	    return "solo_applicant/submitform";
	}



	// 지원서 제출 완료 (SoloResumeDTO를 가져와서 applicant에 넣는다)
	// 1. SoloResumeDTO를 가져온다
	// 2. applicant에 삽입 메서드 넣음
	@PostMapping("submit/{page}/{cid}/{id}")
	String submitReg(PageData pd, @PathVariable String cid,
			 HttpServletRequest request, HttpSession session, ApplicantDTO adto,
			@PathVariable int id, int rsid, Model mm) {
	 try {
		// 개인세션 불러오기
		String sid = (String) session.getAttribute("sid");
		adto.setSDTO(smapper.detailSolo(sid));
		MCompanyDTO companyDTO = mcmapper.deatilCompany(cid);
	    RecruitDTO recruitDTO = recruitMapper.recruitDetail(id);

		// 기업 불러오기
		adto.setMcDTO(mcmapper.deatilCompany(cid));

		// 이력서 불러오기
		adto.setSrDTO(rsmapper.resumedetail(rsid, sid));

		// 공고제목
		adto.setRcDTO(recruitMapper.recruitDetail(id));

		// 이메일 삽입
		adto.setCemail(companyDTO.getCemail());
		
		// 공고 제목
		adto.setRecruitTitle(recruitDTO.getRecruitTitle());

		// 기업 이메일
		System.out.println("모니: " + adto.getCemail());
        // 이메일 관련 변수 설정
        String toEmail = adto.getCemail(); // 받는 사람 이메일 주소를 동적으로 설정
        String subject = "[오왕] 신규지원자 알림"; // 이메일 제목을 동적으로 설정
        String text = "공고 제목 : " + adto.getRecruitTitle() + "\n\n 신규 지원자가 존재합니다! \n\n오왕 홈페이지에서 지원자의 이력서를 열람해주세요 :)"; // 이메일 내용을 동적으로 설정

        // 이메일 보내기

        int Ano = adto.getAno();
        System.out.println("Ano는 :  " + Ano );

        System.out.println("adto고요 cname이 들어옵니다 : " + adto);
        // System.println(alist);
        System.out.println("==================================");
        samapper.appinsert(adto);
        samapper.recnt(id);
        mailService.sendEmail("ajh1070337@naver.com", toEmail, subject, text);

        pd.setMsg("접수가 완료되었습니다.");
        pd.setGoUrl("/solo_applicant/home" + "/" + adto.getPage());

        return "solo_applicant/alert";
    } catch (DuplicateKeyException e) {
        // 중복된 데이터 삽입 시 DuplicateKeyException 처리
        pd.setMsg("기존에 지원한 이력이 존재합니다.");

        // 이전 페이지의 URL을 얻어옴
        String refererUrl = request.getHeader("Referer");

        // 이전 페이지로 리디렉션
        pd.setGoUrl(refererUrl);

        return "solo_applicant/alert";
    }
}
	
	// 개인회원 지원 취소
	@RequestMapping("modify/{page}/{ano}")
	String com_recruit_modify(ApplicantDTO adto, PageData pd, HttpServletRequest request
			, @PathVariable int ano, @PathVariable int page, HttpSession session) {
		System.out.println("modify어서와");
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		ApplicantDTO apdto = samapper.appdetail(adto.ano, solosession.sid);
		System.out.println(apdto);

		
		// apcancel = 2 변경
		samapper.appstate(adto.getAno());
		System.out.println(apdto);
		
	    String contextPath = request.getContextPath();
	    String redirectUrl = contextPath + "/solo_applicant/home" + "/" + adto.getPage();
	    
	    	
		
		return "redirect:" + redirectUrl;	
	}
		
	
	
	// 지원서 삭제 (지원 취소)
	@RequestMapping("delete/{page}/{ano}")
	String deleteApp(ApplicantDTO adto, PageData pd
			, @PathVariable int ano, @PathVariable int page, HttpSession session) {
		// 일단 삭제이벤트 생성
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		ApplicantDTO apdto = samapper.appdetail(adto.ano, solosession.sid);
	   
		pd.setMsg("삭제실패");
		pd.setGoUrl("/solo_applicant/home");
		
	    int cnt = samapper.appdelete(ano);
		System.out.println("deleteTest" + cnt);
		
		if (cnt>0) {
			System.out.println();
			//System.out.println(adto);
			pd.setMsg("삭제되었습니다.");
			pd.setGoUrl("/solo_applicant/home" + "/" + adto.getPage());
		}
		
		
		return "solo_applicant/alert";
	}
	
	/*
	// 지원서에서 채용공고 디테일 이동
	@RequestMapping("detail/{page}/{id}")
	String detail(Model mm, @PathVariable int page, @PathVariable int id) {
		// 조회수증가
		recruitMapper.recruitAddCont(id);
		
		RecruitDTO dto = recruitMapper.recruitDetail(id);
		dto.setPage(page);
		dto.calc(recruitMapper.recruitListCnt());
		LocalDate currentDate = LocalDate.now();
	    LocalDate futureDate = currentDate.plusDays(dto.getRecruitMagam()); 

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd (E)");
	    String formattedDate = futureDate.format(formatter);
	    
		mm.addAttribute("dtro",dto);
	    mm.addAttribute("magam", formattedDate);
		return "solo_applicant/apprecruit_detail";
	}// 채용 디테일

	
	// 지원서 제출 시 이력서 열람
	@RequestMapping("submit/{rsid}")
	String submitResume(Model mm, SoloResumeDTO rdto, HttpSession session, @PathVariable int rsid) {

		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		SoloResumeDTO sdto = rsmapper.resumedetail(rdto.rsid, solosession.sid);

		mm.addAttribute("sdto", sdto);
		mm.addAttribute("solosession", solosession);
		System.out.println(sdto);
		System.out.println(solosession);
		System.out.println(solosession.sid);
		// 이력서 열람만 가능
		return "solo_resume/recruit_resume";
	}
	*/
}
