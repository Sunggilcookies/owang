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
@Alias("rDTO")
public class SoloResumeDTO {
	
	MultipartFile rsmmff; // 증명사진
	
	public int rsid;    // 이력서 번호
	String sid; // 개인회원
	String rsphoto; // 사진
	String rstitle; // 제목
	
	// 경력
	String rscareer; // 경력
	String rscompany; // 회사명
	String rsworkstart; // 근무시작
	String rsworkend; // 근무끝	
	String rswork; // 담당업무
	
	
	String rsacademic; // 학력
	String rsacademicstat; // 학력 상대
	String rsintroduce; // 자기 소개
	
	// 자격증
	String rslicense; // 자격증
	String rslicenseorg; // 자격증 발행처
	String rslicenseyear; // 자격증 발행년도
	
	// 어학 능력
	String rslanguage; // 어학능력
	String rslanguagelevel; // 어학수준
	
	Timestamp rsregdate;
	Timestamp rsmoddate; // 글 수정일 
	Date deletedate;
	
	int cnt; // 총 이력서 개 수
	
	String [] careers = {"신입", "경력"};
	
	public String getUpfile() {
		if(rsphoto == null || rsphoto.trim().equals("") ||  rsphoto.trim().equals("null") ) {
			rsphoto = null;
		}
		return rsphoto;
	}
	
	public boolean isImg() {
		if(getUpfile()==null) {
			return false;
		}
		return Pattern.matches(".{1,}[.](bmp|png|gif|jpg|jpeg)", rsphoto.toLowerCase());
	}
}



