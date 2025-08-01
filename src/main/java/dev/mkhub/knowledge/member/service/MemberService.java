package dev.mkhub.knowledge.member.service;

import dev.mkhub.knowledge.common.exception.DuplicateResourceException;
import dev.mkhub.knowledge.domain.Member;
import dev.mkhub.knowledge.member.dto.RegisterRequestDTO;
import dev.mkhub.knowledge.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 회원가입 - 이메일 중복체크, 닉네임 중복체크
 * 회원가입, 탈퇴 처리등을 담당하는 Service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MemberRepository memberRepository;

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    };

    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    };

    //회원가입 처리
    public void register(RegisterRequestDTO dto) {

        //이메일과 닉네임 중복을 다시한번 확인한다.
        if(memberRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Duplicate.email");
        }

        if(memberRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Duplicate.nickname");
        }

        String encrypted = bCryptPasswordEncoder.encode(dto.getPassword());

        Member member = new Member(dto.getEmail(), encrypted, "USER", dto.getNickname());

        memberRepository.save(member);
    }
}
