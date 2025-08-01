package dev.mkhub.knowledge.member.service;

import dev.mkhub.knowledge.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 회원가입 - 이메일 중복체크, 닉네임 중복체크
 * 회원가입, 탈퇴 처리등을 담당하는 Service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    };

    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    };
}
