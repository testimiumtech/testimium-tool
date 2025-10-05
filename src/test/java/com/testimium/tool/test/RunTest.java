package com.testimium.tool.test;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.collections.Lists;

import java.util.List;

public class RunTest {
    public static void main(String[] args) {
        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();
/*testng.setTestClasses(new Class[] { ExecuteTest.class });
        testng.addListener(tla);
        testng.run();*/

        List<String> suites = Lists.newArrayList();
        suites.add("C:\\Sandeep\\Projects\\testimium\\POC\\Selenium\\codebase\\SmartTestingToolFramework\\sttf\\SmartTestingFramework\\testng.xml");//path to xml..
        //suites.add("c:/tests/testng2.xml");
        testng.setTestSuites(suites);
        testng.run();
    }
}
