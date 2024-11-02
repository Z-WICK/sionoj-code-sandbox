package com.sion.sionojcodesandbox.security;

import java.security.Permission;

/**
 * @Author : wick
 * @Date : 2024/11/2 12:26
 */
public class MySecurityManager extends SecurityManager {
    // 检查所有的权限
    @Override
    public void checkPermission(Permission perm) {
//        super.checkPermission(perm);
    }

    // 检测程序是否可执行文件
    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("checkExec 权限异常：" + cmd);
    }

    // 检测程序是否允许读文件

    @Override
    public void checkRead(String file) {
        System.out.println(file);
        if (file.contains("/Users/wick/Documents/java项目/企业项目实战/OJ判题系统/sionoj-code-sandbox/sionoj-code-sandbox")) {
        }
//        throw new SecurityException("checkRead 权限异常：" + file);
    }

    // 检测程序是否允许写文件
    @Override
    public void checkWrite(String file) {
//        throw new SecurityException("checkWrite 权限异常：" + file);
    }

    // 检测程序是否允许删除文件
    @Override
    public void checkDelete(String file) {
//        throw new SecurityException("checkDelete 权限异常：" + file);
    }

    // 检测程序是否允许连接网络
    @Override
    public void checkConnect(String host, int port) {
//        throw new SecurityException("checkConnect 权限异常：" + host + ":" + port);
    }

}
