package dev.mkhub.knowledge.post.controller;

import dev.mkhub.knowledge.member.security.MemberDetails;
import dev.mkhub.knowledge.post.dto.PostDTO;
import dev.mkhub.knowledge.post.dto.page.PageRequestDTO;
import dev.mkhub.knowledge.post.dto.page.PageResponseDTO;
import dev.mkhub.knowledge.post.dto.search.PostSearchCondition;
import dev.mkhub.knowledge.post.repository.PostRepositoryCustom;
import dev.mkhub.knowledge.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
@Controller
@Slf4j
public class PostController {

    private final PostService postService;

    //게시글 목록
    @GetMapping("/posts")
    public String posts( @AuthenticationPrincipal MemberDetails memberDetails,
                         Pageable pageable,
                         PageRequestDTO pageRequestDTO,
                         @ModelAttribute PostSearchCondition postSearchCondition,
                         Model model) {

        Long memberId = memberDetails.getId();

        PageResponseDTO<PostDTO> result = postService.getPostList(memberId, postSearchCondition, pageRequestDTO);

        model.addAttribute("result", result);
        model.addAttribute("currentPage", pageRequestDTO.getPage());  // 여전히 1-based로 사용 가능

        result.getDtoList().forEach(dto->System.out.println(dto));

        return "post/posts";
    }
}
