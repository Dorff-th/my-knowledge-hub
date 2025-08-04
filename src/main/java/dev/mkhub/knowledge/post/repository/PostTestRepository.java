package dev.mkhub.knowledge.post.repository;

import dev.mkhub.knowledge.post.domain.Post;
import dev.mkhub.knowledge.post.dto.PostDTO;
import dev.mkhub.knowledge.post.dto.projection.PostSummaryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostTestRepository extends JpaRepository<Post, Long> {
    // test1 - native 쿼리 :  조건없는 전체 post 목록조회
    @Query(value = """
        SELECT  a.id, b.name AS category_name, a.title, c.id AS member_id, c.username, a.created_at
            FROM
        post a
            LEFT OUTER join category b ON a.category_id = b.id
            LEFT OUTER join member c ON a.member_id = c.id
        ORDER BY a.id desc
        """, nativeQuery = true)
    List<Object[]> findBbyNativeAllOrderById();

    // test2 - JPQL 쿼리 :  조건없는 전체 post 목록조회
    @Query("""
        SELECT p, c, m FROM Post p
            LEFT OUTER JOIN  p.category c       
            LEFT OUTER JOIN  p.member m
        ORDER BY p.id desc    
        """)
    List<Post> findBbyAllOrderById();

    // test3- JPQL 쿼리 (Projection-interface으로 반환) :  조건없는 전체 post 목록조회
    @Query("""
        SELECT 
            p.id AS id,
            p.title AS title, 
            p.createdAt as createdAt,
            c.name AS categoryName,
            m.username AS username, 
            m.id AS memberId
        FROM Post p
        LEFT OUTER JOIN  p.category c       
        LEFT OUTER JOIN  p.member m
        ORDER BY p.id desc    
        """)
    List<PostSummaryProjection> findBbyAllOrderByIdWithProjection();

    // test4- JPQL 쿼리 (Projection-class (DTO) 으로 반환) :  조건없는 전체 post 목록조회
    @Query("""
        SELECT 
        new dev.mkhub.knowledge.post.dto.PostDTO(
            p.id AS id,
            p.title AS title, 
            p.createdAt as createdAt,
            c.name AS categoryName,
            m.username AS username, 
            m.id AS memberId
           ) 
        FROM Post p
        LEFT OUTER JOIN  p.category c       
        LEFT OUTER JOIN  p.member m
        ORDER BY p.id desc    
        """)
    List<PostDTO> findBbyAllOrderByIdWithDTO();





}
