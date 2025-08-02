package dev.mkhub.knowledge.domain;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

    private String nickname;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 사용자(member) 1 : N 게시물(post)
    @OneToMany(fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments;

    public Member(String email, String password, String role, String nickname) {
        this.username = email;  // username에도 email 들어가게(email을 아이디 처럼 사용하고 싶어서)
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
    }

}
