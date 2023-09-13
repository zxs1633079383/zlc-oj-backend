package com.yupi.zlcoj.model.dto.questionSubmit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/***
 * 提交题目
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {


    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;



    /**
     * 题目 id
     */
    private Long questionId;



}
