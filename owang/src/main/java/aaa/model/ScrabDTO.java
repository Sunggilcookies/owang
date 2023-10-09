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
@Alias("scDTO")
public class ScrabDTO {
	
	SoloResumeDTO srDTO;
	RecruitDTO rcDTO;
	MCompanyDTO mcDTO;
	SoloDTO sDTO;
	
	// 스크랩 번호
	public int scid;
	
	// 이력서
	int rsid;
	
	// 공고
	String recruitTitle; // 공고 제목
	int recruitId; // 지원한 공고의 번호
	
	// 추가
	String realMagam; // 마감일자
	int recruitMoney;  // 급여
	String recruitLocation; // 근무지역
	String recruitMtype; // 월급 ? 연봉 ?
	
	// 기업
	String cid; // 기업명
	String cname; // 기업이름

	// 개인
	String sid;
	
	Timestamp scrabdate;
		
}



