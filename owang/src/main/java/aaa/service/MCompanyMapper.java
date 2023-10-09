package aaa.service;




import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import aaa.model.MCompanyDTO;

@Mapper
public interface MCompanyMapper {
		List<MCompanyDTO> clogoList();
		//기업입력
		void insertCompany(MCompanyDTO company);
		//기업 로그인
		int loginCompany(String userid, String userpw);
		MCompanyDTO deatilaaaCompany(String cid);
		//기업 아이디체크
		int idChk(String cid);
		// 기업타입 가져오기
		Integer getctype(String cid); 
		
		// 회사이름 가져오기
		String getzzcname(String cid); 
		
		// 게시글 상세보기
		
		//기업 정보 상세 수정 삭제 
		MCompanyDTO deatilCompany(String cid);
		int modifffy(MCompanyDTO dto); 
		int delettt(MCompanyDTO company);
		int fileDelete(MCompanyDTO dto);
		int fileloDelete(MCompanyDTO dto);
		// 결제회원처리
		void paycmember(MCompanyDTO dto);
		// 로그인회원처리
		void logincmember(MCompanyDTO dto);
		//관리자 기업정보수정
		int adminmodifffy(MCompanyDTO dto); 
		// 아이디확인
		int findloCompany(String fname, String femail);	
		MCompanyDTO findid(String fname, String femail);	
		
		// 비번찾기
		int findpwComp(String fid, String femail);
		int modifycpw(String cid, String cpw); 
		String findemail(String userid);
		
		// 공고 진행중 개수조회
		
		
}

