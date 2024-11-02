package com.sion.sionojcodesandbox.security;

import lombok.extern.slf4j.Slf4j;

import java.security.Permission;

/**
 * @Author : wick
 * @Date : 2024/11/2 12:19
 * 默认安全管理器
 */
@Slf4j
public class DefaultSecurityManager extends SecurityManager {
    // 检查所有的权限


    @Override
    public void checkPermission(Permission perm) {
        log.info("默认不做任何限制");
        log.info("perm: " + perm);
//        super.checkPermission(perm);

    }
}
