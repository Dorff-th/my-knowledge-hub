package dev.mkhub.knowledge.post.dto;

import lombok.*;

import java.time.LocalDateTime;

/*
Post 상세내용을 반환하는 DTO
 */

@AllArgsConstructor
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
    //private Long commentCount; <-- Post 상세 내용은 댓글 개수는 CommentDTO에서 포함예정
}
