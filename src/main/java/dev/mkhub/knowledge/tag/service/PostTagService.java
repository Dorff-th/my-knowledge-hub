package dev.mkhub.knowledge.tag.service;

import dev.mkhub.knowledge.tag.domain.PostTag;
import dev.mkhub.knowledge.tag.dto.TagDTO;
import dev.mkhub.knowledge.tag.repository.PostTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostTagService {
    private final PostTagRepository postTagRepository;

    //editor 화면에 postTag.id를 가져오기 위해 필요
    public List<TagDTO> getByPostId(Long postId) {
        return postTagRepository.findTagsByPostId(postId);
    }
}
