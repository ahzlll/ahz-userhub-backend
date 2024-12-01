package com.ahz.usercenter.common;

/**
 * 返回工具类
 *
 * @author ahz
 * @version 1.0
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static Result<Object> error(ErrorCode errorCode) {
        return new Result<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static Result<Object> error(int code, String message, String description) {
        return new Result<>(code, null, message, description);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static Result<Object> error(ErrorCode errorCode, String message, String description) {
        return new Result<>(errorCode.getCode(), null, message, description);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static Result<Object> error(ErrorCode errorCode, String description) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), description);
    }
}