package com.example.modoproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@EnableJpaAuditing
@SpringBootApplication
public class ModoProjectApplication {

    public static void main(String[] args) {

        SpringApplication.run(ModoProjectApplication.class, args);
    }
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();

    }
    // 커스텀 에러 페이지 매핑
    @Bean
    public ErrorController errorController() {
        return new ErrorController() {
            @RequestMapping("/error")
            public String handleError() {
                // 커스텀 에러 페이지로 리다이렉트 또는 뷰를 반환
                return "error";
            }
        };
    }
}
