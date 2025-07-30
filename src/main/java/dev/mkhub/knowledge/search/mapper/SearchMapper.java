package dev.mkhub.knowledge.search.mapper;

import dev.mkhub.knowledge.search.dto.SearchResultDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SearchMapper {
    List<SearchResultDTO>  searchPostsByKeyword(@Param("keyword") String keyword);
}
