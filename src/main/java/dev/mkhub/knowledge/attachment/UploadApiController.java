package dev.mkhub.knowledge.attachment;

import dev.mkhub.knowledge.attachment.domain.Attachment;
import dev.mkhub.knowledge.attachment.dto.FileSaveResultDTO;
import dev.mkhub.knowledge.attachment.service.AttachmentService;
import dev.mkhub.knowledge.attachment.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 에디터 이미지 첨부 업로드 Api Controller
 */

@RestController
@RequiredArgsConstructor
@Slf4j
public class UploadApiController {

    private final AttachmentService attachmentService;
    private final CustomFileUtil customFileUtil;

    @PostMapping("/api/images/upload")
    public Map<String, Object> uploadEditorImage(
            @RequestParam("image") MultipartFile file, @RequestParam("tempKey") String tempKey) {

        System.out.println(file.getOriginalFilename());

        // 1. 파일 저장 (FileUtil에서)
        FileSaveResultDTO fileDto = customFileUtil.saveEditorImageFile(file, tempKey);  // DTO 반환

        // 2. 서비스에서 Attachment 엔티티 생성 하고 DB에 저장
        Attachment attachment = attachmentService.uploadEditorImageFile(fileDto);

        // 3. 에디터가 요구하는 응답 포맷
        return Map.of(
                "success", 1,
                "url", fileDto.getPublicUrl()  // 에디터에서 즉시 미리보기 가능
        );
    }
}
