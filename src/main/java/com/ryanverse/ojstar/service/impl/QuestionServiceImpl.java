package com.ryanverse.ojstar.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryanverse.ojstar.common.ErrorCode;
import com.ryanverse.ojstar.constant.CommonConstant;
import com.ryanverse.ojstar.exception.BusinessException;
import com.ryanverse.ojstar.exception.ThrowUtils;
import com.ryanverse.ojstar.model.dto.question.QuestionQueryRequest;
import com.ryanverse.ojstar.model.entity.*;
import com.ryanverse.ojstar.model.vo.QuestionVO;
import com.ryanverse.ojstar.model.vo.UserVO;
import com.ryanverse.ojstar.service.QuestionService;
import com.ryanverse.ojstar.mapper.QuestionMapper;
import com.ryanverse.ojstar.service.UserService;
import com.ryanverse.ojstar.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author Haoran
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-07-07 00:56:57
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

	@Resource
	private UserService userService;


	@Override
	public void validQuestion(Question question, boolean add) {
		if (question == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}

		String title = question.getTitle();
		String content = question.getContent();
		String tags = question.getTags();
		String answer = question.getAnswer();
		String judgeCase = question.getJudgeCase();
		String judgeConfig = question.getJudgeConfig();

		// 创建时，参数不能为空
		if (add) {
			ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
		}
		// 有参数则校验
		if (StringUtils.isNotBlank(title) && title.length() > 80) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
		}
		if (StringUtils.isNotBlank(content) && content.length() > 8192) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
		}
		if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
		}
		if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "judgeCase过长");
		}
		if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "judgeConfig过长");
		}
	}

	/**
	 * 获取查询包装类
	 *
	 * @param questionQueryRequest
	 * @return
	 */
	@Override
	public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
		QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
		if (questionQueryRequest == null) {
			return queryWrapper;
		}
		Long id = questionQueryRequest.getId();
		String title = questionQueryRequest.getTitle();
		String content = questionQueryRequest.getContent();
		List<String> tags = questionQueryRequest.getTags();
		String answer = questionQueryRequest.getAnswer();
		Long userId = questionQueryRequest.getUserId();
		String sortField = questionQueryRequest.getSortField();
		String sortOrder = questionQueryRequest.getSortOrder();

		// 拼接查询条件
		queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
		queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
		queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
		if (CollUtil.isNotEmpty(tags)) {
			for (String tag : tags) {
				queryWrapper.like("tags", "\"" + tag + "\"");
			}
		}
		queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
		queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
		queryWrapper.eq("isDelete", false);
		queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
				sortField);
		return queryWrapper;
	}

	@Override
	public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
		QuestionVO questionVO = QuestionVO.objToVo(question);
		// 1. 关联查询用户信息
		Long userId = question.getUserId();
		User user = null;
		if (userId != null && userId > 0) {
			user = userService.getById(userId);
		}
		UserVO userVO = userService.getUserVO(user);
		questionVO.setUserVO(userVO);
		return questionVO;
	}

	@Override
	public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
		List<Question> questionList = questionPage.getRecords();
		Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
		if (CollUtil.isEmpty(questionList)) {
			return questionVOPage;
		}
		// 1. 关联查询用户信息
		Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
		Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
				.collect(Collectors.groupingBy(User::getId));

		// 填充信息
		List<QuestionVO> questionVOList = questionList.stream().map(question -> {
			QuestionVO questionVO = QuestionVO.objToVo(question);
			Long userId = question.getUserId();
			User user = null;
			if (userIdUserListMap.containsKey(userId)) {
				user = userIdUserListMap.get(userId).get(0);
			}
			questionVO.setUserVO(userService.getUserVO(user));
			return questionVO;
		}).collect(Collectors.toList());
		questionVOPage.setRecords(questionVOList);
		return questionVOPage;
	}

}




