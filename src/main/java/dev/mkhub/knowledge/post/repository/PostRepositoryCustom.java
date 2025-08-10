package dev.mkhub.knowledge.post.repository;

import dev.mkhub.knowledge.post.dto.PostDTO;
import dev.mkhub.knowledge.post.dto.PostDetailDTO;
import dev.mkhub.knowledge.post.dto.search.PostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {

    //Post 목록 페이징 메서드
    Page<PostDTO> findAll(PostSearchCondition condition, Pageable pageable);

    // Post 상세 내용 조회 메서드
    Optional<PostDetailDTO> findById(Long id);

    List<PostDTO> findPostsByTagName(String tagName);

}
