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

import aaa.model.ApplicantDTO;
import aaa.model.MCompanyDTO;
import aaa.model.PageData;
import aaa.model.PaymentResponseMember.Payment;
import aaa.model.RecruitDTO;
import aaa.model.ReviewDTO;
import aaa.model.SoloDTO;
import aaa.model.SoloResumeDTO;
import aaa.service.CompanyApplicantMapper;
import aaa.service.EndCompanyMapper;
import aaa.service.MCompanyMapper;
import aaa.service.PayMapper;
import aaa.service.PayService;
import aaa.service.RecruitMapper;
import aaa.service.ReviewMapper;
import aaa.service.SoloApplicantMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("company")
public class CompanyController {

	@Resource
	RecruitMapper remapper;

	// 기업
	@Resource
	MCompanyMapper cccmapper;

	@Resource
	EndCompanyMapper endCompanyMapper;

	@Resource
	CompanyApplicantMapper applicantMapper;
	
	@Resource
	ReviewMapper revzMapper;

	@RequestMapping("/list/{cid}/{page}") //회사 공고페이지
	String comRecruitList(Model mm, HttpSession session, RecruitDTO dto, @PathVariable("page") int page,
			@PathVariable("cid") String cid,PageData pd) {
		String sessioncid = (String) session.getAttribute("cid");
		
		
		if (sessioncid != null && sessioncid.equals(cid)) {
			int self = 1;
			mm.addAttribute("self", self);
		}
		//회사이름가져오기
		String zzcname = cccmapper.getzzcname(cid);
		
		// 오늘 날짜 보기
		Date today = new Date();
		// 데이터 상세보기
		List<RecruitDTO> alldata = remapper.recruitCompanyDetail(dto);
		Integer ctype = (int) cccmapper.getctype(cid);
		for (RecruitDTO recruit : alldata) {
			// 공고의 회사 타입 불러오기
			Date ccdate = recruit.realMagam;
			if (ccdate.before(today) || ctype == 1) {
				recruit.setRtype(2);
				remapper.updateRtype(recruit);
			}else if(ctype == 2 && !ccdate.before(today)){
				recruit.setRtype(1);
				remapper.updateRtype(recruit);
			}
		}
		List<RecruitDTO> openData = remapper.recruitCompanyOpen(cid);

		// "채용 마감" 탭에 대한 페이지 정보 계산
		RecruitDTO closeDto = new RecruitDTO();
		closeDto.setCid(cid);
		closeDto.setPage(page);
		closeDto.setLimit(9);
		closeDto.calc(remapper.recruitCompanyCloseCnt(cid));
		closeDto.setLimit(9);
		List<RecruitDTO> closeData = remapper.recruitCompanyClose(closeDto);
		System.out.println("start가 없니 closeDto야? : " + closeDto);
		mm.addAttribute("endsize", remapper.recruitCompanyCloseCnt(cid));
		mm.addAttribute("mainData", openData);
		mm.addAttribute("closeData", closeData);
		mm.addAttribute("closeDto", closeDto); // "채용 마감" 탭 페이지 정보
		mm.addAttribute("zzcname", zzcname); // "채용 마감" 탭 페이지 정보
		return "company/comRecruitList";
	}
	// 상품
	@Resource
	PayMapper paym;
	@Autowired
	PayService payS;

	@RequestMapping("/product")
	String product(Model mm, HttpSession session,PageData pd) {
		// 세션에서 id 가져옴
		String cid = (String) session.getAttribute("cid");
		
		if (cid == null) {
			pd.setMsg("기업회원만 이용가능합니다");
			pd.setGoUrl("/");
			return "solo_resume/alert";
		}
		
		// db정보 가져옴
		MCompanyDTO compinfo = cccmapper.deatilCompany(cid);
		// db정보 가져온 김에 세션 업데이트
		session.setAttribute("companysession", compinfo);

		// cdate가 오늘이전이 아닌 경우 유효기간을 보내줌
		Date cdate = compinfo.getCdate();
		Date today = new Date();
		if (cdate != null && !cdate.before(today)) {
			mm.addAttribute("date", cdate);
		}

		// 아이디로 db의 impuid로 리스트를 만들어 가져오고, 서버에 보내 결제내역을 가져옴
		List<String> impuidList = paym.impuids(cid);
		if (!impuidList.isEmpty()) {
			List<Payment> paymentData = payS.getPaymentData(impuidList);
			mm.addAttribute("paymentData", paymentData);
		}

		return "product/payment";
	}

