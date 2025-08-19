package dev.mkhub.knowledge.menu.service;

import dev.mkhub.knowledge.menu.domain.Menu;
import dev.mkhub.knowledge.menu.dto.MenuDTO;
import dev.mkhub.knowledge.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    @Cacheable(value = "menus", key = "#p0")   // role 단위로 캐싱됨
    public List<MenuDTO> getMenuTreeByRole(String role) {
        List<Menu> menus = menuRepository.findByRoleOrderBySortOrderAsc(role);

        // 1) 엔티티 → DTO 변환
        Map<Long, MenuDTO> dtoMap = menus.stream()
                .map(this::toDTO)
                .collect(Collectors.toMap(MenuDTO::getId, dto -> dto));

        // 2) 트리 구조 구성
        List<MenuDTO> rootMenus = new ArrayList<>();

        for (Menu menu : menus) {
            MenuDTO dto = dtoMap.get(menu.getId());
            if (menu.getParent() != null) {
                // 부모 메뉴가 있으면 부모의 children에 추가
                dtoMap.get(menu.getParent().getId()).getChildren().add(dto);
            } else {
                // 부모가 없으면 루트 메뉴
                rootMenus.add(dto);
            }
        }

        return rootMenus;
    }

    private MenuDTO toDTO(Menu menu) {
        return MenuDTO.builder()
                .id(menu.getId())
                .name(menu.getName())
                .url(menu.getUrl())
                .children(new ArrayList<>()) // 빈 리스트 초기화
                .build();
    }
}
