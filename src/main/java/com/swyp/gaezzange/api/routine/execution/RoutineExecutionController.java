package com.swyp.gaezzange.api.routine.execution;

import com.swyp.gaezzange.authentication.UserContextProvider;
import com.swyp.gaezzange.domain.routine.RoutineApplication;
import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "RoutineExecutionController", description = "Routine Execution API")
@RestController
@RequestMapping("/v1/routine/{routineId}/execution")
@RequiredArgsConstructor
public class RoutineExecutionController {

  private final UserContextProvider userContextProvider;
  private final RoutineApplication routineApplication;

  @PostMapping("")
  public ApiResponse doRoutine(@PathVariable Long routineId, @RequestParam LocalDate targetDate) {
    User user = userContextProvider.getUser();
    routineApplication.addRoutineExecutionIfNotExist(user, routineId, targetDate);
    return ApiResponse.success(null);
  }

  @DeleteMapping("")
  public ApiResponse undoRoutine(@PathVariable Long routineId, @RequestParam LocalDate targetDate) {
    User user = userContextProvider.getUser();
    routineApplication.deleteRoutineExecutionIfExist(user, routineId, targetDate);
    return ApiResponse.success(null);
  }
}
