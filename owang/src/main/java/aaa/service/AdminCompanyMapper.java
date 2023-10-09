package aaa.service;



import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import aaa.model.MCompanyDTO;




@Mapper
public interface AdminCompanyMapper {

	// 전체회원
	List<MCompanyDTO> adminCompanyList(MCompanyDTO dto);
	// 기업 미등록 회원 정리
	List<MCompanyDTO> companyList(MCompanyDTO dto);
	// 기업 등록 회원 정리
	List<MCompanyDTO> companyCapprovalList(MCompanyDTO dto);
	// 기업 등록 전체 회원 정리
	List<MCompanyDTO> companyCapprovalList2(MCompanyDTO dto);
	
	// 재훈추가 - 신규가입기업
	List<MCompanyDTO> companyNewIn(MCompanyDTO dto);
	
	List<MCompanyDTO> join(MCompanyDTO dto);

	// 기업 인증회원 페이지 계산
	int adminAddCont();
	// 기업 전체 페이지 계산
	int adminAddAllCnt();
	// 기업 미인증회원 페이지 계산
	int adminAddMiCont();
	
	// 기업 회원 상세보기
	MCompanyDTO adminCDetail(int cno);

	// 기업 체크
	int checkoutFile(int cno);
	
	// 기업 삭제
	int deleteCompany(int cno);
	
	// 기업 검색기능 테스트
	List<MCompanyDTO> searchCompany(@Param("keyword") String keyword, @Param("searchOption") String searchOption);
	
}
