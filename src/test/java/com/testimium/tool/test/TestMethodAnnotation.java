package com.testimium.tool.test;

import com.testimium.tool.custom.annotation.TestCaseData;

import java.lang.reflect.Method;

public class TestMethodAnnotation {

    @TestCaseData(fileName = "ListScreens.xlsx", sheetName = "Product", isReadAll = false)
    public String testAnnotation() {
        return "null";
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Class<TestMethodAnnotation> obj = TestMethodAnnotation.class;
        for(Method method : obj.getDeclaredMethods()) {
            if(method.isAnnotationPresent(TestCaseData.class)) {
                System.out.println(method.getName() + " : " + method.isAnnotationPresent(TestCaseData.class));
                TestCaseData testData = method.getAnnotation(TestCaseData.class);
                System.out.println(testData.fileName());
                System.out.println(testData.sheetName());
                System.out.println(testData.isReadAll());
            }
        }

        //System.out.println(obj.getDeclaredMethod("testAnnotation", TestCaseData.class).isAnnotationPresent(TestCaseData.class));
        /*Method method = testClass.getClass().getDeclaredMethod(testClass.testAnnotation(), null);
        System.out.println(method.getAnnotation(TestCaseData.class).fileName());*/
    }
}


