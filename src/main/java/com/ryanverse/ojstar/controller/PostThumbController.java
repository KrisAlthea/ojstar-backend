package com.ryanverse.ojstar.controller;

import com.ryanverse.ojstar.common.BaseResponse;
import com.ryanverse.ojstar.common.ErrorCode;
import com.ryanverse.ojstar.common.ResultUtils;
import com.ryanverse.ojstar.exception.BusinessException;
import com.ryanverse.ojstar.model.dto.postthumb.PostThumbAddRequest;
import com.ryanverse.ojstar.model.entity.User;
import com.ryanverse.ojstar.service.PostThumbService;
import com.ryanverse.ojstar.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞接口
 *
 * @author Haoran
 */
@RestController
@RequestMapping("/post_thumb")
@Slf4j
public class PostThumbController {

	@Resource
	private PostThumbService postThumbService;

	@Resource
	private UserService userService;

	/**
	 * 点赞 / 取消点赞
	 *
	 * @param postThumbAddRequest
	 * @param request
	 * @return resultNum 本次点赞变化数
	 */
	@PostMapping("/")
	public BaseResponse<Integer> doThumb (@RequestBody PostThumbAddRequest postThumbAddRequest,
	                                      HttpServletRequest request) {
		if (postThumbAddRequest == null || postThumbAddRequest.getPostId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		// 登录才能点赞
		final User loginUser = userService.getLoginUser(request);
		long postId = postThumbAddRequest.getPostId();
		int result = postThumbService.doPostThumb(postId, loginUser);
		return ResultUtils.success(result);
	}

}
