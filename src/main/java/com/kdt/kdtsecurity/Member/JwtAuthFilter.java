package com.kdt.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

// Lombok이 초기값 없는 final 필드를 인자로 받는 생성자를 만듭니다.
// Spring이 이 생성자에 필요한 빈을 전달합니다. 이미 초기값이 있는 필드는 생성자 인자에서 제외합니다.
@RequiredArgsConstructor
// 이 필터는 Spring 빈으로 자동 등록하지 않고 SecurityConfig에서 직접 체인에 넣습니다.
public class JwtAuthFilter extends OncePerRequestFilter {

    // JWT 발급과 서명·만료·용도 검증을 담당합니다.
    private final JwtService jwtService;

    @Override
    // JWT 검증 결과를 현재 요청의 Authentication으로 연결합니다.
    protected void doFilterInternal(
            // 현재 HTTP 요청에서 메서드·헤더·세션 정보를 읽습니다.
            // Spring이 전달한 응답 객체입니다. 이 객체에 응답 헤더와 상태 코드를 작성합니다.
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // HTTP Authorization 헤더를 읽습니다. 헤더에 JSON 전체나 refresh를 넣지 않습니다.
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        // Bearer가 있는 경우에만 검증합니다. 인증이 필수인지는 뒤의 주소 규칙이 따로 판단합니다.
        if (header != null && header.startsWith("Bearer ")) {
            try {
                // 접두어를 제거한 실제 access 문자열을 얻습니다.
                String token = this.jwtService.bearerValue(header);
                // 서명·만료·access 용도가 확인된 클레임을 받습니다. 실패하면 아래 인증 객체를 만들지 않습니다.
                Claims claims = this.jwtService.parseAccess(token);
                // 검증이 끝난 뒤에만 인증된 객체를 만듭니다.
                UsernamePasswordAuthenticationToken authentication =
                        UsernamePasswordAuthenticationToken.authenticated(
                                // 순서대로 검증한 loginId, 비밀번호를 보관하지 않는 null, 이 코드가 부여한 ROLE_USER 목록입니다.
                                claims.getSubject(), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                // 기존 공유 컨텍스트를 직접 바꾸지 않고 이번 처리에 쓸 빈 컨텍스트를 만듭니다.
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                // 검증된 사용자·권한을 컨텍스트에 넣습니다.
                context.setAuthentication(authentication);
                // 뒤의 인가 필터·Controller가 현재 인증을 읽을 수 있도록 실행 흐름에 연결합니다.
                // 이 줄 뒤의 처리에서는 검증한 loginId를 principal로 읽습니다. 다음 HTTP 요청에는 다시 검증합니다.
                SecurityContextHolder.setContext(context);
            } catch (TokenFailedException ignored) {
                // 여기서는 MVC Advice로 예외를 보내지 않고 인증 정보를 비웁니다.
                // 잘못된 토큰을 인증된 사용자로 남기지 않습니다. 보호 경로의 인가 단계가 401로 거절합니다.
                SecurityContextHolder.clearContext();
            }
        }
        // 다음 Security 처리로 넘깁니다. 공개 경로는 통과할 수 있고 보호 경로는 인증을 요구합니다.
        filterChain.doFilter(request, response);
    }
}