package com.sion.sionojcodesandbox.model;

import com.sion.sionoj.model.dto.questionsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author : wick
 * @Date : 2024/10/30 19:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {
    private List<String> outputList;

    /**
     *接口信息
     */
    private String message;


    /**
     *执行状态
     */
    private Integer status;

    /**
     *判题信息
     */
    private JudgeInfo judgeInfo;
}
