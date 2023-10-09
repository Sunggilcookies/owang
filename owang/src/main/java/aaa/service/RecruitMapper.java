package aaa.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import aaa.model.RecruitDTO;

@Mapper
public interface RecruitMapper {
	// 마감타입
	void updateRecruitMagam(RecruitDTO dto);
	
	// 게시글 정렬
	List<RecruitDTO> recruitList(RecruitDTO dto);

	// 게시글 정렬
	List<RecruitDTO> allrecruitList(RecruitDTO dto);

	// 채용중인 공고 수 계산
	int recruitCnt(String cid, Date cdate);

	List<RecruitDTO> companyROpen(RecruitDTO dto);
	List<RecruitDTO> companyRClose(RecruitDTO dto);
	
	// 재훈 추가
	List<RecruitDTO> companyROpenHOT(RecruitDTO dto);

	// 게시글 페이지 계산
	int recruitListCnt();
	
	int companyROpenCnt();
	int companyRCloseCnt();
	int icompanyROpenCnt(String cid);
	// 조회수 증가
	int recruitAddCont(int recruitId);

	// 게시글 상세보기
	RecruitDTO recruitDetail(int recruitId);

	void updateRtype(RecruitDTO recruit);

	// 게시글 추가
	int recruitInsert(RecruitDTO dto);

	// 게시글 추가할때 id 맨위로 올리기
	int recruitMaxId();

	// 게시글 삭제
	int recruitDelete(int id);

	// 게시글 수정
	int recruitModify(RecruitDTO dto);

	// 달력 데이ㅣ터 삽입 테스트
	List<RecruitDTO> recruitTest();

	// 검색기능 테스트
	List<RecruitDTO> searchRecruit(@Param("keyword") String keyword, @Param("searchOption") String searchOption);

	// 기업 디테일에서 작성한 채용만 보기
	List<RecruitDTO> recruitCompanyDetail(RecruitDTO dto);
	List<RecruitDTO> recruitCompanyOpen(String cid);
	List<RecruitDTO> recruitCompanyClose(RecruitDTO dto);
	int recruitCompanyCloseCnt(String cid);
	
	// 결제시 일률적으로 채용rtype 바꿔줌
	 void payRTypeByCid(String cid);
	 
	 
	 // 기간동안 날짜별 시작된 공고 개수 리스트
	 List<Map<String, Object>> rctRegCnt(String startDate, String endDate);
	 // 기간동안 시작된 공고 개수
	 int rctRangeRegCnt(String startDate, String endDate);
}
