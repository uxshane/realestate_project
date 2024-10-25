package com.example.realestate_api.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityEventConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(SecurityEventConfig.class);

    //로그인 성공
    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws ServletException, IOException {
                
                logger.info("로그인 성공: 사용자 '" + authentication.getName() + "'");
                super.onAuthenticationSuccess(request, response, authentication);
            }
        };
    }
    
    //로그인 실패
    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, 
                                                org.springframework.security.core.AuthenticationException exception) 
                                                throws IOException, ServletException {
                logger.warn("로그인 실패: 이유 '" + exception.getMessage() + "'");
                super.onAuthenticationFailure(request, response, exception);
            }
        };
    }

    // 접근 거부 처리
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            logger.warn("접근 거부: 사용자 '{}', 요청 경로 '{}'", request.getUserPrincipal().getName(), request.getRequestURI());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 거부");
        };
    }

}
