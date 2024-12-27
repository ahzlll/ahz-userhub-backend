package com.ahz.usercenter.exception;

import com.ahz.usercenter.common.ErrorCode;
import com.ahz.usercenter.common.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 全局异常处理器测试
 *
 * @author ahz
 * @version 3.0.1
 */
@SpringBootTest
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    /**
     * 测试处理BusinessException - 正常情况
     */
    @Test
    void testBusinessExceptionHandler() {
        BusinessException exception = new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        Result<?> result = globalExceptionHandler.businessExceptionHandler(exception);

        assertNotNull(result);
        assertEquals(ErrorCode.PARAMS_ERROR.getCode(), result.getCode());
        assertEquals(ErrorCode.PARAMS_ERROR.getMessage(), result.getMessage());
        assertEquals("参数错误", result.getDescription());
    }

    /**
     * 测试处理BusinessException - 带描述信息
     */
    @Test
    void testBusinessExceptionHandlerWithDescription() {
        BusinessException exception = new BusinessException(ErrorCode.NOT_LOGIN, "请先登录");
        Result<?> result = globalExceptionHandler.businessExceptionHandler(exception);

        assertNotNull(result);
        assertEquals(ErrorCode.NOT_LOGIN.getCode(), result.getCode());
        assertEquals(ErrorCode.NOT_LOGIN.getMessage(), result.getMessage());
        assertEquals("请先登录", result.getDescription());
    }

    /**
     * 测试处理RuntimeException - 正常情况
     */
    @Test
    void testRuntimeExceptionHandler() {
        RuntimeException exception = new RuntimeException("运行时异常");
        Result<?> result = globalExceptionHandler.runtimeExceptionHandler(exception);

        assertNotNull(result);
        assertEquals(ErrorCode.SYSTEM_ERROR.getCode(), result.getCode());
        assertEquals("运行时异常", result.getMessage());
    }

    /**
     * 测试处理RuntimeException - 带cause
     */
    @Test
    void testRuntimeExceptionHandlerWithCause() {
        RuntimeException cause = new RuntimeException("根本原因");
        RuntimeException exception = new RuntimeException("运行时异常", cause);
        Result<?> result = globalExceptionHandler.runtimeExceptionHandler(exception);

        assertNotNull(result);
        assertEquals(ErrorCode.SYSTEM_ERROR.getCode(), result.getCode());
        assertEquals("运行时异常", result.getMessage());
        assertEquals("根本原因", result.getDescription());
    }

    /**
     * 测试处理RuntimeException - cause为null
     */
    @Test
    void testRuntimeExceptionHandlerWithNullCause() {
        RuntimeException exception = new RuntimeException("运行时异常", null);
        Result<?> result = globalExceptionHandler.runtimeExceptionHandler(exception);

        assertNotNull(result);
        assertEquals(ErrorCode.SYSTEM_ERROR.getCode(), result.getCode());
        assertEquals("运行时异常", result.getMessage());
        assertEquals("", result.getDescription());
    }

    /**
     * 测试处理RuntimeException - cause的message为null
     */
    @Test
    void testRuntimeExceptionHandlerWithNullCauseMessage() {
        RuntimeException cause = new RuntimeException((String) null);
        RuntimeException exception = new RuntimeException("运行时异常", cause);
        Result<?> result = globalExceptionHandler.runtimeExceptionHandler(exception);

        assertNotNull(result);
        assertEquals(ErrorCode.SYSTEM_ERROR.getCode(), result.getCode());
        assertEquals("运行时异常", result.getMessage());
        assertEquals("", result.getDescription());
    }
}

