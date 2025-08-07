package dev.mkhub.knowledge.attachment.repository;

import dev.mkhub.knowledge.attachment.domain.Attachment;
import dev.mkhub.knowledge.attachment.enums.UploadType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    @Query("SELECT a FROM Attachment a WHERE a.post.id = :postId AND a.uploadType = :uploadType")
    List<Attachment> findByPostId(@Param("postId") Long postId, @Param("uploadType") UploadType uploadType);
}
