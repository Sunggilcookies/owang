package aaa.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import aaa.model.PaymentResponseAll;
import aaa.model.PaymentResponseMember;

@Service
public class PayService {

//   @Value("${iamport.impKey}")
	private String impKey = "4780385322371542";
//   @Value("${iamport.impSecret}")
	private String impSecret = "SS3JF3Z4XOrf6uLjjtTqyIsiI0gH5owDz62Ebcaha64SS9JhQ1c3AdQtDb3fxTpS4EWa3EMIzGIV0Trc";
	private IamportClient client;

	public PayService() {
		this.client = new IamportClient(impKey, impSecret);
	}

	// 토큰
	public String getToken() {
		try {
			IamportResponse<AccessToken> authResponse = client.getAuth();
			AccessToken accessToken = authResponse.getResponse();
			return accessToken.getToken();
		} catch (IamportResponseException e) {
			// Iamport API 응답 예외 처리
			e.printStackTrace();
			System.out.println("토큰발급 api응답 에러");
			return null;
		} catch (IOException e) {
			// IO 예외 처리
			e.printStackTrace();
			System.out.println("토큰발급 에러");
			return null;
		}
	}

	// 결제검증 - 결제된 금액 확인
	public String paymentInfo(String imp_uid, String access_token) {
		try {
			IamportResponse<Payment> paymentResponse = client.paymentByImpUid(imp_uid);
			Payment payment = paymentResponse.getResponse();

			String amount = payment.getAmount().toString();
			return amount;
		} catch (IamportResponseException | IOException e) {
			e.printStackTrace(); // 에러 처리
			System.out.println("결제검증 에러");
			return null; // 에러 발생 시 리턴 값으로 처리할 내용
		}
	}

	// 결제취소
	public void paymentCancel(String access_token, String imp_uid, String reason) {
		try {
			CancelData cancelData = new CancelData(imp_uid, true);
			cancelData.setReason(reason);
			client.cancelPaymentByImpUid(cancelData);
		} catch (IamportResponseException | IOException e) {
			e.printStackTrace(); // 에러 처리
			System.out.println("결제취소 에러");
		}
	}

	@Autowired
	PayMapper paym;

	// imp_uid 리스트로 결제정보 가져오기
	public List<PaymentResponseMember.Payment> getPaymentData(List<String> impuidList) {
		String token = getToken();
		String apiUrl = "https://api.iamport.kr/payments?"
				+ impuidList.stream().map(uid -> "imp_uid[]=" + uid).collect(Collectors.joining("&")) + "&_token="
				+ token;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<PaymentResponseMember> responseEntity = restTemplate.getForEntity(apiUrl,
				PaymentResponseMember.class);
		List<PaymentResponseMember.Payment> paymentData = responseEntity.getBody().getResponse();
		// 세팅
		for (PaymentResponseMember.Payment payment : paymentData) {
			dataformat(payment);
		}
		// 정렬
		Collections.sort(paymentData, new Comparator<PaymentResponseMember.Payment>() {
			public int compare(PaymentResponseMember.Payment a, PaymentResponseMember.Payment b) {
				// started_at 기준으로 내림차순 정렬
				return b.getStarted_at().compareTo(a.getStarted_at());
			}
		});
		return paymentData;
	}

