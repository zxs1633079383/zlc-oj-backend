package com.yupi.zlcoj.jude.codesandbox.impl;

import com.yupi.zlcoj.jude.codesandbox.CodeSandbox;
import com.yupi.zlcoj.jude.codesandbox.model.ExecuteCodeRequest;
import com.yupi.zlcoj.jude.codesandbox.model.ExecuteCodeResponse;

/**
 * 第三方代码沙箱
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("三方接口沙箱");
        return  null;
    }
}
