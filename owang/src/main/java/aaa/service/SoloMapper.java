package aaa.service;

import org.apache.ibatis.annotations.Mapper;


import aaa.model.SoloDTO;

@Mapper
public interface SoloMapper {

	// 회원 입력
	void insertSolo(SoloDTO solo);

	// 로그인 비교
	int loginSolo(String userid, String userpw);

	// 아이디확인
	int idChk(String sid);

	// 개인 정보 상세 수정 삭제
	SoloDTO detailSolo(String sid);
	int delettt(SoloDTO dto);
	int modifffy(SoloDTO dto);
	int fileDelete(SoloDTO dto);
	// 세션받아오기 객체
	SoloDTO resumeSolo(String userid);
	
	// 결제회원처리
	void paysmember(SoloDTO dto);
	

	void loginsmember(SoloDTO dto);
		
	
// 아이디확인
	int findloSolo(String fname, String femail);	
	SoloDTO findid(String fname, String femail);	
	
	// 비번찾기
	int findpwSolo(String fid, String femail);	
	int modifyspw(String sid, String spw);
	String findemail(String userid);
}
