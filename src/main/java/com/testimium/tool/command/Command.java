package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.command.verification.AssertMethods;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.IRequest;
import com.testimium.tool.domain.TestJsonInputData;

import com.testimium.tool.exception.*;
import com.testimium.tool.exception.element.ElementConditionException;
import com.testimium.tool.exception.element.condition.ElementIsNotClickableException;
import com.testimium.tool.executor.ActionExecutor;
import com.testimium.tool.locator.LocatorFactory;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.utility.ImagePath;
import com.testimium.tool.utility.ImageUtil;
import com.testimium.tool.utility.JsonParserUtility;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author Sandeep Agrawal
 */
@FunctionalInterface
public interface Command<T, R> extends AssertMethods {

    R execute(T t) throws CommandException, VerificationException, TestException, ShutdownTestExecution, HandleFailOverTestExecution, HttpTimeoutException, RecoverBrokenTestExecutionException;
    //R validate(T t) throws CommandException, VerificationException;

    //TODO to
    default R executeCmd(T t, String command) throws CommandException, VerificationException, TestException, ShutdownTestExecution, HandleFailOverTestExecution, HttpTimeoutException, RecoverBrokenTestExecutionException {
        //CommandParam param = (CommandParam)t;
        /*switch (command.toUpperCase()){
            case "CLICK":
                AbstractCommandValidator validator = new ValidateCommandWithLocator();
                validator.validate(t);
                break;

        }*/
        //this.validate(t);
        //DriverManager.getInstance().focusCurrentWindow();
        //TestContext.getTestContext("").setCurrentUrl(DriverManager.getInstance().getWebDriver().getCurrentUrl());
        //wait((CommandParam)t);
        return this.execute(t);
    }

    /**
     *
     * @param requestParam
     * @return
     * @throws PropertyKeyNotFoundException if property key not found
     * @throws LocatorNotFoundException if locator not found
     */
    default WebElement wait(IRequest requestParam) throws PropertyKeyNotFoundException, LocatorNotFoundException {
        Objects.requireNonNull(requestParam.getArgs());
        TestContext.getTestContext("").setCurrentUrl(DriverManager.getInstance().getWebDriver().getCurrentUrl());
        //Objects.checkIndex(1, requestParam.getArgs().length);
        DriverManager.getInstance().getFluentWait().until(
                ExpectedConditions.visibilityOfElementLocated(
                        LocatorFactory.getByLocatorProperty(String.valueOf(requestParam.getArgs()[0]))));
        return DriverManager.getInstance().getWebDriver().findElement(LocatorFactory.getByLocatorProperty(requestParam.getArgs()[0]));
        //return waitForElementVisibility(DriverManager.getInstance().getWebDriver(), LocatorFactory.getByLocatorProperty(String.valueOf(requestParam.getArgs()[0])));
    }

    default WebElement wait(String locator) throws PropertyKeyNotFoundException, LocatorNotFoundException {
        Objects.requireNonNull(locator);
        TestContext.getTestContext("").setCurrentUrl(DriverManager.getInstance().getWebDriver().getCurrentUrl());
        DriverManager.getInstance().getFluentWait().until(
                ExpectedConditions.visibilityOfElementLocated(
                        LocatorFactory.getByLocatorProperty(locator)));
        return DriverManager.getInstance().getWebDriver().findElement(LocatorFactory.getByLocatorProperty(locator));
        //return waitForElementVisibility(DriverManager.getInstance().getWebDriver(), LocatorFactory.getByLocatorProperty(locator));
    }

    default WebElement wait(By locator) throws PropertyKeyNotFoundException, LocatorNotFoundException {
        Objects.requireNonNull(locator);
        TestContext.getTestContext("").setCurrentUrl(DriverManager.getInstance().getWebDriver().getCurrentUrl());
        DriverManager.getInstance().getFluentWait().until(
                ExpectedConditions.visibilityOfElementLocated(locator));
        return DriverManager.getInstance().getWebDriver().findElement(locator);
        //return waitForElementVisibility(DriverManager.getInstance().getWebDriver(), locator);
    }

