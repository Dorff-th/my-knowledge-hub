package dev.mkhub.knowledge.search.service;

import dev.mkhub.knowledge.search.dto.SearchResultDTO;
import dev.mkhub.knowledge.search.mapper.SearchMapper;
import dev.mkhub.knowledge.search.util.SearchPostMerger;
import dev.mkhub.knowledge.search.util.SummaryProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchMapper searchMapper;

    public List<SearchResultDTO> search(String keyword) {
        List<SearchResultDTO> rawList = searchMapper.searchPostsByKeyword(keyword);

        List<SearchResultDTO> withSummary = rawList.stream()
                .map(dto -> SummaryProcessor.applySummaryAndMatch(dto, keyword))
                .collect(Collectors.toList());

        return SearchPostMerger.deduplicateByPostId(withSummary);

    }

}
