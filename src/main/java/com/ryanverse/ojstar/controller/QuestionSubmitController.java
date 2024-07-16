package com.ryanverse.ojstar.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryanverse.ojstar.common.BaseResponse;
import com.ryanverse.ojstar.common.ErrorCode;
import com.ryanverse.ojstar.common.ResultUtils;
import com.ryanverse.ojstar.exception.BusinessException;
import com.ryanverse.ojstar.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.ryanverse.ojstar.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.ryanverse.ojstar.model.entity.QuestionSubmit;
import com.ryanverse.ojstar.model.entity.User;
import com.ryanverse.ojstar.model.vo.QuestionSubmitVO;
import com.ryanverse.ojstar.service.QuestionSubmitService;
import com.ryanverse.ojstar.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author Haoran
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
@Deprecated
public class QuestionSubmitController {

	@Resource
	private QuestionSubmitService questionSubmitService;

	@Resource
	private UserService userService;

	/**
	 * 提交题目
	 *
	 * @param questionSubmitAddRequest
	 * @param request
	 * @return resultNum 提交记录id
	 */
	@PostMapping("/")
	public BaseResponse<Long> doQuestionSubmit (@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
	                                            HttpServletRequest request) {
		if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		// 登录才能提交题目
		final User loginUser = userService.getLoginUser(request);
		long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
		return ResultUtils.success(questionSubmitId);
	}

	/**
	 * 查询提交记录
	 *
	 * @param questionSubmitQueryRequest
	 * @param request
	 */
	@PostMapping("/list/page")
	public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage (@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
	                                                                      HttpServletRequest request) {
		long current = questionSubmitQueryRequest.getCurrent();
		long size = questionSubmitQueryRequest.getPageSize();
		final User loginUser = userService.getLoginUser(request);
		// 查询原始题目提交分页信息
		Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
				questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
		// 脱敏
		return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
	}

}
