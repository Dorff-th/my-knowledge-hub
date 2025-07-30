package dev.mkhub.knowledge.search.mapper;

import dev.mkhub.knowledge.search.dto.SearchResultDTO;
import dev.mkhub.knowledge.search.util.SummaryProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SearchMapperTest {

    @Autowired
    private SearchMapper searchMapper;

    //@Test
    @DisplayName("검색 쿼리 동작 여부 확인")
    void findPostByKeyword() {
        String keyword = "환경에서";

        List<SearchResultDTO> rawList = searchMapper.searchPostsByKeyword(keyword);
        List<SearchResultDTO> result = rawList.stream()
                .map(dto-> SummaryProcessor.applySummaryAndMatch(dto, keyword))
                .collect(Collectors.toList());

        Map<Long, SearchResultDTO> merged = new LinkedHashMap<>();

        for (SearchResultDTO dto : result) {
            if (!merged.containsKey(dto.getPostId())) {
                merged.put(dto.getPostId(), dto); // 첫 결과만 유지
            }
        }





    }
}