package dev.mkhub.knowledge.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class CommentResponseDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long postId;
    private Long memberId;
    private String username;

}
