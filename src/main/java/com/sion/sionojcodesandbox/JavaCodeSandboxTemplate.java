package com.sion.sionojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.sion.sionojcodesandbox.model.ExecuteCodeRequest;
import com.sion.sionojcodesandbox.model.ExecuteCodeResponse;
import com.sion.sionojcodesandbox.model.ExecuteMessage;
import com.sion.sionojcodesandbox.model.JudgeInfo;
import com.sion.sionojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author : wick
 * @Date : 2024/11/4 15:08
 */
@Slf4j
public class JavaCodeSandboxTemplate implements CodeSandbox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        // 1.用户的代码保存为文件
        File userCodeFile = saveCodeToFile(code);


        //2. 编译代码，得到 class 文件
        ExecuteMessage compiledFileExecuteMessage = compileCode(userCodeFile);
        System.out.println(compiledFileExecuteMessage);


        //3. 执行代码，得到输出结果
        List<ExecuteMessage> executeMessageList = runFile(userCodeFile, inputList);

        //4. 收集整理输出结果
        ExecuteCodeResponse outputResponse = getOutputResponse(executeMessageList);

        //5. 文件清理
        boolean b = deleteFile(userCodeFile);
        if(!b){
            log.error("文件清理失败 , userCodeFilePath = {}",userCodeFile.getAbsolutePath());
        }

        return outputResponse;
    }

    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    private static final long TIME_OUT = 5000L;

    private static final String SECURITY_MANAGER_PATH = "/Users/wick/Documents/java项目/企业项目实战/OJ判题系统/sionoj-code-sandbox/sionoj-code-sandbox/src/main/resources/security";

    private static final String SECURITY_MANAGER_CLASS_NAME = "MySecurityManager";


    /**
     * 1.用户的代码保存为文件
     *
     * @param code 用户代码
     * @return {@link File }
     */
    public File saveCodeToFile(String code) {
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        //判断全局代码目录是否存在,没有则新建
        if (FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        // 把用户的代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
        return userCodeFile;
    }


    /**
     * 2.编译用户代码
     * @param userCodeFile
     * @return {@link ExecuteMessage }
     */
    public ExecuteMessage compileCode(File userCodeFile) {
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            System.out.println(executeMessage);
            if (executeMessage.getExitValue() != 0) {
                throw new RuntimeException("编译错误");
            }
            return executeMessage;

        } catch (Exception e) {
            // return getErrorResponse(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 3.执行文件,获得执行结果列表
     *
     * @param userCodeFile
     * @param inputList
     * @return {@link List }<{@link ExecuteMessage }>
     */
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();

        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                // 超时控制
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        System.out.println("超时");
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                executeMessageList.add(executeMessage);
            } catch (IOException e) {
                throw new RuntimeException("程序执行异常" + e);
            }

        }
        return executeMessageList;


    }

    /**
     * 4.文件清理
     * @param userCodeFile
     * @return boolean
     */
    public boolean deleteFile(File userCodeFile) {

        if (userCodeFile.getParentFile() != null) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();

            boolean del = FileUtil.del(userCodeParentPath);
            log.info("删除文件: {}, 结果: {}", userCodeParentPath, del);
            return del;
        }
        return true;
    }

    /**
     * 5.获取输出结果
     * @param executeMessageList
     * @return {@link ExecuteCodeResponse }
     */
    public ExecuteCodeResponse getOutputResponse(List<ExecuteMessage> executeMessageList) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outputList = new ArrayList<>();
        //取用时最大值,便于判断是否超时
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                // 用户提交的代码执行中存在错误
                // todo 设置枚举值
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if (time != null) {
                maxTime = Math.max(maxTime, time);
            }
        }
        // 正常运行完成
        if (outputList.size() == executeMessageList.size()) {
            // todo 设置枚举值
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        // todo 借助第三方库来获取内存占用 , 原生的过于麻烦算了吧
        //judgeInfo.setMemory();

        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }


    /**
     * 6.获取错误响应
     *
     * @param e
     * @return
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 表示代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }


}
