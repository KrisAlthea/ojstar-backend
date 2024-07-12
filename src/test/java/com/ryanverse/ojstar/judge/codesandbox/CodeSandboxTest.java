package com.ryanverse.ojstar.judge.codesandbox;

import com.ryanverse.ojstar.judge.codesandbox.impl.ExampleCodeSandboxImpl;
import com.ryanverse.ojstar.judge.codesandbox.model.ExecuteCodeRequest;
import com.ryanverse.ojstar.judge.codesandbox.model.ExecuteCodeResponse;
import com.ryanverse.ojstar.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ClassName: CodeSandboxTest
 * Package: com.ryanverse.ojstar.judge.codesandbox
 * Description:
 *
 * @Author Haoran
 * @Create 2024/7/11 20:02
 * @Version 1.0
 */
@SpringBootTest
class CodeSandboxTest {

	@Value("${codesandbox.type:example}")
	private String codeSandboxType;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()) {
			String type = scanner.next();
			CodeSandbox codeSandbox = CodeSandboxFactory.getCodeSandbox(type);
			String code = "int main() { }";
			String language = QuestionSubmitLanguageEnum.JAVA.getValue();
			List<String> inputList = Arrays.asList("1 2", "3 4");
			ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
					.code(code)
					.language(language)
					.inputList(inputList)
					.build();
			ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
		}
	}

	@Test
	void executeCode () {
		String codeSandboxType = "remote";
		CodeSandbox codeSandbox = CodeSandboxFactory.getCodeSandbox(codeSandboxType);
		String code = "System.out.println(\"Hello World!\");";
		String language = QuestionSubmitLanguageEnum.JAVA.getValue();
		List<String> inputList = Arrays.asList("1 2", "3 4");
		ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
				.code(code)
				.language(language)
				.inputList(inputList)
				.build();
		ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
		assertNotNull(executeCodeResponse);
	}
	@Test
	void executeCodeByValue () {
		CodeSandbox codeSandbox = CodeSandboxFactory.getCodeSandbox(codeSandboxType);
		String code = "System.out.println(\"Hello World!\");";
		String language = QuestionSubmitLanguageEnum.JAVA.getValue();
		List<String> inputList = Arrays.asList("1 2", "3 4");
		ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
				.code(code)
				.language(language)
				.inputList(inputList)
				.build();
		ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
		assertNotNull(executeCodeResponse);
	}
	@Test
	void executeCodeByProxy () {
		CodeSandbox codeSandbox = CodeSandboxFactory.getCodeSandbox(codeSandboxType);
		codeSandbox = new CodeSandboxProxy(codeSandbox);
		String code = "System.out.println(\"Hello World!\");";
		String language = QuestionSubmitLanguageEnum.JAVA.getValue();
		List<String> inputList = Arrays.asList("1 2", "3 4");
		ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
				.code(code)
				.language(language)
				.inputList(inputList)
				.build();
		ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
		assertNotNull(executeCodeResponse);
	}
}