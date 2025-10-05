package com.testimium.tool.action;

import com.testimium.tool.base.webdriver.AbstractWebDriver;
import com.testimium.tool.base.webdriver.ChromeWebDriver;

import java.util.function.Supplier;

public enum WebDriverType {
    CHROME(ChromeWebDriver::new),
    WEBDRIVER_CHROME_DRIVER(ChromeWebDriver::new);

    private Supplier<AbstractWebDriver> driverInstantiator;

    public AbstractWebDriver getInstance() {
        return driverInstantiator.get();
    }

    WebDriverType(Supplier<AbstractWebDriver> driverInstantiator) {
        this.driverInstantiator = driverInstantiator;
    }
}
