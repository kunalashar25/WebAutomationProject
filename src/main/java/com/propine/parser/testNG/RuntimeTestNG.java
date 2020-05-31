package com.propine.parser.testNG;

import com.propine.parser.constants.PathConstants;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RuntimeTestNG {

	Logger logger = LoggerFactory.getLogger(RuntimeTestNG.class);

	/**
	 * Create TestNG Object
	 *
	 * @return testNG
	 */
	public TestNG create() {

		logger.info("Creating TestNG");

		// creating TestNG object
		TestNG testNG = new TestNG();

		// creating list of suites to add to the testNG
		List<XmlSuite> suiteList = new ArrayList<>();

		// creating new Suite
		XmlSuite suite = new XmlSuite();
		suite.setName("Runtime TestNG");
		suite.setThreadCount(5);
		suite.setParallel(XmlSuite.ParallelMode.METHODS);

		// creating list of tests to add to suite tag
		List<XmlTest> testList = new ArrayList<>();

		// creating new test
		XmlTest test = new XmlTest(suite);
		test.setName("Demo");

		// creating class List to add all the class that are set for execution
		List<XmlClass> classList = new ArrayList<XmlClass>();

		// to get all classes containing testcases
		Reflections reflections = getAllTestcaseClass();
		Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);

		// Iterating on all classes
		for (Class c : allClasses) {

			// creating class
			XmlClass cls = new XmlClass(c);

			// to fetch all methods of a class
			Method[] allMethods = c.getDeclaredMethods();

			// created list to add all the methods from a class that has to be executed.
			List<XmlInclude> includeMethods = new ArrayList<>();

			// iterating on all methods and adding to the class
			for (Method method : allMethods) {

				/*
					Code to filter methods on runtime can be added here.
					You can fetch methods to be executed from an external file and can compare by methods name here.
					If matched then add it to the list. So we can control which method to run.
				 */

				// creating method list to add to each class
				includeMethods.add(new XmlInclude(method.getName()));
			}

			// if method list is not empty then do following
			if (!includeMethods.isEmpty()) {

				// adding all methods to the testNg configuration
				cls.setIncludedMethods(includeMethods);

				// adding class to the class list
				classList.add(cls);
			}
		}

		// adding all classed to test tag
		test.setXmlClasses(classList);

		// adding test to the list
		testList.add(test);

		// adding all tests to the suite
		suite.setTests(testList);

		// adding suite to the list
		suiteList.add(suite);

		logger.info("Generated XML File:\n" + suite.toXml());

		// adding all suites to testNG
		testNG.setXmlSuites(suiteList);

		return testNG;
	}

	/**
	 * It'll scan all the classes placed at below path
	 *
	 * @return Reflection object
	 */
	private static Reflections getAllTestcaseClass() {

		final ConfigurationBuilder config = new ConfigurationBuilder()
				.setScanners(new ResourcesScanner(), new SubTypesScanner(false))
				.setUrls(ClasspathHelper.forPackage(PathConstants.TESTCASE_PACKAGE_PATH))
				.filterInputsBy(new FilterBuilder().includePackage(PathConstants.TESTCASE_PACKAGE_PATH));

		final Reflections reflect = new Reflections(config);

		return reflect;
	}

	/**
	 * Unit Test to verify TestNG XML is generated
	 */
	@Test
	private void unitTest() {
		RuntimeTestNG testNG = new RuntimeTestNG();
		testNG.create();
	}
}