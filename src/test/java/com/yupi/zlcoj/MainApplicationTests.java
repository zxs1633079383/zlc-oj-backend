package com.yupi.zlcoj;

import com.yupi.zlcoj.config.WxOpenConfig;

import javax.annotation.Resource;

import com.yupi.zlcoj.model.entity.QuestionSubmit;
import com.yupi.zlcoj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.zlcoj.service.QuestionSubmitService;
import com.yupi.zlcoj.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;

/**
 * 主类测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private WxOpenConfig wxOpenConfig;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private RedisUtils redisUtils;

    @Test
    void contextLoads() {
        System.out.println(wxOpenConfig);
    }

    @Test
    void inserTCode() {
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setId(2L);
        questionSubmit.setLanguage("java");
        questionSubmit.setCode("public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        int a = Integer.parseInt(args[0]);\n" +
                "        int b = Integer.parseInt(args[1]);\n" +
                "        System.out.println((a + b));\n" +
                "\n" +
                "    }\n" +
                "}");
        questionSubmit.setJudeInfo("{}");
        questionSubmit.setStatus(0);
        questionSubmit.setQuestionId(2L);
        questionSubmit.setUserId(2L);

        boolean save = questionSubmitService.save(questionSubmit);
        System.out.println("删除是否成功: " + save);
    }

    @Test
    void testRedis() {
        HashMap<String, Object> map = (HashMap<String, Object>) redisUtils.get("submit:" + 1706561617770061825L);
        for (String s : map.keySet()) {
            System.out.println(map.get(s));
        }
    }

}
