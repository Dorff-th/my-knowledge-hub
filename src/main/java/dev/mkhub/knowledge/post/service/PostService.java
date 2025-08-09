package dev.mkhub.knowledge.post.service;

import dev.mkhub.knowledge.attachment.domain.Attachment;
import dev.mkhub.knowledge.attachment.dto.FileSaveResultDTO;
import dev.mkhub.knowledge.attachment.repository.AttachmentRepository;
import dev.mkhub.knowledge.attachment.repository.ImageUploadRepository;
import dev.mkhub.knowledge.attachment.util.GeneralFileUtil;
import dev.mkhub.knowledge.post.domain.Category;
import dev.mkhub.knowledge.member.domain.Member;
import dev.mkhub.knowledge.post.domain.Post;
import dev.mkhub.knowledge.member.repository.MemberRepository;
import dev.mkhub.knowledge.post.dto.PostDetailDTO;
import dev.mkhub.knowledge.post.dto.PostRequestDTO;
import dev.mkhub.knowledge.post.dto.PostUpdateDTO;
import dev.mkhub.knowledge.post.dto.search.PostSearchCondition;
import dev.mkhub.knowledge.post.dto.PostDTO;
import dev.mkhub.knowledge.common.dto.PageRequestDTO;
import dev.mkhub.knowledge.common.dto.PageResponseDTO;
import dev.mkhub.knowledge.post.repository.CategoryRepository;
import dev.mkhub.knowledge.post.repository.PostRepository;
import dev.mkhub.knowledge.post.repository.PostRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostRepositoryCustom postRepositoryCustom;

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    private final ImageUploadRepository imageUploadRepository;

    private final GeneralFileUtil generalFileUtil;
    private final AttachmentRepository attachmentRepository;

    //post 페이징(목록)
    public PageResponseDTO<PostDTO> getPostList(PostSearchCondition postSearchCondition, PageRequestDTO requestDTO) {

        Pageable pageable = PageRequest.of(
                requestDTO.getPage() - 1,
                requestDTO.getSize(),
                Sort.by(Sort.Direction.valueOf(requestDTO.getDirection().toString()), requestDTO.getSort()));


        Page<PostDTO> result =  postRepositoryCustom.findAll(postSearchCondition, pageable);
        List<PostDTO> dtoList = result.getContent();

        return new PageResponseDTO<>(requestDTO, result.getTotalElements(), dtoList, 10);

    }

    //post 상세
    public Optional<PostDetailDTO> getPost(Long id) {

        return postRepositoryCustom.findById(id);

    }

    @Transactional
    public Post createPost(PostRequestDTO dto) {
        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(()->new IllegalArgumentException("사용자가 없습니다."));
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(()->new IllegalArgumentException("카테고리가 없습니다."));

        Post post = new Post(dto.getTitle(), dto.getContent(), member, category);

        Post savedPost = postRepository.save(post);

        //첨부파일 저장
        if(dto.getAttachments() != null && !dto.getAttachments().isEmpty()  ) {
            List<FileSaveResultDTO> fileSaveResultDTO= generalFileUtil.saveFiles(dto.getAttachments(), savedPost.getId());
            //fileSaveResultDTO.forEach(fileSaveDTO->log.debug("fileSaveDTO :" , fileSaveDTO.toString()));

            List<Attachment> attachments = fileSaveResultDTO.stream()
                    .map(saveDto -> Attachment.builder()
                            .post(post)
                            .fileName(saveDto.getFileName())
                            .originFileName(saveDto.getOriginFileName())
                            .fileType(saveDto.getFileType())
                            .fileSize(saveDto.getSize())
                            .fileUrl(saveDto.getFileUrl())
                            .uploadType(saveDto.getUploadType())
                            .uploadedAt(LocalDateTime.now())
                            .build())
                    .toList();

            attachmentRepository.saveAll(attachments);
        }

        //에디터에 첨부된 이미지 파일 정보에 저장된 post id 업데이트
        String tempKey = dto.getTempKey();
        boolean existsByTempKey = imageUploadRepository.existsByTempKey(tempKey);
        if(existsByTempKey) {
            imageUploadRepository.updatePostIdByTempKey(savedPost.getId(), tempKey);
        }

        return savedPost;
    }

    //Post 수정요청할때 필요한 해당 post 작성자(Member) id값을 조회하기 위해 필요
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }


    @Transactional
    public int editPost(PostUpdateDTO dto) {

        if(dto.getAttachments() != null && !dto.getAttachments().isEmpty()  ) {
            List<FileSaveResultDTO> fileSaveResultDTO= generalFileUtil.saveFiles(dto.getAttachments(), dto.getId());
            //fileSaveResultDTO.forEach(fileSaveDTO->log.debug("fileSaveDTO :" , fileSaveDTO.toString()));

            Post post = postRepository.findById(dto.getId()).orElseThrow(()->new IllegalArgumentException("해당 post가 없음!"));

            List<Attachment> attachments = fileSaveResultDTO.stream()
                    .map(saveDto -> Attachment.builder()
                            .post(post)
                            .fileName(saveDto.getFileName())
                            .originFileName(saveDto.getOriginFileName())
                            .fileType(saveDto.getFileType())
                            .fileSize(saveDto.getSize())
                            .fileUrl(saveDto.getFileUrl())
                            .uploadType(saveDto.getUploadType())
                            .uploadedAt(LocalDateTime.now())
                            .build())
                    .toList();

            attachmentRepository.saveAll(attachments);
        }

        //첨부된 파일중 삭제 대상 첨부 파일 삭제
        if( dto.getDeleteIds() != null && !dto.getDeleteIds().isEmpty()) {
            attachmentRepository.deleteAllByIdInBatch(dto.getDeleteIds());
        }



      return  postRepository.updatePostById(dto);
    }


    //Post 삭제
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
