package com.testimium.tool.sample;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GetMapValueType {
    private Map<String, Number> map = new HashMap<String, Number>();

    public static void main(String[] args) {
        try {
            ParameterizedType pt = (ParameterizedType)GetMapValueType.class.getDeclaredField("map").getGenericType();
            for(Type type : pt.getActualTypeArguments()) {
                System.out.println(type.toString());
            }
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
