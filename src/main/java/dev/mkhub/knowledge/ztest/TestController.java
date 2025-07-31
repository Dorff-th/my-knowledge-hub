package dev.mkhub.knowledge.ztest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test/tailwind")
    public String testTailwind() {
        return "test-tailwind";
    }
}