    default WebElement findElement(IRequest requestParam) throws PropertyKeyNotFoundException, LocatorNotFoundException, ElementConditionException {
        Objects.requireNonNull(requestParam.getArgs());
        //Objects.checkIndex(1, requestParam.getArgs().length);
        waitForPageToLoad(DriverManager.getInstance().getWebDriver(), 30);
        DriverManager.getInstance().focusCurrentWindow();
        //waitForElement(DriverManager.getInstance().getWebDriver(), LocatorFactory.getByLocatorProperty(String.valueOf(requestParam.getArgs()[0])));
        //waitForElementVisibility(DriverManager.getInstance().getWebDriver(), LocatorFactory.getByLocatorProperty(String.valueOf(requestParam.getArgs()[0])));
        //return fluentWaitForElement(DriverManager.getInstance().getWebDriver(), LocatorFactory.getByLocatorProperty(String.valueOf(requestParam.getArgs()[0])));
        /*return DriverManager.getInstance().getFluentWait().until(
                ExpectedConditions.visibilityOfElementLocated(
                        LocatorFactory.getByLocatorProperty(String.valueOf(requestParam.getArgs()[0]))));*/
        WebElement element = null;
        //waitForElementVisibility(DriverManager.getInstance().getWebDriver(), LocatorFactory.getByLocatorProperty(String.valueOf(requestParam.getArgs()[0])));

        switch (getCommandWithIgnoreCondition(requestParam.getCommand(), requestParam.getArgs()).toUpperCase()){
            case"CLICK":
            case"DOUBLECLICK":
                element = waitForElementClickable(DriverManager.getInstance().getWebDriver(), LocatorFactory.getByLocatorProperty(String.valueOf(requestParam.getArgs()[0])));
                break;
            default:
                //Command with ignore condition like clickable will be only process the default case
                element = waitForElementVisibility(DriverManager.getInstance().getWebDriver(), LocatorFactory.getByLocatorProperty(String.valueOf(requestParam.getArgs()[0])));

        }
        return element;
    }

    default List<WebElement> findElements(IRequest requestParam) throws PropertyKeyNotFoundException, LocatorNotFoundException {
        Objects.requireNonNull(requestParam.getArgs());
        //Objects.checkIndex(1, requestParam.getArgs().length);
        waitForPageToLoad(DriverManager.getInstance().getWebDriver(), 30);
        //waitForElement(fluentWaitForElementsDriverManager.getInstance().getWebDriver(), LocatorFactory.getByLocatorProperty(String.valueOf(requestParam.getArgs()[0])));
        return fluentWaitForElements(DriverManager.getInstance().getWebDriver(), LocatorFactory.getByLocatorProperty(String.valueOf(requestParam.getArgs()[0])));
        /*DriverManager.getInstance().getFluentWait().until(
                ExpectedConditions.visibilityOfElementLocated(
                        LocatorFactory.getByLocatorProperty(String.valueOf(requestParam.getArgs()[0]))));*/
    }

    default WebElement findElement(By locator) throws PropertyKeyNotFoundException, LocatorNotFoundException {
        Objects.requireNonNull(locator);
        //Objects.checkIndex(1, requestParam.getArgs().length);
        waitForPageToLoad(DriverManager.getInstance().getWebDriver(), 30);
        DriverManager.getInstance().focusCurrentWindow();
        //return fluentWaitForElement(DriverManager.getInstance().getWebDriver(), locator);
        return waitForElementVisibility(DriverManager.getInstance().getWebDriver(), locator);
    }

    default List<WebElement> findElements(By locator) throws PropertyKeyNotFoundException, LocatorNotFoundException {
        Objects.requireNonNull(locator);
        //Objects.checkIndex(1, requestParam.getArgs().length);
        waitForPageToLoad(DriverManager.getInstance().getWebDriver(), 30);
        return fluentWaitForElements(DriverManager.getInstance().getWebDriver(), locator);
        //return waitForElementVisibility(DriverManager.getInstance().getWebDriver(), locator);
    }

    default List<WebElement> getWebElements(String propertyKey) throws PropertyKeyNotFoundException, LocatorNotFoundException {
        return DriverManager.getInstance().getWebDriver().findElements(LocatorFactory.getByLocatorProperty(propertyKey));
    }
    /**
     *
     * @param propertyKey input param
     * @throws PropertyKeyNotFoundException if property key not found
     * @throws LocatorNotFoundException if locator not found
     */
    default void scrollToElement(String propertyKey) throws PropertyKeyNotFoundException, LocatorNotFoundException {
        ((JavascriptExecutor) DriverManager.getInstance().getWebDriver()).executeScript("arguments[0].scrollIntoView(true);",
                DriverManager.getInstance().getWebDriver().findElement(LocatorFactory.getByLocatorProperty(propertyKey)));
    }

