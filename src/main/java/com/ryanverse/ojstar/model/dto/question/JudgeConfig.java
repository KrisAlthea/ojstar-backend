package com.ryanverse.ojstar.model.dto.question;

import lombok.Data;

/**
 * ClassName: JudgeCase
 * Package: com.ryanverse.ojstar.model.dto.question
 * Description:
 *
 * @Author Haoran
 * @Create 2024/7/7 1:29
 * @Version 1.0
 */
@Data
public class JudgeConfig {
	/**
	 * 时间限制(ms)
	 */
	private Long timeLimit;

	/**
	 * 内存限制(kb)
	 */
	private Long memoryLimit;

	/**
	 * 堆栈限制(kb)
	 */
	private Long stackLimit;
}
