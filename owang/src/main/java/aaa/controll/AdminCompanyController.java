package aaa.controll;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import aaa.model.ApplicantDTO;
import aaa.model.MCompanyDTO;
import aaa.model.PageData;
import aaa.model.RecruitDTO;
import aaa.service.AdminCompanyMapper;
import aaa.service.CompanyApplicantMapper;
import aaa.service.EndCompanyMapper;
import aaa.service.MCompanyMapper;
import aaa.service.PayMapper;
import aaa.service.RecruitMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin_company")
public class AdminCompanyController {

	@Resource
	AdminCompanyMapper adminMapper;

	@Resource
	MCompanyMapper crccMapper;
	
	@Resource
	PayMapper paym;
	
	@Resource
	RecruitMapper rcm;
	
	@Resource
	EndCompanyMapper endCompanyMapper;
	@Resource
	CompanyApplicantMapper applicantMapper;

	//static String path = "E:\\BackEnd_hakwon\\Spring_Team\\owang\\src\\main\\webapp\\companyup";

	@GetMapping("/search")
	String search(@RequestParam("keyword") String keyword, @RequestParam("searchOption") String searchOption,
			@ModelAttribute MCompanyDTO dto,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {
		List<MCompanyDTO> data = adminMapper.searchCompany(keyword, searchOption);
		dto.setPage(page);
		dto.calc(data.size());
		
		for (MCompanyDTO mCompanyDTO : data) {
			mCompanyDTO.setPage(page);
			mCompanyDTO.calc(data.size());
		}
		int startIndex = dto.getStart();
		int endIndex = startIndex + dto.getLimit();
		if (endIndex > data.size()) {
			endIndex = data.size();
		}
		List<MCompanyDTO> pageinatedData = data.subList(startIndex, endIndex);
		try {
			if (pageinatedData.size() > 0) {
				model.addAttribute("mainData", pageinatedData);
			}else {
				
			}
		}catch (Exception e) {
			model.addAttribute("mainData", "검색결과가 없습니다.");
		}
		
		return "admin/company/csearchResult";
	}

	// 공고 관리 페이지 ( 공고 리스트 )
		@RequestMapping("/cmanagement/{page}")
		String cmanagement(Model mm, RecruitDTO dto, @PathVariable("page") int page) {

			// 날짜 포맷팅
			Date today = new Date();
			String day = rctCntDateFormat(today, 0);
			String week = rctCntDateFormat(today, -7);
			String month = rctCntDateFormat(today, -30);
			String year = rctCntDateFormat(today, -365);

			// 기간별 도넛그래프 data
			int todayRct = rcm.rctRangeRegCnt(day, day);
			int weekRct = rcm.rctRangeRegCnt(week, day);
			int monthRct = rcm.rctRangeRegCnt(month, day);
			int yearRct = rcm.rctRangeRegCnt(year, day);

			// 1년간 월별 막대그래프 그릴 data
			List<Map<String, Object>> graphData = rcm.rctRegCnt(year, day);

			// 공고테이블
			dto.calc(rcm.recruitListCnt());
			List<RecruitDTO> data = rcm.recruitList(dto);

			mm.addAttribute("todayRct", todayRct);
			mm.addAttribute("weekRct", weekRct);
			mm.addAttribute("monthRct", monthRct);
			mm.addAttribute("yearRct", yearRct);
			mm.addAttribute("graphData", graphData);
			mm.addAttribute("mainData", data);
			
			return "admin/company/cmanagement";
		}

	// date형의 날짜, 변경할 일 수를 입력하면 계산해서 string형("yyyy-MM-dd")으로 반환
	public String rctCntDateFormat(Date date, int valid) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, valid);
		Date mdate = calendar.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(mdate);
	}

	// 기업 리스트
	@RequestMapping("list/{page}")
	String list(Model mm, MCompanyDTO dto) {


		dto.calc(adminMapper.adminAddAllCnt());
		List<MCompanyDTO> data = adminMapper.adminCompanyList(dto);
		System.out.println(data.size());
		mm.addAttribute("mainData", data);
		//mm.addAttribute("realData", adminMapper.companyCapprovalList(dto));
		return "/admin/company/cList";
	}// 기업 리스트

	@RequestMapping("cRegList/{page}")
	String cRegList(Model mm, MCompanyDTO dto, @PathVariable int page) {
		dto.setPage(page);
		dto.calc(adminMapper.adminAddCont());
		System.out.println(dto);
		List<MCompanyDTO> data = adminMapper.companyCapprovalList(dto);
		mm.addAttribute("realData", data);
		return "/admin/company/cRegList";
	}

	// 미인증
	@RequestMapping("cMiList/{page}")
	String cMiList(Model mm, MCompanyDTO dto) {

		dto.calc(adminMapper.adminAddMiCont());
		List<MCompanyDTO> data = adminMapper.companyList(dto);
		mm.addAttribute("mainData", data);

		return "/admin/company/cMiList";
	}

	// 기업정보수정

	@GetMapping("modify/{cno}")
	String modify(Model mm, HttpSession session, @PathVariable int cno) {
		MCompanyDTO rcdto = adminMapper.adminCDetail(cno);

		mm.addAttribute("dto", crccMapper.deatilCompany(rcdto.getCid()));

		return "/admin/company/modify";
	}

	@PostMapping("modify/{cno}")
	String modifyReg(MCompanyDTO dto, @PathVariable int cno, PageData pd, HttpServletRequest request,
			HttpSession session) {
		pd.setMsg("수정실패");
		pd.setGoUrl("/admin_company/modify" + dto.getCno());

		if (dto.getCcompanyFile() == null) {
			System.out.println("기존파일없음");
			fileSavecompany(dto, request);
		}
		System.out.println(dto);
		int cnt = crccMapper.adminmodifffy(dto); // 메서드안의 값이 들어와서 cnt 값이 1이됌

		if (cnt > 0) {
			pd.setMsg("수정되었습니다.");
			pd.setGoUrl("/admin_company/detail/1/" + dto.getCno());
		}

		return "join/join_alert";
	}

	@RequestMapping("delete/{cno}")
	String delete(MCompanyDTO dto, Model mm, @PathVariable int cno, HttpServletRequest request) {
		MCompanyDTO rcdto = adminMapper.adminCDetail(cno);
		paym.endMem(rcdto.getCid());
		// 탈퇴할 기업의 지원내역들
		List<ApplicantDTO> byeapp = endCompanyMapper.endapplist(rcdto.getCid());
		
		// 탈퇴할 기업의 공고 조회
		List<RecruitDTO> byerecruit = endCompanyMapper.endrecruitList(rcdto.getCid());
		
		int cnt = adminMapper.deleteCompany(cno);
		
		if (cnt > 0) {
			// 탈퇴할 기업추가
			endCompanyMapper.endCompanyInsert(rcdto);
			// 탈퇴할 기업의 지원내역 추가(insert 한번에 안되더군요)
			for (ApplicantDTO applicantDTO : byeapp) {
				endCompanyMapper.endappinsert(applicantDTO);
			}
			// 탈퇴할 기업의 공고추가
			for (RecruitDTO recruitDTO : byerecruit) {
				endCompanyMapper.endrecruitInsert(recruitDTO);
			}
			
			fileDeleteModule(rcdto, request);
		}

		return "redirect:/admin_company/list/1";
	}

	

	// 관리자 기업 디테일
	@RequestMapping("detail/{page}/{cno}")
	String detail(Model mm, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@PathVariable int cno) {
		// 조회수증가
		System.out.println(cno + "나다 cno");
		MCompanyDTO dto = adminMapper.adminCDetail(cno);
		dto.setPage(page);
		dto.calc(adminMapper.adminAddCont());

		mm.addAttribute("dto", dto);
		System.out.println(dto + "디테일");
		return "/admin/company/cDetail";
	}

	// 파일 다운로드
	@RequestMapping("/companyup/download/{filename}")
	void download(@PathVariable String filename, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("이벤트체크=> 파일 다운로드");
		try {
			System.out.println(filename + ", " + "파일님 오셧나요");
			String path = request.getServletContext().getRealPath("companyup");
			FileInputStream fis = new FileInputStream(path + "\\" + filename);
			// 인코딩 설정
			String encFName = URLEncoder.encode(filename, "utf-8");

			response.setHeader("Content-Disposition", "attachment; filename=" + encFName);

			ServletOutputStream sos = response.getOutputStream();

			byte[] buf = new byte[1024];
			while (fis.available() > 0) {
				int len = fis.read(buf); // 읽은후 buf에 저장
				// len = 넣은 길이
				sos.write(buf, 0, len);// buf 0부터 len 만큼

			}

			sos.close();
			fis.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}// 파일 다운로드

	// 수락여기
	@RequestMapping("checkId/{cno}")
	String checkId(@PathVariable int cno, MCompanyDTO dto, HttpSession session) {
		MCompanyDTO mcseDto = (MCompanyDTO) session.getAttribute("companysession");

		if (mcseDto != null) {
			System.out.println("오셧나요?");
			System.out.println("mcseDto : " + mcseDto);
			mcseDto.setCapproval(true);
			session.setAttribute("companysession", mcseDto);
		}

		System.out.println(adminMapper.checkoutFile(dto.getCno()) + "왔나요");
		adminMapper.checkoutFile(dto.getCno());
		return "redirect:/admin_company/list/1";
	}

	// 사업자등록증파일저장
	void fileSavecompany(MCompanyDTO cto, HttpServletRequest request) {

		// 파일 업로드 유무 확인
		if (cto.getMmff() == null || cto.getMmff().isEmpty()) {
			return;
		}

		String path = request.getServletContext().getRealPath("companyup");
		// path = "C:\\Final_Team\\owang\\src\\main\\webapp\\member\\companyup";

		int dot = cto.getMmff().getOriginalFilename().lastIndexOf(".");
		String fDomain = cto.getMmff().getOriginalFilename().substring(0, dot);
		String ext = cto.getMmff().getOriginalFilename().substring(dot);

		cto.setCcompanyFile(fDomain + ext);
		File ff = new File(path + "\\" + cto.getCcompanyFile());
		int cnt = 1;
		while (ff.exists()) {

			cto.setCcompanyFile(fDomain + "_" + cnt + ext);
			ff = new File(path + "\\" + cto.getCcompanyFile());
			cnt++;
		}

		try {
			FileOutputStream fos = new FileOutputStream(ff);

			fos.write(cto.getMmff().getBytes());

			fos.close();
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}

	// 파일삭제
		void fileDeleteModule(MCompanyDTO delDTO, HttpServletRequest request) {
			if (delDTO.getCcompanyFile() != null) {
				String path = request.getServletContext().getRealPath("companyup");
				new File(path + "\\" + delDTO.getCcompanyFile()).delete();
			}
		}// 파일삭제
	

	// 파일수정삭제
	@PostMapping("fileDelete/{cno}")
	String fileDelete(@PathVariable int cno, PageData pd, HttpServletRequest request) {

		MCompanyDTO dto = adminMapper.adminCDetail(cno);

		pd.setMsg("파일 삭제실패");
		pd.setGoUrl("/admin_company/modify/" + dto.getCno());
		System.out.println("삭제하기전dto"+dto);
		int cnt = crccMapper.fileDelete(dto);
		System.out.println("modifyReg:" + cnt);
		if (cnt > 0) {
			fileDeleteModule(dto, request);
			pd.setMsg("파일 삭제되었습니다.");
		}

		return "join/join_alert";
	}
}