	// 아임포트 정보 가공
	public void dataformat(PaymentResponseMember.Payment payment) {
		// imp_uid로 db정보 검색해서 id 가져옴, 탈퇴회원의 경우 표시
		String id = paym.idget(payment.getImp_uid());
		if (id != null && paym.isEndMem(id)) {
			id += "(탈퇴)";
		}
		payment.setId(id);

		// 시간데이터 포맷팅
		payment.setPaid_at(unixformat(payment.getPaid_at()));
		payment.setStarted_at(unixformat(payment.getStarted_at()));
		payment.setFailed_at(unixformat(payment.getFailed_at()));
		payment.setCancelled_at(unixformat(payment.getCancelled_at()));

		// status를 수정
		Map<String, String> statusToKor = new HashMap<>();
		statusToKor.put("paid", "결제완료");
		statusToKor.put("ready", "결제대기");
		statusToKor.put("cancelled", "결제취소");
		statusToKor.put("failed", "결제실패");
		payment.setStatus(statusToKor.get(payment.getStatus()));

		// pg_provider를 수정
		Map<String, String> pgToKor = new HashMap<>();
		pgToKor.put("kakaopay", "카카오");
		pgToKor.put("html5_inicis", "이니시스");
		payment.setPg_provider(pgToKor.get(payment.getPg_provider()));

		// pay_method를 수정
		Map<String, String> methodToKor = new HashMap<>();
		methodToKor.put("trans", "계좌이체");
		methodToKor.put("point", "포인트결제");
		methodToKor.put("card", "카드결제");
		payment.setPay_method(methodToKor.get(payment.getPay_method()));

		// 테스트를 위해 금액을 1000으로 나누어서 결제했기에 다시 곱해서 표시
		if (payment.getAmount() < 1000) {
			payment.setAmount(payment.getAmount() * 1000);
			payment.setCancel_amount(payment.getCancel_amount() * 1000);
		}
		// 천단위에 ,를 찍어서 표시
		payment.setFormatAmount(String.format("%,d", payment.getAmount())+"원");
		payment.setFormatCancle(String.format("%,d", payment.getCancel_amount())+"원");
	}

	public void dataformat(PaymentResponseAll.Pagedpayment payment) {
		// imp_uid로 db정보 검색해서 id 가져옴, 탈퇴회원의 경우 표시
		String id = paym.idget(payment.getImp_uid());
		if (id != null && paym.isEndMem(id)) {
			id += "(탈퇴)";
		}
		payment.setId(id);

		// 시간데이터 포맷팅
		payment.setPaid_at(unixformat(payment.getPaid_at()));
		payment.setStarted_at(unixformat(payment.getStarted_at()));
		payment.setFailed_at(unixformat(payment.getFailed_at()));
		payment.setCancelled_at(unixformat(payment.getCancelled_at()));

		// status를 수정
		Map<String, String> statusToKor = new HashMap<>();
		statusToKor.put("paid", "결제완료");
		statusToKor.put("ready", "결제대기");
		statusToKor.put("cancelled", "결제취소");
		statusToKor.put("failed", "결제실패");
		payment.setStatus(statusToKor.get(payment.getStatus()));

		// pg_provider를 수정
		Map<String, String> pgToKor = new HashMap<>();
		pgToKor.put("kakaopay", "카카오");
		pgToKor.put("html5_inicis", "이니시스");
		payment.setPg_provider(pgToKor.get(payment.getPg_provider()));

		// 테스트를 위해 금액을 1000으로 나누어서 결제했기에 다시 곱해서 표시
		if (payment.getAmount() < 1000) {
			payment.setAmount(payment.getAmount() * 1000);
			payment.setCancel_amount(payment.getCancel_amount() * 1000);
		}
		// 천단위에 ,를 찍어서 표시
		payment.setFormatAmount(String.format("%,d", payment.getAmount())+"원");
		payment.setFormatCancle(String.format("%,d", payment.getCancel_amount())+"원");
	}

	// Date형으로 받은 날짜 포맷팅 메서드
	public String dateformat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	// 날짜+시간 포맷팅 메서드
	public String timeformat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	// UNIXtimestamp를 String으로 포맷팅 메서드
	public String unixToString(long date) {
		if (date > 0) {
			Date unixToString = new Date(date * 1000L);
			return timeformat(unixToString);
		}
		return "0";
	}

	// String형으로 받은 UNIXtimestamp를 String으로 포맷팅 메서드
	public String unixformat(String date) {
		if (!date.equals("0")) {
			Long dateInLong = Long.parseLong(date);
			return unixToString(dateInLong);
		}
		return "0";
	}

	// 포맷팅된 형태(yyyy-MM-dd)의 String을 date로
	public Date stringToDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date datedDate = null; // 변수를 먼저 선언
		try {
			datedDate = sdf.parse(date);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			System.out.println("파싱 에러");
		}
		return datedDate;
	}

	// 포맷팅된 형태(yyyy-MM-dd)의 String을 Unix타임스탬프로
	public Long stringToUnix(String date) {
		Date datedDate = stringToDate(date);
		// Date 객체를 Unix타임스탬프로 변환
		long unixDate = datedDate.getTime() / 1000;
		return unixDate;
	}

	// 유효기간 변경
	public Date dateAddValid(Date date, int valid) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, valid);
		Date mdate = calendar.getTime();
		return mdate;
	}
}