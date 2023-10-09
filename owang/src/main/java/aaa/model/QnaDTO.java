package aaa.model;

import java.util.Date;
import java.util.regex.Pattern;

import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Alias("qDTO")
@Data

//QnA
public class QnaDTO {
	

	int qnaId, cnt, seq, lev, gid, start, limit = 3,pageLimit=4, page, pageStart, pageEnd, pageTotal;
	String qnaTitle, qnaName, qnaPw, qnaUpfile, qnaContent, msg, goUrl ;
	String grade;
	
	Date regDate;
	
	QnaDTO(){
		
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


	
}
