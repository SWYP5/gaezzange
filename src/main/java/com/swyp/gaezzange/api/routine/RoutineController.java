package com.swyp.gaezzange.api.routine;

import com.swyp.gaezzange.api.routine.dto.RoutineDto;
import com.swyp.gaezzange.api.routine.dto.RoutineForm;
import com.swyp.gaezzange.authentication.UserContextProvider;
import com.swyp.gaezzange.domain.routine.RoutineApplication;
import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "RoutineController", description = "Routine API")
@RestController
@RequestMapping("/v1/routine")
@RequiredArgsConstructor
public class RoutineController {

  private final UserContextProvider contextProvider;
  private final RoutineApplication routineApplication;

  @Operation(description = "Both startDate and endDate are inclusive.")
  @GetMapping("")
  public ApiResponse<List<RoutineDto>> getDailyRoutines(
      @RequestParam LocalDate startDate,
      @RequestParam LocalDate endDate
  ) {
    User user = contextProvider.getUser();
    //TODO cursor 또는 paging 처리 필요?
    List<RoutineDto> routines = routineApplication.listRoutines(user, startDate, endDate);
    return ApiResponse.success(routines);
  }

  @GetMapping("/{routineId}")
  public ApiResponse<RoutineDto> getRoutine(@PathVariable Long routineId) {
    RoutineDto routine = routineApplication.getRoutine(routineId);
    return ApiResponse.success(routine);
  }

  @PostMapping("")
  public ApiResponse addRoutine(@Valid @RequestBody RoutineForm form) {
    User user = contextProvider.getUser();
    routineApplication.addRoutine(user, form);
    return ApiResponse.success(null);
  }

  @PutMapping("/{routineId}")
  public ApiResponse updateRoutine(@Valid @RequestBody RoutineForm form,
      @PathVariable Long routineId) {
    User user = contextProvider.getUser();
    routineApplication.updateRoutine(user, routineId, form);
    return ApiResponse.success(null);
  }

  @DeleteMapping("/{routineId}")
  public ApiResponse removeRoutine(@PathVariable Long routineId) {
    User user = contextProvider.getUser();
    routineApplication.deleteRoutine(user, routineId);
    return ApiResponse.success(null);
  }
}
