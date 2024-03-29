package com.example.modoproject.configuration;

import com.example.modoproject.service.oauth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableMethodSecurity
public class securityConfig {
    private final oauth2UserService oAuth2UserService;

    public securityConfig(oauth2UserService oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests(config -> config.anyRequest().permitAll());
        http.oauth2Login(oauth2Configurer -> oauth2Configurer
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .userInfoEndpoint()
                        .userService(oAuth2UserService))
                .logout() // 인자 없이 호출
                .logoutUrl("/logout") // 로그아웃 URL 지정
                .logoutSuccessUrl("/login") // 로그아웃 성공 후 리다이렉트할 URL 지정
                .invalidateHttpSession(true) // 세션 무효화 여부 설정
                .addLogoutHandler(customLogoutHandler()); // 커스텀 로그아웃 핸들러 추가

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

            String id = defaultOAuth2User.getAttributes().get("id").toString();
            String body = """
                    {"id":"%s"}
                    """.formatted(id);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            PrintWriter writer = response.getWriter();
            writer.println(body);
            writer.flush();
        });
    }

    public LogoutHandler customLogoutHandler() {
        return new LogoutHandler() {
            @Override
            public void logout(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) {
                System.out.println("User " + authentication.getName() + " 로그아웃되었습니다");
                try {
                    response.sendRedirect("/login");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
    }
}
