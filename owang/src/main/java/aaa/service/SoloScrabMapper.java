package aaa.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import aaa.model.ApplicantDTO;
import aaa.model.ScrabDTO;
import aaa.model.SoloResumeDTO;

@Mapper
public interface SoloScrabMapper {
	
	// 스크랩 등록
	void scinsert(ScrabDTO scdto);
	
	// 스크랩 리스트 출력	
	List<ScrabDTO> sclist(ScrabDTO scdto);

	// 스크랩 삭제
	int scdelete(int scid);
	
	// 스크랩 총 개수
	int sctotal();
	
	// 스크랩 세션 디테일
	ScrabDTO scdetail(int scid, String sid);
}
