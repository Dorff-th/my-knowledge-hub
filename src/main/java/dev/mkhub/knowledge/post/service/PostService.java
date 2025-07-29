package dev.mkhub.knowledge.post.service;

import dev.mkhub.knowledge.post.dto.PostDetailDTO;
import dev.mkhub.knowledge.post.dto.search.PostSearchCondition;
import dev.mkhub.knowledge.post.dto.PostDTO;
import dev.mkhub.knowledge.post.dto.page.PageRequestDTO;
import dev.mkhub.knowledge.post.dto.page.PageResponseDTO;
import dev.mkhub.knowledge.post.repository.PostRepository;
import dev.mkhub.knowledge.post.repository.PostRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostRepositoryCustom postRepositoryCustom;

    //post 페이징(목록)
    public PageResponseDTO<PostDTO> getPostList(Long memberId, PostSearchCondition postSearchCondition, PageRequestDTO requestDTO) {

        Pageable pageable = PageRequest.of(
                requestDTO.getPage() - 1,
                requestDTO.getSize(),
                Sort.by(Sort.Direction.valueOf(requestDTO.getDirection().toString()), requestDTO.getSort()));


        Page<PostDTO> result =  postRepositoryCustom.findAllByMemberId(memberId, postSearchCondition, pageable);
        List<PostDTO> dtoList = result.getContent();

        return new PageResponseDTO<>(requestDTO, result.getTotalElements(), dtoList, 10);

    }

    //post 상세
    public PostDetailDTO getPost(Long postId, Long memberId) {

        return postRepositoryCustom.findByIdAndMemberId(postId, memberId);
    }
}
