package aaa.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import aaa.model.SoloResumeDTO;

@Mapper
public interface SoloResumeMapper {
	
	// 이력서 리스트
	List<SoloResumeDTO> resumelist(String sid);
	
	// 이력서 세션 디테일
	SoloResumeDTO resumedetail(int rsid, String sid);
	
	// 이력서 디테일
	SoloResumeDTO resumefiledetail(int rsid);

	// 이력서 글쓰기
	int resumeinsert(SoloResumeDTO rdto);	
	
	// 이력서 총 개수
	int resumecnt(int rsid, String sid);
	
	// 이력서 삭제
	int resumedelete(int rsid);
	
	// 이력서 수정
	int resumemodify(SoloResumeDTO rdto);
	
	// 파일 삭제
	int fileDelete(int rsid);
}
