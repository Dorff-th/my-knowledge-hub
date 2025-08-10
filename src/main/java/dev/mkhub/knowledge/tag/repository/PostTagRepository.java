package dev.mkhub.knowledge.tag.repository;

import dev.mkhub.knowledge.tag.domain.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
}