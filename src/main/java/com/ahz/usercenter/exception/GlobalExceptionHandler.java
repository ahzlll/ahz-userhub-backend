package com.ahz.usercenter.exception;

import com.ahz.usercenter.common.Result;
import com.ahz.usercenter.common.ErrorCode;
import com.ahz.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author ahz
 * @version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException: {}", e.getMessage(), e);
        // 如果异常消息包含详细信息，将其作为 description 返回
        String description = "";
        if (e.getCause() != null && e.getCause().getMessage() != null) {
            description = e.getCause().getMessage();
        }
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), description);
    }
}
