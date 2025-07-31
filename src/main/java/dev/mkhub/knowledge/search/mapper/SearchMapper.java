package dev.mkhub.knowledge.search.mapper;

import dev.mkhub.knowledge.common.dto.PageRequestDTO;
import dev.mkhub.knowledge.search.dto.SearchFilterDTO;
import dev.mkhub.knowledge.search.dto.SearchResultDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchMapper {
    List<SearchResultDTO>  searchPostsByKeyword(@Param("keyword") String keyword);

    List<SearchResultDTO>  searchPostsByKeywordWithPaging(@Param("keyword") String keyword,
                                                          @Param("pageRequest") PageRequestDTO pageRequestDTO);
    Integer searchPostsByKeywordCount(@Param("keyword") String keyword);

    //상세검색
    List<SearchResultDTO> searchFilteredPostsWithPaging(@Param("searchFilterDTO")SearchFilterDTO filterDTO,
                                                        @Param("pageRequest") PageRequestDTO pageRequestDTO);

    Integer searchFilteredPostsCount(@Param("searchFilterDTO")SearchFilterDTO filterDTO);

    // 위에 있는 것들은 미사용 처리 예정
    List<SearchResultDTO> searchPostsUnified(@Param("searchFilterDTO")SearchFilterDTO filterDTO,
                                                        @Param("pageRequest") PageRequestDTO pageRequestDTO);

    Integer searchPostsUnifiedCount(@Param("searchFilterDTO")SearchFilterDTO filterDTO);

}
