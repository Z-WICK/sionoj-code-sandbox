package com.sion.sionojcodesandbox;


import com.sion.sionojcodesandbox.model.ExecuteCodeRequest;
import com.sion.sionojcodesandbox.model.ExecuteCodeResponse;

public interface CodeSandbox {

     /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
