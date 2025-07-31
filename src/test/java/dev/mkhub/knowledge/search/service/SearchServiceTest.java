package dev.mkhub.knowledge.search.service;

import dev.mkhub.knowledge.common.dto.PageRequestDTO;
import dev.mkhub.knowledge.common.dto.PageResponseDTO;
import dev.mkhub.knowledge.search.dto.SearchFilterDTO;
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

    //@Test
    @DisplayName("검색 쿼리 동작 여부 확인 - 동일 id값을 가진 post 제목 중복 제거 테스트 with paging")
    void findPostByKeywordMerge() {

        String keyword = "API";
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<SearchResultDTO> searchResult = service.searchWithPaging(keyword, pageRequestDTO);
        System.out.println("검색된 row 개수 " + searchResult.getTotalElements());

        searchResult.getDtoList().forEach(result -> System.out.println(result));

    }

    @Test
    @DisplayName("상세검색(검색필터) 결과 테스트 with paging") //searchFilteredPostsWithPaging
    void findBySearchFilteredWithPaging() {

        SearchFilterDTO searchFilterDTO = SearchFilterDTO.builder()
                .keyword("기술")
                .categoryId(3L)
                .titleChecked(true)
                .contentChecked(true)
                .build();

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<SearchResultDTO> searchResult = service.searchPostsUnified(searchFilterDTO, pageRequestDTO);

        searchResult.getDtoList().forEach(result -> System.out.println(result));

        System.out.println("검색된 row 개수 " + searchResult.getTotalElements());
    }


}