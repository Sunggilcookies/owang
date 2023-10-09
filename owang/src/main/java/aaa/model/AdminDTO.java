package aaa.model;

import lombok.Data;

@Data
public class AdminDTO {
	
	String id, pw, adminName, msg, goUrl;
	
	boolean capproval=true;

	public AdminDTO(String id, String pw,String adminName) {
		super();
		this.id = id;
		this.pw = pw;
		this.adminName = adminName;
	}

	public AdminDTO() {
		
	}
	
	public boolean idPwChk(AdminDTO other) {
		return id.equals(other.id) && pw.equals(other.pw);
	}
	
	
	
}
