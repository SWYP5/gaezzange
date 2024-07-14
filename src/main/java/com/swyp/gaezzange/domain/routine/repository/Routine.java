package com.swyp.gaezzange.domain.routine.repository;

import com.swyp.gaezzange.domain.tendency.Tendency;
import com.swyp.gaezzange.util.jpa.BaseTimeEntity;
import com.swyp.gaezzange.util.jpa.DaysOfWeekConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
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
    name = "routines",
    indexes = {
        @Index(name = "routines_idx_01", columnList = "createdAt"),
        @Index(name = "routines_idx_02", columnList = "updatedAt"),
        @Index(name = "routines_idx_03", columnList = "userId, startedDate, endedDate")
    }
)
public class Routine extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long routineId;

  @Column(nullable = false)
  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Tendency tendency;

  @Column(nullable = false)
  private String name;

  @Column
  private String emoji;

  @Column(nullable = false)
  private LocalDate startedDate;

  @Column(nullable = false, columnDefinition = "DATE DEFAULT '9999-12-31'")
  private LocalDate endedDate;

  @Column
  private LocalTime executionTime;

  @Convert(converter = DaysOfWeekConverter.class)
  @Column(columnDefinition = "SET('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY')")
  private Set<DayOfWeek> daysOfWeek;
}
