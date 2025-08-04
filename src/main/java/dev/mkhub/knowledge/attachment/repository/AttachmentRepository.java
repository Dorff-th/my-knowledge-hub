package dev.mkhub.knowledge.attachment.repository;

import dev.mkhub.knowledge.attachment.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByPostId(Long postId);
    void deleteByPostId(Long postId);

    //에디터에 첨부된 이미지 파일 정보에 post id를 update하기 위해 tempKey값이 있는지 조회
    boolean existsByTempKey(String tempKey);

    @Modifying
    @Transactional
    @Query("UPDATE Attachment a SET a.post.id = :postId WHERE a.tempKey = :tempKey")
    void updatePostIdByTempKey(@Param("postId") Long postId, @Param("tempKey") String tempKey);


}
