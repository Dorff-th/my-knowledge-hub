package dev.mkhub.knowledge.post.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/*
Post 상세내용을 반환하는 DTO
 */


@NoArgsConstructor
@Builder
@Data
@ToString
public class PostDetailDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String categoryName;
    private Long memberId;
    private String username;
    private Long categoryId;
    private String nickname;



    public PostDetailDTO(Long id, String title, String content, LocalDateTime createdAt,
                         LocalDateTime updatedAt, String categoryName, Long memberId,
                         String username, Long categoryId, String nickname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categoryName = categoryName;
        this.memberId = memberId;
        this.username = username;
        this.categoryId = categoryId;
        this.nickname = nickname;

    }
}
