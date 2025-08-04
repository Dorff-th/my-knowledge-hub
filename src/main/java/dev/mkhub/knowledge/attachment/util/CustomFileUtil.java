package dev.mkhub.knowledge.attachment.util;

import dev.mkhub.knowledge.attachment.dto.FileSaveResultDTO;
import dev.mkhub.knowledge.attachment.enums.UploadType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 첨부파일 업로드 공통 클래스(일반첨부, 에디터이미지 첨부)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomFileUtil {

    //application.yml 에 설정한  에디터 이미지 첨부파일. 실제 서버 경로 값을 가져온다.
    @Value("${file.upload.path.images.base-dir}")
    private String imageBaseDir;

    //application.yml 에 설정한  에디터 이미지 첨부파일. 외보 공개 URL 값을 가져온다.
    @Value("${file.upload.path.images.public-url}")
    private String imagePublicUrl;

    //application.yml 에 설정한  일반 첨부파일 uploadpath값을 가져온다
    @Value("${file.upload.path.attachments}")
    private String attachmentsPath;

    //에디터 이미지 첨부, 일반 첨부 둘다 초기화
        @PostConstruct
        public void init() {
            initPath(imageBaseDir);
            initPath(attachmentsPath);
        }

        private void initPath(String path) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs(); // 하위 디렉토리까지 생성
            }
            log.info("Upload path initialized: {}", dir.getAbsolutePath());
        }

    /** ✅ 단일 파일 저장 (에디터용 이미지) */
    public FileSaveResultDTO saveEditorImageFile(MultipartFile file) {
        return saveSingleFile(file, imageBaseDir, UploadType.EDITOR_IMAGE.toString());
    }

    /** ✅ 다중 파일 저장 (일반 첨부파일) */
    public List<FileSaveResultDTO> saveFiles(List<MultipartFile> files) {
        List<FileSaveResultDTO> results = new ArrayList<>();
        for (MultipartFile file : files) {
            results.add(saveSingleFile(file, attachmentsPath, UploadType.ATTACHMENT.toString()));
        }
        return results;
    }



    private FileSaveResultDTO saveSingleFile(MultipartFile file, String uploadDir, String uploadType) {
        try {
            String originalName = file.getOriginalFilename();
            String savedName = UUID.randomUUID() + "_" + originalName;

            File target = new File(uploadDir, savedName);
            file.transferTo(target);

            // public URL 생성 (※ /uploads/images/ 는 설정된 정적 매핑 경로에 따라 조정)
            //String publicUrl = "/uploads/images/" + savedName;
            String imagePublicUrl = "/uploads/images/" + savedName;

            return FileSaveResultDTO.builder()
                    .fileName(savedName)
                    .originFileName(originalName)
                    .fileUrl(uploadDir + savedName)     // 실제 파일이 서버의 물리경로
                    .publicUrl(imagePublicUrl)             // 외부 경로
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .uploadType(uploadType)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + file.getOriginalFilename(), e);
        }
    }

}
