package dev.mkhub.knowledge.post.repository;

import dev.mkhub.knowledge.post.domain.Post;
import dev.mkhub.knowledge.post.dto.PostDTO;
import dev.mkhub.knowledge.post.dto.PostUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PostRepository extends JpaRepository<Post, Long> {

    //미사용 - 학습 & 테스트용
    @Query("""
        SELECT 
        new dev.mkhub.knowledge.post.dto.PostDTO(
            p.id,
            p.title, 
            p.createdAt,
            c.name,
            m.username, 
            m.id
           ) 
        FROM Post p
        LEFT OUTER JOIN  p.category c       
        LEFT OUTER JOIN  p.member m
        """)
    Page<PostDTO> findAllByMemberId(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Post a SET a.title = :#{#dto.title}, a.content =:#{#dto.content}, a.category.id =:#{#dto.categoryId}, a.updatedAt =:#{#dto.updatedAt} " +
            "WHERE a.id = :#{#dto.id}")
    int updatePostById(@Param("dto")PostUpdateDTO dto);


}
