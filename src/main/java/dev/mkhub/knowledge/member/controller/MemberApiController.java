package dev.mkhub.knowledge.member.controller;

import dev.mkhub.knowledge.common.exception.DuplicateResourceException;
import dev.mkhub.knowledge.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 회원가입에 필요한 email, nickname 중복 검사를 위한 API 호출용 Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class MemberApiController {

    final private MemberService memberService;

    @GetMapping("/checked-email")
    public ResponseEntity<?> checkedEmail(@RequestParam("email") String email) {

        if (memberService.existsByEmail(email)) {
            throw new DuplicateResourceException("이미 사용 중인 이메일입니다.");
        }
        return ResponseEntity.ok(Map.of("duplicate", false));
    }

    @GetMapping("/checked-nickname")
    public ResponseEntity<?> checkedNickname(@RequestParam("nickname") String nickname) {

        if (memberService.existsByNickname(nickname)) {
            throw new DuplicateResourceException("이미 사용 중인 닉네임입니다.");
        }
        return ResponseEntity.ok(Map.of("duplicate", false));
    }
}
