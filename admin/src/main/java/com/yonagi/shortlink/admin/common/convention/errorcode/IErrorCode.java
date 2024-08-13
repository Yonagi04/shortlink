package com.yonagi.shortlink.admin.common.convention.errorcode;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 平台错误码
 * @date 2024/08/13 20:36
 */
public interface IErrorCode {

    /**
     * @return 错误码
     */
    String code();

    /**
     *
     * @return 错误信息
     */
    String message();
}
