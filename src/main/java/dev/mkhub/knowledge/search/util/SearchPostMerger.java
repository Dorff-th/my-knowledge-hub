package dev.mkhub.knowledge.search.util;

import dev.mkhub.knowledge.search.dto.SearchResultDTO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchPostMerger {

    public static List<SearchResultDTO> deduplicateByPostId(List<SearchResultDTO> rawList) {
        Map<Long, SearchResultDTO> merged = new LinkedHashMap<>();
        for (SearchResultDTO dto : rawList) {
            merged.putIfAbsent(dto.getPostId(), dto);
        }
        return new ArrayList<>(merged.values());
    }
}
