package com.yupi.zlcoj.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.zlcoj.common.ErrorCode;
import com.yupi.zlcoj.exception.BusinessException;
import com.yupi.zlcoj.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.yupi.zlcoj.model.entity.Question;

import com.yupi.zlcoj.model.entity.QuestionSubmit;
import com.yupi.zlcoj.model.entity.User;
import com.yupi.zlcoj.model.enums.QuestionSubmitLanguageEnum;
import com.yupi.zlcoj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.zlcoj.service.QuestionService;
import com.yupi.zlcoj.service.QuestionSubmitService;
import com.yupi.zlcoj.mapper.QuestionSubmitMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author 16330
 * @description 针对表【question_submit(题目提交表)】的数据库操作Service实现
 * @createDate 2023-09-12 14:22:31
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginnUser) {
        // todo 检验编程语言是否范围内.
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum enumByValue = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if(enumByValue == null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言错误");
        }

        Long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }


        //是否已提交题目
        long userId = loginnUser.getId();
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setLanguage(language);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setUserId(userId);
        //设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目提交失败");
        }

        return questionSubmit.getId();

    }

}




