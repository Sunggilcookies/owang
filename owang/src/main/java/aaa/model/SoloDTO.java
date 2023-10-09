package aaa.model;

import java.io.File;
import java.util.Date;

import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Alias("soloDTO")
@Data 
public class SoloDTO {
	MultipartFile mmff; 	//
	public int sno;	//개인부여되는 번호
	public String cid; // 기업아이디
    public String sid; // 아이디
    public String spw; // 비밀번호
    public String spw2; // 암호확인
    public String sname; // 이름
    public String sjumin; // 주민등록번호 앞자리
    public String sbirth; // 생년월일
    public int sage; // 나이
    public String sgender; // 성별
    public String sphone; // 번호
    public String semail; // 이메일
    public String saddress; // 주소
    public String scompanyName; // 재직중인 직장
    public String scompanyFile; // 직장의 재직자 증명파일
	public int stype;
	public Date sdate;
	public Date sjoindate;
	public Date sdeletedate;//탈퇴날짜
	
	public int sbcnt;
	boolean sinjueng;
	   public String searchOption;
	    public String keyword;
	//1 : 일반개인고객 , 2: 결제권 있는 개인고객, 3: 블랙개인
	
int start,limit = 5, pageLimit = 4, page, pageStart, pageEnd, pageTotal, cnt;
	
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
	
	//파일 이름 없을때 null 값 넣기
	public String getScompanyFile() {
		if(scompanyFile==null ||
			scompanyFile.trim().equals("") ||
			scompanyFile.trim().equals("null")) {
			scompanyFile=null;
		}
		return scompanyFile;	
}
	public String getMmffName() {
		return mmff.getOriginalFilename();
	}
	public SoloDTO() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	 // getter와 setter 메서드는 필드에 접근할 때 사용됨
    public String getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(String searchOption) {
        this.searchOption = searchOption;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

	public SoloDTO(MultipartFile mmff, int sno, String sid, String spw, String spw2, String sname, String sjumin,
			String sbirth, int sage, String sgender, String sphone, String semail, String saddress, String scompanyName,
			String scompanyFile, int stype, Date sdate, int sbcnt, boolean sinjueng, String searchOption,
			String keyword, int start, int limit, int pageLimit, int page, int pageStart, int pageEnd, int pageTotal,
			int cnt,String cid) {
		super();
		this.mmff = mmff;
		this.sno = sno;
		this.cid = cid;
		this.sid = sid;
		this.spw = spw;
		this.spw2 = spw2;
		this.sname = sname;
		this.sjumin = sjumin;
		this.sbirth = sbirth;
		this.sage = sage;
		this.sgender = sgender;
		this.sphone = sphone;
		this.semail = semail;
		this.saddress = saddress;
		this.scompanyName = scompanyName;
		this.scompanyFile = scompanyFile;
		this.stype = stype;
		this.sdate = sdate;
		this.sbcnt = sbcnt;
		this.sinjueng = sinjueng;
		this.searchOption = searchOption;
		this.keyword = keyword;
		this.start = start;
		this.limit = limit;
		this.pageLimit = pageLimit;
		this.page = page;
		this.pageStart = pageStart;
		this.pageEnd = pageEnd;
		this.pageTotal = pageTotal;
		this.cnt = cnt;
	}
	
	
	
	
	
	
}