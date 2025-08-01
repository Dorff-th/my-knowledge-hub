package dev.mkhub.knowledge.post.repository;

import dev.mkhub.knowledge.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
