package com.ryanverse.ojstar.judge.codesandbox;

import com.ryanverse.ojstar.judge.codesandbox.model.ExecuteCodeRequest;
import com.ryanverse.ojstar.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * ClassName: CodeSandbox
 * Package: com.ryanverse.ojstar.judge.codesandbox
 * Description: 代码沙箱接口
 *
 * @Author Haoran
 * @Create 2024/7/11 19:48
 * @Version 1.0
 */
public interface CodeSandbox {
	ExecuteCodeResponse executeCode (ExecuteCodeRequest executeCodeRequest);
}
