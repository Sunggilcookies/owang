package aaa.controll;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import aaa.model.ApplicantDTO;
import aaa.model.MCompanyDTO;
import aaa.model.PageData;
import aaa.model.RecruitDTO;
import aaa.model.SoloDTO;
import aaa.model.SoloResumeDTO;
import aaa.service.SoloMapper;
import aaa.service.SoloResumeMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import retrofit2.http.Path;

@Controller
@RequestMapping("solo_resume")
public class SoloResumeController {
	
	@Resource
	SoloResumeMapper rsmapper;
	
	@Resource
	SoloMapper smapper;
	
	// 이력서 리스트
	@RequestMapping("home") // 리스트 임
	String solo_resume(Model mm, SoloResumeDTO rdto, SoloDTO sdto, HttpSession session,PageData pd) {
		// 이력서 총 개수
		// 이게 우선 현재 로그인한 회원 세션(아이디만 넘김 sid) 가져옴
		String sid = (String)session.getAttribute("sid");
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		if (sid == null || solosession == null) {
			pd.setMsg("개인회원만 이용가능합니다");
			pd.setGoUrl("/");
			return "solo_resume/alert";
		}
		
		System.out.println(rdto.rsid);
		List<SoloResumeDTO> data = rsmapper.resumelist(sid);
		
		int cnt = rsmapper.resumecnt(rdto.rsid, solosession.sid);
		//System.out.println(rdto.getCnt());
		mm.addAttribute("mainData", data);
		mm.addAttribute("cnt", cnt);
		System.out.println(solosession);
		System.out.println("cnt : "+cnt);
		System.out.println(rsmapper.resumecnt(rdto.rsid, solosession.sid));
		return "solo_resume/home";
	}
	
	// 이력서 디테일
	@RequestMapping("detail/{rsid}") 
	String solo_resume_detail(Model mm, HttpSession session, SoloResumeDTO rdto) {

		// 세션
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		mm.addAttribute("solosession", solosession);
		
		// 
		mm.addAttribute("rdto", rsmapper.resumedetail(rdto.rsid, solosession.sid));
		System.out.println(solosession);
		System.out.println(rdto);
		return "solo_resume/detail";
	}
	

	// 이력서 등록
	@GetMapping("write")
	String solo_resume_write(SoloResumeDTO rdto, SoloDTO sdto,
			 HttpSession session, Model mm) {
		// 세션 가져옴
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date);
		
