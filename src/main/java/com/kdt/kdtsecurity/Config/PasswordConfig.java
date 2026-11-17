package com.kdt.kdtsecurity.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// 스프링이 이 클래스를 설정 클래스로 읽고, 안의 @Bean 메서드를 실행해 객체를 컨테이너에 등록합니다.
@Configuration
public class PasswordConfig {
    // 반환된 객체를 스프링 빈으로 등록합니다. 다른 클래스에서 PasswordEncoder를 주입받아 쓸 수 있습니다.
    @Bean
    public PasswordEncoder passwordEncoder(){
        // BCrypt 방식으로 비밀번호를 암호화(해시)하는 구현체입니다. 기본 cost(강도)는 10입니다.
        // 회원가입 때는 encode(평문)으로 해시를 만들어 저장하고,
        // 로그인 때는 matches(평문, 저장된 해시)로 일치 여부를 확인합니다.
        // 반환 타입을 인터페이스(PasswordEncoder)로 둬서,
        // 나중에 다른 방식으로 바꿔도 사용하는 쪽 코드는 그대로 둘 수 있습니다.
        return new BCryptPasswordEncoder();
    }
}
