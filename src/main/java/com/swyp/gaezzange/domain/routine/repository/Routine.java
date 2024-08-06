package com.swyp.gaezzange.domain.routine.repository;

import com.swyp.gaezzange.api.routine.dto.RoutineForm;
import com.swyp.gaezzange.domain.category.RoutineCategory;
import com.swyp.gaezzange.domain.tendency.Tendency;
import com.swyp.gaezzange.util.jpa.BaseTimeEntity;
import com.swyp.gaezzange.util.jpa.DaysOfWeekConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(RoutineListener.class)
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

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RoutineCategory category;

  @Column(nullable = false)
  private String name;

  @Column
  private String description;

  @Column
  private Long emoji;

  @Column(nullable = false)
  private LocalDate startedDate;

  @Column(columnDefinition = "DATE DEFAULT '9999-12-31'")
  @Setter
  private LocalDate endedDate;

  @Column
  private LocalTime executionTime;

  @Convert(converter = DaysOfWeekConverter.class)
  @Column(columnDefinition = "SET('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY')")
  private Set<DayOfWeek> daysOfWeek;

  @Setter
  @Column(nullable = false)
  private boolean deleted;

  public void update(RoutineForm form) {
    tendency = form.getTendency();
    category = form.getCategory();
    name = form.getName();
    emoji = form.getEmoji();
    description = form.getDescription();
    startedDate = form.getStartedDate();
    endedDate = form.getEndedDate() == null ? LocalDate.MAX : form.getEndedDate();
    executionTime = form.getExecutionTime();
    daysOfWeek = form.getDaysOfWeek();
  }


}

