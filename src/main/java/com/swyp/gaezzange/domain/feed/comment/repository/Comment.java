package com.swyp.gaezzange.domain.feed.comment.repository;

import com.swyp.gaezzange.util.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "commnets"
)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long feedId;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    @Column(nullable = true)
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<Comment> childComments;

    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean deleted;
}