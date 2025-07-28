package dev.mkhub.knowledge.post.dto.search;

import lombok.*;

import java.time.LocalDate;

/**
Post에서 검색할수 있는 프로퍼티를 관리
 */
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSearchCondition {

    private Long categoryId;
    private String searchType;   // "title", "content", "tc" 등
    private String keyword;


}
