package aaa.model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
@Alias("recruitDTO")
public class RecruitDTO {
	// 만들고보니 등록일 마감일 필요 일단 진행
	
	int recruitId, start, limit = 10, pageLimit = 4, page, pageStart, pageEnd, pageTotal, cnt;
	int rtype;
	public String recruitTitle, recruitName, recruitUpfile, recruitContent, cid, cname, clogo;
	String recruitMtype; //월급
	String msg, goUrl;
	boolean capproval;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date regDate = new Date();
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public Date realMagam = new Date();
	int recruitMagam;
	int recruitMoney; // 급여f
	String recruitLocation; // 근무지역
	String searchOption, keyword;// 검색조건
	int appnum;

	
    private int dayLeft; // 남은 날짜

    // Getter와 Setter 메서드 추가
    public int getDayLeft() {
        return dayLeft;
    }

    public void setDayLeft(int dayLeft) {
        this.dayLeft = dayLeft;
    }
	
	// 중간일 계산 조건 sql에 없어도됨
	boolean recruiting;
	
	MultipartFile mmff;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public String getRegDate() {
		return sdf.format(regDate);
	}

	public String getRealMagam() {
		return sdf.format(realMagam);
	}




	public String getRecruitUpfile() {
		if(recruitUpfile == null || recruitUpfile.trim().equals("") ||  recruitUpfile.trim().equals("null") ) {
			recruitUpfile = null;
		}
		return recruitUpfile;
	}
	
	public boolean isImg() {
		if(getRecruitUpfile()==null) {
			return false;
		}
		return Pattern.matches(".{1,}[.](bmp|png|gif|jpg|jpeg)", recruitUpfile.toLowerCase());
	}
	
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
	
	
	/*
	public String getRealMagam() {
		
		LocalDate currentDate = LocalDate.now();
	    LocalDate futureDate;
	    futureDate = currentDate.plusDays(getRecruitMagam());

	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = futureDate.format(formatter);
	
		
		return formattedDate;
	}
	*/

	public RecruitDTO(String recruitTitle, String recruitName, String recruitContent) {
		this.recruitTitle = recruitTitle;
		this.recruitName = recruitName;
		this.recruitContent = recruitContent;
	}
	public RecruitDTO() {
		super();
	}

	public RecruitDTO(int recruitId, int start, int limit, int pageLimit, int page, int pageStart, int pageEnd,
			int pageTotal, int cnt, int rtype, String recruitTitle, String recruitName, String recruitUpfile,
			String recruitContent, String cid, String cname, String msg, String goUrl, boolean capproval, Date regDate,
			Date realMagam, int recruitMagam, int recruitMoney, String recruitLocation, String searchOption,
			String keyword, MultipartFile mmff, SimpleDateFormat sdf) {
		super();
		this.recruitId = recruitId;
		this.start = start;
		this.limit = limit;
		this.pageLimit = pageLimit;
		this.page = page;
		this.pageStart = pageStart;
		this.pageEnd = pageEnd;
		this.pageTotal = pageTotal;
		this.cnt = cnt;
		this.rtype = rtype;
		this.recruitTitle = recruitTitle;
		this.recruitName = recruitName;
		this.recruitUpfile = recruitUpfile;
		this.recruitContent = recruitContent;
		this.cid = cid;
		this.cname = cname;
		this.msg = msg;
		this.goUrl = goUrl;
		this.capproval = capproval;
		this.regDate = regDate;
		this.realMagam = realMagam;
		this.recruitMagam = recruitMagam;
		this.recruitMoney = recruitMoney;
		this.recruitLocation = recruitLocation;
		this.searchOption = searchOption;
		this.keyword = keyword;
		this.mmff = mmff;
		this.sdf = sdf;
	}

	
	
	
	
}
/*
    # 답글을 안써도 될꺼같아 seq lev gid를 제거했습니다.
	# 임시로 만듬 채용 테이블 recruit
	# recruit_id 무결성을 위한 id
	# recruit_title 채용 게시판 이름 
	# recruit_name 체용 게시판에 작성한 기업 이름 !!! 기업 이름과 교체 !!!
	# recruit_upfile 체용 게시판에 업로드한 이미지 파일
	# recruit_content 체용 게시판에 작성한 내용
	# reg_date 체용 게시판 작성일
	# recruit_magam 채용 게시판 마감일 선택한 수,
	# cnt 체용 게시판 조회수
	CREATE TABLE recruit (
	    recruit_id INT AUTO_INCREMENT PRIMARY KEY,
	    recruit_title VARCHAR(100),
	    recruit_name VARCHAR(100),
	    recruit_upfile VARCHAR(100),
	    recruit_content VARCHAR(9999),
	    reg_date TIMESTAMP,
	    cnt INT
	);
  */
