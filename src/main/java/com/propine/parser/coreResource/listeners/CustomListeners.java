package com.propine.parser.coreResource.listeners;

import com.propine.parser.coreResource.directoryManager.Directory;
import lombok.NonNull;
import org.apache.commons.collections4.MapUtils;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.internal.IResultListener2;

import java.util.LinkedHashMap;
import java.util.Map;

// not creating abstract class as TestNG won't be able to initiate it.
public class CustomListeners implements IResultListener2 {

	Logger logger = Logger.getLogger(CustomListeners.class);

	public static Map<String, Integer> testcaseInvocationCount = new LinkedHashMap<>();

	@Override
	public void onStart(ITestContext context) {
		logger.info("======= Starting TestCase Execution =======");
	}

	@Override
	public void onFinish(ITestContext context) {
		logger.info("======= TestCase Execution Completed =======");
		logger.info("Total Invocation Count per Testcase:: " + testcaseInvocationCount);
	}

	@Override
	public void onTestStart(ITestResult result) {
		logger.info("Executing TestCase:: " + result.getName());

		// update invocation count to the map
		updateInvocationCount(result.getName());

		// create testcase directory
		createTestDirectory(result.getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		logger.info("TestCase:: " + result.getName() + " Executed Successfully!");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		logger.info("TestCase:: " + result.getName() + " Execution Failed!");
	}

	@Override
	public void onTestSkipped(ITestResult result) {

	}

	@Override
	public void beforeConfiguration(ITestResult tr) {

	}

	@Override
	public void onConfigurationSuccess(ITestResult itr) {

	}

	@Override
	public void onConfigurationFailure(ITestResult itr) {

	}

	@Override
	public void onConfigurationSkip(ITestResult itr) {

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	/**
	 * Updating invocation count in map to keep track of how many times testcases was executed
	 *
	 * @param testMethodName testcase name
	 */
	private void updateInvocationCount(@NonNull String testMethodName) {
		if (MapUtils.isNotEmpty(testcaseInvocationCount)) {
			if (testcaseInvocationCount.get(testMethodName) == null) {
				testcaseInvocationCount.put(testMethodName, 1);
			} else {
				int count = testcaseInvocationCount.get(testMethodName);
				testcaseInvocationCount.put(testMethodName, count + 1);
			}
		} else {
			testcaseInvocationCount.put(testMethodName, 1);
		}
	}

	/**
	 * Creating directory in output folder to store step screenshot
	 *
	 * @param testMethodName testcase name
	 */
	private void createTestDirectory(String testMethodName) {
		int i = testcaseInvocationCount.get(testMethodName);

		String directoryName = testMethodName + "_" + i;
		Directory.createTestCaseDirectory(directoryName);
	}
}
