package dev.mkhub.knowledge.attachment.service;

import dev.mkhub.knowledge.attachment.domain.Attachment;
import dev.mkhub.knowledge.attachment.dto.FileSaveResultDTO;
import dev.mkhub.knowledge.attachment.repository.AttachmentRepository;
import dev.mkhub.knowledge.post.domain.Post;
import dev.mkhub.knowledge.attachment.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
                .tempKey(dto.getTempKey())
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

    //작성중인 post에 첨부한(삽입한) 이미지를 다시 삭제할때 삭제 대상 file_name으로 찾아서 삭제
    @Transactional
    public void cleanupUnusedTempImages(String tempKey, List<String> storedNames) {
        //attachmentRepository.deleteByFileName(fileName);

        //삭제 대상 storedNames(즉 fileName들)을 조회하여
        List<Attachment> unusedTempImages = attachmentRepository.findUnusedImages(tempKey, storedNames);

        //삭제대상 storeName 콘솔로 확인
        System.out.println("\n\n\n====삭제대상 storeName 콘솔로 확인");
        System.out.println("tempKey : " + tempKey);
        unusedTempImages.forEach(img->{System.out.println(img.getFileName());});

        //삭제 대상 이미지 파일(서버에 저장된 실제파일명)을 리스트화
        List<String> unusedFileNames = unusedTempImages.stream()
                .map(img->img.getFileName())
                .collect(Collectors.toList());

        if(unusedFileNames.size() > 0) {
            attachmentRepository.deleteByTempKeyAndStoredNames(tempKey, unusedFileNames);
        }

    }
}
