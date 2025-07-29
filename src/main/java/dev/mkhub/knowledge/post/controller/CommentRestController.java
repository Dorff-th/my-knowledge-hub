package dev.mkhub.knowledge.post.controller;

import dev.mkhub.knowledge.member.security.MemberDetails;
import dev.mkhub.knowledge.post.dto.CommentResponseDTO;
import dev.mkhub.knowledge.post.repository.CommentRepository;
import dev.mkhub.knowledge.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class CommentRestController {

    private final CommentService commentService;

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> comments(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable("postId") Long postId) {

        /*try {
            Thread.sleep(2000); // 2초 동안 강제 지연
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }*/

        List<CommentResponseDTO> comments = commentService.getCommentList(postId);

        return ResponseEntity.ok(comments);


    }
}
