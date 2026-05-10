package org.example.test.common.exception;

import org.example.test.common.api.ApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ApiResult<Void> handleApi(ApiException e) {
        return ApiResult.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ApiResult<Void> handleBadCredentials(BadCredentialsException e) {
        return ApiResult.fail(401, "用户名或密码错误");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResult<Void> handleDenied(AccessDeniedException e) {
        return ApiResult.fail(403, "无权限");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<Void> handleValid(MethodArgumentNotValidException e) {
        FieldError fe = e.getBindingResult().getFieldError();
        String msg = fe != null ? fe.getDefaultMessage() : "参数错误";
        return ApiResult.fail(400, msg);
    }

    @ExceptionHandler(Exception.class)
    public ApiResult<Void> handleOther(Exception e) {
        return ApiResult.fail(500, e.getMessage() != null ? e.getMessage() : "服务器错误");
    }
}
