package aaa.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String from, String toEmail, String subject, String text) {
        try {
        	
        	String htmlContent = "<h2 style=\"color: red;\">제목</h2>" // HTML 스타일을 포함한 본문
                    + "<p>내용 내용 내용</p>";
        	
            jakarta.mail.internet.MimeMessage m = mailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(m, "UTF-8");
            h.setFrom(from);      // 발신자 이메일 주소 설정
            h.setTo(toEmail);     // 수신자 이메일 주소 설정
            h.setSubject(subject); // 이메일 제목 설정
            h.setText(text);       // 이메일 본문 설정
            mailSender.send(m);    // MimeMessage 객체를 전달하여 이메일을 보냅니다.
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }
    }
}
