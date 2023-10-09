package aaa.controll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import aaa.model.ApplicantDTO;
import aaa.model.MCompanyDTO;
import aaa.model.PageData;
import aaa.model.RecruitDTO;
import aaa.model.SoloDTO;
import aaa.model.SoloResumeDTO;
import aaa.service.CompanyApplicantMapper;
import aaa.service.MCompanyMapper;
import aaa.service.MailService;
import aaa.service.RecruitMapper;
import aaa.service.SoloApplicantMapper;
import aaa.service.SoloMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("company_applicant")
public class CompanyApplicantController {
	
	@Autowired
    MailService mailService;
	
	@Resource
	MCompanyMapper mmmapper;
	
	@Resource
	CompanyApplicantMapper camapper;
	
	// 개인 회원
	@Resource
	SoloMapper smapper;

	// 채용공고
	@Resource
	RecruitMapper recruitMapper;
	
	// 재훈 추가
	// 개인 지원서
	@Resource
	SoloApplicantMapper samapper;

	// 기업회원 지원자 리스트
		@RequestMapping("home/{page}")
		String com_recruit(Model mm, ApplicantDTO adto, HttpSession session,PageData pd) {
			String cid = (String) session.getAttribute("cid");
			String sid = (String)session.getAttribute("sid");
			
			if (cid == null) {
				pd.setMsg("기업회원만 이용가능합니다");
				pd.setGoUrl("/");
				return "solo_resume/alert";
			}
			
			// 페이징
			adto.calc(samapper.apptotal());
			adto.setCid(cid);
			System.out.println(adto);
			List<ApplicantDTO> appdata = camapper.applist(adto);
			List<ApplicantDTO> apppassdata = camapper.apppasslist(adto);
			List<ApplicantDTO> appnonpassdata = camapper.appnonpasslist(adto);
			mm.addAttribute("appdata", appdata);
			mm.addAttribute("apppassdata", apppassdata);
			mm.addAttribute("appnonpassdata", appnonpassdata);
			String zzcname = mmmapper.getzzcname(cid);
			mm.addAttribute("zzcname", zzcname); // "채용 마감" 탭 페이지 정보
			System.out.println(appdata);
			return "company_applicant/home";
		}


	// 이메일을 이미 보냈는지 여부를 추적하는 변수
	boolean emailSent = false;

	@RequestMapping("detail/{page}/{ano}")
	String com_recruit_detail(Model mm, HttpSession session, ApplicantDTO adto, SoloResumeDTO rdto,
	        @PathVariable int page, @PathVariable int ano) {

	    // 세션
	    MCompanyDTO companysession = (MCompanyDTO) session.getAttribute("companysession");
	    ApplicantDTO cadto = camapper.appdetail(adto.ano, companysession.getCid());
	    RecruitDTO recruitDTO = recruitMapper.recruitDetail(cadto.getRecruitId());
	    System.out.println(recruitDTO);
	    System.out.println("아노..... : " + ano);
	    
	    String sid = (String) session.getAttribute("sid");
	    
	    // 이메일 설정
	    adto.setSemail(cadto.getSemail());
	    adto.setSname(cadto.getSname());
	    adto.setRecruitTitle(recruitDTO.getRecruitTitle());
	    adto.setCname(companysession.getCname());
	    //
	    System.out.println("열람: " + camapper.appread(adto.getAno()));
	    System.out.println("개인회원이메일: " + cadto.getSemail());

	    // 이메일을 이미 보냈는지 여부를 확인하고, 보내지 않았다면 보내고 변수 값을 변경
	    if (!emailSent) {
	        // 이메일 관련 변수 설정
	        String toEmail = adto.getSemail();
	        String subject = "[오왕] " + adto.getCname() + " 인사담당자가 " + adto.getSname() + "님의 이력서를 열람했습니다.";
	        String text = "공고 제목 : " + adto.getRecruitTitle() + "\n\n" + adto.getCname() + "의 인사담당자가 회원님의 이력서를 열람했습니다 :)";
	        mailService.sendEmail("ajh1070337@naver.com", toEmail, subject, text);

	        // 이메일을 보냈음을 표시
	        emailSent = true;
	    }

	    adto.setPage(page);
	    mm.addAttribute("cadto", cadto);
	    System.out.println(cadto);
	    return "company_applicant/detail";
	}


