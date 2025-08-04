package dev.mkhub.knowledge.post.repository;

import dev.mkhub.knowledge.post.domain.Category;
import dev.mkhub.knowledge.member.domain.Member;
import dev.mkhub.knowledge.post.domain.Post;
import dev.mkhub.knowledge.post.dto.PostDTO;
import dev.mkhub.knowledge.post.dto.projection.PostSummaryProjection;
import dev.mkhub.knowledge.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
@Rollback(false)
class PostRepositoryTest {

    @Autowired
    private PostTestRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    //@Test
    void testInsertDummyPosts() {
        String[] sampleTitles = {
                "디지털의 심연: 알고리즘은 우리를 지배하는가?",
                "의식 없는 코드, 그러나 살아 있는 패턴",
                "잊혀진 트랜잭션: 롤백되지 못한 기억들",
                "0과 1 사이의 망각",
                "API의 저편에는 무엇이 있는가?",
                "나를 만든 건 누구인가 - 컴파일되지 못한 자아",
                "스레드 속에서 길을 잃다",
                "프로세스는 죽지 않는다, 단지 좀비가 될 뿐",
                "예외는 무시되지 않는다 - 로그 속의 진실",
                "루프의 저주: 탈출하지 못한 반복문"
        };

        String[] sampleContents = {
                "이 게시물은 기술적인 내용을 담고 있습니다. 자세한 설명은 생략합니다.",
                "간단한 예제를 통해 개념을 설명하고자 합니다.",
                "이건 단순한 테스트용 데이터입니다.",
                "해당 글은 MyKnowledgeHub 프로젝트의 더미 포스트입니다.",
                "개발자들을 위한 기초 가이드입니다.",
                "이 글은 자동 생성된 게시글입니다.",
                "내용은 랜덤하게 선택되었습니다.",
                "Post 엔티티의 insert를 테스트하기 위한 목적입니다.",
                "MySQL과 JPA 연동을 확인하고자 합니다.",
                "Spring Boot 환경에서 실행된 테스트 결과입니다."
        };

        List<Member> members = memberRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        for (int i = 1; i <= 70; i++) {
            Member author = members.get(i % members.size());
            Category category = categories.get(i % categories.size());

            String title = sampleTitles[ThreadLocalRandom.current().nextInt(sampleTitles.length)];
            String content = sampleContents[ThreadLocalRandom.current().nextInt(sampleContents.length)];

            Post post = new Post(
                    title,
                    content,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    author,
                    category
            );

            postRepository.save(post);
        }
    }

    //@Test
    @DisplayName("Post 전체 목록 조회: Native Query")
    void testFindAllNativePost() {

        List<Object[]> posts = postRepository.findBbyNativeAllOrderById();

        List<PostDTO> result = posts.stream()
                .map(PostDTO::objectToDTO)
                .collect(Collectors.toList());

        result.forEach(postDTO -> System.out.println(postDTO.toString()));
    }

    //@Test
    @DisplayName("Post 전체 목록 조회 : JPQL ")
    void testFineAllPost() {
        List<Post> posts = postRepository.findBbyAllOrderById();

        posts.forEach(post->{System.out.println(post.getTitle());});
    }

    //@Test
    @DisplayName("Post 전체 목록 조회 : JPQL Porjection interface 사용")
    void testFineAllPostWithProjection() {
        List<PostSummaryProjection> posts = postRepository.findBbyAllOrderByIdWithProjection();

        posts.forEach(post->{System.out.println(post.getTitle());});
    }

    @Test
    @DisplayName("Post 전체 목록 조회 : JPQL Porjection interface 사용")
    void testFindAllPostWithDTO() {


        List<PostDTO> posts = postRepository.findBbyAllOrderByIdWithDTO();

        posts.forEach(dto->{System.out.println(dto);});
    }


}