package com.yupi.zlcoj.jude.codesandbox;

import com.yupi.zlcoj.jude.codesandbox.model.ExecuteCodeRequest;
import com.yupi.zlcoj.jude.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandbox {

    //todo 提供可提供查看代码沙箱状态的接口。。

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);

}
