package dev.mkhub.knowledge.post.dto.search;

import com.querydsl.core.BooleanBuilder;
import dev.mkhub.knowledge.domain.QPost;
import org.springframework.stereotype.Component;

@Component
public class PostSearchPredicateBuilder {

    public BooleanBuilder createSearchBuilder(PostSearchCondition condition) {
        QPost post = QPost.post;
        BooleanBuilder builder = new BooleanBuilder();

        //builder.and(post.member.id.eq(memberId));

        if (condition.getSearchType() != null && condition.getKeyword() != null) {
            switch (condition.getSearchType()) {
                case "title" -> builder.and(post.title.contains(condition.getKeyword()));
                case "content" -> builder.and(post.content.contains(condition.getKeyword()));
                case "tc" -> builder.and(
                        post.title.contains(condition.getKeyword())
                                .or(post.content.contains(condition.getKeyword()))
                );
            }
        }

        Long searchCategoryId = condition.getCategoryId();
        if(searchCategoryId != null) {
            if(searchCategoryId > 0) {  //카테고리 ID 가 1이상이면 해당 카테코리 ID 조건 추가 :
                builder.and(post.category.id.eq(condition.getCategoryId()));
            }
        }



        return builder;
    }

}
