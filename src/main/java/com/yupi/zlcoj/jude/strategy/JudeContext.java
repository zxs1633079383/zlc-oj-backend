package com.yupi.zlcoj.jude.strategy;

import com.yupi.zlcoj.model.dto.question.JudeCase;
import com.yupi.zlcoj.jude.codesandbox.model.JudeInfo;
import com.yupi.zlcoj.model.entity.Question;
import com.yupi.zlcoj.model.entity.QuestionSubmit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 上下文(在策略上下文传递的信息)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class JudeContext {

    private JudeInfo judeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudeCase> judeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
