package org.example.thumb.exception;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.example.thumb.common.BaseResponse;
import org.example.thumb.common.ErrorCode;
import org.example.thumb.common.ResultUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author pine
 */
@RestControllerAdvice
@Slf4j
// 在接口文档中隐藏
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResultUtils.error(ErrorCode.OPERATION_ERROR, e.getMessage());
    }
}
