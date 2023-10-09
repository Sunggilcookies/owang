package aaa.controll;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import aaa.model.MCompanyDTO;
import aaa.model.NoticeDTO;
import aaa.model.QnaDTO;
import aaa.model.RecruitDTO;
import aaa.service.AdminCompanyMapper;
import aaa.service.AskMapper;
import aaa.service.MCompanyMapper;
import aaa.service.NoticeMapper;
import aaa.service.QnaMapper;
import aaa.service.RecruitMapper;
import jakarta.annotation.Resource;

@Controller
@RequestMapping("/main")
public class MainController {
	
	@Resource
	AdminCompanyMapper adminMapper;
	
	@Resource // 맵퍼가져오기
	NoticeMapper ntmp; //공지맵퍼
	
	@Resource
	MCompanyMapper crcMapper;
	
	@Resource
	RecruitMapper recruitMapper;
	
	
	@RequestMapping("")
	String main(Model mm, 
			NoticeDTO ndto,
			RecruitDTO rdto,
			MCompanyDTO mdto) {
		
		//////// 공지사항 시작 ////////////
		ndto.setPage(1);
		ndto.setLimit(1);
		ndto.calc(ntmp.listCnt());
		//System.out.println(ndto);
		List<NoticeDTO>data = ntmp.list(ndto);
		mm.addAttribute("mainData", data);
		
		// /////// 공지사항 끝  ////////////////
		
		//////////// 공고리스트 시작  /////////////////
		Date today = new Date();
		rdto.setPage(1);
		rdto.calc(recruitMapper.recruitListCnt());

		List<RecruitDTO> alldata = recruitMapper.allrecruitList(rdto);

		for (RecruitDTO recruit : alldata) {
			// 공고의 회사 타입 불러오기
			Integer ctype = (int) crcMapper.getctype(recruit.getCid());
			Date ccdate = recruit.realMagam;

			if (ccdate.before(today) || ctype == 1) {
				recruit.setRtype(2);
				recruitMapper.updateRtype(recruit);
			}
		}

		// "채용 가능" 탭에 대한 페이지 정보 계산
		RecruitDTO openDto = new RecruitDTO();
		openDto.setPage(1);
		
		openDto.calc(recruitMapper.companyROpenCnt());
		// 지원자수가 많은순으로 정렬
		List<RecruitDTO> openData = recruitMapper.companyROpenHOT(openDto);
		
		
		mm.addAttribute("openData", openData);
		mm.addAttribute("openDto", openDto); // "채용 가능" 탭 페이지 정보

		//////////// 공고리스트 끝 /////////////////
		
		////////////기업 리스트 시작  /////////////////
		mdto.setPage(1);
		mdto.calc(adminMapper.adminAddCont());
		mdto.setLimit(9);
		//System.out.println(mdto);
		List<MCompanyDTO> mdata = adminMapper.adminCompanyList(mdto);
		
		mm.addAttribute("mdata",mdata);
		
		// 신규입점기업
		mm.addAttribute("realData", adminMapper.companyNewIn(mdto));
		 
		////////////기업 리스트 시작  /////////////////
		
		
		
		
		return "main";
	}
	
}
