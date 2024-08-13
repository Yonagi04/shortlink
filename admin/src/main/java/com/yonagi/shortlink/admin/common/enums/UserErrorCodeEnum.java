package com.yonagi.shortlink.admin.common.enums;

import com.yonagi.shortlink.admin.common.convention.errorcode.IErrorCode;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description
 * @date 2024/08/13 20:42
 */
public enum UserErrorCodeEnum implements IErrorCode {
    USER_NOT_EXIST("B000200", "用户记录不存在"),

    USER_EXIST("B000201", "用户记录已存在");

    private final String code;

    private final String message;

    UserErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
