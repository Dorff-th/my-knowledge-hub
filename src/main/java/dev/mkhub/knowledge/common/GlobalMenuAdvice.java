package dev.mkhub.knowledge.common;

import dev.mkhub.knowledge.menu.dto.MenuDTO;
import dev.mkhub.knowledge.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;

/**
 * 전역 메뉴 주입
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalMenuAdvice {

    private final MenuService menuService;

    @ModelAttribute("menus")
    public List<MenuDTO> addMenus(Authentication auth) {
        if (auth == null) {
            return Collections.emptyList(); // 비로그인 사용자
        }
        String role = auth.getAuthorities().iterator().next().getAuthority();
        return menuService.getMenuTreeByRole(role);  // 캐시된 메뉴 반환
    }
}
