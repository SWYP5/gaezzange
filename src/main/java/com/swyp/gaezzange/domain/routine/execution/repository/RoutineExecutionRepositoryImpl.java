package com.swyp.gaezzange.domain.routine.execution.repository;

import static com.swyp.gaezzange.domain.routine.repository.QRoutine.routine;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swyp.gaezzange.domain.routine.repository.QRoutine;
import com.swyp.gaezzange.domain.routine.repository.Routine;
import com.swyp.gaezzange.domain.tendency.Tendency;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;

@RequiredArgsConstructor
public class RoutineExecutionRepositoryImpl implements RoutineExecutionRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  private final static QRoutine qRoutine = routine;
  private final static QRoutineExecution qRoutineExecution = QRoutineExecution.routineExecution;

  @Override
  public Map<Routine, List<RoutineExecution>> findExecutionsByRoutine(
      Long userId, LocalDate from, LocalDate to
  ) {

    List<Tuple> results = getRoutineExecutionsBetween(userId, from, to);

    return results.stream()
        .collect(Collectors.groupingBy(
            tuple -> tuple.get(qRoutine),
            Collectors.mapping(
                tuple -> tuple.get(qRoutineExecution),
                Collectors.toList()
            )
        ));
  }

  @Override
  public Map<LocalDate, Triple<Long, Tendency, Long>> countExecutionsByDate(
      Long userId, LocalDate from, LocalDate to
  ) {

    List<Tuple> results = getRoutineExecutionsBetween(userId, from, to);

    return results.stream()
        .collect(Collectors.toMap(
            tuple -> tuple.get(qRoutineExecution.executedDate),
            tuple -> Triple.of(
                tuple.get(qRoutine.routineId),
                tuple.get(qRoutine.tendency),
                tuple.get(qRoutineExecution.count())
            )
        ));
  }

  private List<Tuple> getRoutineExecutionsBetween(Long userId, LocalDate from, LocalDate to) {
    return jpaQueryFactory.select(qRoutine, qRoutineExecution)
        .from(qRoutine)
        .leftJoin(qRoutineExecution).on(
            qRoutine.routineId.eq(qRoutineExecution.routineId)
                .and(qRoutineExecution.executedDate.between(from, to))) // both are inclusive
        .where(
            qRoutine.deleted.isFalse().and(qRoutine.userId.eq(userId))
                .and(qRoutine.startedDate.loe(from).and(qRoutine.endedDate.goe(to)))
        ).fetch();
  }
}