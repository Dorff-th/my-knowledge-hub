package dev.mkhub.knowledge.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.mkhub.knowledge.member.domain.QMember;
import dev.mkhub.knowledge.post.domain.QCategory;
import dev.mkhub.knowledge.post.domain.QComment;
import dev.mkhub.knowledge.post.domain.QPost;
import dev.mkhub.knowledge.post.dto.PostDetailDTO;
import dev.mkhub.knowledge.post.dto.search.PostSearchCondition;
import dev.mkhub.knowledge.post.dto.search.PostSearchPredicateBuilder;
import dev.mkhub.knowledge.post.domain.QCategory;
import dev.mkhub.knowledge.member.domain.QMember;
import dev.mkhub.knowledge.post.domain.QPost;
import dev.mkhub.knowledge.post.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final PostSearchPredicateBuilder createSearchBuilder;

    @Override
    public Page<PostDTO> findAll(PostSearchCondition condition, Pageable pageable) {
        QPost post = QPost.post;
        QMember member = QMember.member;
        QCategory category = QCategory.category;
        QComment comment = QComment.comment;

        BooleanBuilder builder = createSearchBuilder.createSearchBuilder(condition);

        List<PostDTO> content = queryFactory
                .select(Projections.constructor(PostDTO.class,
                        post.id,
                        post.title,
                        post.createdAt,
                        category.name,
                        member.username,
                        member.id,
                        comment.count(),
                        member.nickname
                ))
                .from(post)
                .leftJoin(post.category, category)
                .leftJoin(post.member, member)
                .leftJoin(comment).on(comment.post.eq(post))
                .where(builder)
                .groupBy(post.id, post.title, post.createdAt, category.name, member.username, member.id) // ✅ group by로 중복 제거
                .orderBy(post.id.desc())
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

    @Override
    public Optional<PostDetailDTO> findById(Long postId) {

        QPost post = QPost.post;
        QMember member = QMember.member;
        QCategory category = QCategory.category;

        PostDetailDTO content = queryFactory
                .select(Projections.constructor(PostDetailDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.createdAt,
                        post.updatedAt,
                        category.name,
                        member.id,
                        member.username,
                        category.id,
                        member.nickname
                ))
                .from(post)
                .leftJoin(post.category, category)
                .leftJoin(post.member, member)
                .where(post.id.eq(postId))
                .fetchOne();

        return Optional.ofNullable(content);
    }
}

