package com.ryanverse.ojstar.judge.codesandbox.impl;

import com.ryanverse.ojstar.judge.codesandbox.CodeSandbox;
import com.ryanverse.ojstar.judge.codesandbox.model.ExecuteCodeRequest;
import com.ryanverse.ojstar.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * ClassName: ThirdPartyCodeSandboxImpl
 * Package: com.ryanverse.ojstar.judge.codesandbox.impl
 * Description: 第三方代码沙箱
 *
 * @Author Haoran
 * @Create 2024/7/11 20:00
 * @Version 1.0
 */
public class ThirdPartyCodeSandboxImpl implements CodeSandbox {
	@Override
	public ExecuteCodeResponse executeCode (ExecuteCodeRequest executeCodeRequest) {
		System.out.println("ThirdPartyCodeSandboxImpl");
		return null;
	}
}
