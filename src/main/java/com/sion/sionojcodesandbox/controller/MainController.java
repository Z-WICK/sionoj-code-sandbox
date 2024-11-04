package com.sion.sionojcodesandbox.controller;

import com.sion.sionojcodesandbox.JavaNativeCodeSandbox;
import com.sion.sionojcodesandbox.model.ExecuteCodeRequest;
import com.sion.sionojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author : wick
 * @Date : 2024/11/1 17:08
 */
@RestController("/")
public class MainController {

    @Resource
    private JavaNativeCodeSandbox javaNativeCodeSandbox;


    @GetMapping("/health")
    public String health() {
        return "OK";
    }


    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode (@RequestBody ExecuteCodeRequest executeCodeRequest){
        if(executeCodeRequest == null){
            throw new RuntimeException("请求参数为空");
        }
        return javaNativeCodeSandbox.executeCode(executeCodeRequest);
    }

}
