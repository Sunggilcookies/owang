package aaa.model;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("payDTO")
public class PaymentDTO {
	// 사이트 아이디
	String sid;
	String cid;
	// 아임포트 거래고유번호
	String impUid;
	// 상품아이디
	String productId;
	// 상품명
	String pname;
	// 결제금액
	int amount;
	// 결제시간
	Date paidAt;
	// 탈퇴일
	Date endDate;
}
