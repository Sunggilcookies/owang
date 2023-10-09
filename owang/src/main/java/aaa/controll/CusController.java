package aaa.controll;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import aaa.model.NoticeDTO;

import aaa.service.NoticeMapper;
import jakarta.annotation.Resource;

import jakarta.servlet.http.HttpServletRequest;

//공지사항 컨트롤러
//연결다하고 html이름 변경하기
@Controller
@RequestMapping("/notice") //공지사항 - 화룡님꺼 이어서
public class CusController {
	
	@Resource // 맵퍼가져오기
	NoticeMapper ntmp; //공지맵퍼
	
	//리스트
	@RequestMapping("notice/{page}")
	String notice(Model mm, NoticeDTO dto) {
		
		dto.calc(ntmp.listCnt());
		System.out.println(dto);
		List<NoticeDTO>data = ntmp.list(dto);
		mm.addAttribute("mainData", data);
		
		return "notice/list";
	}
	
	//상세페이지 연결 ok
	@RequestMapping("detail/{page}/{id}")
	String detail(Model mm, @PathVariable int page, @PathVariable int id) {
		ntmp.addCount(id);//조회수증가
		NoticeDTO dto = ntmp.detail(id);
		dto.setPage(page);
		mm.addAttribute("ndto", dto); // dto로 넣어서 오류났었음
		return "notice/detail";
	}
	
	//글쓰기 페이지
	@GetMapping("insert/{page}")
	String insert(NoticeDTO dto) {

		return "notice/insertForm";
	}
	
	
	@PostMapping("insert/{page}")
	String insertReg(NoticeDTO dto, HttpServletRequest request) {
		dto.setId(ntmp.maxId()+1);
		fileSave(dto,request); // 사진첨부
		ntmp.insseerr(dto);
		dto.setMsg("작성되었습니다.");
		dto.setGoUrl("/notice/notice/1");
		System.out.println(dto);

		return "notice/alert";
	}
	
	//게시글삭제 - 관리자만 삭제ok

	
	@RequestMapping("delete/{page}/{id}") // 지워지는데 뷰에서 오류->ok
	String deleteReg(NoticeDTO dto, HttpServletRequest request) {
		
		
		dto.setMsg("삭제에 실패했습니다.");
		dto.setGoUrl("/notice/delete/"+dto.getPage()+"/"+dto.getId());
		
		NoticeDTO delDTO = ntmp.detail(dto.getId());
		
		
		
		int cnt = ntmp.delettt(delDTO);
		//int cnt = ntmp.delettt(dto);
		System.out.println("deleteReg:"+cnt);
		if(cnt>0) {
			
			dto.setMsg("삭제되었습니다.");
			dto.setGoUrl("/notice/notice/1"); // 수정완ok
		}

		return "notice/alert";
	}
	
	//수정페이지
	@GetMapping("modify/{page}/{id}")
	String modify(Model mm, @PathVariable int page, @PathVariable int id) {
		
		NoticeDTO dto = ntmp.detail(id);
		dto.setPage(page);
		mm.addAttribute("dto",dto);
		
		
		return "notice/modifyform";
	}
	
