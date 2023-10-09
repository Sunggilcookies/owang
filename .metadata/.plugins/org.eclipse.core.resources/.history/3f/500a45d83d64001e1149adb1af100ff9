package aaa.controll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import aaa.model.AdminDTO;
import aaa.model.ApplicantDTO;
import aaa.model.MCompanyDTO;
import aaa.model.RecruitDTO;
import aaa.model.SoloDTO;
import aaa.model.SoloResumeDTO;
import aaa.service.AdminCompanyMapper;
import aaa.service.CompanyApplicantMapper;
import aaa.service.MCompanyMapper;
import aaa.service.RecruitMapper;
import aaa.service.SoloApplicantMapper;
import aaa.service.SoloResumeMapper;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/recruit")
public class RecruitController {

	// path 정의 다같이 쓰는거라 위에 빼둠 받으시고 테스트할때 폴더 경로 바꾸시면되여!!
	// 승우꺼
	static String path = "E:\\BackEnd_hakwon\\ver1\\Spring_TeamVer_1\\Spring_TeamVer_1\\owang\\src\\main\\webapp\\up";

	@Resource
	RecruitMapper recruitMapper;

	@Resource
	MCompanyMapper crcMapper;
	// 지원하기때문에 넣었음
	@Resource
	SoloResumeMapper rsmapper;

	@Resource
	AdminCompanyMapper adrmMapper;

	@Resource
	SoloApplicantMapper applicantMapper;
	
	@Resource
	CompanyApplicantMapper camapper;

	@GetMapping("/search")
	public String search(@RequestParam("keyword") String keyword, @RequestParam("searchOption") String searchOption,
			@ModelAttribute RecruitDTO dto,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page, Model model) {

		List<RecruitDTO> data = recruitMapper.searchRecruit(keyword, searchOption);

		// 페이징 정보 설정
		dto.setPage(page); // 페이지 설정
		dto.calc(data.size()); // 페이지 계산 메서드 호출

		// 해당 페이지에 필요한 데이터만 가져오기
		int startIndex = dto.getStart();
		int endIndex = startIndex + dto.getLimit();
		if (endIndex > data.size()) {
			endIndex = data.size();
		}
		List<RecruitDTO> paginatedData = data.subList(startIndex, endIndex);

		try {
			if (paginatedData.size() > 0) {

				model.addAttribute("mainData", paginatedData);
			} else {

			}
		} catch (Exception e) {
			model.addAttribute("mainData", "검색결과가 없습니다");
		}

		return "recruit/searchResult";
	}

	@RequestMapping("calendar")
	String calendar(Model mm, RecruitDTO dto) {
		Date today = new Date();
		dto.calc(recruitMapper.recruitListCnt());

		List<RecruitDTO> alldata = recruitMapper.allrecruitList(dto);

		for (RecruitDTO recruit : alldata) {
			// 공고의 회사 타입 불러오기
			Integer ctype = (int) crcMapper.getctype(recruit.getCid());
			Date ccdate = recruit.realMagam;

			if (ccdate.before(today) || ctype == 1) {
				recruit.setRtype(2);
				recruitMapper.updateRtype(recruit);
			}
		}
		return "recruit/recruit_calendar";
	}

	@ResponseBody
	@PostMapping("calendar2")
	Object recruitTest() {

		ArrayList<LinkedHashMap<String, String>> list = new ArrayList<>();
		for (RecruitDTO recruitDTO : recruitMapper.recruitTest()) {
			LinkedHashMap<String, String> map = new LinkedHashMap<>();
			map.put("id", recruitDTO.getRecruitId() + "");
			map.put("title", "시작    " + recruitDTO.getRecruitTitle() + "  " + recruitDTO.getRegDate());
			map.put("start", recruitDTO.getRegDate());

			list.add(map);

		}

		return list;
	}

