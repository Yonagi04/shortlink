package com.yonagi.shortlink.project.common.convention.exception;

import com.yonagi.shortlink.project.common.convention.errorcode.BaseErrorCode;
import com.yonagi.shortlink.project.common.convention.errorcode.IErrorCode;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 客户端异常处理
 * @date 2024/08/13 21:03
 */
public class ClientException extends AbstractException {

    public ClientException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    public ClientException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public ClientException(String message) {
        this(message, null, BaseErrorCode.CLIENT_ERROR);
    }

    public ClientException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    @Override
    public String toString() {
        return "ClientException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
