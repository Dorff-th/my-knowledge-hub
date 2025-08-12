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
import dev.mkhub.knowledge.tag.domain.PostTag;
import dev.mkhub.knowledge.tag.dto.TagDTO;
import dev.mkhub.knowledge.tag.service.PostTagService;
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

import java.util.Arrays;
import java.util.Collections;
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

    private final PostTagService postTagService;

    /**
     * 게시글 목록 페이지
     *
     * - 설명: 전체 게시글 목록을 조회하여 출력
     * - URL: /posts
     * - View: posts/list.html
     */
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

    /**
     * 게시글 상세 페이지
     *
     * - 설명: 선택한 게시글의 상세 내용을 출력
     * - URL: /posts/{id}
     * - View: posts/detail.html
     */
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

        List<TagDTO> postTags= postTagService.getByPostId(id);
        model.addAttribute("postTags", postTags);

        return "posts/detail";
    }



    /**
     * 신규 게시글 작성 페이지
     *
     * - 설명: 사용자가 게시글을 작성할 수 있는 화면
     * - URL: /posts/write
     * - View: posts/z_writer_proto.html (테스트 이후 z_writer_proto.html -> writer.html로 변경예정)
     */
    @GetMapping("/write")
    public String showWriter(Model model) {
        List<Category> categories = categoryService.findAllCategory();
        model.addAttribute("categories", categories);
        return "posts/writer_proto";
    }


    /**
     * 신규 게시글 등록 처리
     *
     * - 설명: 사용자가 입력한 게시글 데이터를 저장 후 목록 페이지로 리다이렉트
     * - 요청: PostRequestDTO
     * - 응답: /posts/{id} (신규 게시글 페이지로 리다이렉트)
     */
    @PostMapping("/write")
    public String createNewPost(@AuthenticationPrincipal MemberDetails loginUser,
                                PostRequestDTO dto) {

        Long memberId = loginUser.getId();
        dto.setMemberId(memberId);

        Post savedPost = postService.createPost(dto);

        return "redirect:/posts/" + savedPost.getId() + "?fromSave=true&saveType=created";
    }

    /**
     * 게시글 수정 페이지
     *
     * - 설명: 기존 게시글 내용을 수정할 수 있는 화면
     * - URL: /posts/{id}/edit
     * - View: posts/z_editor_proto.html (테스트 이후 editor_poroto.html -> editor.html로 변경예정)
     */
    @GetMapping("/{id}/edit")
    public String editor(@PathVariable("id") Long id, Model model,
                         @AuthenticationPrincipal MemberDetails loginUser) {
        //기존 Post 내용 조회
        PostDetailDTO post = postService.getPost(id).orElseThrow(()-> new IllegalArgumentException("해당 Post가 없습니다."));

        if(!post.getMemberId().equals(loginUser.getId())) {
            throw new AccessDeniedException("권한이 없습니다.");
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

        //PostTag 조회하여 view에 전달
        List<TagDTO> postTags= postTagService.getByPostId(id);
        model.addAttribute("postTags", postTags);


        return "posts/editor_proto";
    }

    @PostMapping("/{id}/temp/edit")
    public void editPostTemp(@AuthenticationPrincipal MemberDetails loginUser, @ModelAttribute PostUpdateDTO dto) {

        /*log.debug("===new attachments ");
        log.debug("신규추가 file 개수  " + dto.getAttachments().size());
        dto.getAttachments().forEach(att->log.debug("new add file : " + att.getOriginalFilename() + ", " + att.getSize()));

        //삭제대상 attachment id 확인
        log.debug("\n===delete for dto.getDeleteIds");
        log.debug("delete file 개수  " + dto.getDeleteIds().size());
        dto.getDeleteIds().forEach(id->{log.debug("id :" + id);});*/

        log.debug("====\n\n\n 신규입력된 tags : " + dto.getTags());
        log.debug("====\n  삭제예정 tag ids : " + dto.getDeleteTagIds());

        String delTagIdsStr = dto.getDeleteTagIds();

        //List<Long> 타입으로 변환
        List<Long> result = (delTagIdsStr == null || delTagIdsStr.isBlank())
                ? Collections.emptyList()
                : Arrays.stream(delTagIdsStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .toList();

    }


    /**
     * 게시글 수정 처리
     *
     * - 설명: 수정 폼에서 전달된 데이터로 기존 게시글 내용을 업데이트 후 상세 페이지로 리다이렉트
     * - 요청: PostUpdateDTO
     * - 응답: /posts/{id} (수정된 게시글 상세 페이지)
     */
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


    /**
     * 게시글 삭제 처리
     *
     * - 설명: 게시글 상세 페이지에서 삭제를하면 목록으로 리다이렉트 
     * - 요청: id
     * - 응답: /posts (게시글 목록 페이지)
     */
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

    /**
     * 선택한 태그만 나오는 목록
     *
     */
    @GetMapping("/tags/{tagName}")
    public String getPostsByTag(@PathVariable("tagName") String tagName, Model model) {
        List<PostDTO> posts = postService.getPostsByTag(tagName);
        model.addAttribute("tagName", tagName);
        model.addAttribute("posts", posts);
        return "posts/tag-list";
    }

}
