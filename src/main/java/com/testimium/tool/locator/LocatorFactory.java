package com.testimium.tool.locator;

import com.testimium.tool.exception.LocatorNotFoundException;
import com.testimium.tool.exception.PropertyKeyNotFoundException;
import com.testimium.tool.reader.PropertyReader;
import org.openqa.selenium.By;
/**
 * @author Sandeep Agrawal
 */
public class LocatorFactory {
    /**
     * TODO This method has to be implemented in a dynamic way
     * Find element BY propertyKey which should contain both locator and location
     * @param propertyKey input param
     * @return selenium By
     * @throws LocatorNotFoundException if locator not found
     * @throws PropertyKeyNotFoundException if property key not found
     */
    public static By getByLocatorProperty(String propertyKey) throws LocatorNotFoundException, PropertyKeyNotFoundException {
        //TODO handle exception here for reading from property
        if(null == PropertyReader.getProperty(propertyKey)) {
            throw new PropertyKeyNotFoundException(propertyKey);
        }
        String[] object = PropertyReader.getProperty(propertyKey).split(",");

        try {
            return getByLocator(object[0], object[1]);
        } catch (ArrayIndexOutOfBoundsException aio) {
            aio.printStackTrace();
            throw new LocatorNotFoundException(propertyKey);
        }
    }

    public static By getByLocator(String locator, String locatorValue) throws LocatorNotFoundException {
        //Find by xpath
        if(locator.trim().equalsIgnoreCase("XPATH")){
            return By.xpath(locatorValue.replace("$$$",",").trim());
        }
        //find by class
        else if(locator.trim().equalsIgnoreCase("CLASSNAME")){
            return By.className(locatorValue.trim());
        }
        //find by name
        else if(locator.trim().equalsIgnoreCase("NAME")) {
            return By.name(locatorValue.trim());
        }
        //find by id
        else if(locator.trim().equalsIgnoreCase("id")) {
            return By.id(locatorValue.trim());
        }
        //Find by css
        else if(locator.trim().equalsIgnoreCase("CSS")){
            return By.cssSelector(locatorValue.trim());
        }
        //find by link
        else if(locator.trim().equalsIgnoreCase("LINK")){
            return By.linkText(locatorValue.trim());
        }
        //find by partial link
        else if(locator.trim().equalsIgnoreCase("PARTIALLINK")){
            return By.partialLinkText(locatorValue.trim());
        } else  {
            //TODO What if either locator or locator value is not configured in property file
            throw new LocatorNotFoundException(locator.trim());
        }
    }
}
