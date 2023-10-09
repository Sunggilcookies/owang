package aaa.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import aaa.model.RecruitDTO;
import aaa.model.SoloDTO;

@Mapper
public interface AdminSolo {

	// 게시글 정렬
		List<SoloDTO> soloList(SoloDTO dto);
		List<SoloDTO> soloonList(SoloDTO dto);
		List<SoloDTO> solooffList(SoloDTO dto);
	
	
	//전체회원관리 수정삭제
	SoloDTO detailSolo(String sid);
	int delettt(String sid);

	// 게시글 페이지 계산
		int soloListCnt();
		int soloonListCnt();
		int solooffListCnt();
	// 게시글 정렬 재직인증x
	List<SoloDTO> solocomList(SoloDTO dto);

	// 재직 승인
	int checkoutFile(int sno);
	
	// 게시글 페이지 계산
	int solocomListCnt();
	//서치
	List<SoloDTO> searcha( String keyword,String searchOption);
	//서치 카운트톨탈
	int searchcount(String keyword,String searchOption);
}
