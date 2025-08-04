package dev.mkhub.knowledge.post.repository;

import dev.mkhub.knowledge.post.domain.Post;
import dev.mkhub.knowledge.post.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
    
    //Post 상세조회 -  JpaRepository 자체 지원 findById를 서비스 로직에서 구현하면 되니까 별도 메서드 선언 필요 없음
    

}
