package com.yonagi.shortlink.project.common.convention.exception;

import com.yonagi.shortlink.project.common.convention.errorcode.BaseErrorCode;
import com.yonagi.shortlink.project.common.convention.errorcode.IErrorCode;

import java.util.Optional;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 服务端异常处理
 * @date 2024/08/13 21:10
 */
public class ServerException extends AbstractException {

    public ServerException(String message, Throwable throwable, IErrorCode errorCode) {
        super(Optional.ofNullable(message).orElse(errorCode.message()), throwable, errorCode);
    }

    public ServerException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public ServerException(String message) {
        this(message, null, BaseErrorCode.SERVICE_ERROR);
    }

    public ServerException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    @Override
    public String toString() {
        return "ServerException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
