package com.swyp.gaezzange.util;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiResponse<T> {
    private String code;
    private String message;
    private T data;

    private static final String SUCCESS_CODE = "SUCCESS";

    public static <T> ApiResponse success(T data) {
        return ApiResponse.builder()
                .code(SUCCESS_CODE)
                .message(null)
                .data(data)
                .build();
    }

    public static <T> ApiResponse fail(String code, String message, T data) {
        return ApiResponse.builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }
}
