package com.swyp.gaezzange.util.S3;

import lombok.Getter;

public enum FileType {
  USER_IMAGE("user-image"),
  FEED_IMAGE("feed-image");

  @Getter
  private final String basePath;

  FileType(String basePath) {
    this.basePath = basePath;
  }
}