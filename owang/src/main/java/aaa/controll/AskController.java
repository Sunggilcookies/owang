package aaa.controll;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.List;

import aaa.model.AdminDTO;
import aaa.model.AskDTO;
import aaa.model.MCompanyDTO;
import aaa.model.SoloDTO;
import aaa.service.AskMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.util.FileSystemUtils;

@Controller
@RequestMapping("/ask") // QnA게시판
public class AskController {

	@Resource
	AskMapper mapper;

	@RequestMapping("list/{page}")
	String list(Model mm, AskDTO dto, HttpSession session) {
		String sid = (String) session.getAttribute("sid");
		String cid = (String) session.getAttribute("cid");
		AdminDTO adto = (AdminDTO) session.getAttribute("adminSession");
		String zuserid;
		
		
		if (sid == null) {
			zuserid = cid;
		} else {
			zuserid = sid;
		}
		dto.calc(mapper.listCnt());
		dto.setPname(zuserid);
		System.out.println("askdto" + dto);
		
		 if (sid == null && cid == null && adto == null) {
		        dto.setMsg("회원만 이용 가능합니다");
		        dto.setGoUrl("/");
		        return "ask/ask_alert";
		 }

		
	
		
		//값을 리스트로받아 그다음에
		//그리스트를 하나씩 하나씩 
	

		if (adto != null) {
			System.out.println("관리자입니다.");
			List<AskDTO> admindata = mapper.askList(dto);
			mm.addAttribute("mainData", admindata);
		} else {	//개인 
			System.out.println("유저입니다.");
			List<AskDTO> data = mapper.useraskList(dto);
			mm.addAttribute("mainData", data);
		}

		/*
		 * 관리자일때 List<AskDTO>data = mapper.askList();
		 */

		return "ask/ask_list";
	}

	@RequestMapping("detail/{page}/{id}")
	String detail(Model mm, @PathVariable int page, @PathVariable int id) {
		mapper.addCount(id);// 조회수증가
		AskDTO dto = mapper.detail(id);
		dto.setPage(page);
		
		mm.addAttribute("askdto", dto);
		
		String gid = String.valueOf(dto.getGid()); // int를 문자열로 변환
		int result = mapper.gidChk(gid);
		
		//gid count 1
		String type = "";
		
		if(result == 1) {
			type = "1";
		}
		//gid count 2
		if(result == 2) {
			type = "2";
		}
		mm.addAttribute("type",type);
		
		
//		mapper.gidChk(dto.getGid());
//		
//		String type = ""; // 초기화
//		if(mapper.gidChk(dto.getGid())==1) {
//			type = "1";
//		}
//		if(mapper.gidChk(dto.getGid())==2) {
//			type="2";
//		}
//		
//		mm.addAttribute("type",type);
		
		 
		
		
		//dto.setHasReply(dto.getSeq() > 0); // seq가 0보다 크면 답변이 있다고 판단
		
		//답변o-수정삭제x
				//dto.setHasReply(dto.getSeq() > 0); // 예시: seq가 0보다 크면 답변이 있다고 판단
				//gid가 같다면
				
				//dto.setHasReply(dto.getGid() != dto.getGid());
				/*
				 * 매퍼에서 이 gid인 애가 몇개야? 1 2 1이면 답변을안달고 2이면 두개가달리면
				 * 
				 * if (dto.setHasReply(dto.getGid() == dto.getGid())) { dto.setHasReply(true);
				 * 답변이 있다면 true로 설정 } else { dto.setHasReply(false); // 답변이 없다면 false로 설정 }
				 */				 
				
		return "ask/ask_detail";
	}

	// 글을써보자
	@GetMapping("insert/{page}")
	String insert(Model mm, AskDTO dto, HttpSession session, SoloDTO sdto, MCompanyDTO cdto) {
		String sid = (String) session.getAttribute("sid");
		String cid = (String) session.getAttribute("cid");

		System.out.println(sid + cid);
		if (sid == null) {
			mm.addAttribute("uid", cid);
		} else {
			mm.addAttribute("uid", sid);
		}

		return "ask/ask_insertForm";
	}
	
	 // 주문번호를 가지고 온 경우
	   @GetMapping("insert/{page}/{impUid}")
	   String insertImpUid(Model mm, HttpSession session, AskDTO dto, @PathVariable String impUid) {
	      String sid = (String) session.getAttribute("sid");
	      String cid = (String) session.getAttribute("cid");
	      if (sid == null) {
	         mm.addAttribute("uid", cid);
	      } else {
	         mm.addAttribute("uid", sid);
	      }
	      return "ask/ask_insertForm";
	   }


	@PostMapping("insert/{page}")
	String insertReg(AskDTO dto, HttpServletRequest request) {

		dto.setId(mapper.maxId() + 1);
		fileSave(dto, request);
		dto.setPname(request.getParameter("uid"));
		System.out.println(dto);
		mapper.askInsert(dto);
		dto.setMsg("게시글 등록되었습니다.");
		dto.setGoUrl("/ask/list/1");

		return "ask/ask_alert";
	}
	
