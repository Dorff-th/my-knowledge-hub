package dev.mkhub.knowledge.menu.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class MenuDTO {
    private Long id;
    private String name;
    private String url;
    private List<MenuDTO> children = new ArrayList<>();
}
