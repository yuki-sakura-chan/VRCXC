package com.github.vrcxc.utils;

import lombok.Data;

@Data
public class R<T> {

    private int code;

    private String message;

    private T data;

    public R() {
    }

    public R(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> R<T> success() {
        return new R<>(200, "success");
    }

    public static <T> R<T> success(T data) {
        return new R<>(200, "success", data);
    }

    public static <T> R<T> error() {
        return new R<>(500, "error");
    }

    public static <T> R<T> error(String message) {
        return new R<>(500, message);
    }

    public static <T> R<T> error(int code, String message) {
        return new R<>(code, message);
    }

    public static <T> R<T> error(int code, String message, T data) {
        return new R<>(code, message, data);
    }

    public boolean isSuccess() {
        return this.code == 200;
    }

}
