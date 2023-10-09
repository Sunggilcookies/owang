package aaa.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import aaa.model.AskDTO;


@Mapper //MyBatis - SQL 쿼리를 실행하고 데이터베이스와 상호 작용하는 코드를 내장
//메서드를 통해 SQL 쿼리를 호출
//밑에메서드들로 sql쿼리 호출한거임
public interface AskMapper {
	
	//리스트 
	List<AskDTO> askList(AskDTO dto);
	//회원리스트
	List<AskDTO> useraskList(AskDTO dto);

	
	AskDTO detail(int id);
	
	int askInsert(AskDTO dto);
	
	int askDelete(AskDTO dto);
	
	int askModify(AskDTO dto);
	
	int listCnt();
	
	int maxId();
	
	void addCount(int id);
	
	int fileDelete(AskDTO dto);
	
	int insertReply(AskDTO dto);
	
	int gidChk(String gid);
	
	// 관리자 인덱스에서 사용하기 위해 생성 - gid가 1개만 있는 게시글(미답변)의 개수
	int noReCnt();
	
	
}