	@ResponseBody
	@PostMapping("calendar3")
	Object recruitTest3() {

		ArrayList<LinkedHashMap<String, String>> list = new ArrayList<>();
		for (RecruitDTO recruitDTO : recruitMapper.recruitTest()) {
			LinkedHashMap<String, String> map = new LinkedHashMap<>();
			map.put("id", recruitDTO.getRecruitId() + "");
			map.put("title", "마감    " + recruitDTO.getRecruitTitle() + "  " + recruitDTO.getRealMagam());
			map.put("start", recruitDTO.getRealMagam());

			list.add(map);

		}

		return list;
	}

	@ResponseBody
	@PostMapping("calendar23")
	public List<LinkedHashMap<String, String>> recruitTest2And3() {
		List<LinkedHashMap<String, String>> list = new ArrayList<>();

		for (RecruitDTO recruitDTO : recruitMapper.recruitTest()) {
			LocalDate startDate = recruitDTO.regDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate endDate = recruitDTO.realMagam.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			LinkedHashMap<String, String> map = new LinkedHashMap<>();
			map.put("id", recruitDTO.getRecruitId() + "");
			map.put("title", "[시작]    " + recruitDTO.getRecruitTitle() + "  " + recruitDTO.getCname());

			if (!(endDate.isBefore(startDate) || endDate.isEqual(startDate))) {
				map.put("start", recruitDTO.getRegDate());
			}

			list.add(map);
		}

		// calendar3 데이터 가져오기
		for (RecruitDTO recruitDTO : recruitMapper.recruitTest()) {
			LocalDate startDate = recruitDTO.regDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate endDate = recruitDTO.realMagam.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			LinkedHashMap<String, String> map = new LinkedHashMap<>();
			map.put("id", recruitDTO.getRecruitId() + "");
			map.put("title", "[마감]    " + recruitDTO.getRecruitTitle() + "  " + recruitDTO.getCname());
			map.put("cid", recruitDTO.getCid());

			if (!(startDate.isAfter(endDate) || startDate.isEqual(endDate))) {
				map.put("start", recruitDTO.getRealMagam());
			}

			list.add(map);
		}

		// 채용가능
		for (RecruitDTO recruitDTO : recruitMapper.recruitTest()) {
			LocalDate startDate = recruitDTO.regDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate endDate = recruitDTO.realMagam.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			// 공고의 시작일부터 마감일까지 모든 날짜에 해당하는 공고를 생성
			while (!startDate.isAfter(endDate)) {
				LinkedHashMap<String, String> map = new LinkedHashMap<>();
				map.put("id", recruitDTO.getRecruitId() + "");
				map.put("cid", recruitDTO.getCid());

				LocalDate currentDate = LocalDate.now();
				if (!currentDate.isAfter(startDate) && !currentDate.isBefore(endDate)) {
					// map.put("title", "[마감] " + recruitDTO.getRecruitTitle() + " " +
					// recruitDTO.getCname());
					// map.put("start", endDate.toString());
				} else {

					map.put("title", "[모집중] " + recruitDTO.getRecruitTitle() + " " + recruitDTO.getCname());

					if (!endDate.equals(startDate) || !startDate.equals(endDate)) {
						map.put("start", startDate.toString());
					}
				}

				list.add(map);
				startDate = startDate.plusDays(1);
			}
		}

		return list;
	}

