package dev.mkhub.knowledge.post.controller;

import dev.mkhub.knowledge.post.dto.PostDTO;
import dev.mkhub.knowledge.post.dto.PostDetailDTO;
import dev.mkhub.knowledge.common.dto.PageRequestDTO;
import dev.mkhub.knowledge.common.dto.PageResponseDTO;
import dev.mkhub.knowledge.post.dto.search.PostSearchCondition;
import dev.mkhub.knowledge.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    //게시글 목록
    @GetMapping("")
    public String posts( Pageable pageable,
                         PageRequestDTO pageRequestDTO,
                         @ModelAttribute PostSearchCondition postSearchCondition,
                         HttpServletRequest request,
                         Model model) {

        /*try {
            Thread.sleep(2000); // 2초 동안 강제 지연
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }*/

        PageResponseDTO<PostDTO> result = postService.getPostList(postSearchCondition, pageRequestDTO);

        model.addAttribute("result", result);
        model.addAttribute("currentPage", pageRequestDTO.getPage());  // 여전히 1-based로 사용 가능

        //페이징 공통 Thymeleaf fragment 를 쓰기 위해 현재 URI 를 view에 넘김
        String requestURI = request.getRequestURI();  // 예: "/posts"
        model.addAttribute("requestURI", requestURI);


        return "posts/list";
    }

    //게시글(post) 상세 내용 조회
    @GetMapping("/{id}")
    public String post(@PathVariable("id") Long id,
                       Model model) {

        PostDetailDTO postDetailDTO = postService.getPost(id)
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("post", postDetailDTO);

        return "posts/detail";
    }

    //editor-demo
    @GetMapping("/editor-demo")
    public String showEditorDemo() {
        return "posts/editor-demo"; // templates/post/editor-demo.html
    }

    @PostMapping("/editor-demo")
    public String handleEditorSubmit(@RequestParam String title,
                                     @RequestParam String content) {
        System.out.println("제목: " + title);
        System.out.println("내용: " + content); // 마크다운 텍스트

        return "redirect:/posts/editor-demo"; // 다시 폼으로
    }

    @GetMapping("/edit")
    public String showEditor() {
        return "posts/editor";
    }

}