	// 기업회원 지원자 삭제
	@RequestMapping("delete/{page}/{ano}")
	String com_recruit_delete(ApplicantDTO adto, PageData pd, @PathVariable int ano, @PathVariable int page,
			HttpSession session) {
		// 일단 삭제이벤트 생성
		MCompanyDTO companysession = (MCompanyDTO) session.getAttribute("companysession");
		ApplicantDTO cadto = camapper.appdetail(adto.ano, companysession.getCid());

		pd.setMsg("삭제실패");
		pd.setGoUrl("/company_applicant/home");

		int cnt = samapper.appdelete(ano);
		System.out.println("deleteTest" + cnt);

		if (cnt > 0) {
			System.out.println();
			// System.out.println(adto);
			pd.setMsg("삭제되었습니다.");
			pd.setGoUrl("/company_applicant/home" + "/" + adto.getPage());
		}

		return "company/alert";
	}

	// 지원자 합격
	@RequestMapping("pass/{page}/{ano}")
	String com_recruit_pass(ApplicantDTO adto, PageData pd, HttpServletRequest request, @PathVariable int ano,
			@PathVariable int page, HttpSession session) {
		System.out.println("modify어서와");
		MCompanyDTO companysession = (MCompanyDTO) session.getAttribute("companysession");
		ApplicantDTO cadto = camapper.appdetail(adto.ano, companysession.getCid());
	    RecruitDTO recruitDTO = recruitMapper.recruitDetail(cadto.getRecruitId());
		System.out.println(cadto);

		// pd.setGoUrl("/company/applicant" + "/" + adto.getPage());
		camapper.apppass(adto.getAno());
		System.out.println(cadto);

		String contextPath = request.getContextPath();
		String redirectUrl = contextPath + "/company_applicant/home" + "/" + adto.getPage();

		adto.setSemail(cadto.getSemail());
	    adto.setSname(cadto.getSname());
	    adto.setRecruitTitle(recruitDTO.getRecruitTitle());
	    adto.setCname(companysession.getCname());
		
		// 메일 보내기
		String toEmail = adto.getSemail();
        String subject = "[오왕] " + adto.getSname() + "님 " + adto.getCname() + " 채용결과 안내";
        String text = "공고 제목 : " + adto.getRecruitTitle() + "\n\n" + adto.getCname() + "에 합격하신 것을 진심으로 축하드립니다 :)";
        mailService.sendEmail("ajh1070337@naver.com", toEmail, subject, text);

		return "redirect:" + redirectUrl;
	}

	// 지원자 불합격
	@RequestMapping("nonpass/{page}/{ano}")
	String com_recruit_nonpass(ApplicantDTO adto, PageData pd, HttpServletRequest request, @PathVariable int ano,
			@PathVariable int page, HttpSession session) {
		System.out.println("modify어서와");
		MCompanyDTO companysession = (MCompanyDTO) session.getAttribute("companysession");
		ApplicantDTO cadto = camapper.appdetail(adto.ano, companysession.getCid());
	    RecruitDTO recruitDTO = recruitMapper.recruitDetail(cadto.getRecruitId());
		System.out.println(cadto);

		// pd.setGoUrl("/company/applicant" + "/" + adto.getPage());
		camapper.appnonpass(cadto.getAno());
		System.out.println("cadto = " + cadto);

		String contextPath = request.getContextPath();
		String redirectUrl = contextPath + "/company_applicant/home" + "/" + adto.getPage();

		adto.setSemail(cadto.getSemail());
	    adto.setSname(cadto.getSname());
	    adto.setRecruitTitle(recruitDTO.getRecruitTitle());
	    adto.setCname(companysession.getCname());
		
		// 메일 보내기
		String toEmail = adto.getSemail();
        String subject = "[오왕] " + adto.getSname() + "님 " + adto.getCname() + " 채용결과 안내";
        String text = "공고 제목 : " + adto.getRecruitTitle() + "\n\n" + adto.getCname() 
        			+ "에서 진행한 채용 지원에 대해 감사드립니다.\n지원서 및 면접 결과를 심사한 후, 이번 채용 과정에서는 다른 후보자를 선택하기로 결정했습니다.\n"
        			+  adto.getSname() + "님의 지원에 대한 관심과 노력에 진심으로 감사드립니다. 불편을 끼쳐 드려 죄송하게 생각하며,"
        					+ " 저희 오왕은 여러분의 미래 경력에 성공과 행운을 기원합니다.\n\n감사합니다.";
        mailService.sendEmail("ajh1070337@naver.com", toEmail, subject, text);
        
		return "redirect:" + redirectUrl;
	}
}
