package com.swyp.gaezzange.domain.routine.service;

import static com.swyp.gaezzange.contants.ExceptionConstants.RoutineExceptionConstants.CODE_ROUTINE_NOT_FOUND;
import static com.swyp.gaezzange.contants.ExceptionConstants.RoutineExceptionConstants.MESSAGE_ROUTINE_NOT_FOUND;

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

  public List<Routine> listRoutinesOnTargetDate(Long userId, LocalDate startDate, LocalDate endDate) {
    return repository.findAllByUserIdAndStartedDateLessThanEqualAndEndedDateGreaterThanEqualAndDeletedIsFalse(
        userId, endDate, startDate
    );
  }

  public Routine findRoutineById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new BizException(CODE_ROUTINE_NOT_FOUND, MESSAGE_ROUTINE_NOT_FOUND));
  }
}
