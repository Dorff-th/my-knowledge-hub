package dev.mkhub.knowledge.post.service;

import dev.mkhub.knowledge.post.dto.PostDTO;
import dev.mkhub.knowledge.post.dto.PostDetailDTO;
import dev.mkhub.knowledge.common.dto.PageRequestDTO;
import dev.mkhub.knowledge.common.dto.PageResponseDTO;
import dev.mkhub.knowledge.common.dto.SortDirection;
import dev.mkhub.knowledge.post.dto.search.PostSearchCondition;
import dev.mkhub.knowledge.post.repository.PostRepository;
import dev.mkhub.knowledge.post.repository.PostRepositoryCustom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostRepositoryCustom postRepositoryCustom;

    @Test
    @DisplayName("Post 페이징 처리 테스트 with QueryDSL")
    void testPostsPaging() {

        Long memberId = 23L;    // 테스트 Member id

        SortDirection sortDirection = SortDirection.ASC; // 정렬 역순
        String sortColumn = "id";

        //검색조건 테스트
        PostSearchCondition postSearchCondition = PostSearchCondition.builder()
                .searchType("title")
                .keyword("API")
                .build();

        PageRequestDTO pageRequestDTO = new PageRequestDTO(1, 10, sortColumn, sortDirection);
        PageResponseDTO<PostDTO> result = postService.getPostList(postSearchCondition, pageRequestDTO);

        System.out.println(result.getTotalElements());
        result.getDtoList().forEach(postDTO -> System.out.println(postDTO));


    }

    @Test
    @DisplayName("Post 상세 내용 조회 테스트")
    void testPostDetail() {
        Long id = 80L;


        PostDetailDTO postDetailDTO = postRepositoryCustom.findById(id)
                .orElseThrow(()->new IllegalArgumentException("ERROR"));

        System.out.println(postDetailDTO);
    }


}