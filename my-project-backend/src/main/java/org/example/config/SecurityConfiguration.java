package org.example.config;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entity.RestBean;
import org.example.entity.vo.response.AuthorizeVO;
import org.example.filter.JwtAuthorizeFilter;
import org.example.utils.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
public class SecurityConfiguration {

    @Resource
    JwtUtils utils;

    @Resource
    JwtAuthorizeFilter jwtAuthorizeFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        // 允许所有/api/auth/前缀的请求，其余的需要登录后才能访问
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 登录配置
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .failureHandler(this::onAuthenticationFailure)
                        .successHandler(this::onAuthenticationSuccess)
                )
                // 退出登录配置
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)
                )
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint(this::onUnauthorized)
                        .accessDeniedHandler(this::onAccessDeny)
                )
                .csrf(AbstractHttpConfigurer::disable)
                // 不需要处理session
                .sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 将自定义的验证jwt令牌的过滤器放在登录验证过滤器前才会生效
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        User user = (User) authentication.getPrincipal();
        String token = utils.creatJwt(user, 1, "小明");
        AuthorizeVO vo = new AuthorizeVO();
        vo.setUsername("小明");
        vo.setRole("");
        vo.setToken(token);
        vo.setExpire(utils.expireTime());
        response.getWriter().write(RestBean.success(vo).asJsonString());
    }

    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());
    }

    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) {

    }

    public void onAccessDeny(HttpServletRequest request,
                             HttpServletResponse response,
                             AccessDeniedException exception) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(RestBean.forbidden(exception.getMessage()).asJsonString());
    }

    public void onUnauthorized(HttpServletRequest request,
                               HttpServletResponse response,
                               AuthenticationException exception) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());
    }

}
