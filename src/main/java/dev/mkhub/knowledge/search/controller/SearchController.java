package dev.mkhub.knowledge.search.controller;

import dev.mkhub.knowledge.common.dto.PageRequestDTO;
import dev.mkhub.knowledge.common.dto.PageResponseDTO;
import dev.mkhub.knowledge.search.dto.SearchResultDTO;
import dev.mkhub.knowledge.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("")
    public String search(@RequestParam(name = "keyword") String keyword,
                         PageRequestDTO pageRequestDTO,
                         Model model) {

        PageResponseDTO<SearchResultDTO> result = searchService.searchWithPaging(keyword, pageRequestDTO);

        model.addAttribute("result", result);
        model.addAttribute("currentPage", pageRequestDTO.getPage());

        return "search/search";
    }
}