	// 기업
	@RequestMapping("mypage")
	String detaizlmy(Model mm, HttpSession session,PageData pd) {
		String cid = (String) session.getAttribute("cid");
	
		
		if (cid == null) {
			pd.setMsg("기업회원만 이용가능합니다");
			pd.setGoUrl("/");
			return "solo_resume/alert";
		}
		String zzcname = cccmapper.getzzcname(cid);
		mm.addAttribute("zzcname", zzcname); // "채용 마감" 탭 페이지 정보
		mm.addAttribute("dto", cccmapper.deatilCompany(cid));

		return "company/detailmypage";
	}

	// 채용 리스트
		// 기업관리
		// 기업정보상세보기
	@RequestMapping("detail/{cid}")
	String detail(@PathVariable String cid,ReviewDTO rdto,MCompanyDTO cDTO, Model mm, RecruitDTO dto,HttpSession session) {
		Date today = new Date();
		//타입처리해주기
		List<RecruitDTO> alldata = remapper.recruitCompanyDetail(dto);
		Integer ctype = (int) cccmapper.getctype(cid);
		for (RecruitDTO recruit : alldata) {
			// 공고의 회사 타입 불러오기
			Date ccdate = recruit.realMagam;
			if (ccdate.before(today) || ctype == 1) {
				recruit.setRtype(2);
				remapper.updateRtype(recruit);
			}else if(ctype == 2 && !ccdate.before(today)){
				recruit.setRtype(1);
				remapper.updateRtype(recruit);
			}
		}
		//기업리뷰가져오기
		int reviewcount = revzMapper.cccnt(cid);
		List<RecruitDTO> openData = remapper.recruitCompanyOpen(cid);
		List<ReviewDTO> revData = revzMapper.reviewzzList(cid);
		System.out.println(openData);
		mm.addAttribute("mainData", openData);
		mm.addAttribute("dto", cccmapper.deatilCompany(cid));
		mm.addAttribute("revData", revData);
		mm.addAttribute("revcount",reviewcount);
		return "company/detail";
	}

	// 기업정보수정

	@GetMapping("modify")
	String modify(Model mm, HttpSession session) {
		String cid = (String) session.getAttribute("cid");
		mm.addAttribute("dto", cccmapper.deatilCompany(cid));

		return "company/modify";
	}

	@PostMapping("modify")
	String modifyReg(MCompanyDTO dto, PageData pd, HttpServletRequest request, HttpSession session) {
		pd.setMsg("수정실패");
		pd.setGoUrl("/company/modify");

		if (dto.getCcompanyFile() == null) {
			System.out.println("기존파일없음");
			fileSavecompany(dto, request);
		}
		if (dto.getClogo() == null) {
		System.out.println("로고파일없음");
		fileloSavecompany(dto, request);
		}

		int cnt = cccmapper.modifffy(dto); // 메서드안의 값이 들어와서 cnt 값이 1이됌

		if (cnt > 0) {

			pd.setMsg("수정되었습니다. 다시로그인 부탁드립니다.");
			pd.setGoUrl("/login/main");
			// 세션수정
			MCompanyDTO companysession = cccmapper.deatilaaaCompany(dto.getCid());
			session.setAttribute("companysession", companysession);
			session.invalidate();

		}

		return "join/join_alert";
	}

	// 개인정보삭제

	@GetMapping("delete")
	String delete(HttpSession session, Model mm, ApplicantDTO adto,RecruitDTO dto,PageData pd) {
		String cid = (String) session.getAttribute("cid");
		List<RecruitDTO> openData = remapper.recruitCompanyOpen(cid);
		System.out.println("ㅎㅇ"+openData.size());
		if(openData.size() > 0) {
			pd.setMsg("진행중가 공고가 있을 시 삭제가 불가능합니다.");
			pd.setGoUrl("/company/list/"+cid +"/1");
			return "join/join_alert";
		}
		
		mm.addAttribute("cid", cid);

		return "company/delete";
	}

