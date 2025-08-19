package dev.mkhub.knowledge.menu.repository;

import dev.mkhub.knowledge.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    // 특정 권한(role)에 해당하는 모든 메뉴 조회
    List<Menu> findByRoleOrderBySortOrderAsc(String role);
}

