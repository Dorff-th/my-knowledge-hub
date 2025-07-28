package dev.mkhub.knowledge.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.mkhub.knowledge.domain.QComment;
import dev.mkhub.knowledge.post.dto.search.PostSearchCondition;
import dev.mkhub.knowledge.post.dto.search.PostSearchPredicateBuilder;
import dev.mkhub.knowledge.domain.QCategory;
import dev.mkhub.knowledge.domain.QMember;
import dev.mkhub.knowledge.domain.QPost;
import dev.mkhub.knowledge.post.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final PostSearchPredicateBuilder createSearchBuilder;

    @Override
    public Page<PostDTO> findAllByMemberId(Long memberId, PostSearchCondition condition, Pageable pageable) {
        QPost post = QPost.post;
        QMember member = QMember.member;
        QCategory category = QCategory.category;
        QComment comment = QComment.comment;

        BooleanBuilder builder = createSearchBuilder.createSearchBuilder(memberId, condition);

        List<PostDTO> content = queryFactory
                .select(Projections.constructor(PostDTO.class,
                        post.id,
                        post.title,
                        post.createdAt,
                        category.name,
                        member.username,
                        member.id,
                        comment.count()
                ))
                .from(post)
                .leftJoin(post.category, category)
                .leftJoin(post.member, member)
                .leftJoin(comment).on(comment.post.eq(post))
                .where(builder)
                .groupBy(post.id, post.title, post.createdAt, category.name, member.username, member.id) // ✅ group by로 중복 제거
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(post.count())
                .from(post)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}

