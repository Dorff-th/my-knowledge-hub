package dev.mkhub.knowledge.tag.repository;

import dev.mkhub.knowledge.tag.domain.PostTag;
import dev.mkhub.knowledge.tag.dto.TagDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    //postId값으로 태그 조회
    @Query("SELECT new dev.mkhub.knowledge.tag.dto.TagDTO(pt, t.name) " +
            "FROM PostTag pt " +
            "JOIN pt.tag t " +
            "WHERE pt.post.id = :postId")
    List<TagDTO> findTagsByPostId(@Param("postId") Long postId);

    // 수정할 게시글(postId)에서 삭제할 tag id들을 찾아서 삭제한다.
    void deleteByPostIdAndTagIdIn(Long postId, List<Long> tagIds);
}