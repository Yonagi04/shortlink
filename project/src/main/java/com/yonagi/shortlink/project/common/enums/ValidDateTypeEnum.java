package com.yonagi.shortlink.project.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 有效期类型
 * @date 2024/08/18 08:30
 */
@RequiredArgsConstructor
public enum ValidDateTypeEnum {

    /**
     * 永久有效
     */
    PERMANENT(0),

    /**
     * 自定义
     */
    CUSTOM(1);

    @Getter
    private final int type;
}
