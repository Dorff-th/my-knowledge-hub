package dev.mkhub.knowledge.post.repository;

import dev.mkhub.knowledge.post.domain.Comment;
import dev.mkhub.knowledge.member.domain.Member;
import dev.mkhub.knowledge.post.domain.Post;
import dev.mkhub.knowledge.member.repository.MemberRepository;
import dev.mkhub.knowledge.post.dto.CommentResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private  PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;


    private Random random = new Random();

    //@Test
    @DisplayName("comment dummy 데이터 insert")
    void insertDummyComments() {
        List<Long> memberIds = List.of(22L, 23L, 24L);
        int numberOfComments = 50;

        for (int i = 1; i <= numberOfComments; i++) {
            // 랜덤 Post, Member 가져오기
            Long randomPostId = (long) (random.nextInt(80) + 1); // 1 ~ 80
            Long randomMemberId = memberIds.get(random.nextInt(memberIds.size()));

            Optional<Post> optionalPost = postRepository.findById(randomPostId);
            Optional<Member> optionalMember = memberRepository.findById(randomMemberId);

            if (optionalPost.isEmpty() || optionalMember.isEmpty()) continue;

            Post post = optionalPost.get();
            Member member = optionalMember.get();

            Comment comment = Comment.builder()
                    .content("댓글 더미 #" + i)
                    .createdAt(LocalDateTime.now().minusDays(random.nextInt(30)))
                    .post(post)
                    .member(member)
                    .build();

            commentRepository.save(comment);
        }

        System.out.println(numberOfComments + "개의 댓글이 저장되었습니다.");
    }

    @Test
    @DisplayName("특정 게시물(Post)의 코멘트 목록을 조회한다.")
    void testFindAllByPostId() {
        Long postId = 70L;

        List<CommentResponseDTO> result = commentRepository.findAllByPostId(postId);

        result.forEach(dto->System.out.println(dto));
    }
}