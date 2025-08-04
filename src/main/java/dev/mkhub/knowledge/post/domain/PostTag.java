package dev.mkhub.knowledge.post.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post_tag")
@Getter
@Setter
@IdClass(PostTag.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTag {

    @Id
    @Column(nullable = false, unique = true)
    private Long postId;

    @Id
    @Column(nullable = false, unique = true)
    private Long tagId;

    @ManyToOne
    @JoinColumn(name = "postId", insertable = false, updatable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "tagId", insertable = false, updatable = false)
    private Tag tag;
}
