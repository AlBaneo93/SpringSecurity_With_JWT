package com.example.security.utils;

public class ApiUtils<T> {

  public static <T> ApiResults<T> success(T response) {
    return new ApiResults<T>(true, response, null);
  }

  public static ApiResults<?> error(int code, String msg) {
    return new ApiResults(false, null, new ApiError(code, msg));
  }

  public static class ApiResults<T> {

    boolean success;

    T response;

    ApiError error;

    public ApiResults(boolean success, T response, ApiError error) {
      this.success = success;
      this.response = response;
      this.error = error;
    }

  }

  static class ApiError {

    int code;

    String msg;

    public ApiError(int code, String msg) {
      this.code = code;
      this.msg = msg;
    }

  }

}
