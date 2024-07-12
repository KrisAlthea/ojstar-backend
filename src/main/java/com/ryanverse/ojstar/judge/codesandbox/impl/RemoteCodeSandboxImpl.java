package com.ryanverse.ojstar.judge.codesandbox.impl;

import com.ryanverse.ojstar.judge.codesandbox.CodeSandbox;
import com.ryanverse.ojstar.judge.codesandbox.model.ExecuteCodeRequest;
import com.ryanverse.ojstar.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * ClassName: RemoteCodeSandboxImpl
 * Package: com.ryanverse.ojstar.judge.codesandbox.impl
 * Description: 远程代码沙箱(实际调用)
 *
 * @Author Haoran
 * @Create 2024/7/11 19:59
 * @Version 1.0
 */
public class RemoteCodeSandboxImpl implements CodeSandbox {
	@Override
	public ExecuteCodeResponse executeCode (ExecuteCodeRequest executeCodeRequest) {
		System.out.println("RemoteCodeSandboxImpl");
		return null;
	}
}
