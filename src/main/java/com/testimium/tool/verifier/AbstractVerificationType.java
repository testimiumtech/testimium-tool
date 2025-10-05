package com.testimium.tool.verifier;

import com.aventstack.extentreports.Status;
import com.testimium.tool.base.DriverManager;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.AssertParameter;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.InputParameter;
import com.testimium.tool.domain.assertions.Assertions;

import com.testimium.tool.exception.*;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.report.generator.ReportGenerator;
import com.testimium.tool.utility.JsonParserUtility;
import com.testimium.tool.utility.PropertyUtility;
import com.testimium.tool.utility.StringUtility;
import com.testimium.tool.verifier.domain.UiTableDetail;
import com.testimium.tool.verifier.expectedparam.DefaultVerification;
import com.testimium.tool.verifier.expectedparam.ExpectedParamTypeVerification;
import com.testimium.tool.verifier.expectedparam.FileVerification;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Sandeep Agrawal
 */
public abstract class AbstractVerificationType {
    private InputParameter inputParameter;
    private AssertParameter assertParameter;

    /**
     *
     * @param commandParam Input param
     * @throws VerificationException If verification failed
     */
    public abstract void verify(CommandParam commandParam) throws VerificationException;

    /**
     * Method is used to parse the assert params json.
     * @param assetJson Input param
     * @throws JsonParsingException If JSON parsing fails
     * @throws AssertParamNotFoundException If assertion json not found
     */
    @SuppressWarnings("unchecked")
    protected void setAssertParameter(String assetJson)
            throws JsonParsingException, AssertParamNotFoundException {
        if(StringUtils.isNotEmpty(assetJson)) {
            this.assertParameter = new JsonParserUtility<AssertParameter>().parse(assetJson, AssertParameter.class);
        } else
            throw new AssertParamNotFoundException();
    }

    /**
     * Method is used to parse the input params json.
     * @param inputJson input param
     * @throws JsonParsingException If JSON parsing fails
     * @throws InputParamNotFoundException If input json not found
     */
    protected void setInputParameter(String inputJson)
            throws JsonParsingException, InputParamNotFoundException {
        if(StringUtils.isNotEmpty(inputJson)) {
            this.inputParameter = new JsonParserUtility<InputParameter>().parse(inputJson, InputParameter.class);
        } else
            throw new InputParamNotFoundException();
    }

    /**
     *  Method is used to execute database scripts like complex Select query and store procedure
     * @param sqlQueryKey input param
     * @return result from database
     * @throws DBException If database exception
     * @throws DBConnectorException if database connection issue
     */
    protected List<Map<String, Object>> getDatabaseResult(String sqlQueryKey) throws DBException, DBConnectorException {
        if(PropertyUtility.isDBConnectionEnabled()) {
            return TestContext.getTestContext("").getMainDBConnector().executeSQL(prepareSqlQuery(
                    String.valueOf(this.inputParameter.getDbScript().get((StringUtils.isEmpty(sqlQueryKey))? "sqlQuery" : sqlQueryKey))),
                    TestContext.getTestContext("").getMainConnectorKey());
        } else {
            throw new DBException("Database connection is not enabled in tool setting. ");
        }
    }

    /*protected void assertTest(List<Map<String, Object>> actual) {
            this.assertTest(null, actual);
    }*/

    /**
     * Assert method
     * @param uiTableDetail input param
     */
    protected void assertTest(UiTableDetail uiTableDetail) {
        if(null != this.assertParameter.getAssertParams()) {
            this.assertParameter.getAssertParams().entrySet().stream().filter(entry -> null != entry.getValue()).
                    forEach(entry -> {
                        System.out.println("Assertion key " + entry.getKey() + " = " + entry.getValue().toString());
                        processAssertion(uiTableDetail, entry.getValue());
                    });
        }
    }

    /**
     * Assert method
     * @param actual input param
     */
    protected void assertTest(List<Map<String, Object>> actual) {
        if(null != this.assertParameter.getAssertParams()) {
            this.assertParameter.getAssertParams().entrySet().stream().filter(entry -> null != entry.getValue()).
                    forEach(entry -> {
                        LogUtil.logTestCaseMsg("Assertion key = " + entry.getKey());
                        processAssertion(actual, entry.getValue());
                    });
        }
    }

    /**
     * Assert Method
     * @param actual input param
     * @param expectedJsonKey input param
     */
    protected void assertTest(List<Map<String, Object>> actual, String expectedJsonKey) {
        if(null != this.assertParameter.getAssertParams()) {
            LogUtil.logTestCaseMsg("Assertion key = " + expectedJsonKey);
            if(StringUtils.isEmpty(expectedJsonKey)) {
                assertTest(actual);
            } else {
                processAssertion(actual, this.assertParameter.getAssertParams().get(expectedJsonKey));
            }
        } else {
            ReportGenerator.getReportInstance().getChildNodeLevel1().log(Status.INFO, "<br><font color='green'> Expected/Assert parameter (JSON, CSV, Excel) not found </font>");
        }
    }

