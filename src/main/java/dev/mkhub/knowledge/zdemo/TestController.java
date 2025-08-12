package dev.mkhub.knowledge.zdemo;

import dev.mkhub.knowledge.member.security.MemberDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test/tailwind")
    public String testTailwind() {
        return "test-tailwind";
    }

    /*@GetMapping("/test/acc")
    public ResponseEntity<String> testAcc() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                   .body("403 권한 없음 테스트");
    }*/

    @GetMapping("/test/acc")
    public void  testAcc(@AuthenticationPrincipal MemberDetails loginUser) {
        System.out.println(loginUser.getId()  + " , " +  loginUser.getAuthorities() + " , " +  loginUser.getUsername());
        throw new AccessDeniedException("권한 없음!");
    }
}
