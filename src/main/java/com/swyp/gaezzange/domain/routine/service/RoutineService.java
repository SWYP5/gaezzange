package com.swyp.gaezzange.domain.routine.service;

import com.swyp.gaezzange.domain.routine.repository.Routine;
import com.swyp.gaezzange.domain.routine.repository.RoutineRepository;
import com.swyp.gaezzange.exception.customException.BizException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineService {
  private final RoutineRepository repository;

  @Transactional
  public Routine createRoutine(Routine routine) {
    return repository.save(routine);
  }

  public List<Routine> listRoutinesOnTargetDate(Long userId, LocalDate targetDate) {
    return repository.findAllByUserIdAndTargetDateBetweenStartedDateAndEndedDate(userId, targetDate);
  }

  public Routine findRoutineById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new BizException("NOT_FOUND_ROUTINE", "루틴을 찾을 수 없습니다."));
    );
  }

}
