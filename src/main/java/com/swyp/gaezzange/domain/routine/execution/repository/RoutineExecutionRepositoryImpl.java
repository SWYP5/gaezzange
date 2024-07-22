package com.swyp.gaezzange.domain.routine.execution.repository;

import static com.swyp.gaezzange.domain.routine.repository.QRoutine.routine;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swyp.gaezzange.domain.routine.repository.QRoutine;
import com.swyp.gaezzange.domain.routine.repository.Routine;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

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
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    list -> list.stream().filter(e -> e != null).toList()
                )
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
                .and(qRoutine.startedDate.loe(to).and(qRoutine.endedDate.goe(from)))
        ).fetch();
  }
}