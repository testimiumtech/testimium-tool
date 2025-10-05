package com.testimium.tool.custom.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Inherited;
/**
 * @author Sandeep Agrawal
 */
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface TestCaseData {
    String fileName() default "";
    String sheetName() default "";
    boolean isReadAll() default false;
}
