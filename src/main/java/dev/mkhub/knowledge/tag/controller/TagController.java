package dev.mkhub.knowledge.tag.controller;

import dev.mkhub.knowledge.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * 자동완성 태그 목록 조회
     * @param query 사용자가 입력한 문자열
     * @return 태그 이름 목록(JSON 배열)
     */
    @GetMapping("/suggest")
    public List<String> suggestTags(@RequestParam("query") String query) {
        return tagService.getTagSuggestions(query);
    }
}