	@PostMapping("modify/{page}/{id}")
	String modifyReg(HttpServletRequest request, NoticeDTO dto) {
		
		dto.setMsg("수정 실패입니다.");
	    dto.setGoUrl("notice/modify" + dto.getPage() + "/" +dto.getId());

	    // 수정된 데이터를 데이터베이스에 저장
	    int cnt = ntmp.modifffy(dto);
	    
	    System.out.println("cnt!"+cnt);
	    
	    if (cnt > 0) {
	    		
	    		if(dto.getUpfile()==null) {
	    			fileSave(dto, request);
	    		}
	    		
	    		ntmp.modifffy(dto);
	        dto.setMsg(" 공지사항 - 수정되었습니다.");
	        dto.setGoUrl("/notice/detail/" + dto.getPage() + "/" + dto.getId());
	    }
		
		
		return "notice/alert"; 
	}
	
	
	//파일저장
	void fileSave(NoticeDTO dto, HttpServletRequest request) {
	
	//파일 업로드 유무 확인
		if(dto.getNoticemmf().isEmpty()) {
			return;
		}
		
		
		String path = request.getServletContext().getRealPath("up");
		//path = "C:\\Users\\콩쥐\\Desktop\\final\\0913 Merge\\Spring_Team\\owang\\src\\main\\webapp\\up";
		
		
		int dot = dto.getNoticemmf().getOriginalFilename().lastIndexOf("."); // .의 위치를 찾음
		String fDomain = dto.getNoticemmf().getOriginalFilename().substring(0, dot); //확장자를 제외한 부분을 추출
		String ext = dto.getNoticemmf().getOriginalFilename().substring(dot); //확장자를 추출
		
		
		dto.setUpfile(fDomain+ext);  //새로운 이름을 설정
		File ff = new File(path+"\\"+dto.getUpfile()); // 전체파일 경로 설정
		int cnt = 1; //파일 이름의 중복을 피하기위해서
		while(ff.exists()) {
			 
			dto.setUpfile(fDomain+"_"+cnt+ext);
			ff = new File(path+"\\"+dto.getUpfile());
			cnt++;
		} //love.jpg 이미 중복이라면 - > love_2.jpg로 바꾼다는뜻
		
		try {
			FileOutputStream fos = new FileOutputStream(ff);
			
			fos.write(dto.getNoticemmf().getBytes()); //dto.getMmff() - 파일데이터가져옴
												 //dto.getMmff() - 문자열을 바이트 배열로 변환
												//fos.write - 바이트 배열을 파일에 씁니다. 이 부분은 실제로 파일에 데이터를 쓰는 부분으로, 파일을 생성하고 파일에 데이터를 기록
			fos.close(); // 닫아줍니다.
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
}
	
	@PostMapping("fileDelete")
	String fileDelete(NoticeDTO dto, HttpServletRequest request) {
		
		NoticeDTO delDTO = ntmp.detail(dto.getId());
		dto.setMsg("공지사팡 파일 삭제에 실패했습니다");
		dto.setGoUrl("/notice/modify/"+dto.getPage()+"/"+dto.getId());
		
		int cnt = ntmp.fileDelete(dto);
		System.out.println("modifyReg"+cnt);
		if(cnt>0) {
			fileDeleteModule(delDTO,request);
			dto.setMsg("공지사항 파일 삭제되었습니다");
		}
		
		
		return "notice/alert";
	}	
	
	
	//파일 삭제
		void fileDeleteModule(NoticeDTO delDTO, HttpServletRequest request) {
			if(delDTO.getUpfile()!=null) {//파일이 있다면
				//시스템 경로를 문자열로 저장
				String path = request.getServletContext().getRealPath("up");
				path = "C:\\Users\\콩쥐\\Desktop\\final\\0912 Merge\\Spring_Team\\Spring_Team\\owang\\src\\main\\webapp\\up";
				
				 new File(path+"\\"+delDTO.getUpfile()).delete();
				
				// new File - 새로운 파일 객체 생성
				// path -  파일이 저장된 디렉토리의 실제 파일 시스템 경로
				//delDTO.getUpfile(): delDTO 객체에서 파일 이름을 가져옴 - 실제로 삭제할 파일의 이름
				//.delete() -  파일 객체가 나타내는 파일을 삭제
			}
		
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//화룡님기재하셨던거~
//	@RequestMapping("notice/notice_view1")
//	String notice_view1() {
//		
//		return "notice/notice_view1";
//	}
//	
//	@RequestMapping("notice/notice_view2")
//	String notice_view2() {
//		
//		return "notice/notice_view2";
//	}
//	
//	
//	@RequestMapping("notice/notice_view3")
//	String notice_view3() {
//		
//		return "notice/notice_view3";
//	}
//	
//	@RequestMapping("notice/notice_view4")
//	String notice_view4() {
//		
//		return "notice/notice_view4";
//	}
//	
//	@RequestMapping("notice/notice_view5")
//	String notice_view5() {
//		
//		return "notice/notice_view5";
//	}
	
	
	
}