		mm.addAttribute("today", today);
		mm.addAttribute("solosession", solosession);
		System.out.println(solosession);
		return "solo_resume/write";
	}
	
	// 이력서 제출
	@PostMapping("write")
	String solo_resume_writeReg(SoloResumeDTO rdto, PageData pd, Model mm, 
			HttpServletRequest request) {

		fileSave(rdto, request);
		fileSave2(rdto, request);
		
		rsmapper.resumeinsert(rdto);
		pd.setMsg("이력서가 등록되었습니다.");
		pd.setGoUrl("home");
		return "solo_resume/alert";
	}

	// 이력서 수정
	@GetMapping("modify/{rsid}")
	String solo_resume_modify(SoloResumeDTO rdto, Model mm ,HttpSession session,
			@PathVariable int rsid) { 
		
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		SoloResumeDTO sdto = rsmapper.resumedetail(rdto.rsid, solosession.sid);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date);
		mm.addAttribute("today", today);
		mm.addAttribute("solosession", solosession);
		mm.addAttribute("sdto", sdto);
		System.out.println(sdto);

		return "solo_resume/modifyForm";
	}
	
	
	// 이력서 수정 제출
	@PostMapping("modify/{rsid}")
	String modifyReg(SoloResumeDTO rdto, PageData pd, HttpSession session,
			HttpServletRequest request) {
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		System.out.println("수정제출 : " + rdto);
		pd.setMsg("수정실패");
		pd.setGoUrl("/solo_resume/modify/" + rdto.rsid);
		System.out.println("수정전"+rdto);
		if(rdto.getRsmmff()!=null) {
			fileSave(rdto, request);			
		}
		int cnt = rsmapper.resumemodify(rdto);
		System.out.println("modifyReg:"+cnt);
		if(cnt>0) {
			System.out.println("수정후"+rdto);
			// 수정이 되었을때 파일을 저장해야 함 (파일 저장)
			
			pd.setMsg("수정되었습니다.");
			pd.setGoUrl("/solo_resume/detail/"+rdto.getRsid());
		}

		return "solo_resume/alert";
	}
	
	// 수정폼에서 파일 삭제
	@PostMapping("fileDelete/{rsid}")
	String fileDelete(SoloResumeDTO rdto, PageData pd, HttpServletRequest request, @PathVariable int rsid) {
		
		SoloResumeDTO delDTO = rsmapper.resumefiledetail(rsid);
		pd.setMsg("파일 삭제실패");
		// 삭제 실패하면 수정페이지로
		pd.setGoUrl("/solo_resume/modify/" + rdto.getRsid());
				
		// 파일 삭제 mapper 추가
		int cnt = rsmapper.fileDelete(rdto.rsid);
		System.out.println("fileDelete:"+cnt);
		if(cnt>0) {
			fileDeleteModule(delDTO, request);
			pd.setMsg("파일 삭제되었습니다.");
			pd.setGoUrl("/solo_resume/modify/" + rdto.getRsid());
		}
		return "solo_resume/alert";
	}
	
	
	
	// 이력서 삭제
	@RequestMapping("delete/{rsid}")
	String delete(SoloResumeDTO rdto, HttpServletRequest request, PageData pd 
			, @PathVariable int rsid, HttpSession session) {
		// 일단 삭제이벤트 생성
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		SoloResumeDTO sdto = rsmapper.resumedetail(rdto.rsid, solosession.sid);
		System.out.println(sdto);
		
		pd.setMsg("삭제실패");
		pd.setGoUrl("/solo_resume/home/" + rsid);
		
		int cnt = rsmapper.resumedelete(rsid);
		System.out.println("deleteTest" + cnt);
		
		if (cnt>0) {
			fileDeleteModule(rdto, request);
			System.out.println();
			System.out.println(rdto);
			pd.setMsg("삭제되었습니다.");
			pd.setGoUrl("/solo_resume/home");
		}
		
		
		return "solo_resume/alert";
	}
	
	
    // 파일 저장
	void fileSave(SoloResumeDTO rdto, HttpServletRequest request) {
		
		//파일 업로드 유무 확인
		if(rdto.getRsmmff().isEmpty()) {
			return;
		}
		
		String path = request.getServletContext().getRealPath("resumephoto");
		// 이건 가상서버이다, 배포 시에는 realPath로 가져온다
		
		// 점 처리
		int dot = rdto.getRsmmff().getOriginalFilename().lastIndexOf(".");
		// 
		String fDomain = rdto.getRsmmff().getOriginalFilename().substring(0, dot);
		String ext = rdto.getRsmmff().getOriginalFilename().substring(dot);

		
		rdto.setRsphoto(fDomain+ext); 
		File rsmmff = new File(path+"\\"+rdto.getRsphoto());
		int cnt = 1;
		
		while(rsmmff.exists()) {
			 
			rdto.setRsphoto(fDomain+"_"+cnt+ext);
			rsmmff = new File(path+"\\"+rdto.getRsphoto());
			cnt++;
		}
		
		
		
		try {
			FileOutputStream fos = new FileOutputStream(rsmmff);
			
			fos.write(rdto.getRsmmff().getBytes());
			
			fos.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	// 파일 저장
	void fileSave2(SoloResumeDTO rdto, HttpServletRequest request) {
		
		//파일 업로드 유무 확인
		if(rdto.getRsmmff().isEmpty()) {
			return;
		}
		
		String path = request.getServletContext().getRealPath("applicantup");
		// 이건 가상서버이다, 배포 시에는 realPath로 가져온다
		
		// 점 처리
		int dot = rdto.getRsmmff().getOriginalFilename().lastIndexOf(".");
		// 
		String fDomain = rdto.getRsmmff().getOriginalFilename().substring(0, dot);
		String ext = rdto.getRsmmff().getOriginalFilename().substring(dot);
		
		
		rdto.setRsphoto(fDomain+ext); 
		File rsmmff = new File(path+"\\"+rdto.getRsphoto());
		int cnt = 1;
		
		while(rsmmff.exists()) {
			
			rdto.setRsphoto(fDomain+"_"+cnt+ext);
			rsmmff = new File(path+"\\"+rdto.getRsphoto());
			cnt++;
		}
		
		
		
		try {
			FileOutputStream fos = new FileOutputStream(rsmmff);
			
			fos.write(rdto.getRsmmff().getBytes());
			
			fos.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	void fileDeleteModule(SoloResumeDTO delDTO, HttpServletRequest request) {
		if(delDTO.getRsphoto()!=null) {
			String path = request.getServletContext().getRealPath("resumephoto");
			
			new File(path+"\\"+delDTO.getRsphoto()).delete();
		}
	}   
}