package com.yupi.zlcoj.jude.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.zlcoj.common.ErrorCode;
import com.yupi.zlcoj.exception.BusinessException;
import com.yupi.zlcoj.jude.codesandbox.CodeSandbox;
import com.yupi.zlcoj.jude.codesandbox.model.ExecuteCodeRequest;
import com.yupi.zlcoj.jude.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

/***
 * 远程代码沙箱(实际调用远程接口的沙箱)
 */
public class RemoteCodeSandboxImpl implements CodeSandbox {

    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        //远程调用 代码沙箱的接口...
        String url = "http://localhost:8090/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode RemoteStandbox error message=" + responseStr);
        }


        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
