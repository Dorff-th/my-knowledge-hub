package dev.mkhub.knowledge.attachment.repository;

import dev.mkhub.knowledge.attachment.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByPostId(Long postId);
    void deleteByPostId(Long postId);
}
