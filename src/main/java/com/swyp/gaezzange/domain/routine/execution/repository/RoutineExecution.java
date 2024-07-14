package com.swyp.gaezzange.domain.routine.execution.repository;

import com.swyp.gaezzange.util.jpa.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDate;
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
    name = "routine_executions",
    indexes = {
        @Index(name = "routine_executions_idx_01", columnList = "createdAt"),
        @Index(name = "routine_executions_idx_02", columnList = "updatedAt"),
        @Index(name = "routine_executions_idx_03", columnList = "routineId")
    }
)
public class RoutineExecution extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long routineExecutionId;

  @Column(nullable = false)
  private Long routineId;

  @Column(nullable = false)
  private LocalDate executedDate;
}
