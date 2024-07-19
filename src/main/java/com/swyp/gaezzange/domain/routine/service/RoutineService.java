package com.swyp.gaezzange.domain.routine.service;

import com.swyp.gaezzange.domain.routine.repository.Routine;
import com.swyp.gaezzange.domain.routine.repository.RoutineRepository;
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
}
