package com.swyp.gaezzange.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class DateUtil {

  public static Set<DayOfWeek> getDaysOfWeekBetween(LocalDate startDate, LocalDate endDate) {
    Set<DayOfWeek> daysOfWeek = new HashSet<>();

    // 시작일이 종료일보다 이후일 경우 빈 리스트 반환
    if (startDate.isAfter(endDate)) {
      return daysOfWeek;
    }

    LocalDate currentDate = startDate;
    LocalDate fullDate = startDate.plusDays(6);
    LocalDate lastDate = fullDate.isAfter(endDate) ? endDate : fullDate;

    // 시작일부터 종료일까지의 요일을 리스트에 추가 (7일 차이 이상은 확인할 필요없음)
    while (!currentDate.isAfter(lastDate)) {
      daysOfWeek.add(currentDate.getDayOfWeek());
      currentDate = currentDate.plusDays(1);
    }

    return daysOfWeek;
  }
}