package com.swyp.gaezzange.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        List<String> roleNames = new ArrayList<>();

        System.out.println("onAuthenticationSuccess");
        System.out.println(authentication.getPrincipal());
        authentication.getAuthorities().forEach(authority->{
            roleNames.add(authority.getAuthority());
        });
        System.out.println(roleNames.get(0) );
        if(roleNames.get(0).equals("ROLE_USER")) {
            System.out.println("유저");
            response.sendRedirect("http://localhost:8090/login");
        }
//        else if(roleNames.get(0).equals("ROLE_ADMIN")) {
//            System.out.println("관리자");
////            response.sendRedirect("http://localhost:8080/fastfood/superhome");
//        }
    }


}
