package dev.mkhub.knowledge.search.service;

import dev.mkhub.knowledge.search.dto.SearchResultDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SearchServiceTest {

    @Autowired
    private SearchService service;

    @Test
    @DisplayName("검색 쿼리 동작 여부 확인 - 동일 id값을 가진 post 제목 중복 제거 테스트")
    void findPostByKeywordMerge() {

        String keyword = "API";

        List<SearchResultDTO> searchResult = service.search(keyword);

        searchResult.forEach(result -> System.out.println(result));

    }

}