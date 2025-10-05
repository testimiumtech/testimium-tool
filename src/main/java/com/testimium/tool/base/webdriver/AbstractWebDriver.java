package com.testimium.tool.base.webdriver;

import com.testimium.tool.reader.PropertyReader;
import org.openqa.selenium.WebDriver;

/**
 *  Handle web driver
 * @param <T> request type
 * @param <R> Response Type
 */
public abstract class AbstractWebDriver<T, R> {

    /**
     * It a web driver
     */
    protected WebDriver driver;

    /**
     * Load abstract web driver
     * @param options input param
     * @param driverPath input param
     * @return object R
     */
    protected abstract R loadWebDriver(T options, String driverPath);

    /**
     * Abstract load options
     * @return object type T
     */
    protected abstract T loadOptions();

    /**
     *  Set timezone
     * @param timezoneId input param
     */
    public abstract void setTimezone(String timezoneId);

    /**
     * Reset timezone
     */
    public abstract void resetTimezone();

    /**
     * Load web driver
     * @return object type R
     */
    public R loadDriver() {
        return loadWebDriver(loadOptions(), PropertyReader.getProperty("web.browser.driver.path"));
        //return this.driver;
    }

    /**
     * get web driver
     * @return webdriver object
     */
    public WebDriver getDriver() {
        return driver;
    }
}
