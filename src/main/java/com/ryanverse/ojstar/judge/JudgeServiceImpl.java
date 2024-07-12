package com.ryanverse.ojstar.judge;

import cn.hutool.json.JSONUtil;
import com.ryanverse.ojstar.common.ErrorCode;
import com.ryanverse.ojstar.exception.BusinessException;
import com.ryanverse.ojstar.judge.codesandbox.CodeSandbox;
import com.ryanverse.ojstar.judge.codesandbox.CodeSandboxFactory;
import com.ryanverse.ojstar.judge.codesandbox.CodeSandboxProxy;
import com.ryanverse.ojstar.judge.codesandbox.model.ExecuteCodeRequest;
import com.ryanverse.ojstar.judge.codesandbox.model.ExecuteCodeResponse;
import com.ryanverse.ojstar.judge.strategy.JudgeContext;
import com.ryanverse.ojstar.model.dto.question.JudgeCase;
import com.ryanverse.ojstar.model.dto.questionsubmit.JudgeInfo;
import com.ryanverse.ojstar.model.entity.Question;
import com.ryanverse.ojstar.model.entity.QuestionSubmit;
import com.ryanverse.ojstar.model.enums.QuestionSubmitStatusEnum;
import com.ryanverse.ojstar.service.QuestionService;
import com.ryanverse.ojstar.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: JudgeServiceImpl
 * Package: com.ryanverse.ojstar.judge
 * Description:
 *
 * @Author Haoran
 * @Create 2024/7/11 21:19
 * @Version 1.0
 */
@Service
public class JudgeServiceImpl implements JudgeService {

	@Resource
	private QuestionService questionService;

	@Resource
	private QuestionSubmitService questionSubmitService;

	@Resource
	private JudgeManager judgeManager;

	@Value("${codesandbox.type:example}")
	private String codeSandboxType;

	/**
	 * 判题
	 *
	 * @param questionId 题目ID
	 * @return QuestionSubmitVO
	 */
	@Override
	public QuestionSubmit doJudge (long questionSubmitId) {
		// 1. 传入题目的提交id,获取对应的题目和提交信息
		QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
		// 判空
		if (questionSubmit == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未找到对应的题目提交信息");
		}
		Long questionId = questionSubmit.getQuestionId();
		// 获取题目信息
		Question question = questionService.getById(questionId);
		// 判空
		if (question == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未找到对应的题目信息");
		}
		// 2. 如果不是等待状态,则不进行判题
		if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.PENDING.getValue())) {
			throw new BusinessException(ErrorCode.OPERATION_ERROR, "当前题目正在判题中");
		}
		// 3. 更新题目提交状态为判题中, 防止重复判题
		QuestionSubmit questionSubmitResult = new QuestionSubmit();
		questionSubmitResult.setId(questionSubmitId);
		questionSubmitResult.setStatus(QuestionSubmitStatusEnum.JUDGING.getValue());
		boolean update = questionSubmitService.updateById(questionSubmitResult);
		if (!update) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新题目提交状态失败");
		}
		// 4. 调用代码沙箱进行判题
		CodeSandbox codeSandbox = CodeSandboxFactory.getCodeSandbox(codeSandboxType);
		codeSandbox = new CodeSandboxProxy(codeSandbox);
		String code = questionSubmit.getCode();
		String language = questionSubmit.getLanguage();
		String judgeCaseStr = question.getJudgeCase();
		List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
		List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
		ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
				.code(code)
				.language(language)
				.inputList(inputList)
				.build();
		ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
		List<String> outputList = executeCodeResponse.getOutputList();
		// 5. 根据执行结果, 更新题目的提交状态和信息
		JudgeContext judgeContext = new JudgeContext();
		judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
		judgeContext.setInputList(inputList);
		judgeContext.setOutputList(outputList);
		judgeContext.setJudgeCaseList(judgeCaseList);
		judgeContext.setQuestion(question);
		judgeContext.setQuestionSubmit(questionSubmit);
		JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
		// 6. 修改数据库中的判题结果
		questionSubmitResult = new QuestionSubmit();
		questionSubmitResult.setId(questionSubmitId);
		questionSubmitResult.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
		questionSubmitResult.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
		update = questionSubmitService.updateById(questionSubmitResult);
		if (!update) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新题目提交状态失败");
		}
		// 重新获取最新状态
		return questionSubmitService.getById(questionSubmitId);
	}
}
