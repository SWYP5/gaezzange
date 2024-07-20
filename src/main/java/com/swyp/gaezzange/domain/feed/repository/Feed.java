package com.swyp.gaezzange.domain.feed.repository;

import com.swyp.gaezzange.domain.category.FeedCategory;
import com.swyp.gaezzange.domain.tendency.Tendency;
import com.swyp.gaezzange.util.jpa.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
        name = "feeds"
)
public class Feed extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedId;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tendency tendency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeedCategory category;

    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean deleted;

    public boolean validateUserId(long userId) {
        return userId == this.userId;
    }

    public void updateFeed(Tendency tendency, FeedCategory category, String content) {
        this.tendency = tendency;
        this.category = category;
        this.content = content;
    }

    public void deleteFeed() {
        this.deleted = true;
    }
}
