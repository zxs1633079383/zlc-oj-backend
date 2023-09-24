package com.yupi.zlcoj.jude.codesandbox.impl;

import com.yupi.zlcoj.jude.codesandbox.CodeSandbox;
import com.yupi.zlcoj.jude.codesandbox.model.ExecuteCodeRequest;
import com.yupi.zlcoj.jude.codesandbox.model.ExecuteCodeResponse;
import com.yupi.zlcoj.jude.codesandbox.model.JudeInfo;
import com.yupi.zlcoj.model.enums.JudeInfoMessageEnum;
import com.yupi.zlcoj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/***
 *  案例代码沙箱(仅为了跑通流程.)
 */
public class ExampleCodeSandboxImpl implements CodeSandbox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("实例代码沙箱");

        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());

        JudeInfo judeInfo = new JudeInfo();
        judeInfo.setMessage(JudeInfoMessageEnum.ACCEPTED.getText());
        judeInfo.setMemory(100L);
        judeInfo.setTime(100L);
        executeCodeResponse.setJudeInfo(judeInfo);

        return executeCodeResponse;


    }
}
