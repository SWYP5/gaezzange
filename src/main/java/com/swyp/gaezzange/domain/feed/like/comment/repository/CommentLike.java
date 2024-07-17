package com.swyp.gaezzange.domain.feed.like.comment.repository;

import com.swyp.gaezzange.util.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "comment_likes"
)
public class CommentLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentLikeId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long commentId;

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean deleted;

    public void toggleLike() {
        this.deleted = !this.deleted;
    }
}
