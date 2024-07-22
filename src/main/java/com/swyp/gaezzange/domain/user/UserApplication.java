package com.swyp.gaezzange.domain.user;

import static com.swyp.gaezzange.util.DateUtil.getStartOfLastWeek;

import com.swyp.gaezzange.api.user.dto.UserInfoForm;
import com.swyp.gaezzange.api.user.dto.UserProfileForm;
import com.swyp.gaezzange.domain.routine.RoutineApplication;
import com.swyp.gaezzange.domain.routine.execution.dto.RoutineExecutionCount;
import com.swyp.gaezzange.domain.tendency.Tendency;
import com.swyp.gaezzange.domain.user.auth.repository.UserAuth;
import com.swyp.gaezzange.domain.user.auth.service.UserAuthService;
import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.domain.user.service.UserService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserApplication {

  private final UserService userService;
  private final UserAuthService userAuthService;
  private final RoutineApplication routineApplication;

  @Transactional
  public User onboard(UserAuth userAuth, UserInfoForm form) {
    User user = userService.saveUser(User.from((form)));
    userAuthService.updateUser(userAuth, user);
    return user;
  }

  @Transactional
  public void updateProfile(User user, UserProfileForm form) {
    userService.saveUser(user.updateProfile((form)));
  }

  @Async
  @Transactional
  public void syncUserTendency(Long userId) {
    User user = userService.getById(userId).get();
    LocalDate lastMonday = getStartOfLastWeek();
    LocalDate lasSunday = getStartOfLastWeek().plus(7, ChronoUnit.DAYS);
    Map<LocalDate, RoutineExecutionCount> countMap = routineApplication.countRoutineExecution(user,
        lastMonday, lasSunday);

    RoutineExecutionCount result = countMap.values().stream()
        .reduce(new RoutineExecutionCount(), (a, b) -> {
          a.setBaezzangeCount(a.getBaezzangeCount() + b.getBaezzangeCount());
          a.setGaemiCount(a.getGaemiCount() + b.getGaemiCount());
          return a;
        }
    );

    if (result.getGaemiCount() == result.getBaezzangeCount()) {
      return;
    }

    if(result.getBaezzangeCount() >= result.getGaemiCount()) {
      user.setTendency(Tendency.BAEZZANGE);
    } else {
      user.setTendency(Tendency.GAEMI);
    }

    userService.saveUser(user);
  }
}
