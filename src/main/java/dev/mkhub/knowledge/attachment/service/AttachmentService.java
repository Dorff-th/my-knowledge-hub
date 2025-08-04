package dev.mkhub.knowledge.attachment.service;

import dev.mkhub.knowledge.attachment.domain.Attachment;
import dev.mkhub.knowledge.attachment.dto.FileSaveResultDTO;
import dev.mkhub.knowledge.attachment.repository.AttachmentRepository;
import dev.mkhub.knowledge.post.domain.Post;
import dev.mkhub.knowledge.attachment.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final CustomFileUtil fileUtil;

    //에디터 이미지 첨부파일 저장
    public Attachment uploadEditorImageFile(FileSaveResultDTO dto) {
        Attachment attachment = Attachment.builder()
                .post(null)
                .fileName(dto.getFileName())
                .originFileName(dto.getOriginFileName())
                .fileType(dto.getFileType())
                .fileSize(dto.getSize())
                .fileUrl(dto.getFileUrl())
                .publicUrl(dto.getPublicUrl())
                .uploadType(dto.getUploadType())
                .uploadedAt(LocalDateTime.now())
                .build();

        return attachmentRepository.save(attachment);

    }

    //일반 첨부파일 저장(다중 파일 업로드)
    public List<Attachment> uploadAttachments(Post post,  List<FileSaveResultDTO> fileResults) {

        List<Attachment> attachments = fileResults.stream()
                .map(dto -> Attachment.builder()
                        .post(post)
                        .fileName(dto.getFileName())
                        .originFileName(dto.getOriginFileName())
                        .fileType(dto.getFileType())
                        .fileSize(dto.getSize())
                        .fileUrl(dto.getFileUrl())
                        .uploadType(dto.getUploadType())
                        .uploadedAt(LocalDateTime.now())
                        .build())
                .toList();


        return attachmentRepository.saveAll(attachments);
    }
}