    /**
     * Process assertion
     * @param actual input param
     * @param assertions input param
     */
    private void processAssertion(List<Map<String, Object>> actual, Assertions assertions) {
        //TODO this switch must be common for HTML Verification as well
        //TODO Maintain Factory for based on type of verification
        ExpectedParamTypeVerification verificationType = null;
        try {
            if("NULL".equals(String.valueOf(assertions.getExpectedParamType()).toUpperCase())) {
                verificationType = new DefaultVerification(assertions, actual);
            } else {
                verificationType = new FileVerification(assertions, actual);
                //throw new RuntimeException("SQLTableVerification : System does not support expectedParamType " + entry.getValue().getExpectedParamType());
            }
            verificationType.verify();
        } catch (VerificationException vex) {
            throw new RuntimeException("VerificationException " + vex.getMessage(), vex);
        } /*catch (DBException dbex) {
                throw new RuntimeException("SQLTableVerification " + dbex.getMessage(), dbex);
            }*/
        //if(null != verificationType)
        //verificationType.verify();
        //TODO If expected param not defined in AssertParam.java json then throw exception
                            /* else
                                throw new VerificationException("Expected parameters not defined");*/
    }

    /**
     * Process assertion
     * @param uiTableDetail input param
     * @param assertions input param
     */
    private void processAssertion(UiTableDetail uiTableDetail, Assertions assertions) {
        //TODO this switch must be common for HTML Verification as well
        //TODO Maintain Factory for based on type of verification
        ExpectedParamTypeVerification verificationType = null;
        try {
            if("NULL".equals(String.valueOf(assertions.getExpectedParamType()).toUpperCase())) {
                verificationType = new DefaultVerification(assertions, uiTableDetail);
            } else {
                verificationType = new FileVerification(assertions, uiTableDetail);
                //throw new RuntimeException("SQLTableVerification : System does not support expectedParamType " + entry.getValue().getExpectedParamType());
            }
            verificationType.verify();
        } catch (VerificationException vex) {
            throw new RuntimeException("VerificationException " + vex.getMessage(), vex);
        } /*catch (DBException dbex) {
                throw new RuntimeException("SQLTableVerification " + dbex.getMessage(), dbex);
            }*/
        //if(null != verificationType)
        //verificationType.verify();
        //TODO If expected param not defined in AssertParam.java json then throw exception
                            /* else
                                throw new VerificationException("Expected parameters not defined");*/
    }

    /**
     * Prepare the sql query based on replacing global and copied variable placeholders
     * @param sqlQuery
     * @return response sql statement
     */
    private String prepareSqlQuery(String sqlQuery) {
        //String sqlQuery = inputParam.getDbScript().get((null == commandParam.getArgs() || commandParam.getArgs().length == 0) ? "sqlQuery" : commandParam.getArgs()[0]);

            List<String> replaceHolders = new StringUtility().getPlaceHoldersBetweenStrings("<%=", "=%>", sqlQuery);

            if (inputParameter.isUseGlobalVariable()) {
                for (int itr = 0; itr < replaceHolders.size(); itr++) {
                    sqlQuery = sqlQuery.replaceAll("<%=" + replaceHolders.get(itr) + "=%>",
                            String.valueOf(TestContext.getTestContext("").getGlobalVariable().get(replaceHolders.get(itr))));
                }
            }

            if (inputParameter.isUseCopiedVariable()) {
                for (int itr = 0; itr < replaceHolders.size(); itr++) {
                    sqlQuery = sqlQuery.replaceAll("<%=" + replaceHolders.get(itr) + "=%>",
                            String.valueOf(TestContext.getTestContext("").getCopiedVariable().get(replaceHolders.get(itr))));
                }
            }

        /*TestContext.getTestContext("").getTest()*/
        ReportGenerator.getReportInstance().getChildNodeLevel1().log(Status.INFO, "<br><font color='green'> SQL Queries: " + sqlQuery + "</font>");
        LogUtil.logTestCaseMsg("SQL Queries: " + sqlQuery);
        return sqlQuery;
    }

    /**
     * Fluent wait for elements
     * @param driver input param
     * @param locator input param
     * @return list of web elements
     */
    protected List<WebElement> fluentWaitForElements(WebDriver driver, By locator) {
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        return fluentWait.until(webDriver -> driver.findElements(locator));
    }

    /**
     * Wait to page load
     * @param driver input param
     */
    protected void waitForPageToLoad(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * find elements by locator
     * @param locator input param
     * @return list of WebElement
     * @throws PropertyKeyNotFoundException
     * @throws LocatorNotFoundException
     */
    protected List<WebElement> findElements(By locator) throws PropertyKeyNotFoundException, LocatorNotFoundException {
        Objects.requireNonNull(locator);
        //Objects.checkIndex(1, requestParam.getArgs().length);
        waitForPageToLoad(DriverManager.getInstance().getWebDriver());
        return fluentWaitForElements(DriverManager.getInstance().getWebDriver(), locator);
    }
}