    default void moveToElement(String propertyKey) throws PropertyKeyNotFoundException, LocatorNotFoundException {
        WebDriver webDriver = DriverManager.getInstance().getWebDriver();
        new Actions(webDriver).moveToElement(
                webDriver.findElement(
                        LocatorFactory.getByLocatorProperty(propertyKey))
        ).build().perform();
    }

    /**
     *
     * @param param
     * @throws CommandException
     */
    default void handleFailOver(CommandParam param) throws CommandException, ShutdownTestExecution, TestException, HandleFailOverTestExecution, RecoverBrokenTestExecutionException {
        processMultiTestSteps(param, param.getArgs()[0]);
    }

    /**
     * Process multiple test steps
     * @param param input param
     * @throws CommandException if command exception
     * @throws ShutdownTestExecution If any unwanted exception
     * @throws TestException if test fails
     * @throws HandleFailOverTestExecution If not able to handle fail over
     * @throws RecoverBrokenTestExecutionException If not able to recover test
     */
    default void processMultiTestSteps(CommandParam param) throws CommandException, ShutdownTestExecution, TestException, HandleFailOverTestExecution, RecoverBrokenTestExecutionException {
        processMultiTestSteps(param, null);
    }

    /**
     * Execute Javascript
     * @param script input param
     * @param args input param
     * @return object
     */
    default Object executeJavaScript(String script, Object... args) {
        return ((JavascriptExecutor) DriverManager.getInstance().getWebDriver()).executeScript(script, args);
    }

    default Object executeJavaScript(String script) {
        return ((JavascriptExecutor) DriverManager.getInstance().getWebDriver()).executeScript(script);
    }

    /**
     *
     * @param param input param
     * @return response TestJsonInputData
     * @throws CommandException if any exception
     */
    default TestJsonInputData getTestJsonInputData(CommandParam param) throws CommandException {
        if(StringUtils.isEmpty(param.getInputParam()) || "".equalsIgnoreCase(param.getInputParam().trim()))
            throw new CommandException("input json data not found for operation "+ param.getArgs()[0]);
        TestJsonInputData inputData = null;
        try {
            inputData = new JsonParserUtility<TestJsonInputData>().parse(param.getInputParam(), TestJsonInputData.class);
            System.out.println(inputData.toString());
        } catch (JsonParsingException ex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with param '" + param.toString()+ "'. " + ex.getMessage(), ex);
        }
        return inputData;
    }
    /* *//**
     *
     * @param param
     * @throws CommandException
     *//*
    private void processMultiTestSteps(CommandParam param, String failOver) throws CommandException {

        getTestJsonInputData(param).getFormElements().entrySet().stream()
                .filter(key -> key.getKey().equalsIgnoreCase(null == failOver ? param.getArgs()[0] : failOver))
                .forEach(map -> {
                    System.out.println("value: " + map.getValue());
                    map.getValue().stream().forEach(innerM -> {
                        innerM.entrySet().stream().forEach(innerMap -> {
                            if (innerMap.getKey().equalsIgnoreCase("TestStep")) {
                                System.out.println("value1: " + innerMap.getValue());
                                String[] operationStep = innerMap.getValue().trim().split("\\s+");
                                String[] setParams = new String[operationStep.length - 1];
                                for (int i = 1; i < operationStep.length; i++) {
                                    setParams[i - 1] = operationStep[i];
                                }
                                try {
                                    new ActionExecutor().execute(new CommandParam(param.getTestCaseName(), operationStep[0].trim(), setParams, param.getInputParam(), param.getAssertParam()));
                                } catch (ShutdownTestExecution | HandleFailOverTestExecution shutdownTestExecution) {
                                    //TODO FIX ME
                                    System.out.println("Class Command.processMultiTestSteps() ");
                                    shutdownTestExecution.printStackTrace();
                                }
                            }
                        });
                    });
                });
    }*/

