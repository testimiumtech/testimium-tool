package com.testimium.tool.base;

import com.testimium.tool.action.WebDriverType;
import com.testimium.tool.base.webdriver.AbstractWebDriver;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.reader.PropertyReader;
import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

//import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * @author Sandeep Agrawal
 */
public class DriverManager {

    private WebDriver driver;
    protected WebDriverWait webDriverWait;
    protected FluentWait fluentWait;
    protected TakesScreenshot takeScreenShot;
    private AbstractWebDriver abstractWebDriver;
    private DriverManager(){}

    private static class LaunchDriverHelper{
        private static final DriverManager INSTANCE = new DriverManager();
    }


    public void loadDriver(){
        String webDriverStr = PropertyReader.getProperty("web.browser.driver");
        if(StringUtils.isEmpty(webDriverStr)) {
            webDriverStr = "WEBDRIVER_CHROME_DRIVER";
        } else {
            webDriverStr = webDriverStr.replace(".", "_").toUpperCase();
        }

        abstractWebDriver = (AbstractWebDriver) WebDriverType.valueOf(WebDriverType.class, webDriverStr).getInstance().loadDriver();
        //this.driver = WebDriverType.valueOf(WebDriverType.class, webDriverStr).getInstance().loadDriver();
        this.driver = abstractWebDriver.getDriver();

        /*try {
            new OpenUrlCmd().execute(null);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }*/
        /*if(StringUtils.isNotEmpty(webDriverStr)) {

           switch (webDriverStr) {
                case "WEBDRIVER_CHROME_DRIVER":
                case "CHROME":
                    this.driver = WebDriverType.valueOf(WebDriverType.class, webDriverStr).getInstance().loadDriver();
                default:
            }
            this.driver = WebDriverType.valueOf(WebDriverType.class, webDriverStr).getInstance().loadDriver();
        } else {
            this.driver = WebDriverType.valueOf(WebDriverType.class,
                    "WEBDRIVER_CHROME_DRIVER").getInstance().loadDriver();
        }*/
        String timezoneId = (String) ((JavascriptExecutor) DriverManager.getInstance().getWebDriver()).executeScript("return  Intl.DateTimeFormat().resolvedOptions().timeZone;");
        if(StringUtils.isEmpty(PropertyUtility.getLocalTimezone())) {
            PropertyReader.setProperty("tool.local.timezone", timezoneId);
            LogUtil.logToolMsg("Local timezone: " + PropertyUtility.getLocalTimezone());
        }

        if(StringUtils.isEmpty(PropertyUtility.getBrowserTimezone())) {
            PropertyReader.setProperty("tool.browser.timezone", timezoneId);
            LogUtil.logToolMsg("Browser timezone: " + PropertyUtility.getBrowserTimezone());
        }

        takeScreenShot = (TakesScreenshot)this.driver;
        //this.webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.fluentWait = new FluentWait(driver);
        this.fluentWait.withTimeout(Duration.ofSeconds(Long.valueOf(PropertyUtility.getLocatorLoadingTimeout())));
        this.fluentWait.pollingEvery(Duration.ofSeconds(1));
        this.fluentWait.ignoring(NoSuchElementException.class);
    }


    public static synchronized DriverManager getInstance(){
        if(null == LaunchDriverHelper.INSTANCE.driver)
            LaunchDriverHelper.INSTANCE.loadDriver();
        return LaunchDriverHelper.INSTANCE;
    }

    public WebDriver getWebDriver() {
        return this.driver;
    }

    /*public WebDriverWait getWebDriverWait() {
        return this.webDriverWait;
    }*/

    public FluentWait getFluentWait() {
        return this.fluentWait;
    }

    public String takeScreenShot(String fileName) throws IOException {
            //Call getScreenshotAs method to create image file
            File srcFile=takeScreenShot.getScreenshotAs(OutputType.FILE);
            //Move image file to new destination
            File destFile=new File(PropertyUtility.getScreenshotLocation() +"//"+fileName+".png");
            //Copy file at destination
            FileUtils.copyFile(srcFile, destFile);
            //return "<img src='file://" + destFile.getAbsolutePath() + "' atl='"+ destFile.getAbsolutePath() +  "'> \n" + destFile.getAbsolutePath();
        return "<img src='../../"  + PropertyUtility.getScreenshotLocation() +"/"+fileName+".png' atl='"+ destFile.getAbsolutePath() +  "' width='800' height='500'> \n" + destFile.getAbsolutePath();
    }

    public String takeScreenShotBase64(String fileName) throws IOException {
        //Call getScreenshotAs method to create image file
        return takeScreenShot.getScreenshotAs(OutputType.BASE64);
    }

    public void closeCurrentBrowser(){
        if(null == LaunchDriverHelper.INSTANCE.driver)
            LaunchDriverHelper.INSTANCE.loadDriver();
        this.driver.close();
    }

    public void shutdownWebDriver(){
        if(null != LaunchDriverHelper.INSTANCE.driver)
            driver.quit();
    }

    public void refereshPage(){
        if(null != LaunchDriverHelper.INSTANCE.driver)
            driver.navigate().refresh();
    }

    //TODO Need to handle NoSuchWindowException at the time of focus on Current window
    public void focusCurrentWindow() /*throws WebDriverException*/ {

        //String currentWindow = this.driver.getWindowHandle();
        // open the new tab here
        for (String handle : this.driver.getWindowHandles()) {
            /*if (!handle.equals(currentWindow)) {
                this.driver.switchTo().window(handle);
            }*/
            for(int i=0;i<=this.driver.getWindowHandles().size();i++){
                if(i==0){
                    System.out.println("Print handle"+handle);
                    continue;
                }
            }
            driver.switchTo().window(handle);
        }
    }

    public void switchTab(int tabIndex) {
        List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        if(tabIndex < tabs.size()) {
            LogUtil.logTestCaseMsg("DriverManager - Switch to browser tab index: " + tabIndex);
            driver.switchTo().window(tabs.get(tabIndex));
        } else {
            LogUtil.logTestCaseErrorMsg("DriverManager - Failed to Switch to browser tab index: " + tabIndex, null);
        }
    }

    public WebElement scrollToElement(String script, WebElement element){
        //((JavascriptExecutor) driver).executeScript("javascript:window.scrollBy("+ element.getLocation().x,element.getLocation().y +")");
       ((JavascriptExecutor) this.driver).executeScript(script, element);
        /*try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        return element;
    }


    public void setBrowserTimezone(String timezoneId){
        abstractWebDriver.setTimezone(timezoneId);
    }

    public void resetBrowserTimezone(){
        abstractWebDriver.resetTimezone();
    }
}