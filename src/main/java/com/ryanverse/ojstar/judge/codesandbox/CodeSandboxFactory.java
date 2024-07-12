package com.ryanverse.ojstar.judge.codesandbox;

import com.ryanverse.ojstar.judge.codesandbox.impl.ExampleCodeSandboxImpl;
import com.ryanverse.ojstar.judge.codesandbox.impl.RemoteCodeSandboxImpl;
import com.ryanverse.ojstar.judge.codesandbox.impl.ThirdPartyCodeSandboxImpl;

/**
 * ClassName: CodeSandboxFactory
 * Package: com.ryanverse.ojstar.judge.codesandbox
 * Description: 代码沙箱工厂(静态)
 *
 * @Author Haoran
 * @Create 2024/7/11 20:10
 * @Version 1.0
 */
public class CodeSandboxFactory {
	/**
	 * 获取代码沙箱实例
	 *
	 * @param codeSandboxType 代码沙箱类型
	 *                        example: 示例代码沙箱
	 *                        remote: 远程代码沙箱
	 *                        thirdParty: 第三方代码沙箱
	 * @return 代码沙箱实例
	 */
	public static CodeSandbox getCodeSandbox (String codeSandboxType) {
		switch (codeSandboxType) {
			case "example":
				return new ExampleCodeSandboxImpl();
			case "remote":
				return new RemoteCodeSandboxImpl();
			case "thirdParty":
				return new ThirdPartyCodeSandboxImpl();
			default:
				// 返回默认类型
				return new ExampleCodeSandboxImpl();
//				throw new IllegalArgumentException("Invalid codeSandboxType: " + codeSandboxType);
		}
	}

}
