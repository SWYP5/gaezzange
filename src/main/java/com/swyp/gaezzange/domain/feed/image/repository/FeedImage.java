package com.swyp.gaezzange.domain.feed.image.repository;

import static com.swyp.gaezzange.contants.SystemConstants.S3Constants.S3_URL;

import com.swyp.gaezzange.util.jpa.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Setter
    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean deleted;

    public void updateFeedImagePath(String feedImagePath) {
        this.feedImagePath = feedImagePath;
    }


    public String getFeedImagePathWithS3Url() {
        return feedImagePath == null ? null : S3_URL + feedImagePath;
    }
}
