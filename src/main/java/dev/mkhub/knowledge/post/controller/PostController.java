package dev.mkhub.knowledge.post.controller;

import dev.mkhub.knowledge.attachment.dto.AttachmentViewDTO;
import dev.mkhub.knowledge.attachment.enums.UploadType;
import dev.mkhub.knowledge.attachment.service.AttachmentService;
import dev.mkhub.knowledge.attachment.util.FormatFileSize;
import dev.mkhub.knowledge.post.domain.Category;
import dev.mkhub.knowledge.post.domain.Post;
import dev.mkhub.knowledge.member.security.MemberDetails;
import dev.mkhub.knowledge.post.dto.PostDTO;
import dev.mkhub.knowledge.post.dto.PostDetailDTO;
import dev.mkhub.knowledge.common.dto.PageRequestDTO;
import dev.mkhub.knowledge.common.dto.PageResponseDTO;
import dev.mkhub.knowledge.post.dto.PostRequestDTO;
import dev.mkhub.knowledge.post.dto.PostUpdateDTO;
import dev.mkhub.knowledge.post.dto.search.PostSearchCondition;
import dev.mkhub.knowledge.post.service.CategoryService;
import dev.mkhub.knowledge.post.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    private final CategoryService categoryService;

    private final AttachmentService attachmentService;

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
                       Model model,
                       @AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : #this") MemberDetails loginUser) {

        PostDetailDTO postDetailDTO = postService.getPost(id)
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("post", postDetailDTO);

        model.addAttribute("loginUserId",
                loginUser != null ? loginUser.getMember().getId() : null);

        UploadType uploadType = UploadType.ATTACHMENT;

        //첨부파일 목록 조회
        attachmentService.attachmentsByPostId(id, uploadType).forEach(attachment -> {log.debug(attachment.getOriginFileName());});

        List<AttachmentViewDTO> attachmentViewDTOList = attachmentService.attachmentsByPostId(id, uploadType).stream()
                .map(attachment -> AttachmentViewDTO.builder()
                        .id(attachment.getId())
                        .originalName(attachment.getOriginFileName())
                        .fileSizeText(FormatFileSize.formatFileSize(attachment.getFileSize()))
                        .uploadType(attachment.getUploadType())
                        .build())
                .collect(Collectors.toList());

        model.addAttribute("attachments", attachmentViewDTOList);

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

    @GetMapping("/write")
    public String showWriter(Model model) {
        List<Category> categories = categoryService.findAllCategory();
        model.addAttribute("categories", categories);
        return "posts/writer";
    }


    @PostMapping("/write")
    public String createNewPost(@AuthenticationPrincipal MemberDetails loginUser,
                                PostRequestDTO dto) {

        Long memberId = loginUser.getId();
        dto.setMemberId(memberId);

        Post savedPost = postService.createPost(dto);

        return "redirect:/posts/" + savedPost.getId() + "?fromSave=true&saveType=created";
    }

    // 기존 post 수정화면
    @GetMapping("/{id}/edit")
    public String editor(@PathVariable("id") Long id, Model model,
                         @AuthenticationPrincipal MemberDetails loginUser) {
        //기존 Post 내용 조회
        PostDetailDTO post = postService.getPost(id).orElseThrow(()-> new IllegalArgumentException("해당 Post가 없습니다."));



        if(!post.getMemberId().equals(loginUser.getId())) {
            System.out.println("권한없음");
            throw new AccessDeniedException("권한이 없습니다.");
        } else {
            System.out.println("권한있음!");
        }

        model.addAttribute("post", post);

        //category 목록 조회
        List<Category> categories = categoryService.findAllCategory();
        model.addAttribute("categories", categories);

        UploadType uploadType = UploadType.ATTACHMENT;

        //첨부파일 목록 조회
        attachmentService.attachmentsByPostId(id, uploadType).forEach(attachment -> {log.debug(attachment.getOriginFileName());});

        List<AttachmentViewDTO> attachmentViewDTOList = attachmentService.attachmentsByPostId(id, uploadType).stream()
                .map(attachment -> AttachmentViewDTO.builder()
                        .id(attachment.getId())
                        .originalName(attachment.getOriginFileName())
                        .fileSizeText(FormatFileSize.formatFileSize(attachment.getFileSize()))
                        .uploadType(attachment.getUploadType())
                        .build())
                .collect(Collectors.toList());

        model.addAttribute("attachments", attachmentViewDTOList);

        return "posts/editor";
    }

    //Post 수정 처리
    @PostMapping("/{id}/edit")
    public String editPost(@AuthenticationPrincipal MemberDetails loginUser, @ModelAttribute PostUpdateDTO dto) {

        Post post = postService.getPostById(dto.getId())
                .orElseThrow(()-> new IllegalArgumentException("Post가 존재하지 않습니다!"));

        if(post.getMember().getId().equals(loginUser.getId())) {
            new AccessDeniedException("권한이 없습니다.");
        }

        Long memberId = loginUser.getId();
        dto.setMemberId(memberId);

        int updatedCount = postService.editPost(dto);

        if(updatedCount == 0 ) {
            new EntityNotFoundException("업데이트 실패!");
        }

        return "redirect:/posts/" + dto.getId() + "?fromSave=true&saveType=updated";
    }


    //Post 삭제 - 연관관계에 있는 Attachement, Comment 도 함께 삭제
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") Long id,
                             @AuthenticationPrincipal MemberDetails loginUser) {


        Post post = postService.getPostById(id)
                .orElseThrow(()-> new IllegalArgumentException("Post가 존재하지 않습니다!"));

        if(post.getMember().getId().equals(loginUser.getId())) {
            new AccessDeniedException("권한이 없습니다.");
        }

        postService.deletePost(id);

        return  "redirect:/posts?fromDelete=true";
    }

}