	  // 주문번호를 가지고 온 경우
	   @PostMapping("insert/{page}/{impUid}")
	   String insertReg2(AskDTO dto, HttpServletRequest request, @PathVariable String impUid) {
	      dto.setId(mapper.maxId() + 1);
	      fileSave(dto, request);
	      dto.setPname(request.getParameter("uid"));
	      // 주문번호를 넣어주기
	      dto.setImp(impUid);
	      mapper.askInsert(dto);
	      dto.setMsg("게시글 등록되었습니다.");
	      dto.setGoUrl("/ask/list/1");

	      return "ask/ask_alert";
	   }
	   
	   

	// 사진첨부

	void fileSave(AskDTO dto, HttpServletRequest request) {

		// 파일 업로드 확인
		if (dto.getMmff().isEmpty()) {
			return;
		}

		String path = request.getServletContext().getRealPath("askup");
		//path = "C:\\Users\\콩쥐\\Desktop\\final\\fighting\\Spring_TeamVer_1\\owang\\src\\main\\webapp\\askup";
		
		System.out.println(path);

		int dot = dto.getMmff().getOriginalFilename().lastIndexOf("."); // . 위치 찾음
		String fDomain = dto.getMmff().getOriginalFilename().substring(0, dot); // 확장자 제외한 부분 추출
		String ext = dto.getMmff().getOriginalFilename().substring(dot); // 확장자 추출

		dto.setUpfile(fDomain + ext); // 새로운 이름 설정
		File ff = new File(path + "\\" + dto.getUpfile()); // 전체파일 경로 설정
		int cnt = 1; // 파일이름 중복을 피하기위해
		while (ff.exists()) {
			dto.setUpfile(fDomain + "_" + cnt + ext);
			ff = new File(path + "\\" + dto.getUpfile());
			cnt++;
		} // abc.jpg (중복)-> abc_2.jpg로 바꾼다

		try {
			FileOutputStream fos = new FileOutputStream(ff);

			fos.write(dto.getMmff().getBytes()); // dto.getMmff() - 파일데이터가져옴
													// dto.getMmff() - 문자열을 바이트 배열로 변환
													// fos.write - 바이트 배열을 파일에 씁니다. 이 부분은 실제로 파일에 데이터를 쓰는 부분으로, 파일을 생성하고
													// 파일에 데이터를 기록
			fos.close(); // 닫아줌!
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 글을지워보자꾸나
	@RequestMapping("delete/{page}/{id}")
	String delete(AskDTO dto, HttpServletRequest request, @PathVariable int id) {

//		AskDTO adto = mapper.detail(id);
//		System.out.println("ask 삭제 dto"+adto);
		dto.setMsg("삭제에 실패했습니다");
		dto.setGoUrl("/ask/detail" + dto.getPage() + "/" + dto.getId());

		AskDTO delDTO = mapper.detail(dto.getId());

		// int cnt = mapper.askDelete(delDTO);
		int cnt = mapper.askDelete(dto);

		System.out.println("deleteReg:" + cnt);

		if (cnt > 0) {

			fileDeleteModule(delDTO, request);

			dto.setMsg("삭제완료되었습니다.");
			dto.setGoUrl("/ask/list/1");
		}
		
	
		
		return "ask/ask_alert";
	}

	// 수정페이지
	@GetMapping("modify/{page}/{id}")
	String modify(Model mm, @PathVariable int page, @PathVariable int id) {

		AskDTO dto = mapper.detail(id);
		dto.setPage(page);
		mm.addAttribute("dto", dto);

		return "ask/ask_modifyform";
	}

	@PostMapping("modify/{page}/{id}")
	String modifyReg(AskDTO dto, HttpServletRequest request) {

		dto.setMsg("QnA게시글 수정 실패입니다.");
		dto.setGoUrl("/ask/modify" + dto.getPage() + "/" + dto.getId());

		// 수정된 데이터를 데이터베이스에 저장
		int cnt = mapper.askModify(dto);

		System.out.println("1:1의 cnt" + cnt);

		if (cnt > 0) {

			if (dto.getUpfile() == null) {
				fileSave(dto, request);
			}

			mapper.askModify(dto);
			dto.setMsg("1:1 -수정되었습니다.");
			dto.setGoUrl("/ask/detail/" + dto.getPage() + "/" + dto.getId());
		}

		return "ask/ask_alert";
	}

	// 답변을만들어보자!
	@GetMapping("reply/{page}/{id}")
	String reply(Model mm, @PathVariable int page, @PathVariable int id) {

		AskDTO dto = new AskDTO();
		dto.setPage(page);
		dto.setId(id);
		//dto.setTitle("호로로롤로"); // 초기값 설정
		mm.addAttribute("askdto", dto);

		return "ask/ask_replyForm";
	}

//	@PostMapping("reply/{page}/{id}")
//	String replyReg(AskDTO dto, HttpServletRequest request, @PathVariable int page, @PathVariable int id) {
//	    dto.setPage(page);
//	    dto.setGid(id); // 부모 글의 id를 gid로 설정하여 답글 그룹을 형성
//	    dto.setLev(1);  // 답글의 계층을 1로 설정
//	    dto.setSeq(1);  // 답글의 순서를 1로 설정 (첫 번째 답글)
//
//	    // Mapper를 사용하여 답글 데이터를 데이터베이스에 저장
//	    int result = mapper.insertReply(dto);
//	    
//
//	    if (result > 0) {
//	        dto.setMsg("답글 작성이 완료되었습니다.");
//	        dto.setGoUrl("/ask/detail/" + page + "/" + id);
//	    } else {
//	        dto.setMsg("답글 작성에 실패했습니다.");
//	        dto.setGoUrl("/ask/reply/" + page + "/" + id);
//	    }
//	    
//	    return "ask/ask_alert";
//	}

	@PostMapping("reply/{page}/{id}")
	String replyReg(AskDTO dto, HttpServletRequest request, @PathVariable("page") int page,
			@PathVariable("id") int id) {
		dto.setPage(page);
		dto.setGid(id); // 부모 글의 id를 gid로 설정하여 답글 그룹을 형성
		dto.setLev(1); // 답글의 계층을 1로 설정
		dto.setSeq(1); // 답글의 순서를 1로 설정 (첫 번째 답글)
		fileSave(dto, request);
		

		// Mapper를 사용하여 답글 데이터를 데이터베이스에 저장
		int result = mapper.insertReply(dto);

		if (result > 0) {
			dto.setMsg("답변 작성이 완료되었습니다.");

			// 답변이 있는 글의 detail 페이지로 이동
			// dto.setGoUrl("/ask/detail/" + page + "/" + id);
			// 답변작성 시 목록으로 가게 이동
			dto.setGoUrl("/ask/list/1");

		} else {
			dto.setMsg("답변 작성에 실패했습니다.");
			dto.setGoUrl("/ask/reply/" + page + "/" + id);
		}

		return "ask/ask_alert";
	}

	@PostMapping("fileDelete")
	String fileDelete(AskDTO dto, HttpServletRequest request) {

		AskDTO delDTO = mapper.detail(dto.getId());
		dto.setMsg("파일삭제에 실패했습니다");
		dto.setGoUrl("/ask/modify/" + dto.getPage() + "/" + dto.getId());

		int cnt = mapper.fileDelete(dto);
		System.out.println("modifyReg" + cnt);
		if (cnt > 0) {
			fileDeleteModule(delDTO, request);
			dto.setMsg("업로드하신 파일 삭제되었습니다");
		}

		return "ask/ask_alert";
	}

	// 파일 삭제
	void fileDeleteModule(AskDTO delDTO, HttpServletRequest request) {
		if (delDTO.getUpfile() != null) {// 파일이 있다면
			// 시스템 경로를 문자열로 저장
			String path = request.getServletContext().getRealPath("askup");
			//path = "C:\\Users\\콩쥐\\Desktop\\final\\fighting\\Spring_TeamVer_1\\owang\\src\\main\\webapp\\askup";

			new File(path + "\\" + delDTO.getUpfile()).delete();

			// new File - 새로운 파일 객체 생성
			// path - 파일이 저장된 디렉토리의 실제 파일 시스템 경로
			// delDTO.getUpfile(): delDTO 객체에서 파일 이름을 가져옴 - 실제로 삭제할 파일의 이름
			// .delete() - 파일 객체가 나타내는 파일을 삭제
		}

	}
	
	   
	   // 파일 다운로드
	   @RequestMapping("/askup/download/{filename}")
	   void download(@PathVariable String filename,
	               HttpServletRequest request,
	               HttpServletResponse response) {
	      System.out.println("이벤트체크=> 파일 다운로드");
	      try {
	         System.out.println(filename+", "+"파일님 오셧나요");
	         String path = request.getServletContext().getRealPath("askup");
	         FileInputStream fis = new FileInputStream(path + "\\"+filename);
	         // 인코딩 설정
	         String encFName = URLEncoder.encode(filename,"utf-8");
	         response.setHeader("Content-Disposition", "attachment; filename=" + encFName);
	         ServletOutputStream sos = response.getOutputStream();
	         byte[] buf = new byte[1024];
	         while (fis.available()>0) {
	            int len = fis.read(buf); // 읽은후 buf에 저장
	            // len = 넣은 길이
	            sos.write(buf,0,len);// buf 0부터 len 만큼 
	         }
	         sos.close();
	         fis.close();
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	      }

	   }// 파일 다운로드

}
