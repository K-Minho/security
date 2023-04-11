package xyz.cyanin.securityapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;
import xyz.cyanin.securityapp.core.jwt.JwtAuthorizationFilter;

@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager));
            super.configure(builder);
        }
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // 1. CSRF 해제
        http.csrf().disable(); // csrf 잘못된 경로로 오는것을 막음 | postman 접근 해야함 CSR할때

        // 2. iframe 차단
        http.headers().frameOptions().disable();

        // 3. CORS 설정
        http.cors().configurationSource(configurationSource());

        // 4. JSession 사용 거부
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 5. form 로그인 해제
        http.formLogin().disable();

        // 6. http 정책 해제 BasicAuthentication 필터 해제
        http.httpBasic().disable();

        // 7. XSS (lucy 필터)

        // 8. 커스텀 필터 설정 (시큐리티 필터 대체)
        http.apply(new CustomSecurityFilterManager());

        // 9. http 인증 실패시
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            // checkpoint filter handler 필요
            log.debug("디버그 : 인증실패 : " + authException.getMessage());
            log.info("디버그 : 인증실패 : " + authException.getMessage());
            log.warn("디버그 : 인증실패 : " + authException.getMessage());
            log.error("디버그 : 인증실패 : " + authException.getMessage());
        });
        
        // 10. http 권한 실패시
        http.exceptionHandling().accessDeniedHandler((request, response, accessDeniedException) -> {
            log.debug("디버그 : 권한 실패 : " + accessDeniedException.getMessage());
            log.info("디버그 : 권한 실패 : " + accessDeniedException.getMessage());
            log.warn("디버그 : 권한 실패 : " + accessDeniedException.getMessage());
            log.error("디버그 : 권한 실패 : " + accessDeniedException.getMessage());
        });

        // 3. 인증, 권한 필터 설정
        http.authorizeRequests(
            authorize -> authorize.antMatchers("/users/**").authenticated()
            .antMatchers("/manager/**")
            .access("hasRole('admin') or hasRole('manager')")
            .antMatchers("/admin/**").hasRole("admin")
            .anyRequest().permitAll()
        );
        
        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 (프론트 앤드 IP만 허용 react)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization"); // 브라우저가 토큰을 자바스크립트로 읽는것을 허용하게 함
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

 
}
