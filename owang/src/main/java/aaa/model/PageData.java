package aaa.model;

import org.springframework.stereotype.Component;

import aaa.service.AdminCompanyMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Component
@Data
public class PageData {
	
	String msg, goUrl;
	
	
}
