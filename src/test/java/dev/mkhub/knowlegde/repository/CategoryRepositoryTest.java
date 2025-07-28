package dev.mkhub.knowlegde.repository;

import dev.mkhub.knowledge.domain.Category;
import dev.mkhub.knowledge.post.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
@Transactional
@Rollback(false)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void insertCategoryDummyData() {
        String[] categoryNames = {
                "딥러닝", "머신러닝", "Spring Boot", "Kafka", "Redis",
                "JPA", "Hibernate", "Docker", "Kubernetes", "Clean Code",
                "RxJava", "Design Patterns", "AI 윤리", "데이터베이스", "Git",
                "CI/CD", "REST API", "GraphQL", "클라우드 인프라", "테스트 코드"
        };

        // 랜덤으로 7개 뽑아 insert
        List<String> randomList = ThreadLocalRandom.current()
                .ints(0, categoryNames.length)
                .distinct()
                .limit(7)
                .mapToObj(i -> categoryNames[i])
                .toList();

        List<Category> categoryList = randomList.stream()
                .map(Category::new)
                .toList();

        categoryRepository.saveAll(categoryList);

        System.out.println("✅ 랜덤 카테고리 insert 완료: " + randomList);
    }

}