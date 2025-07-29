package dev.mkhub.knowledge.post.repository;

import dev.mkhub.knowledge.domain.Comment;
import dev.mkhub.knowledge.post.dto.CommentResponseDTO;
import dev.mkhub.knowledge.post.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment, Long> {

    @Query("""
        SELECT 
        new dev.mkhub.knowledge.post.dto.CommentResponseDTO(
            c.id,
            c.content, 
            c.createdAt,
            c.post.Id,
            m.id,
            m.username
           ) 
        FROM Comment c
        INNER JOIN  c.member m
        WHERE c.post.Id = :postId
        """)
    List<CommentResponseDTO> findAllByPostId(@Param("postId") Long postId);
}
