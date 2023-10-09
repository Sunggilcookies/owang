package aaa.controll;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import aaa.model.QnaDTO;
import aaa.service.QnaMapper;
import java.util.List;
import jakarta.annotation.Resource;

@Controller
@RequestMapping("/qna") //QnA
public class QnaController {
	
	@Resource //맵퍼ok
	QnaMapper qmp;
	
	//qna list
	@RequestMapping("qna/{page}")
	String qna(Model mm, QnaDTO dto) {
		
		dto.calc(qmp.listCnt()); // 페이징처리
		System.out.println("dto" + dto);
		List<QnaDTO>data = qmp.list(dto);
		mm.addAttribute("mainData",data);
		
		
		return "qna/qna";
	}
	
	
	
	
	
	
	//화룡님
//	@RequestMapping("qna/qna_view1")
//	String qna_view1() {
//		
//		return "qna/qna_view1";
//	}
//	
//	@RequestMapping("qna/qna_view2")
//	String qna_view2() {
//		
//		return "qna/qna_view2";
//	}
//	
//	
//	@RequestMapping("qna/qna_view3")
//	String qna_view3() {
//		
//		return "qna/qna_view3";
//	}
//	
//	@RequestMapping("qna/qna_view4")
//	String qna_view4() {
//		
//		return "qna/qna_view4";
//	}
//	
//	@RequestMapping("qna/qna_view5")
//	String qna_view5() {
//		
//		return "qna/qna_view5";
//	}
//	
//	@RequestMapping("qna/qna_view6")
//	String qna_view6() {
//		
//		return "qna/qna_view6";
//	}
//	
//	@RequestMapping("qna/qna_view7")
//	String qna_view7() {
//		
//		return "qna/qna_view7";
//	}
	
	
	
}
