package com.yupi.zlcoj.service;

import com.yupi.zlcoj.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.yupi.zlcoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.zlcoj.model.entity.User;

/**
* @author 16330
* @description 针对表【question_submit(题目提交表)】的数据库操作Service
* @createDate 2023-09-12 14:22:31
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     *  题目提交
     * @param question
     * @param loginnUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest question, User loginnUser);



}