    /**
     * Process Multi test steps
     * @param param input param
     * @throws CommandException if command exception
     * @throws ShutdownTestExecution If any unwanted exception
     * @throws TestException if test fails
     * @throws HandleFailOverTestExecution If not able to handle fail over
     * @throws RecoverBrokenTestExecutionException If not able to recover test
     */
    private void processMultiTestSteps(CommandParam param, String failOver) throws CommandException, ShutdownTestExecution, TestException, HandleFailOverTestExecution, RecoverBrokenTestExecutionException {
        StringBuilder builder = new StringBuilder();
        boolean isIfBlockStart = false;
        for (Map.Entry<String, List<Map<String, String>>> entry : getTestJsonInputData(param).getFormElements().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(null == failOver ? param.getArgs()[0] : failOver)) {
                List<Map<String, String>> listOfMapValue = entry.getValue();
                System.out.println("value: " + listOfMapValue);
                for (int itr = 0; itr < listOfMapValue.size(); itr++) {
                    for (Map.Entry<String, String> innerEntryMap : listOfMapValue.get(itr).entrySet()) {
                        if (innerEntryMap.getKey().equalsIgnoreCase("TestStep")) {
                            isIfBlockStart = addTestSteps(builder, innerEntryMap, isIfBlockStart);
                            if (isIfBlockStart == false) {
                                if (builder.length() > 0) {
                                    builder.delete(0, builder.length());
                                }
                                System.out.println("value1: " + innerEntryMap.getValue());
                                String[] operationStep = innerEntryMap.getValue().trim().split("\\s+");
                                String[] setParams = new String[operationStep.length - 1];
                                for (int i = 1; i < operationStep.length; i++) {
                                    setParams[i - 1] = operationStep[i];
                                }
                                CommandParam newParam = new CommandParam(
                                        param.getTestCaseName(),
                                        operationStep[0].trim(),
                                        setParams,
                                        param.getInputParam(),
                                        param.getAssertParam()
                                );
                                newParam.setNestedNodeEnabled(param.isNestedNodeEnabled());
                                new ActionExecutor().execute(newParam);
                            }
                        }
                    }
                }
            }
        }
    }
   /* private void processMultiTestSteps(CommandParam param, String failOver) throws CommandException, ShutdownTestExecution {
        try {
            StringBuilder builder = new StringBuilder();
            AtomicBoolean isNestedNodeEnabled = new AtomicBoolean(param.isNestedNodeEnabled());
            AtomicBoolean isIfBlockStart = new AtomicBoolean(false);
            getTestJsonInputData(param).getFormElements().entrySet().stream()
                    .filter(key -> key.getKey().equalsIgnoreCase(null == failOver ? param.getArgs()[0] : failOver))
                    .forEach(map -> {
                        System.out.println("value: " + map.getValue());
                        map.getValue().stream().forEach(innerM -> {
                            innerM.entrySet().stream().forEach(innerMap -> {
                                if (innerMap.getKey().equalsIgnoreCase("TestStep")) {
                                    isIfBlockStart.set(addTestSteps(builder, innerMap, isIfBlockStart.get()));
                                    if (isIfBlockStart.get() == false) {
                                        if (builder.length() > 0) {
                                            builder.delete(0, builder.length());
                                        }
                                        System.out.println("value1: " + innerMap.getValue());
                                        String[] operationStep = innerMap.getValue().trim().split("\\s+");
                                        String[] setParams = new String[operationStep.length - 1];
                                        for (int i = 1; i < operationStep.length; i++) {
                                            setParams[i - 1] = operationStep[i];
                                        }
                                        try {
                                            CommandParam newParam = new CommandParam(
                                                    param.getTestCaseName(),
                                                    operationStep[0].trim(),
                                                    setParams,
                                                    param.getInputParam(),
                                                    param.getAssertParam()
                                            );
                                            newParam.setNestedNodeEnabled(isNestedNodeEnabled.get());
                                            new ActionExecutor().execute(newParam);
                                        } catch (ShutdownTestExecution | HandleFailOverTestExecution |
                                                 TestException shutdownTestExecution) {
                                            //TODO FIX ME
                                            System.out.println("Class Command.processMultiTestSteps() ");
                                            shutdownTestExecution.printStackTrace();
                                            throw new RuntimeException(shutdownTestExecution);
                                        }
                                    }
                                }
                            });
                        });
                    });
        } catch (RuntimeException rex){
            if(rex.getMessage().contains("Stoping the test execution because of the exception:"))
            throw  new ShutdownTestExecution("");
        }
    }*/

    private boolean addTestSteps(StringBuilder builder, Map.Entry<String, String> testStepStr, boolean isIfBlockStart) {
        if (StringUtils.isNotEmpty(testStepStr.getValue())) {
            //builder = new StringBuilder();
            //this.testSteps = new ArrayList<>();
            //boolean isIfBlockEnd = false;
            //Start IF
            if (isIfBlockStart) {
                if (testStepStr.getValue().startsWith("ENDIF")) {
                    builder.append(testStepStr.getValue());
                    isIfBlockStart = false;
                    //isIfBlockEnd = true;
                    testStepStr.setValue(builder.toString());
                    testStepStr.setValue(testStepStr.getValue().replaceFirst("#@#", " "));
                    System.out.println(testStepStr);
                } else {
                    //builder.append("@@");
                    builder.append(testStepStr.getValue().replaceAll("\\s+", "#@#"));
                    builder.append("#@#");
                }
            } else if (testStepStr.getValue().startsWith("IF")) {
                isIfBlockStart = true;
                //builder.append(testStepStr.replaceFirst("IF", "IfCondition").replaceAll("\\s+", "#@#"));
                builder.append(testStepStr.getValue().replaceAll("\\s+", "#@#"));
                builder.append("#@#");
            }
        }

        return isIfBlockStart;
    }

    /*default boolean assertElement(String actual, String expected) throws VerificationException {
        StringBuilder htmlBuilder = new StringBuilder();
        String actualStr = null;
        String expectedStr = null;
        boolean isPass = true;
        htmlBuilder.append("<HTML><BODY>" +
                "<TABLE border=1>" +
                "<TR align='center'>" +
                "<TH>#Row</TH><TH>Actual</TH><TH></TH><TH>Expected</TH></TR>" +
                "</TR><TR>");
        boolean isActualEmpty = StringUtils.isEmpty(actual);
        boolean isExpectedEmpty = StringUtils.isEmpty(expected);

        if(isActualEmpty && !isExpectedEmpty) {
            isPass = false;
            actualStr = "Value is Null/Empty";
            expectedStr = expected;
        } else if(isExpectedEmpty && !isActualEmpty) {
            isPass = false;
            actualStr = actual;
            expectedStr = "Value is Null/Empty";
        } else if(!actual.equals(expected)) {
            isPass = false;
            actualStr = actual;
            expectedStr = expected;
        }

        if(!isPass){
            htmlBuilder.append("<TD>" + 1 + "</TD>");
            htmlBuilder.append("<TD>" + actualStr + "</TD>");
            htmlBuilder.append("<TD><img src='images/symbols/not-equal-sign.png' width='42' height='42' style='vertical-align:middle'></TD>");
            htmlBuilder.append("<TD>" + expectedStr + "</TD>");
            htmlBuilder.append("</TR>");
            htmlBuilder.append("</TABLE></BODY></HTML>");
            throw new VerificationException("<br>Verification failed - Found differences between actual and expected record : <br>" + htmlBuilder.toString());
        }
        return isPass;
    }*/

    default void closeWindowsPrintPopup() {
        try {
            /*Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_P);
            robot.getAutoDelay();
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_P);*/
            Screen screen = new Screen();
            Pattern componentImage1 = ImageUtil.matchImagePattern(screen, ImagePath.getToolImagePath() + "WindowsPrintPopup/CancelBtn.png");
            //Pattern componentImage1 = new Pattern(FileUtility.getAbsolutePath("images/WindowsPrintPopup/CancelBtn.png"));
            //Pattern componentImage2 = new Pattern(FileUtility.getAbsolutePath("images/WindowsPrintPopup/WindowsPrintPopupBtns.png"));
            screen.wait(componentImage1, 2);
            screen.click(componentImage1);
        } catch (FindFailed ff) {
            LogUtil.logTestCaseMsg("Command.closeWindowsPrintPopup not found");
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.logTestCaseErrorMsg("Command.closeWindowsPrintPopup() ", ex);
        }
    }

    default void waitForPageToLoad(WebDriver driver, long seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }

    default WebElement waitForElementVisibility(WebDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        //WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    default WebElement waitForElementClickable(WebDriver driver, By locator) throws ElementConditionException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        if(null == element){
            throw new ElementIsNotClickableException(locator.toString());
        }
        return element;
    }
    default WebElement fluentWaitForElement(WebDriver driver, By locator) {
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        WebElement element = fluentWait.until(webDriver -> driver.findElement(locator));
        return element;
    }

    default List<WebElement> fluentWaitForElements(WebDriver driver, By locator) {
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        return fluentWait.until(webDriver -> driver.findElements(locator));
    }

    default Alert waitForAlert(WebDriver driver, long second) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        //WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        return alert;
    }

    /**
     * This method will return the combined command and ignore condition
     * @param opration
     * @param params
     * @return
     */
    private String getCommandWithIgnoreCondition(String opration, String[] params) {
        //String paramStr = "";
        if(params.length == 0) {
            //return paramStr = opration;
            return opration;
        }
        for (String param : params) {
            switch(param.toUpperCase()){
                case "-AVOIDCLICKABLECHECK":
                    opration = opration + param;
                    break;
            }
        }
        return opration;
    }
}
