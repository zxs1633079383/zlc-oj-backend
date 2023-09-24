package com.yupi.zlcoj.jude;

import com.yupi.zlcoj.jude.strategy.DefaultJudeStrategyImpl;
import com.yupi.zlcoj.jude.strategy.JavaLanguageJudeStrategyImpl;
import com.yupi.zlcoj.jude.strategy.JudeContext;
import com.yupi.zlcoj.jude.strategy.JudeStrategy;
import com.yupi.zlcoj.jude.codesandbox.model.JudeInfo;
import com.yupi.zlcoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理 简化调用
 */
@Service
public class JudeManager {

    /**
     * 执行判题
     * @param judeContext
     * @return
     */
    JudeInfo doJuge(JudeContext judeContext){
        QuestionSubmit questionSubmit = judeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudeStrategy judeStrategy = new DefaultJudeStrategyImpl();
        if("java".equals(language)){
            judeStrategy = new JavaLanguageJudeStrategyImpl();
        }

        return  judeStrategy.doJude(judeContext);

    }


}

