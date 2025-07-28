package dev.mkhub.knowledge.domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role;

    private LocalDateTime createdAt;

    // 사용자(member) 1 : N 게시물(post)
    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments;

    public Member(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
