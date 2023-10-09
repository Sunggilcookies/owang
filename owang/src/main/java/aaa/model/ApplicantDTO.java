package aaa.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
@Alias("aDTO")
public class ApplicantDTO {
	
	SoloResumeDTO srDTO;
	RecruitDTO rcDTO;
	MCompanyDTO mcDTO;
	SoloDTO sDTO;
	
	// 이력서
	int rsid;
	
	// 공고
	String recruitTitle; // 공고 제목
	int recruitId; // 지원한 공고의 번호
	
	// 기업
	String cid; // 기업명
	String cname; // 기업이름
	String cemail; // 기업이메일
	
	// 개인
	String sid;
	String sname; // 개인회원
	String sbirth; // 생년월일
    int sage; // 나이
    String sgender; // 성별
    String sphone; // 번호
    String semail; // 이메일
    String saddress; // 주소
    String scompanyName; // 재직중인 직장
    String scompanyFile; // 직장의 재직자 증명파일

	// 지원서
	public int ano;  // 지원자 번호
	String apphoto;
	String aptitle; 
	String apcareer;
	String apcompany;
	String apworkstart;
	String apworkend;
	String apwork;
	String apacademic;
	String apacademicstat;
	String apintroduce;
	String aplicense;
	String aplicenseorg;
	String aplicenseyear;
	String aplanguage;
	String aplanguagelevel;
	int apcancel;
	int apread;
	int appass;
	Timestamp apsubmitdate;
	int appnum;
	// 페이징 처리
    private int start;
    private int limit = 5;
    private int pageLimit = 4;
    private int page;
    private int pageStart;
    private int pageEnd;
    private int pageTotal;
	
	
	public void calc(int total) {	
		start = (page -1) * limit;
		
		pageStart = (page -1)/pageLimit*pageLimit +1;
		pageEnd = pageStart + pageLimit -1;
		
		
		pageTotal = total / limit;
		if(total % limit != 0) {
			pageTotal++;
		}
		
		if(pageEnd > pageTotal) {
			pageEnd = pageTotal;
		}
		
	}
	
	// 생성자에 필드 초기화 또는 설정 코드 추가
    public ApplicantDTO() {
        super();
    }

    public boolean isImg() {
		if(getApphoto()==null) {
			return false;
		}
		return Pattern.matches(".{1,}[.](bmp|png|gif|jpg|jpeg)", apphoto.toLowerCase());
	}

 
}


