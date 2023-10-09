package aaa.controll;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin_recruit")
public class AdminRecruitController {
	@RequestMapping("/rmanagement")
	String rmanagement() {
		return "/admin/recruit/rmanagement";
	}
	
}
