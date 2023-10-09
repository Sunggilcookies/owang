package aaa.model;

import java.util.List;

import lombok.Data;

// 결제상태로 모든 결제내역 가져오기
@Data
public class PaymentResponseAll {
	private int code;
	private String message;
	private PagedDataList response;

	@Data
	public static class PagedDataList {
		private int total;
		private int previous;
		private int next;
		private List<Pagedpayment> list;
	}

	@Data
	public static class Pagedpayment {
		// 아이디 - 받아온 정보에는 없지만 db를 통해 매칭해서 넣음
		String id;
		// 금액의 천단위에 ,를 찍음
		String formatAmount;
		String formatCancle;
		// 아임포트 고유번호
		private String imp_uid;
		// 주문번호
		private String merchant_uid;
		// 결제수단
		private String pay_method;
		// 결제채널(pc,mobile)
		private String channel;
		// pg사 정보
		private String pg_provider;
		private String emb_pg_provider;
		private String pg_tid;
		private String pg_id;
		// 에스크로 여부
		private boolean escrow;
		// 카드 승인번호
		private String apply_num;
		// 은행(계좌이체)
		private String bank_code;
		private String bank_name;
		// 카드
		private String card_code;
		private String card_name;
		private int card_quota;
		private String card_number;
		private int card_type;
		// 가상계좌
		private String vbank_code;
		private String vbank_name;
		private String vbank_num;
		private String vbank_holder;
		private int vbank_date;
		private int vbank_issued_at;
		// 상품명
		private String name;
		// 상품가격
		private int amount;
		// 취소금액
		private int cancel_amount;
		// 통화(한국돈인지)
		private String currency;
		// 주문자
		private String buyer_name;
		private String buyer_email;
		private String buyer_tel;
		private String buyer_addr;
		private String buyer_postcode;
		// 옵션정보
		private String custom_data;
		private String user_agent;
		// 결제상태
		private String status;
		// 결제시작 시간
//        private int started_at;
		private String started_at;
		// 결제완료 시간
//        private int paid_at;
		private String paid_at;
		// 결제실패 시간
//        private int failed_at;
		private String failed_at;
		// 결제취소 시간
//        private int cancelled_at;
		private String cancelled_at;
		// 실패이유
		private String fail_reason;
		// 취소이유
		private String cancel_reason;
		// 매출전표 링크
		private String receipt_url;
		private boolean cash_receipt_issued;
		private String customer_uid;
		private String customer_uid_usage;
	}
}
