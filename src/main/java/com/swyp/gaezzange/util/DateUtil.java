package com.swyp.gaezzange.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
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

  public static LocalDate getStartOfWeek() {
    // 현재 날짜와 시간을 가져옵니다.
    LocalDateTime now = LocalDateTime.now();

    // 현재 날짜를 기준으로 해당 주의 월요일을 찾습니다.
    return now.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
  }

  public static LocalDate getStartOfLastWeek() {
    return getStartOfWeek().minus(1, ChronoUnit.WEEKS);
  }
}