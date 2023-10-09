package aaa.model;

import java.util.Date;
import java.util.regex.Pattern;
import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Alias("askDTO")
@Data
public class AskDTO {
	//seq : 답글의 순서
	//lev : 답글의 계층
	//gid : 답글 그룹에 속하는 답글들을 식별하는 데 사용
	
	//파일첨부
	MultipartFile mmff;
	

	int id, cnt, seq, lev, gid, start, limit = 20,pageLimit=5, page, pageStart, pageEnd, pageTotal;
	String title, pname, imp, upfile, content, msg, goUrl;
	String cid;
	String grade;
	
	Date regDate;
	
	private boolean hasReply; // 답변 여부를 나타내는 플래그
	
	public boolean isHasReply() {
        return hasReply;
    }
	
	public void setHasReply(boolean hasReply) {
        this.hasReply = hasReply;
    }
	
	public AskDTO(){
		
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


	public AskDTO(String title, String pname, String imp, String content) {
		super();
		this.title = title;
		this.pname = pname;
		this.imp = imp;
		this.content = content;
	}
	
	//파일
	public String getUpfile() {
		if(upfile == null || upfile.trim().equals("") ||  upfile.trim().equals("null") ) {
			upfile = null;
		}
		return upfile;
	}
	
	/*그림*/
	/*public boolean isImg() {
		if(getUpfile()==null) {
			return false;
		}
		return Pattern.matches(".{1,}[.](bmp|png|gif|jpg|jpeg)", upfile.toLowerCase());
	}*/
	
	
	public boolean isImg() {
		if(getUpfile()==null) {
			return false;
		}
		return Pattern.matches(".{1,}[.](bmp|png|gif|jpg|jpeg|hwp|pdf|hwpx)", upfile.toLowerCase());
	}
	
	
	
	
	
	
	
	
	
	
	
}
