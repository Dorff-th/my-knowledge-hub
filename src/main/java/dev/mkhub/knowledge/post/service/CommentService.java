package dev.mkhub.knowledge.post.service;

import dev.mkhub.knowledge.post.dto.CommentResponseDTO;
import dev.mkhub.knowledge.post.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentResponseDTO> getCommentList(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }
}
