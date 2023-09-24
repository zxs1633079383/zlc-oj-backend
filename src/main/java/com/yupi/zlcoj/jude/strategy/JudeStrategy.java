package com.yupi.zlcoj.jude.strategy;

import com.yupi.zlcoj.jude.codesandbox.model.JudeInfo;

/**
 * 判题策略
 */
public interface JudeStrategy {

    /**
     * 执行判题
     *
     * @param judeContext
     * @return
     */
    JudeInfo doJude(JudeContext judeContext);
}
