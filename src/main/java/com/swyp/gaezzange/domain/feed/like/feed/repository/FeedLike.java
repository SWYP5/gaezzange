package com.swyp.gaezzange.domain.feed.like.feed.repository;

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
        name = "feed_likes"
)
public class FeedLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedLikeId;

    @Column(nullable = false)
    private Long feedId;

    @Column(nullable = false)
    private Long userId;

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean deleted;

    public void toggleLike() {
        this.deleted = !this.deleted;
    }

}
