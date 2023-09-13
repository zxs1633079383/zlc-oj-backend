package com.yupi.zlcoj.controller;

import com.yupi.zlcoj.common.BaseResponse;
import com.yupi.zlcoj.common.ErrorCode;
import com.yupi.zlcoj.common.ResultUtils;
import com.yupi.zlcoj.exception.BusinessException;
import com.yupi.zlcoj.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.yupi.zlcoj.model.entity.User;
import com.yupi.zlcoj.service.QuestionSubmitService;
import com.yupi.zlcoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum 本次提交变化数
     */
    @PostMapping("/")
    public BaseResponse<Long> doSubmitQuestion(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
            HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId()<= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交
        final User loginUser = userService.getLoginUser(request);
        Long questionId = questionSubmitAddRequest.getQuestionId();
        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }

}
