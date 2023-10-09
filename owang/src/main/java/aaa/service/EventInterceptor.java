package aaa.service;


import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import aaa.model.AdminDTO;
import aaa.model.MCompanyDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class EventInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession();
        AdminDTO dto = (AdminDTO) session.getAttribute("adminSession");
        MCompanyDTO mdto = (MCompanyDTO)session.getAttribute("companySession");
      
        
        System.out.println( session.getAttribute("adminSession") );
        if (dto == null || session.getAttribute("adminSession") == null) {
           dto = new AdminDTO();
            System.out.println("이벤트체크 핸들러님 오셧나요 1");
            
            dto.setMsg("접근이 불허된 페이지 입니다.");
            dto.setGoUrl("/");
            // 리다이렉트 처리
            //response.sendRedirect("/admin/adminAlert");
            response.sendRedirect("/admin/adminFail");
            return false;
        }

        return true;
    }
}
