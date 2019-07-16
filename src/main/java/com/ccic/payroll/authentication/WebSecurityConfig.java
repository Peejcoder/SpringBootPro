package com.ccic.payroll.authentication;

import com.ccic.payroll.common.AlertAuthenticationLoginObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);
    @Autowired
    private AlertAuthenticationProvider authProvider;

    @Autowired
    AlertAuthenticationLoginObject loginAuthentication;

    @Autowired
    private AlertLoginSuccessHandler alertLoginSuccessHandler;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().addFilterBefore(authenticationFilter(), AlertAuthenticationFilter.class)
                .authorizeRequests()
                /*.antMatchers("/login",
                        "/js/**",
                        "/html/**",
                        "/fonts/**",
                        "/css/**",
                        "/img/**",
                        "/js/**",
                        "/api/**",
                        "/payroll/api/**")
                .permitAll().anyRequest().authenticated().and().exceptionHandling()
                .defaultAuthenticationEntryPointFor(loginUrlauthenticationEntryPointWithJson(),
                        new AntPathRequestMatcher("/api/**"))*/
                .and()
                .formLogin()
                .loginPage("/login")
                .and().logout().permitAll().logoutUrl("/logout")
                .logoutSuccessHandler(new AlertLogoutSuccessHandler());
        http.headers().frameOptions().disable();
    }

    @Bean
    public AuthenticationEntryPoint loginUrlauthenticationEntryPointWithJson() {
        return new LoginUrlAuthenticationEntryPoint("/nologin");
    }

    /**
     * authenticationFilter get authentication filter
     *
     * @return
     * @throws Exception
     */
    public AlertAuthenticationFilter authenticationFilter() throws Exception {
        AlertAuthenticationFilter filter = new AlertAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(alertLoginSuccessHandler);
        filter.setAuthenticationFailureHandler(failureLogin());
        return filter;
    }

    /**
     * failed login handler
     *
     * @return
     */
    public AuthenticationFailureHandler failureLogin() {
        AuthenticationFailureHandler handler = new AuthenticationFailureHandler() {

            @Override
            public void onAuthenticationFailure(HttpServletRequest arg0, HttpServletResponse arg1,
                                                AuthenticationException arg2) throws IOException, ServletException {
                loginAuthentication.setMessage(arg2.getMessage());
                arg1.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                arg1.sendRedirect("/login?error=true");
            }
        };

        return handler;
    }

}