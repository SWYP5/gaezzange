package com.swyp.gaezzange.contants;

public abstract class ExceptionConstants {

  public static class UserExceptionConstants {

    public static final String CODE_USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String MESSAGE_USER_NOT_FOUND = "사용자를 찾을 수 없습니다.";
    public static final String CODE_ALREADY_ONBOARDED = "ALREADY_ONBOARDED";
    public static final String MESSAGE_ALREADY_ONBOARDED = "이미 온보딩을 완료했습니다.";
    public static final String CODE_UNKNOWN_USER = "UNKNOWN_USER";
    public static final String MESSAGE_UNKNOWN_USER = "다시 로그인 해주세요.";
  }

  public static class RoutineExceptionConstants {

    public static final String CODE_ROUTINE_NOT_FOUND = "ROUTINE_NOT_FOUND";
    public static final String MESSAGE_ROUTINE_NOT_FOUND = "루틴을 찾을 수 없습니다.";
    public static final String CODE_NOT_OWNED_ROUTINE = "NOT_OWNED_ROUTINE";
    public static final String MESSAGE_NOT_OWNED_ROUTINE = "본인의 루틴이 아닙니다.";
  }

  public static class FeedExceptionConstants {

    public static final String CODE_FEED_NOT_FOUND = "FEED_NOT_FOUND";
    public static final String MESSAGE_FEED_NOT_FOUND = "피드를 찾을 수 없습니다.";
  }

}
