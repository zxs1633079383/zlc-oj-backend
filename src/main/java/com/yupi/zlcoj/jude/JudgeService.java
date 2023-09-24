package com.yupi.zlcoj.jude;

import com.yupi.zlcoj.model.entity.QuestionSubmit;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判断业务
     *
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit duJudge(long questionSubmitId);


}
