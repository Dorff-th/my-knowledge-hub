package dev.mkhub.knowledge.post.repository;

import dev.mkhub.knowledge.post.dto.PostDTO;
import dev.mkhub.knowledge.post.dto.search.PostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    //Post 목록 페이징 메서드
    Page<PostDTO> findAllByMemberId(Long memberId, PostSearchCondition condition, Pageable pageable);

}
