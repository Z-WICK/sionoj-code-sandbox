package com.sion.sionojcodesandbox.controller;

import com.sion.sionojcodesandbox.JavaDockerCodeSandbox;
import com.sion.sionojcodesandbox.JavaNativeCodeSandbox;
import com.sion.sionojcodesandbox.model.ExecuteCodeRequest;
import com.sion.sionojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author : wick
 * @Date : 2024/11/1 17:08
 */
@RestController("/")
public class MainController {

        // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Resource
    private JavaNativeCodeSandbox javaNativeCodeSandbox;

    @Resource
    private JavaDockerCodeSandbox javaDockerCodeSandbox;


    @GetMapping("/health")
    public String health() {
        return "OK";
    }


    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode (@RequestBody ExecuteCodeRequest executeCodeRequest,
                                     HttpServletRequest request,
                                     HttpServletResponse response){
        // 基本的认证
        String authHeader = request.getHeader(AUTH_REQUEST_HEADER);
        if (!AUTH_REQUEST_SECRET.equals(authHeader)) {
            response.setStatus(403);
            return null;
        }
        if(executeCodeRequest == null){
            throw new RuntimeException("请求参数为空");
        }
        return javaDockerCodeSandbox.executeCode(executeCodeRequest);
    }

}
