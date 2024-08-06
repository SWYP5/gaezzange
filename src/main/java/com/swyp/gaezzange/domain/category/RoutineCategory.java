package com.swyp.gaezzange.domain.category;

import com.swyp.gaezzange.domain.tendency.Tendency;
import lombok.Getter;

public enum RoutineCategory {
  WORK_AND_STUDY(Tendency.GAEMI),
  HEALTH(Tendency.GAEMI),
  SELF_DEVELOPMENT(Tendency.GAEMI),
  NETWORKING(Tendency.GAEMI),
  CERTIFICATION(Tendency.GAEMI),
  GAEMI_ETC(Tendency.GAEMI),

  REST(Tendency.BAEZZANGE),
  HOBBY(Tendency.BAEZZANGE),
  GAME(Tendency.BAEZZANGE),
  FOOD(Tendency.BAEZZANGE),
  MUSIC_AND_SHOW(Tendency.BAEZZANGE),
  MEETING(Tendency.BAEZZANGE),
  BAEZZANGE_ETC(Tendency.BAEZZANGE);

  @Getter
  private final Tendency tendency;

  RoutineCategory(Tendency tendency) {
    this.tendency = tendency;
  }
}
