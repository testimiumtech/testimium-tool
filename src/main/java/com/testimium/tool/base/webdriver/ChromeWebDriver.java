package com.testimium.tool.base.webdriver;

import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.utility.OSValidator;
import com.testimium.tool.utility.PropertyUtility;
//import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v129.emulation.Emulation;
//import org.openqa.selenium.devtools.v129.emulation.Emulation;
//import org.openqa.selenium.devtools.v126.emulation.Emulation;
//import org.openqa.selenium.devtools.v125.emulation.Emulation;
//import org.openqa.selenium.devtools.v122.emulation.Emulation;
//import org.openqa.selenium.devtools.v115.emulation.Emulation;
//import org.openqa.selenium.devtools.v110.emulation.Emulation;


import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 *  Chrome Web Driver Class
 */
public class ChromeWebDriver extends AbstractWebDriver<ChromeOptions, ChromeWebDriver> {

    private DevTools devTools;

    /**
     *  Chrome Web Driver Class constructor
     */
    public ChromeWebDriver() {
    }

    @Override
    protected ChromeWebDriver loadWebDriver(ChromeOptions chromeOptions, String driverPath) {
        if(StringUtils.isEmpty(driverPath)) {
            //WebDriverManager.chromedriver().setup();
        } else {
            System.setProperty("webdriver.chrome.driver", driverPath);
        }

        ChromeDriver chromeDriver = new ChromeDriver(chromeOptions);
        //this.driver = new ChromeDriver((ChromeOptions)chromeOptions);
        chromeDriver.manage().window().maximize();
        chromeDriver.manage().deleteAllCookies();
        //TODO load implicit nd explicit wait time from external configuration
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        //implicitlyWait(10, TimeUnit.SECONDS);
        //DevTools devTools = ((ChromeOptions)this.driver).get

        //Set Timezone
        //TODO check if separate commands can set the timezone in between and disconnect the devtool session
        if(StringUtils.isNotEmpty(PropertyUtility.getBrowserTimezone())) {
            /*Map<String, Object> parameters = new HashMap<>();
            parameters.put("timezoneId", PropertyUtility.getBrowserTimezone());
            chromeDriver.executeCdpCommand("Emulation.setTimezoneOverride", parameters);*/
            //TODO check if separate commands can set the timezone in between and disconnect the devtool session
            /*DevTools devTools = chromeDriver.getDevTools();
            devTools.createSession();
            //devTools.send(Emulation.setTimezoneOverride("America/New_York"));
            devTools.send(Emulation.setTimezoneOverride(PropertyUtility.getBrowserTimezone()));*/
        }

       /* //TODO check if separate commands can set the timezone in between and disconnect the devtool session
         DevTools devTools = chromeDriver.getDevTools();
         devTools.createSession();
         devTools.send(Emulation.setTimezoneOverride("America/New_York"));*/

        //END
        this.driver = chromeDriver;
        return this;
    }

    @Override
    protected ChromeOptions loadOptions() {
        Map<String, Object> prefs = new HashMap<String, Object>();
        //prefs.put("profile.default_content_settings.popups", 0);
        //prefs.put("browser.download.folderlist", 0);
        //prefs.put("download.default_directory",  "C:/Users/globa/Downloads");
        //prefs.put("browser.helperApps.neverAsk.open","application/pdf");
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);


        ChromeOptions options = new ChromeOptions();
        //Start
        //If you are using Chrome version 115 or newer, please consult the Chrome for Testing availability dashboard. This page provides convenient JSON endpoints for specific ChromeDriver version downloading.
        //For versions 115 and newer
        String chromeBrowserExePath = PropertyUtility.getChromeBrowserExePath();
        if(null != chromeBrowserExePath)
            options.setBinary(chromeBrowserExePath);
        //End

        //Start Solution for
        //Below option is added to resolve the issue:
        //Unable to establish websocket connection to http://localhost:54609/devtools/browser/9b305c14-d48a-4cac-b588-ee479c235e57
        //Build info: version: '4.1.2', revision: '9a5a329c5a'
        //System info: host: 'DESKTOP-QICKKGL', ip: '192.168.18.100', os.name: 'Windows 10', os.arch: 'amd64', os.version: '10.0', java.version: '11.0.14.1'
        //Driver info: driver.version: AbstractWebDriver
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        // Add the argument for incognito mode
        //options.addArguments("--incognito");
        //options.setCapability("credentials_enable_service", false);
        /*options.addArguments("--headless"); // Required for PDF printing
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");*/
        options.addArguments("--disable-credentials_enable_service");
        options.addArguments("--disable-profile.password_manager_enabled");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-blink-features=AutomationControlled");
        //Added because ctrl+ P giving 'Print Preview Failed' error in print window
        options.addArguments("--disable-features=PrintCompositorLPAC");
        //options.addArguments("--window-size=1366, 768");
        //options.add_experimental_option("excludeSwitches", ["enable-automation"])
        //options.add_experimental_option('useAutomationExtension', False)
        //END Solution

        //Start Solution for to override browser timezone
        //DesiredCapabilities caps = new DesiredCapabilities();
        //options.setCapability("browserstack.timezone", "New_York");
        //END Solution
        if(!OSValidator.isWindows()) {
            //options.setHeadless(true);
            //options.addArguments("--headless");
            options.addArguments("--window-size=1440, 900");
            options.addArguments("--no-sandbox");
            //options.addArguments("--disable-dev-shm-usage");
            //options.setExperimentalOption("useAutomationExtension", false);
        }
        options.setExperimentalOption("prefs", prefs);
        return options;
    }

    @Override
    public void setTimezone(String timezoneId) {
        if(StringUtils.isEmpty(timezoneId))
            LogUtil.logTestCaseErrorMsg("timezoneId is empty", null);

        this.devTools = ((ChromeDriver)this.getDriver()).getDevTools();
        //this.devTools.createSession();
        //devTools.send(Emulation.setTimezoneOverride("America/New_York"));
        this.devTools.send(Emulation.setTimezoneOverride(timezoneId));
    }

    @Override
    public void resetTimezone() {
        this.devTools = ((ChromeDriver)this.getDriver()).getDevTools();
        //this.devTools.disconnectSession();
        //this.devTools.createSession();
        this.devTools.send(Emulation.setTimezoneOverride(PropertyUtility.getLocalTimezone()));
    }
}
