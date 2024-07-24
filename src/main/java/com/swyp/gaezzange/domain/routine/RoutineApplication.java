package com.swyp.gaezzange.domain.routine;

import static com.swyp.gaezzange.contants.ExceptionConstants.RoutineExceptionConstants.CODE_NOT_OWNED_ROUTINE;
import static com.swyp.gaezzange.contants.ExceptionConstants.RoutineExceptionConstants.MESSAGE_NOT_OWNED_ROUTINE;

import com.swyp.gaezzange.api.routine.dto.RoutineDto;
import com.swyp.gaezzange.api.routine.dto.RoutineForm;
import com.swyp.gaezzange.api.routine.execution.dto.RoutineExecutionResultDto;
import com.swyp.gaezzange.domain.routine.execution.dto.RoutineExecutionCount;
import com.swyp.gaezzange.domain.routine.execution.repository.RoutineExecution;
import com.swyp.gaezzange.domain.routine.execution.service.RoutineExecutionService;
import com.swyp.gaezzange.domain.routine.repository.Routine;
import com.swyp.gaezzange.domain.routine.service.RoutineService;
import com.swyp.gaezzange.domain.tendency.Tendency;
import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.exception.customException.BizException;
import com.swyp.gaezzange.util.DateUtil;
import com.swyp.gaezzange.util.SetUtil;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoutineApplication {

  private final RoutineService routineService;
  private final RoutineExecutionService routineExecutionService;

  @Transactional
  public void addRoutine(User user, RoutineForm form) {
    routineService.createRoutine(
        Routine.builder()
            .userId(user.getUserId())
            .category(form.getCategory())
            .name(form.getName())
            .emoji(form.getEmoji())
            .description(form.getDescription())
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
    if (form.getEndedDate() != null && form.getStartedDate() != null) {
      List<RoutineExecution> executions = routineExecutionService.listRoutineExecution(routineId)
          .stream().filter(
              it -> it.getExecutedDate().isBefore(form.getStartedDate()) || it.getExecutedDate()
                  .isAfter(form.getEndedDate())).toList();
      routineExecutionService.deleteRoutineExecutions(executions);
    }
  }

  @Transactional
  public void deleteRoutine(User user, Long routineId) {
    Routine routine = routineService.findRoutineById(routineId);
    List<RoutineExecution> executions = routineExecutionService.listRoutineExecution(routineId);
    validateOwnedRoutine(user, routine);

    routineExecutionService.deleteRoutineExecutions(executions);
    routine.setDeleted(true);
  }

  @Transactional
  public void addRoutineExecutionIfNotExist(User user, Long routineId, LocalDate targetDate) {
    Routine routine = routineService.findRoutineById(routineId);
    validateOwnedRoutine(user, routine);
    if (targetDate.isAfter(routine.getEndedDate()) || targetDate.isBefore(
        routine.getStartedDate())) {
      throw new BizException("INVALID_TARGET_DATE", "루틴 설정 기간에 포함되지 않는 날짜입니다.");
    }

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
    if (startDate.isAfter(endDate)) {
      throw new BizException("INVALID_PARAMETERS", "startDate가 endDate 이후 입니다.");
    }
    List<Routine> routines = routineService.listRoutinesOnTargetDate(user.getUserId(), startDate,
        endDate);
    Set<DayOfWeek> dayOfWeekSet = DateUtil.getDaysOfWeekBetween(startDate, endDate);

    return routines.stream()
        .filter(it -> SetUtil.hasIntersection(it.getDaysOfWeek(), dayOfWeekSet))
        .map(r -> RoutineDto.from(r))
        .toList();
  }

  public List<RoutineExecutionResultDto> listRoutineExecutions(User user, LocalDate from,
      LocalDate to) {
    Map<Routine, List<RoutineExecution>> executionsByRoutine =
        routineExecutionService.getRoutineExecutionsByRoutine(user.getUserId(), from, to);

    log.info("executionsByRoutine {}", executionsByRoutine);

    return executionsByRoutine.entrySet().stream()
        .map(entry ->
            new RoutineExecutionResultDto(
                RoutineDto.from(entry.getKey()),
                extractExecutionDates(entry.getValue())
            )
        ).toList();
  }

  public Map<LocalDate, RoutineExecutionCount> countRoutineExecution(
      User user, LocalDate from, LocalDate to
  ) {
    Map<Routine, List<RoutineExecution>> executionsByRoutine =
        routineExecutionService.getRoutineExecutionsByRoutine(user.getUserId(), from, to);

    Map<LocalDate, RoutineExecutionCount> countMap = new HashMap<>();
    executionsByRoutine.entrySet().forEach(entry -> {
      Tendency tendency = entry.getKey().getTendency();
      List<LocalDate> executionDates = extractExecutionDates(entry.getValue());
      for (LocalDate ed : executionDates) {
        countMap.computeIfAbsent(ed, k -> new RoutineExecutionCount())
            .plusByTendency(tendency);
      }
    });

    return countMap;
  }

  private void validateOwnedRoutine(User user, Routine routine) {
    if (!user.getUserId().equals(routine.getUserId())) {
      throw new BizException(CODE_NOT_OWNED_ROUTINE, MESSAGE_NOT_OWNED_ROUTINE);
    }
  }

  private List<LocalDate> extractExecutionDates(List<RoutineExecution> executions) {
    return executions.stream().map(RoutineExecution::getExecutedDate).toList();
  }
}
