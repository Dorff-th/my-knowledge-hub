package dev.mkhub.knowledge.attachment.dto;

import lombok.*;

import java.util.List;

/**
 * 에디터(ToastUI 마크디운)에 작성된 내용중 첨부된 이미지를 다시 삭제할경우에 필요한 요청 DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TempImageCleanupRequestDTO {
    private String tempKey;
    private List<String> storedNames;
}
