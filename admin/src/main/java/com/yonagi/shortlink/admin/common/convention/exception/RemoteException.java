package com.yonagi.shortlink.admin.common.convention.exception;

import com.yonagi.shortlink.admin.common.convention.errorcode.BaseErrorCode;
import com.yonagi.shortlink.admin.common.convention.errorcode.IErrorCode;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 远程调用异常处理
 * @date 2024/08/13 21:09
 */
public class RemoteException extends AbstractException {

    public RemoteException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    public RemoteException(String message) {
        this(message, null, BaseErrorCode.REMOTE_ERROR);
    }

    public RemoteException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    @Override
    public String toString() {
        return "RemoteException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
