package com.kdt.kdtsecurity.Member;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;

@Configuration
@EnableWebSecurity//이래야 시큐리티가 활성화됨
public class SecurityConfig {

    private final JwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf->csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(
                        session->session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .requestCache(cache -> cache
                        .requestCache(new NullRequestCache()))
                .formLogin(form-> form.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout ->logout.disable())
                .authorizeHttpRequests(
                        auth-> auth.requestMatchers(
                                "/","/index.html","/favicon.ico"
                                ,"/hello","/error","/swagger-ui"
                                ,".html","/swagger-ui/**"
                                        ,"/v3/api-docs/**").permitAll()
                                .requestMatchers(
                                        HttpMethod.OPTIONS,"/api/**"
                                ).permitAll()
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/auth/login",
                                        "/api/v1/auth/token",
                                        "/api/v1/token-cookie",
                                        "/api/v1/token/refresh",
                                        "/api/v1/token/refresh-cookie",
                                        "/api/v1/auth/loutgout-cookie",
                                        "/api/v1/auth/api-key"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET,
                                        "/api/v1/posts",
                                        "/api/v1/posts/**"
                                ).permitAll()
                                .requestMatchers(
                                        "/api/v1/auth/me"
                                ).authenticated() //이거는 인증된 사람만 가능하게!!!!!
                                .requestMatchers("/api/v1/posts","/api/v1/posts/**"
                                ).authenticated()
                                .anyRequest()
                                .denyAll()
                        )

                )
    }


}
