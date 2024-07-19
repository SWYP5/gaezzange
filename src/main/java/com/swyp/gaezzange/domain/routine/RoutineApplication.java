package com.swyp.gaezzange.domain.routine;

import com.swyp.gaezzange.api.routine.dto.RoutineDto;
import com.swyp.gaezzange.api.routine.dto.RoutineForm;
import com.swyp.gaezzange.domain.routine.repository.Routine;
import com.swyp.gaezzange.domain.routine.service.RoutineService;
import com.swyp.gaezzange.domain.user.repository.User;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineApplication {

  private final RoutineService routineService;

  @Transactional
  public void addRoutine(User user, RoutineForm form) {
    routineService.createRoutine(
        Routine.builder()
            .userId(user.getUserId())
            .tendency(form.getTendency())
            .startedDate(form.getStartedDate())
            .endedDate(form.getEndedDate())
            .daysOfWeek(form.getDaysOfWeek())
            .executionTime(form.getExecutionTime())
            .build()
    );
  }

  public RoutineDto getRoutine(Long routineId) {
    Routine routine = routineService.findRoutineById(routineId);
    return RoutineDto.from(routine);
  }

  public List<RoutineDto> listRoutinesOnTargetDate(User user, LocalDate targetDate) {
    List<Routine> routines = routineService.listRoutinesOnTargetDate(user.getUserId(), targetDate);
    return routines.stream()
        .map(r -> RoutineDto.from(r))
        .toList();
  }
}
