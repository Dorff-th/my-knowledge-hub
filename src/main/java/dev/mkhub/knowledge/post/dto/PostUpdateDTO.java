package dev.mkhub.knowledge.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 *   기존 Post 수정 요청을 하는 DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostUpdateDTO {

    private Long id;
    @NotBlank(message = "{NotBlankPostTitle}")
    private String title;

    @NotBlank(message = "{NotBlankPostContent}")
    private String content;

    private Long memberId;

    private Long categoryId;
}
