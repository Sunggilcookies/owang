package aaa.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;


import aaa.model.SoloDTO;
import aaa.model.SoloResumeDTO;

@Mapper
public interface EndSoloMapper {
	// 삭제한 회원 삽입
	int endSoloInsert(SoloDTO dto);
	
	// 아이디 검사
	int idChk(String sid);
	
	// 개인탈퇴회원리스트
	List<SoloDTO> endSoloList();
	
	// 개인 탈퇴 회원 
	int endSolocnt();
}
