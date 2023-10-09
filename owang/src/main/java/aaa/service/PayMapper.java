package aaa.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import aaa.model.PaymentDTO;

@Mapper
public interface PayMapper {
	
	// 기업탈퇴 조회
	List<PaymentDTO> endpay(String cid);
	List<PaymentDTO> endpay2(String sid);

	// 결제목록보기
	List<PaymentDTO> list();
	

	// 결제상세보기
	PaymentDTO detail(String impUid);

	// 결제내역생성
	int insert(PaymentDTO dto);

//	// 결제내역삭제
//	int delete(PaymentDTO dto);

	// 결제취소시 금액 0으로 수정하기
	void payCancel(String impUid);

	// 아임포트고유번호로 db에서 아이디 찾기
	String idget(String impUid);

	// id로 impuid리스트 가져오기
	List<String> impuids(String id);

	// 일별 매출액
	List<Map<String, Object>> dailytotal(Date startDate, Date endDate);

	// 아이디별 매출액
	List<Map<String, Object>> totalbys(Date startDate, Date endDate);

	List<Map<String, Object>> totalbyc(Date startDate, Date endDate);

	// 상품별 매출액
	List<Map<String, Object>> totalbyp(Date startDate, Date endDate);

	// 탈퇴회원 내역에 탈퇴일 추가
	int endMem(String id);

	// id로 탈퇴회원인지 확인
	boolean isEndMem(String id);
	
	// date에 결제된 건수가 몇개인지
	int dayPayCnt(String date);

}
