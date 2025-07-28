package dev.mkhub.knowledge.post.repository;

import dev.mkhub.knowledge.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository  extends JpaRepository<Comment, Long> {
}
