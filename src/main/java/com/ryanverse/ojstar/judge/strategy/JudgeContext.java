package com.ryanverse.ojstar.judge.strategy;

import com.ryanverse.ojstar.model.dto.question.JudgeCase;
import com.ryanverse.ojstar.judge.codesandbox.model.JudgeInfo;
import com.ryanverse.ojstar.model.entity.Question;
import com.ryanverse.ojstar.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * ClassName: JudgeContext
 * Package: com.ryanverse.ojstar.judge.strategy
 * Description: 判题上下文(用于定义在策略中传递的参数)
 *
 * @Author Haoran
 * @Create 2024/7/12 14:38
 * @Version 1.0
 */
@Data
public class JudgeContext {
	private JudgeInfo judgeInfo;

	private List<String> inputList;

	private List<String> outputList;

	private List<JudgeCase> judgeCaseList;

	private Question question;

	private QuestionSubmit questionSubmit;
}
