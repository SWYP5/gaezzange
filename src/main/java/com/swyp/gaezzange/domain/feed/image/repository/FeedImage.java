package com.swyp.gaezzange.domain.feed.image.repository;

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
        name = "feed_images"
)
public class FeedImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedImageId;

    @Column(nullable = false)
    private Long feedId;

    @Column(nullable = false)
    private String feedImagePath;

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean deleted;
}
