package dev.mkhub.knowledge.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

    @GetMapping("/board/free")
    public String free() {
        return "board/free";
    }

    @GetMapping("/board/notice")
    public String notice() {
        return "board/notice";
    }

}
