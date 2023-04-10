package xyz.cyanin.securityapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // 1. CSRF, CORS
        http.csrf().disable(); // csrf 잘못된 경로로 오는것을 막음 | postman 접근 해야함 CSR할때
        http.cors().disable(); // cors 자바스크립트 공격 막음

        // 2. Form 로그인 설정
        http.formLogin().
        loginPage("/loginForm")
        .loginProcessingUrl("/login") // post + x-www-FormUrlEncoded
        .defaultSuccessUrl("/") // 기억된 페이지가 없으면 해당 페이지로 이동
        .successHandler((req, resp, authentication)->{
            System.out.println("디버그 : 로그인 완료"); // 본 코드에 디버그 적을것
        }) // 성공시 로그등을 남김
        .failureHandler((req, resp, ex)->{
            System.out.println("디버그 : 로그인 실패" + ex.getMessage());
        }); // 실패시 로그등을 남김
        
        // 3. 인증, 권한 필터 설정
        http.authorizeRequests(
            authorize -> authorize.antMatchers("/users/**")
            .authenticated().antMatchers("/admin/**").hasRole("admin")
            .antMatchers("/manager/**")
            .access("hasRole('admin') or hasRole('manager')")
            .anyRequest().permitAll()
        );
        
        return http.build();
    }
}
