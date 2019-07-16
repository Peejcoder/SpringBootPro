package com.ccic.payroll.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AlertLogoutSuccessHandler implements LogoutSuccessHandler {
    private static Logger log = LoggerFactory.getLogger(AlertLogoutSuccessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        if (authentication != null) {
            log.info("onLogoutSuccess......" + authentication.getName());
        }

        String URL = request.getContextPath() + "/login";
        log.info(URL);
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(URL);
    }
}
