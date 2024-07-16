package com.swyp.gaezzange.domain.feed.repository;

import com.swyp.gaezzange.domain.category.Category;
import com.swyp.gaezzange.domain.tendency.Tendency;
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
    private Category category;

    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean deleted;
}
