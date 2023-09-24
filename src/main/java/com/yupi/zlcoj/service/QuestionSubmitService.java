package com.yupi.zlcoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.zlcoj.model.dto.question.QuestionQueryRequest;
import com.yupi.zlcoj.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.yupi.zlcoj.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.yupi.zlcoj.model.entity.Question;
import com.yupi.zlcoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.zlcoj.model.entity.User;
import com.yupi.zlcoj.model.vo.QuestionSubmitVO;
import com.yupi.zlcoj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 16330
 * @description 针对表【question_submit(题目提交表)】的数据库操作Service
 * @createDate 2023-09-12 14:22:31
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param question
     * @param loginnUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest question, User loginnUser);


    /**
     * 获取查询条件
     *
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);


    /**
     * 获取帖子封装
     *
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取帖子封装
     *
     * @param questionPage
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionPage, User loginUser);

}
