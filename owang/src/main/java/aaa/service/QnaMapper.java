package aaa.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import aaa.model.NoticeDTO;
import aaa.model.QnaDTO;

@Mapper //MyBatis - SQL 쿼리를 실행하고 데이터베이스와 상호 작용하는 코드를 내장
//메서드를 통해 SQL 쿼리를 호출
//밑에메서드들로 sql쿼리 호출한거임
public interface QnaMapper {

	
	List<QnaDTO>list(QnaDTO dto);
//	
//	NoticeDTO detail(int id);
//	
//	int insseerr(NoticeDTO dto);
//	
//	int delettt(NoticeDTO dto);
//	
//	int modifffy(NoticeDTO dto);
//	
	int listCnt();
//	
//	int maxId();
//	
//	void addCount(int id);
//	
//	int fileDelete(NoticeDTO dto);
//	
//	int idPwChk(NoticeDTO dto);
	
}
