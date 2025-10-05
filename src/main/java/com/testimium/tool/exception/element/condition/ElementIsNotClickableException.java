package com.testimium.tool.exception.element.condition;

import com.testimium.tool.exception.element.ElementConditionException;

public class ElementIsNotClickableException extends ElementConditionException {
    public ElementIsNotClickableException(String locator) {
        super(String.format("Html element '[%s]' is not enable or clickable", locator));
    }
}
