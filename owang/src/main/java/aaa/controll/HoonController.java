package aaa.controll;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HoonController {
	
	@RequestMapping("/home")	
	String hoonGo() {
		System.out.println("안재훈 브런치");

		return "hoon"; 
	}
}
