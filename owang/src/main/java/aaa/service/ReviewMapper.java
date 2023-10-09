package aaa.service;



import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import aaa.model.ReviewDTO;




@Mapper
public interface ReviewMapper {

	// 기업 회원 정리
	List<ReviewDTO> reviewList(ReviewDTO dto);
	List<ReviewDTO> reviewList2(String cid);
	List<ReviewDTO> reviewList3(String sid);
	//리뷰리스트
	List<ReviewDTO> reviewzzList(String cid);
	// 리뷰 페이지 계산
	int reviewCnt(String cid);
	
	// 리뷰 삽입
	int reveiwInsert(ReviewDTO dto);
	
	ReviewDTO reviewDetail(ReviewDTO dto);
	
	int reviewModify(ReviewDTO dto);
	
	int reviewDelete(ReviewDTO dto);
	
	// 리뷰 1개이상쓴사람
	int ccnt(String sid);
	// 기업리뷰수
	int cccnt(String cid);

	// 개인 리뷰 리스트
	List<ReviewDTO> soloreviewList(ReviewDTO dto);
}
