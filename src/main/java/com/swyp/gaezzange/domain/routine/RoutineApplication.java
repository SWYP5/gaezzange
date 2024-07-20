package com.swyp.gaezzange.domain.routine;

import com.swyp.gaezzange.api.routine.dto.RoutineDto;
import com.swyp.gaezzange.api.routine.dto.RoutineForm;
import com.swyp.gaezzange.domain.routine.execution.repository.RoutineExecution;
import com.swyp.gaezzange.domain.routine.execution.service.RoutineExecutionService;
import com.swyp.gaezzange.domain.routine.repository.Routine;
import com.swyp.gaezzange.domain.routine.service.RoutineService;
import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.exception.customException.BizException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineApplication {

  private final RoutineService routineService;
  private final RoutineExecutionService routineExecutionService;

  @Transactional
  public void addRoutine(User user, RoutineForm form) {
    routineService.createRoutine(
        Routine.builder()
            .userId(user.getUserId())
            .category(form.getCategory())
            .tendency(form.getTendency())
            .startedDate(form.getStartedDate())
            .endedDate(form.getEndedDate())
            .daysOfWeek(form.getDaysOfWeek())
            .executionTime(form.getExecutionTime())
            .build()
    );
  }

  @Transactional
  public void updateRoutine(User user, Long routineId, RoutineForm form) {
    Routine routine = routineService.findRoutineById(routineId);
    validateOwnedRoutine(user, routine);

    routine.update(form);
  }

  @Transactional
  public void deleteRoutine(User user, Long routineId) {
    Routine routine = routineService.findRoutineById(routineId);
    validateOwnedRoutine(user, routine);

    routine.setDeleted(true);
  }

  @Transactional
  public void addRoutineExecutionIfNotExist(User user, Long routineId, LocalDate targetDate) {
    Routine routine = routineService.findRoutineById(routineId);
    validateOwnedRoutine(user, routine);

    Optional<RoutineExecution> routineExecution = routineExecutionService.getRoutine(routineId,
        targetDate);

    if (!routineExecution.isPresent()) {
      RoutineExecution newRoutineExecution = RoutineExecution.builder()
          .routineId(routineId)
          .executedDate(targetDate)
          .build();

      routineExecutionService.save(newRoutineExecution);
    }
  }

  @Transactional
  public void deleteRoutineExecutionIfExist(User user, Long routineId, LocalDate targetDate) {
    Routine routine = routineService.findRoutineById(routineId);
    validateOwnedRoutine(user, routine);

    Optional<RoutineExecution> routineExecution = routineExecutionService.getRoutine(routineId,
        targetDate);

    if (routineExecution.isPresent()) {
      routineExecutionService.delete(routineExecution.get());
    }
  }

  public RoutineDto getRoutine(Long routineId) {
    Routine routine = routineService.findRoutineById(routineId);
    return RoutineDto.from(routine);
  }

  public List<RoutineDto> listRoutines(User user, LocalDate startDate, LocalDate endDate) {
    List<Routine> routines = routineService.listRoutinesOnTargetDate(user.getUserId(), startDate,
        endDate);
    return routines.stream()
        .map(r -> RoutineDto.from(r))
        .toList();
  }

  private void validateOwnedRoutine(User user, Routine routine) {
    if (!user.getUserId().equals(routine.getUserId())) {
      throw new BizException("NOT_OWNED_ROUTINE", "본인의 루틴이 아닙니다.");
    }
  }
}
