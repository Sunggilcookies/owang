package aaa.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import aaa.model.ApplicantDTO;
import aaa.model.AskDTO;
import aaa.model.MCompanyDTO;
import aaa.model.RecruitDTO;
import aaa.model.SoloDTO;


@Mapper
public interface EndCompanyMapper {
	// 삭제한 회원 삽입
	int endCompanyInsert(MCompanyDTO dto);
	
	// 아이디 검사
	int idChk(String cid);
	
	// 기업탈퇴회원리스트
	List<MCompanyDTO> endCompanyList(MCompanyDTO dto);
	
	// 기업 탈퇴 회원 
	int endCompanycnt();
	
	// 탈퇴 기업 지원 내역 저장 
	void endappinsert(ApplicantDTO dto);
	
	// 기업의 지원 내역 리스트 
	List<ApplicantDTO> endapplist(String cid); 
	
	// 탈퇴할 기업의 공고 조회 
	List<RecruitDTO> endrecruitList(String cid);
	
	// 기업의 공고 추가
	int endrecruitInsert(RecruitDTO dto);
	
	// 1:1 내역 저장(아직안함)
	int endaskInsert(AskDTO dto);
	
	RecruitDTO endrecruitone(String recruit_title);
	
	// 탈퇴한 기업의 공고 조회endRecruitRealList
	List<RecruitDTO> endRecruitRealList(String cid);
	
	// 탈회한 기업의 공고 이력내역 조회
	List<ApplicantDTO> endApplicantList(String cid, String rectuit_title);
}
