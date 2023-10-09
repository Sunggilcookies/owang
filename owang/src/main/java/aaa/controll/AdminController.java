package aaa.controll;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import aaa.model.AdminDTO;
import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	
	// 로그인 폼
	@PostMapping("/admin")
	String adminLoginReg(AdminDTO dto, HttpSession session) {
		 // 아이디와 비밀번호를 검증하고 세션 발급
		AdminDTO adminDTO = new AdminDTO("admin","1234","관리자" );
		System.out.println("이벤트체크1");
		System.out.println(adminDTO.toString());
		System.out.println(dto.toString());
		System.out.println(adminDTO.idPwChk(dto)+"뭐라나옴");
        if (adminDTO.idPwChk(dto)) {
            session.setAttribute("adminSession", adminDTO);
            dto.setMsg("로그인 완료");
            dto.setGoUrl("/admin_product/index");
            System.out.println("이벤트체크2");
            return "/admin/adminAlert";
        } else {
        	dto.setMsg("로그인 실패");
            dto.setGoUrl("/admin/adminLogin");
            System.out.println("이벤트체크3");
            return "/admin/adminAlert"; // 로그인 실패
        }
	}
	@GetMapping("/adminLogin")
	String adminLogin(AdminDTO dto, HttpSession session) {
		return "/admin/adminLogin";
	}
	
	// 관리자 페이지
	@RequestMapping("/adminM")
	String adminM() {
		return "/admin/adminM";
	}
	
	@RequestMapping("/adminFail")
	String adminF() {
		return "/admin/adminFail";
	}
	

	
	/*
	// 관리자 계정 생성
	@RequestMapping("/adminLogin")
	@ResponseBody
	String make(HttpSession session) {
		session.setAttribute("admin", "1234");
		return "<h1>관리자를 선택해주세요</h1>"
				+ "<a href='/admin/loginForm'>관리자1</a>";
	}
	
	// 로그인 폼
	@RequestMapping("/loginForm")
	String adminLogin() {
		return "admin/adminLogin";
	}
	
	// 관리자 페이지
	@RequestMapping("/adminReg")
	String admin(AdminDTO admin, HttpSession session) {
		HashMap<String, AdminDTO> map = new HashMap<>();
		map.put("admin", new AdminDTO("aaa","관리자1","1234"));
		admin.setMsg("로그인 실패");
		if (map.containsKey(admin.getId())) {
			AdminDTO compareAdmin = map.get(admin.getId());
			
			if (compareAdmin.idPwChk(admin)) {
				admin.setMsg(admin.getAdminName()+"님 로그인 환영합니다.");
				session.setAttribute("admin", admin);
			}
		}
		
		return "admin/adminAlert";
	}
	*/
	
}


