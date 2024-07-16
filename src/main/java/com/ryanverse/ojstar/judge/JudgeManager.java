package com.ryanverse.ojstar.judge;

import com.ryanverse.ojstar.judge.strategy.DefaultJudgeStrategyImpl;
import com.ryanverse.ojstar.judge.strategy.JavaJudgeStrategyImpl;
import com.ryanverse.ojstar.judge.strategy.JudgeContext;
import com.ryanverse.ojstar.judge.strategy.JudgeStrategy;
import com.ryanverse.ojstar.judge.codesandbox.model.JudgeInfo;
import com.ryanverse.ojstar.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * ClassName: JudgeManager
 * Package: com.ryanverse.ojstar.judge
 * Description:
 *
 * @Author Haoran
 * @Create 2024/7/12 15:04
 * @Version 1.0
 */
@Service
public class JudgeManager {

	/**
	 * 执行判题
	 *
	 * @param judgeContext 判题上下文
	 * @return JudgeInfo
	 */
	JudgeInfo doJudge (JudgeContext judgeContext) {
		QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
		String language = questionSubmit.getLanguage();
		JudgeStrategy judgeStrategy = new DefaultJudgeStrategyImpl();
		if ("java".equals(language)) {
			judgeStrategy = new JavaJudgeStrategyImpl();
		}
		return judgeStrategy.doJudge(judgeContext);
	}

}
