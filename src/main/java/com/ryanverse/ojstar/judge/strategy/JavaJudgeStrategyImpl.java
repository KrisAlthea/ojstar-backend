package com.ryanverse.ojstar.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.ryanverse.ojstar.model.dto.question.JudgeCase;
import com.ryanverse.ojstar.model.dto.question.JudgeConfig;
import com.ryanverse.ojstar.judge.codesandbox.model.JudgeInfo;
import com.ryanverse.ojstar.model.entity.Question;
import com.ryanverse.ojstar.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * ClassName: JavaJudgeStrategyImpl
 * Package: com.ryanverse.ojstar.judge.strategy
 * Description: Java判题策略
 *
 * @Author Haoran
 * @Create 2024/7/12 14:58
 * @Version 1.0
 */
public class JavaJudgeStrategyImpl implements JudgeStrategy {
	/**
	 * 判题
	 *
	 * @param judgeContext 判题上下文
	 * @return JudgeInfo
	 */
	@Override
	public JudgeInfo doJudge (JudgeContext judgeContext) {
		JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
		List<String> inputList = judgeContext.getInputList();
		List<String> outputList = judgeContext.getOutputList();
		Question question = judgeContext.getQuestion();
		List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();

		Long memory = judgeInfo.getMemory();
		Long time = judgeInfo.getTime();

		JudgeInfo judgeInfoResult = new JudgeInfo();
		judgeInfoResult.setMemory(memory);
		judgeInfoResult.setTime(time);

		JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
		// 1. 先判断沙箱执行的结果输出数量是否和预期输出数量相等
		if (outputList.size() != inputList.size()) {
			judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
			judgeInfoResult.setMessage(judgeInfoMessageEnum.getValue());
			return judgeInfoResult;
		}
		// 2. 依次判断每一项输出和预期输出是否相等
		for (int i = 0; i < judgeCaseList.size(); i++) {
			JudgeCase judgeCase = judgeCaseList.get(i);
			String output = outputList.get(i);
			if (!output.equals(judgeCase.getOutput())) {
				judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
				judgeInfoResult.setMessage(judgeInfoMessageEnum.getValue());
				return judgeInfoResult;
			}
		}
		// 3. 判题题目的限制是否符合要求
		String judgeConfigStr = question.getJudgeConfig();
		JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
		Long memoryLimit = judgeConfig.getMemoryLimit();
		Long timeLimit = judgeConfig.getTimeLimit();
		if (memory > memoryLimit) {
			judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
			judgeInfoResult.setMessage(judgeInfoMessageEnum.getValue());
			return judgeInfoResult;
		}
		// 假设 Java 判题额外需要10s
		long timeLimitJava = timeLimit + 10000L;
		if (time > timeLimitJava) {
			judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
			judgeInfoResult.setMessage(judgeInfoMessageEnum.getValue());
			return judgeInfoResult;
		}
		judgeInfoResult.setMessage(judgeInfoMessageEnum.getValue());
		return judgeInfoResult;
	}
}
