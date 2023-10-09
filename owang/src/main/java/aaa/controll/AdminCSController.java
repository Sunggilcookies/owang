package aaa.controll;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin_cs")
public class AdminCSController {
	@RequestMapping("admin_customer")
	String management() {
		return "admin/cs/admin_customer";
	}
}
