package com.ryanverse.ojstar.judge.codesandbox.impl;

import com.ryanverse.ojstar.judge.codesandbox.CodeSandbox;
import com.ryanverse.ojstar.judge.codesandbox.model.ExecuteCodeRequest;
import com.ryanverse.ojstar.judge.codesandbox.model.ExecuteCodeResponse;
import com.ryanverse.ojstar.model.dto.questionsubmit.JudgeInfo;
import com.ryanverse.ojstar.model.enums.JudgeInfoMessageEnum;
import com.ryanverse.ojstar.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * ClassName: ExampleCodeSandboxImpl
 * Package: com.ryanverse.ojstar.judge.codesandbox.impl
 * Description:
 *
 * @Author Haoran
 * @Create 2024/7/11 19:58
 * @Version 1.0
 */
public class ExampleCodeSandboxImpl implements CodeSandbox {
	@Override
	public ExecuteCodeResponse executeCode (ExecuteCodeRequest executeCodeRequest) {
		List<String> inputList = executeCodeRequest.getInputList();
		ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
		executeCodeResponse.setOutputList(inputList);
		executeCodeResponse.setMessage("测试执行成功");
		executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
		JudgeInfo judgeInfo = new JudgeInfo();
		judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
		judgeInfo.setMemory(100L);
		judgeInfo.setTime(100L);
		executeCodeResponse.setJudgeInfo(judgeInfo);
		return executeCodeResponse;
	}
}
