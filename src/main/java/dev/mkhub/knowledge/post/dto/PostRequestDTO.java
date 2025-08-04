package dev.mkhub.knowledge.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostRequestDTO {

    @NotBlank(message = "{NotBlankPostTitle}")
    private String title;

    @NotBlank(message = "{NotBlankPostContent}")
    private String content;

    private Long memberId;

    private Long categoryId;

    private String tempKey;

}
