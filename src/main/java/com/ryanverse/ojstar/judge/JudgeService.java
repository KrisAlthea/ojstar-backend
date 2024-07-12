package com.ryanverse.ojstar.judge;

import com.ryanverse.ojstar.model.entity.QuestionSubmit;

/**
 * ClassName: JudgeService
 * Package: com.ryanverse.ojstar.judge
 * Description: 判题服务
 *
 * @Author Haoran
 * @Create 2024/7/11 21:15
 * @Version 1.0
 */
public interface JudgeService {

	/**
	 * 判题
	 *
	 * @param questionSubmitId 题目ID
	 * @return QuestionSubmit
	 */
	QuestionSubmit doJudge (long questionSubmitId);
}
