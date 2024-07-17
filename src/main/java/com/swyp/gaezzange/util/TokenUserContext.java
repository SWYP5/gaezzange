package com.swyp.gaezzange.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class TokenUserContext {
  private Long userId;
  private Long userAuthId;
  private String email;
  private String role;
  private String provider;
}