	@PostMapping("delete")
	String deleteReg(
		MCompanyDTO dto, PageData pd, ApplicantDTO adto, HttpSession session) {

		pd.setMsg("삭제실패");
		pd.setGoUrl("/company/delete");
		
		
		
		// 탈퇴할 기업 조회
		MCompanyDTO byedto = cccmapper.deatilCompany(dto.getCid());
		
		// 탈퇴할 기업의 지원내역들
		List<ApplicantDTO> byeapp = endCompanyMapper.endapplist(byedto.getCid());
		
		// 탈퇴할 기업의 공고 조회
		List<RecruitDTO> byerecruit = endCompanyMapper.endrecruitList(dto.getCid());
		
		// 결제내역에 탈퇴일 추가
		paym.endMem(dto.getCid());
		
		int cnt = cccmapper.delettt(dto); // 메서드안의 값이 들어와서 cnt 값이 1이됌

		if (cnt > 0) {
			// 탈퇴할 기업추가
			endCompanyMapper.endCompanyInsert(byedto);
			// 탈퇴할 기업의 지원내역 추가(insert 한번에 안되더군요)
			for (ApplicantDTO applicant : byeapp) {
				endCompanyMapper.endappinsert(applicant);
			}
			// 탈퇴할 기업의 공고추가
			for (RecruitDTO recruitDTO : byerecruit) {
				endCompanyMapper.endrecruitInsert(recruitDTO);
			}

			pd.setMsg("삭제되었습니다.");
			pd.setGoUrl("/");
			session.invalidate();
		}

		return "join/join_alert";
	}

	// 파일수정삭제
	@PostMapping("fileDelete")
	String fileDelete(MCompanyDTO dto, PageData pd, HttpServletRequest request) {
		System.out.println(dto + "난 씨아이디");
		MCompanyDTO delDTO = cccmapper.deatilCompany(dto.getCid());
		pd.setMsg("파일 삭제실패");
		pd.setGoUrl("/company/modify");

		int cnt = cccmapper.fileDelete(dto);
		System.out.println("modifyReg:" + cnt);
		if (cnt > 0) {
			fileDeleteModule(delDTO, request);
			pd.setMsg("파일 삭제되었습니다.");
		}

		return "join/join_alert";
	}
	// 로고수정삭제
			@PostMapping("fileloDelete")
			String fileloDelete(MCompanyDTO dto, PageData pd, HttpServletRequest request) {
				System.out.println(dto + "안녕사ㅔ유'");
				MCompanyDTO delDTO = cccmapper.deatilCompany(dto.getCid());
				pd.setMsg("파일 삭제실패");
				pd.setGoUrl("/company/modify");
				
				int cnt = cccmapper.fileloDelete(dto);
				System.out.println("modifyReg:" + cnt);
				if (cnt > 0) {
					fileloDeleteModule(delDTO, request);
					pd.setMsg("파일 삭제되었습니다.");
				}

				return "join/join_alert";
			}
			// 로고파일저장
			void fileloSavecompany(MCompanyDTO cto, HttpServletRequest request) {

				// 파일 업로드 유무 확인
				if (cto.getLommff() == null || cto.getLommff().isEmpty()) {
					return;
				}

				String path = request.getServletContext().getRealPath("companylogoup");
				// path = "C:\\Final_Team\\owang\\src\\main\\webapp\\member\\companyup";

				int dot = cto.getLommff().getOriginalFilename().lastIndexOf(".");
				String fDomain = cto.getLommff().getOriginalFilename().substring(0, dot);
				String ext = cto.getLommff().getOriginalFilename().substring(dot);

				cto.setClogo(fDomain + ext);
				File ff = new File(path + "\\" + cto.getClogo());
				int cnt = 1;
				while (ff.exists()) {

					cto.setClogo(fDomain + "_" + cnt + ext);
					ff = new File(path + "\\" + cto.getClogo());
					cnt++;
				}

				try {
					FileOutputStream fos = new FileOutputStream(ff);

					fos.write(cto.getLommff().getBytes());

					fos.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 파일지우기
	void fileDeleteModule(MCompanyDTO delDTO, HttpServletRequest request) {
		if (delDTO.getCcompanyFile() != null) {
			String path = request.getServletContext().getRealPath("companyup");
			/*
			 * path = "E:\\BackEnd_hakwon\\N_SpringWorks\\exboard2\\src\\main\\webapp\\up";
			 */

			new File(path + "\\" + delDTO.getCcompanyFile()).delete();
		}
	}
	// 파일지우기
			void fileloDeleteModule(MCompanyDTO delDTO, HttpServletRequest request) {
				if (delDTO.getClogo() != null) {
					String path = request.getServletContext().getRealPath("companylogoup");
					/*
					 * path = "E:\\BackEnd_hakwon\\N_SpringWorks\\exboard2\\src\\main\\webapp\\up";
					 */

					new File(path + "\\" + delDTO.getClogo()).delete();
				}
			}
}