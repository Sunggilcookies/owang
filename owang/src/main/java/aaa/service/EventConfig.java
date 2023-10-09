package aaa.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.Resource;

@Configuration
public class EventConfig implements WebMvcConfigurer{
   
   @Resource
   EventInterceptor interceptor;

   @Override
   public void addInterceptors(InterceptorRegistry registry) {
      
      registry.addInterceptor(interceptor).addPathPatterns("/admin/adminM")
      .addPathPatterns("/admin_cs/**")
      .addPathPatterns("/admin_company/**")
      .addPathPatterns("/admin_solo/**")
      .addPathPatterns("/admin_product/**")
      .addPathPatterns("/admin_member/**");
      
      
   }
   
   
   
}