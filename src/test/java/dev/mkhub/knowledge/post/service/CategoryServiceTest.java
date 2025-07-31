package dev.mkhub.knowledge.post.service;

import dev.mkhub.knowledge.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    @Test
    @DisplayName("Category 모든 목록을 조회 테스트")
    void findAllCategory() {
        List<Category> categories = categoryService.findAllCategory();

        categories.forEach(category -> {System.out.println(category.getId() + " : " + category.getName());});
    }
}