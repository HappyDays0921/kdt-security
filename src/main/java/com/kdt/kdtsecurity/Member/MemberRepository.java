package com.kdt.kdtsecurity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Member를 Long 번호로 관리하는 저장소 인터페이스입니다. Spring Data JPA가 구현 객체를 준비합니다.
public interface MemberRepository extends JpaRepository<Member, Long> {
    // loginId로 회원 한 명을 조회합니다. 없을 수도 있어 Optional을 반환합니다.
    Optional<Member> findByLoginId(String loginId);
}