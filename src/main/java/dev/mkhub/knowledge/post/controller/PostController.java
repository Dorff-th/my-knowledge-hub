package dev.mkhub.knowledge.post.controller;

import dev.mkhub.knowledge.member.security.MemberDetails;
import dev.mkhub.knowledge.post.dto.PostDTO;
import dev.mkhub.knowledge.post.dto.PostDetailDTO;
import dev.mkhub.knowledge.post.dto.page.PageRequestDTO;
import dev.mkhub.knowledge.post.dto.page.PageResponseDTO;
import dev.mkhub.knowledge.post.dto.search.PostSearchCondition;
import dev.mkhub.knowledge.post.repository.PostRepositoryCustom;
import dev.mkhub.knowledge.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Controller
@Slf4j
public class PostController {

    private final PostService postService;

    //게시글 목록
    @GetMapping("/posts")
    public String posts( Pageable pageable,
                         PageRequestDTO pageRequestDTO,
                         @ModelAttribute PostSearchCondition postSearchCondition,
                         Model model) {

        PageResponseDTO<PostDTO> result = postService.getPostList(postSearchCondition, pageRequestDTO);

        model.addAttribute("result", result);
        model.addAttribute("currentPage", pageRequestDTO.getPage());  // 여전히 1-based로 사용 가능

        //result.getDtoList().forEach(dto->System.out.println(dto));

        return "posts/list";
    }

    //게시글(post) 상세 내용 조회
    @GetMapping("/posts/{id}")
    public String post(@PathVariable("id") Long id,
                       Model model) {

        PostDetailDTO postDetailDTO = postService.getPost(id)
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("post", postDetailDTO);

        return "posts/detail";
    }
}