	// 채용 리스트
	// 채용가능 리스트
	@RequestMapping("list/{page}")
	String list(Model mm, RecruitDTO dto, @PathVariable("page") int page) {

		// 오늘 날짜 보기
		Date today = new Date();

		// 데이터 상세보기
		dto.calc(recruitMapper.recruitListCnt());

		List<RecruitDTO> alldata = recruitMapper.allrecruitList(dto);
		LocalDateTime now = LocalDateTime.now();
		for (RecruitDTO recruit : alldata) {
			// 공고의 회사 타입 불러오기
			Integer ctype = (int) crcMapper.getctype(recruit.getCid());
			Date ccdate = recruit.realMagam;
			LocalDate calcnow = LocalDate.now();
			if (ccdate.before(today) || ctype == 1) {
				recruit.setRtype(2);
				recruitMapper.updateRtype(recruit);
			}

		}

		// "채용 가능" 탭에 대한 페이지 정보 계산
		RecruitDTO openDto = new RecruitDTO();
		openDto.setPage(page);
		openDto.calc(recruitMapper.companyROpenCnt());
		List<RecruitDTO> openData = recruitMapper.companyROpen(openDto);

		for (RecruitDTO recruit1 : openData) {
			// 공고의 회사 타입 불러오기
			Integer ctype = (int) crcMapper.getctype(recruit1.getCid());
			Date ccdate = recruit1.realMagam;
			LocalDate calcnow = LocalDate.now();
			if (ccdate.before(today) || ctype == 1) {
				recruit1.setRtype(2);
				recruitMapper.updateRtype(recruit1);
			}
			
			LocalDateTime deadline = LocalDateTime.parse(recruit1.getRealMagam() + "T23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
			Duration duration = Duration.between(now, deadline);
			long daysRemaining = duration.toDays();
			recruit1.setRecruitMagam((int) daysRemaining);
			//System.out.println("아임커밍");
			
			recruitMapper.updateRecruitMagam(recruit1);
				

		}

		mm.addAttribute("openData", openData);
		mm.addAttribute("openDto", openDto); // "채용 가능" 탭 페이지 정보

		return "recruit/recruit_list";
	}

	// 채용마감 리스트
	@RequestMapping("list/close/{page}")
	String listClose(Model mm, RecruitDTO dto, @PathVariable("page") int page) {
		// 오늘 날짜 보기
		Date today = new Date();

		// 데이터 상세보기
		dto.calc(recruitMapper.recruitListCnt());

		List<RecruitDTO> alldata = recruitMapper.allrecruitList(dto);
		LocalDate calnow = LocalDate.now();
		for (RecruitDTO recruit : alldata) {
			// 공고의 회사 타입 불러오기
			Integer ctype = (int) crcMapper.getctype(recruit.getCid());
			Date ccdate = recruit.realMagam;

			if (ccdate.before(today) || ctype == 1) {
				recruit.setRtype(2);
				recruitMapper.updateRtype(recruit);
			}
	
			

		}

		// "채용 마감" 탭에 대한 페이지 정보 계산
		RecruitDTO closeDto = new RecruitDTO();
		closeDto.setPage(page);
		closeDto.calc(recruitMapper.companyRCloseCnt());
		List<RecruitDTO> closeData = recruitMapper.companyRClose(closeDto);
		LocalDateTime now = LocalDateTime.now();
		for (RecruitDTO recruitDTO : closeData) {
			LocalDateTime deadline = LocalDateTime.parse(recruitDTO.getRealMagam() + "T23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
			Duration duration = Duration.between(now, deadline);
			long daysRemaining = duration.toDays();
			recruitDTO.setRecruitMagam((int) daysRemaining);
			//System.out.println("아임커밍");
			
			recruitMapper.updateRecruitMagam(recruitDTO);
		}

		mm.addAttribute("closeData", closeData);
		mm.addAttribute("closeDto", closeDto); // "채용 마감" 탭 페이지 정보

		return "recruit/recruit_list2";
	}

	@RequestMapping("list/all/{page}")
	String open(Model mm, RecruitDTO dto, @PathVariable("page") int page) {
		dto.calc(recruitMapper.recruitListCnt());
		LocalDateTime now = LocalDateTime.now();
		List<RecruitDTO> data = recruitMapper.recruitList(dto);
		for (RecruitDTO recruitDTO : data) {
			LocalDateTime deadline = LocalDateTime.parse(recruitDTO.getRealMagam() + "T23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
			Duration duration = Duration.between(now, deadline);
			long daysRemaining = duration.toDays();
			recruitDTO.setRecruitMagam((int) daysRemaining);
			//System.out.println("아임커밍");
			
			recruitMapper.updateRecruitMagam(recruitDTO);
		}
		  // 오름차순 정렬
	    List<RecruitDTO> ascendingData = new ArrayList<>(data);
	    ascendingData.sort(Comparator.comparing(RecruitDTO::getRegDate));

	    // 내림차순 정렬
	    List<RecruitDTO> descendingData = new ArrayList<>(data);
	    descendingData.sort(Comparator.comparing(RecruitDTO::getRegDate).reversed());
		
		mm.addAttribute("mainData", data);
		mm.addAttribute("ascendingData", ascendingData); // 오름차순 정렬 데이터
	    mm.addAttribute("descendingData", descendingData); // 내림차순 정렬 데이터

		return "recruit/recruit_list3";
	}

	// 채용 디테일
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

		mm.addAttribute("dtro", dto);
		mm.addAttribute("magam", formattedDate);
		return "/recruit/recruit_detail";
	}// 채용 디테일

	// 채용 디테일 새롭게 작성!
	@RequestMapping("detail/{cid}/{page}/{id}")
	String cdetail(Model mm, @PathVariable int page, @PathVariable int id) {
		// 조회수증가
		recruitMapper.recruitAddCont(id);

		RecruitDTO dto = recruitMapper.recruitDetail(id);
		if (dto == null) {
			System.out.println("일루안빠졌나?");
			dto = new RecruitDTO();
			dto.setMsg("삭제된 공고 입니다.");
			dto.setGoUrl("/solo_applicant/home/1");
			mm.addAttribute("recruitDTO", dto);
			return "recruit/recruit_alert";
		}
		dto.setPage(page);
		dto.calc(recruitMapper.recruitListCnt());
		LocalDate currentDate = LocalDate.now();
		LocalDate futureDate = currentDate.plusDays(dto.getRecruitMagam());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd (E)");
		String formattedDate = futureDate.format(formatter);

		mm.addAttribute("dto", dto);
		mm.addAttribute("magam", formattedDate);
		return "/recruit/recruit_detail";
	}// 채용 디테일

	@GetMapping("insert/{page}")
	String insert(RecruitDTO dto, HttpSession session, MCompanyDTO company, Model mm) {
		MCompanyDTO mcseDto = (MCompanyDTO) session.getAttribute("companysession");
		AdminDTO admin = (AdminDTO) session.getAttribute("adminSession");
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(date);

		mcseDto = crcMapper.deatilCompany(mcseDto.getCid());

		List<MCompanyDTO> data = adrmMapper.companyCapprovalList(company);
		mm.addAttribute("mainData", data);
		// 기업 또는 관리자 중 하나라도 로그인되었고 승인되었을 때 삽입 페이지로 이동
		session.setAttribute("companysession", mcseDto);
		if ((mcseDto != null && mcseDto.isCapproval() && mcseDto.getCtype() == 2)
				|| (admin != null && admin.isCapproval())) {

			if (admin == null) {
				// cid의 데이터 중 recruit테이블의 realMagam과 cdate가 지나지 않은 공고의 개수를 셈
				int a = recruitMapper.icompanyROpenCnt(mcseDto.getCid());
				if (a > 4) {
					dto.setMsg("현재 채용 중인 공고는 5개를 초과할 수 없습니다.");
					dto.setGoUrl("/company/list/" + mcseDto.getCid() + "/1");
					return "recruit/recruit_alert";
				}
			}
			mm.addAttribute("today", today);

			return "recruit/recruit_insert";
		} else if ((mcseDto != null && mcseDto.isCapproval() && mcseDto.getCtype() == 1)) {
			dto.setMsg("채용공고권 구매시 이용가능합니다.");
			dto.setGoUrl("/product/notice");
			return "recruit/recruit_alert";
		} else {
			dto.setMsg("로그인 시간이 만료되었거나 승인되지 않은 사용자입니다.");
			dto.setGoUrl("/recruit/list/1");
			return "recruit/recruit_alert";
		}
	}

	// 채용 삽입
	@PostMapping("insert/{page}")
	String insertReg(RecruitDTO dto, HttpServletRequest request, HttpSession session) {

		MCompanyDTO mcseDto = (MCompanyDTO) session.getAttribute("companysession");
		AdminDTO admin = (AdminDTO) session.getAttribute("adminSession");

		if ((mcseDto != null && mcseDto.isCapproval()) || (admin != null && admin.isCapproval())) {
			dto.setMsg("아직 승인되지 않은 기업입니다.");
			dto.setGoUrl("/recruit/list/1");
		}
		System.out.println("mcseDto : " + mcseDto);
		MCompanyDTO mc = crcMapper.deatilaaaCompany(mcseDto.getCid());

		dto.setCname(mc.getCname());
		dto.setCid(mcseDto.getCid());

		
		// 재훈 추가		
		dto.setClogo(mc.getClogo());
		dto.setRecruitId(recruitMapper.recruitMaxId() + 1);
		dto.setRecruitId(recruitMapper.recruitMaxId() + 1);

		// System.out.println(dto);
		fileSave(dto, request);
		recruitMapper.recruitInsert(dto);
		// System.out.println(dto);
		dto.setMsg("작성되었습니다.");
		dto.setGoUrl("/recruit/list/1");
		return "recruit/recruit_alert";

	}// 채용 삽입

	// 파일 저장
	void fileSave(RecruitDTO dto, HttpServletRequest request) {

		// 파일 업로드 유무 확인
		if (dto.getMmff().isEmpty()) {
			return;
		}

		int dot = dto.getMmff().getOriginalFilename().lastIndexOf(".");
		String fDomain = dto.getMmff().getOriginalFilename().substring(0, dot);
		String ext = dto.getMmff().getOriginalFilename().substring(dot);

		dto.setRecruitUpfile(fDomain + ext);
		File ff = new File(path + "\\" + dto.getRecruitUpfile());
		int cnt = 1;
		while (ff.exists()) {

			dto.setRecruitUpfile(fDomain + "_" + cnt + ext);
			ff = new File(path + "\\" + dto.getRecruitUpfile());
			cnt++;
		}

		try {
			FileOutputStream fos = new FileOutputStream(ff);

			fos.write(dto.getMmff().getBytes());

			fos.close();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}// 파일 저장

	// 파일삭제
	void fileDeleteModule(RecruitDTO delDTO, HttpServletRequest request) {
		if (delDTO.getRecruitUpfile() != null) {

			new File(path + "\\" + delDTO.getRecruitUpfile()).delete();
		}
	}// 파일삭제

	@RequestMapping("delete/{page}/{id}")
	String delete(RecruitDTO dto, HttpServletRequest request, @PathVariable int id,HttpSession session) {
		String cid = (String) session.getAttribute("cid");
		RecruitDTO rdto = recruitMapper.recruitDetail(id);
		System.out.println(dto);
		dto.setMsg("삭제실패");
		dto.setGoUrl("/recruit/detail/" +dto.getCid()+"/"+ dto.getPage() + "/" + id);
		
		if (cid==null) {
			int cnt = recruitMapper.recruitDelete(id);
			System.out.println("deleteTest" + cnt);

			if (cnt > 0) {
				fileDeleteModule(rdto, request);
				System.out.println();
				System.out.println(rdto);
				dto.setMsg("삭제되었습니다.");
				dto.setGoUrl("/recruit/list/1");
			}
		}
		
		List<ApplicantDTO> appfind = camapper.appfind(cid);
		System.out.println("이력서 조회"+appfind.size());
		if (appfind.size() > 0) {
			dto.setMsg("조회 안된 이력서가 "+appfind.size()+"건 존재합니다");
			dto.setGoUrl("/company_applicant/home/1");
			
		}else {
			
			// 일단 dto가안와서 page에있는 id로 죽임 form으로 넘긴게안이라서 올방법을 일단 못찾음
			int cnt = recruitMapper.recruitDelete(id);
			System.out.println("deleteTest" + cnt);

			if (cnt > 0) {
				fileDeleteModule(rdto, request);
				System.out.println();
				System.out.println(rdto);
				dto.setMsg("삭제되었습니다.");
				dto.setGoUrl("/recruit/list/1");
			}
			
		}
	

		return "recruit/recruit_alert";
	}

	// 채용 수정페이지
	@GetMapping("modify/{page}/{id}")
	String modify(Model mm, @PathVariable int page, @PathVariable int id) {

		RecruitDTO dto = recruitMapper.recruitDetail(id); // mapper라는 객체의 recruitDetail메서드 호출

		// 수정하려는 상세 정보 가져오기위해 detail메서드 사용 - 여기서 detail은 mapper.xml의 detail

		mm.addAttribute("dto", dto);

		return "recruit/recruit_modifyform";
	}

	@PostMapping("modify/{page}/{id}")
	String modifyReg(RecruitDTO dto, HttpServletRequest request, @PathVariable int page) {

		dto.setMsg("수정 실패입니다.");
		dto.setGoUrl("recruit/modify" + dto.getPage() + "/" + dto.getRecruitId());
		dto.setPage(page);
		// 수정된 데이터를 데이터베이스에 저장
		fileSave(dto, request);
		int cnt = recruitMapper.recruitModify(dto);
		System.out.println("수정" + dto);
		System.out.println("cnt" + cnt);

		if (cnt > 0) {
			dto.setMsg("수정되었습니다.");
			dto.setGoUrl("/recruit/detail/" + dto.getCid() + "/" + dto.getPage() + "/" + dto.getRecruitId());
		}

		return "recruit/recruit_alert";
	}

	// 지원서 제출 페이지
	@RequestMapping("submit/{page}/{rsid}")
	String submit(Model mm, SoloResumeDTO rdto, @PathVariable int page, @PathVariable int rsid, HttpSession session) {
		String sid = (String) session.getAttribute("sid");
		SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		List<SoloResumeDTO> data = rsmapper.resumelist(sid);
		mm.addAttribute("mainData", data);

		// 일단 이력서 가져옴
		// List<SoloResumeDTO> data = rsmapper.resumelist(rdto.rsid);
		// mm.addAttribute("mainData", rsmapper.resumedetail(rdto.rsid));
		// mm.addAttribute("mainData", data);
		// System.out.println(data + "data왔니?");
		return "recruit/recruit_submitform";
	}

	// 지원서 제출 완료 (SoloResumeDTO를 가져와서 applicant에 넣는다)
	// 1. SoloResumeDTO를 가져온다
	// 2. applicant에 삽입 메서드 넣음
	@RequestMapping("submit/{page}/{id}/{rsid}")
	String submitReg(RecruitDTO dto, HttpServletRequest request, @PathVariable int rsid, HttpSession session) {
		// SoloResumeDTO 한개 불러옴
		// String sid = (String)session.getAttribute("sid");
		// SoloDTO solosession = (SoloDTO) session.getAttribute("solosession");
		dto.setMsg("접수가 완료되었습니다.");
		dto.setGoUrl("recruit/list");

		return "recruit/recruit_alert";
	}

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

	// 파일다운로드
	@GetMapping("download/{ff}")
	void download(@PathVariable String ff, HttpServletRequest request, HttpServletResponse response) {

		String path = request.getServletContext().getRealPath("up");

		try {
			FileInputStream fis = new FileInputStream(path + "\\" + ff);
			String encFName = URLEncoder.encode(ff, "utf-8");
			System.out.println(ff + "->" + encFName);
			response.setHeader("Content-Disposition", "attachment;filename=" + encFName);

			ServletOutputStream sos = response.getOutputStream();

			byte[] buf = new byte[1024];

			// int cnt = 0;
			while (fis.available() > 0) { // 읽을 내용이 남아 있다면
				int len = fis.read(buf); // 읽어서 -> buf 에 넣음
											// len : 넣은 byte 길이

				sos.write(buf, 0, len); // 보낸다 : buf의 0부터 len 만큼

				// cnt ++;
				// System.out.println(cnt+":"+len);
			}

			sos.close();
			fis.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
