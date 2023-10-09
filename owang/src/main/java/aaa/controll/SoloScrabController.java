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
import aaa.model.ScrabDTO;
import aaa.model.SoloDTO;
import aaa.model.SoloResumeDTO;
import aaa.service.MCompanyMapper;
import aaa.service.RecruitMapper;
import aaa.service.SoloMapper;
import aaa.service.SoloApplicantMapper;
import aaa.service.SoloResumeMapper;
import aaa.service.SoloScrabMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("solo_scrab")
public class SoloScrabController {

	// 스크랩
	@Resource
	SoloScrabMapper scmapper;

	// 개인 회원
	@Resource
	SoloMapper smapper;

	// solo_recruit >> home에 기업 디테일
	// 채용공고
	@Resource
	RecruitMapper recruitMapper;

	// 기업
	@Resource
	MCompanyMapper mcmapper;
	
	

	// 스크랩 리스트
	@RequestMapping("home/{page}")
	String solo_scrab(Model mm, ScrabDTO scdto, 
			 @PathVariable int page,HttpSession session,PageData pd) {
		String sid = (String) session.getAttribute("sid");
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		if (sid == null || solosession == null) {
			pd.setMsg("개인회원만 이용가능합니다");
			pd.setGoUrl("/");
			return "solo_resume/alert";
		}
		scdto.setSid(sid);
		System.out.println(scdto);
		List<ScrabDTO> appdata = scmapper.sclist(scdto);
		mm.addAttribute("appdata", appdata);

		System.out.println(appdata);
		return "solo_scrab/home";
	}
	
	// 스크랩 추가
	@GetMapping("insert/{cid}/{page}/{recruitId}")
	String scrab_insert(PageData pd,  HttpSession session, ScrabDTO scdto,
	        @PathVariable int recruitId, @PathVariable String cid, Model mm) {

	    // 개인세션 불러오기
	    String sid = (String) session.getAttribute("sid");
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		if (sid == null || solosession == null) {
			pd.setMsg("개인회원만 이용가능합니다");
			pd.setGoUrl("/");
			return "solo_resume/alert";
		}
	    // 개인 정보 설정
	    scdto.setSid(sid);

	    // 기업 정보 조회
	    MCompanyDTO companyDTO = mcmapper.deatilCompany(cid);

	    // 공고 정보 조회
	    RecruitDTO recruitDTO = recruitMapper.recruitDetail(recruitId);

	    // 스크랩 정보 설정
	    scdto.setCid(companyDTO.getCid());
	    scdto.setCname(companyDTO.getCname());
	    scdto.setRecruitId(recruitDTO.getRecruitId());
	    scdto.setRecruitTitle(recruitDTO.getRecruitTitle());
	    scdto.setRealMagam(recruitDTO.getRealMagam());
	    scdto.setRecruitMoney(recruitDTO.getRecruitMoney());
	    scdto.setRecruitLocation(recruitDTO.getRecruitLocation());
	    
	    // 스크랩 정보 추가
	    scmapper.scinsert(scdto);
	    int scid = scdto.getScid();
	    String Ccid = scdto.getCid();
	    System.out.println("scid는 :  " + scid );

	    System.out.println("adto고요 cname이 들어옵니다 scdto: " + scdto);
	    System.out.println("==================================");

	    pd.setMsg("스크랩 완료되었습니다.");
	    pd.setGoUrl("/solo_scrab/home/1");

	    return "solo_scrab/alert";
	}

	
	// 스크랩 삭제
	@RequestMapping("delete/{scid}")
	String delete(ScrabDTO scdto, HttpServletRequest request, PageData pd 
			, @PathVariable int scid, HttpSession session) {
		// 일단 삭제이벤트 생성
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		//ScrabDTO ssdto = scmapper.scdetail(scdto.scid, solosession.sid);
		System.out.println(scdto);
		
		pd.setMsg("삭제실패");
		pd.setGoUrl("/solo_scrab/home/1");
		
		int cnt = scmapper.scdelete(scid);
		System.out.println("deleteTest" + cnt);
		
		if (cnt>0) {
			System.out.println();
			System.out.println(scdto);
			pd.setMsg("삭제되었습니다.");
			pd.setGoUrl("/solo_scrab/home/1");
		}
		
		
		return "solo_scrab/alert";
	}
	
	/*
	// 스크랩 등록
	@GetMapping("insert/{cid}/{page}/{recruitId}")
	String scrab_insert(PageData pd,  HttpSession session, ScrabDTO scdto,
			@PathVariable int recruitId, @PathVariable String cid, Model mm) {

		// 개인세션 불러오기
		String sid = (String) session.getAttribute("sid");

		// 아이디 저장
		SoloDTO sdto = new SoloDTO();
		sdto = smapper.detailSolo(sid);
		scdto.setSid(sdto.sid);
		
		
		// 기업명
		scdto.setMcDTO(mcmapper.deatilCompany(cid));

		// 공고제목
		scdto.setRcDTO(recruitMapper.recruitDetail(recruitId));

		// 지원서제목
		// adto.setAptitle(data.getRstitle());
		scmapper.scinsert(scdto);
		int scid = scdto.getScid();
		String Ccid = scdto.getCid();
		System.out.println("scid는 :  " + scid );


		// List<ApplicantDTO> alist = rcmapper.appselect(adto);
		System.out.println("adto고요 cname이 들어옵니다 scdto: " + scdto);
		// System.out.println(alist);
		System.out.println("==================================");

		pd.setMsg("스크랩 완료되었습니다.");
		pd.setGoUrl("/solo_scrab/home/1");

		return "solo_scrab/alert";
	}
	*/

}
  