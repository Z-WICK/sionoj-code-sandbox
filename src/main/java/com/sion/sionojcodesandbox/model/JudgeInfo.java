package com.sion.sionojcodesandbox.model;

import lombok.Data;

/**
 * @Author : wick
 * @Date : 2024/10/27 12:14
 */
@Data
public class JudgeInfo {

    /**
    * 程序执行信息
    * */
    private String  message;

    /**
    * 消耗内存(kb)
    * */
    private Long memory;

    /**
    *消耗时间(kb)
    * */
    private Long time;
}
