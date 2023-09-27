package com.yupi.zlcoj.jude;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.json.JSONUtil;
import com.yupi.zlcoj.common.ErrorCode;
import com.yupi.zlcoj.exception.BusinessException;
import com.yupi.zlcoj.jude.codesandbox.CodeSandbox;
import com.yupi.zlcoj.jude.codesandbox.CodeSandboxFactory;
import com.yupi.zlcoj.jude.codesandbox.CodeSandboxProxy;
import com.yupi.zlcoj.jude.codesandbox.model.ExecuteCodeRequest;
import com.yupi.zlcoj.jude.codesandbox.model.ExecuteCodeResponse;
import com.yupi.zlcoj.jude.strategy.JudeContext;
import com.yupi.zlcoj.model.dto.question.JudeCase;
import com.yupi.zlcoj.jude.codesandbox.model.JudeInfo;
import com.yupi.zlcoj.model.entity.Question;
import com.yupi.zlcoj.model.entity.QuestionSubmit;
import com.yupi.zlcoj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.zlcoj.service.QuestionService;
import com.yupi.zlcoj.service.QuestionSubmitService;
import com.yupi.zlcoj.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class JudgeServiceImpl implements JudgeService {


    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Value("${codesandbox.type:example}")
    private String type;

    @Resource
    private JudeManager judeManager;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public QuestionSubmit duJudge(long questionSubmitId) {
        //1. 传入题目的提交id,根据id获取到对应的题目,提交信息(代码,语言等)
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);


        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }

        Long questionId = questionSubmit.getQuestionId();

        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        Integer status = questionSubmit.getStatus();
        if (status != QuestionSubmitStatusEnum.WAITING.getValue()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }

        //2. 更改判题(题目提交) 的状态

        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        } else {
            //存入redis 更新判题中 (2)
            HashMap<String, Object> map = (HashMap<String, Object>) redisUtils.get("submit:" + questionSubmitId);
            map.put("questionSubmitId", questionSubmitUpdate.getId());
            map.put("judgeInfo", questionSubmitUpdate.getJudeInfo());
            map.put("status", questionSubmitUpdate.getStatus());
            redisUtils.set("submit:" + questionSubmitUpdate.getId(), map);
//            redisUtils.set("submit:" + questionSubmitUpdate.getId(), questionSubmitUpdate.getStatus());
        }

        Long id = questionSubmit.getId();
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        String judeInfo = questionSubmit.getJudeInfo();
        Long userId = questionSubmit.getUserId();
        Date createTime = questionSubmit.getCreateTime();
        Date updateTime = questionSubmit.getUpdateTime();
        Integer isDelete = questionSubmit.getIsDelete();

        //3. 调用沙箱,获取到执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        // 获取输入用例
        String judeCaseStr = question.getJudeCase();
        List<JudeCase> judeCaseList = JSONUtil.toList(judeCaseStr, JudeCase.class);

        List<String> inputList = judeCaseList.stream().map(JudeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);

        //4. 根据沙箱的执行结果. 设置题目的判题状态和信息
        //封装上下文
        JudeContext judeContext = new JudeContext();
        // 兜底方法, 解决前端编译错误, 前端无线调用
        if (executeCodeResponse.getStatus() == null) {
            JudeInfo judeInfo1 = new JudeInfo();
            judeInfo1.setMessage("编译错误, 请检查代码");
            judeInfo1.setMemory(0L);
            judeInfo1.setTime(0L);
            //直接更新状态为2.,judgeinfo 为 该内容  然后抛异常终止吧
            judeContext.setJudeInfo(judeInfo1);
            QuestionSubmit questionSubmitError = new QuestionSubmit();
            questionSubmitError.setId(questionSubmitId);
            questionSubmitError.setJudeInfo(JSONUtil.toJsonStr(judeInfo1));
            questionSubmitError.setStatus(2);
            boolean flag = questionSubmitService.updateById(questionSubmitError);
            //存入redis 结果 (成功 或者 失败) (3)
            HashMap<String, Object> map = (HashMap<String, Object>) redisUtils.get("submit:" + 1706561617770061825L);
            map.put("questionSubmitId", questionSubmitId);
            map.put("judgeInfo", questionSubmitError.getJudeInfo());
            map.put("status", questionSubmitError.getStatus());
            redisUtils.set("submit:" + questionSubmitError.getId(), map);

            //执行到这里报错???
            QuestionSubmit submitError = questionSubmitService.getById(questionSubmitId);
            log.info("是否执行到这里报错??");

            return submitError;
        } else {
            judeContext.setJudeInfo(executeCodeResponse.getJudeInfo());
        }
        List<String> outputList = executeCodeResponse.getOutputList();


        // judeinfo是沙箱那边提供的
        judeContext.setInputList(inputList);
        judeContext.setOutputList(outputList);
        judeContext.setJudeCaseList(judeCaseList);
        judeContext.setQuestion(question);
        judeContext.setQuestionSubmit(questionSubmit);

        //生成策略
        JudeInfo judeInfoResult = judeManager.doJuge(judeContext);
//        JudeStrategy judeStrategy = new DefaultJudeStrategyImpl();
//        JudeInfo judeInfoResult = judeStrategy.doJude(judeContext);
        // 修改数据库的结果
        QuestionSubmit questionSubmitResult = new QuestionSubmit();
        questionSubmitResult.setId(questionSubmitId);
        questionSubmitResult.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitResult.setJudeInfo(JSONUtil.toJsonStr(judeInfoResult));
        boolean update2 = questionSubmitService.updateById(questionSubmitResult);
        if (!update2) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新失败");
        } else {
            //存入redis 结果 (成功 或者 失败) (3)
            HashMap<String, Object> map = (HashMap<String, Object>) redisUtils.get("submit:" + 1706561617770061825L);
            map.put("questionSubmitId", questionSubmitId);
            map.put("judgeInfo", questionSubmitResult.getJudeInfo());
            map.put("status", questionSubmitResult.getStatus());
            redisUtils.set("submit:" + questionSubmitResult.getId(), map);
//            redisUtils.set("submit:" + questionSubmitResult.getId(), questionSubmitResult.getStatus());
        }

        QuestionSubmit questionSubmitResultFinal = questionSubmitService.getById(questionSubmitId);
        return questionSubmitResultFinal;


    }
}
