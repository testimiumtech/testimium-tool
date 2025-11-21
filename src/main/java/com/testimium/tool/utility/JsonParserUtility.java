package com.testimium.tool.utility;

import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.AssertParameter;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.InputParameter;
import com.testimium.tool.domain.TestJsonInputData;
import com.testimium.tool.exception.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Sandeep Agrawal
 */
public class JsonParserUtility<T> {

    public T parse(String json, Class<T> cls) throws JsonParsingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        T t = null;
        try {
            t = objectMapper.readValue(json, cls);
        } catch (JsonProcessingException ex) {
            throw new JsonParsingException("Not able to parse input json data: " + json, ex);
        }
        return t;
    }

    public static AssertParameter getAssertParameter()
            throws AssertParamNotFoundException, AssertParamParsingException {

        if(StringUtils.isNotEmpty(TestContext.getTestContext("").getTestExpectedJson())) {
            try {
                return new JsonParserUtility<AssertParameter>()
                        .parse(TestContext.getTestContext("").getTestExpectedJson(), AssertParameter.class);
            } catch (JsonParsingException e) {
                throw new AssertParamParsingException(e);
            }
        } else
            throw new AssertParamNotFoundException();
    }

    public static InputParameter getInputParam() throws InputParamNotFoundException, InputParamParsingException {
        if(StringUtils.isNotEmpty(TestContext.getTestContext("").getTestInputJson())) {
            try {
                return new JsonParserUtility<InputParameter>()
                        .parse(TestContext.getTestContext("").getTestInputJson(), InputParameter.class);
            } catch (JsonParsingException e) {
                throw new InputParamParsingException(e);
            }
        } else
            throw new InputParamNotFoundException();
    }

}
