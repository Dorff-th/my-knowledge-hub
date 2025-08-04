package dev.mkhub.knowledge.attachment.domain;

import dev.mkhub.knowledge.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "attachment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(nullable = false)
    //private Long postId;

    private String fileName;    // 서버에 저장되는 실제 파일이름(파일이름은 UUID 형식)
    private String fileUrl;  // 실제 서버에 저장되는 물리경로 (예 C:/xxx/xxx)

    private String publicUrl;    // 웹브라우저 url 호출경로 (예 localhost:8080/upload/images/)

    private String originFileName;  // 첨부파일 원래 이름

    private String fileType;

    private Long fileSize;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    private String uploadType;  // 첨부파일형식 : 에디터 이미지 첨부파일(EDITOR_IMAGE), 일반 첨부파일(ATTACHMENT)



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
