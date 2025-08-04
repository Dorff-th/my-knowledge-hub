package dev.mkhub.knowledge.search.controller;

import dev.mkhub.knowledge.common.dto.PageRequestDTO;
import dev.mkhub.knowledge.common.dto.PageResponseDTO;
import dev.mkhub.knowledge.post.domain.Category;
import dev.mkhub.knowledge.post.service.CategoryService;
import dev.mkhub.knowledge.search.dto.SearchFilterDTO;
import dev.mkhub.knowledge.search.dto.SearchResultDTO;
import dev.mkhub.knowledge.search.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    private final CategoryService categoryService;

    @GetMapping("")
    public String search(@ModelAttribute  SearchFilterDTO searchFilterDTO,
                         @ModelAttribute PageRequestDTO pageRequestDTO,
                         HttpServletRequest request,    // pagination 템플릿 사용을 위해 필요
                         Model model) {

        //기본 검색결과
        PageResponseDTO<SearchResultDTO> result = searchService.searchPostsUnified(searchFilterDTO, pageRequestDTO);
        model.addAttribute("result", result);
        model.addAttribute("currentPage", pageRequestDTO.getPage());

        //Category 목록 조회
        List<Category> categories = categoryService.findAllCategory();
        model.addAttribute("categories", categories);
        model.addAttribute("searchFilterDTO", searchFilterDTO);

        String queryString = searchService.buildQueryString(searchFilterDTO);

        model.addAttribute("queryString", queryString); // 페이지 이동시 검색 조건을 유지하기 위해 필요

        //페이징 공통 Thymeleaf fragment 를 쓰기 위해 현재 URI 를 view에 넘김
        String requestURI = request.getRequestURI();  // 예: "/search"
        model.addAttribute("requestURI", requestURI);


        return "search/search";
    }
}
