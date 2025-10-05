package com.testimium.tool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GetMapValueType {
    private static Map<String, Object> map = new HashMap<String, Object>();
    private static Map<String, Object> actMap = new HashMap<String, Object>();

    public static void main(String[] args) {
        /*try {
            ParameterizedType pt = (ParameterizedType)GetMapValueType.class.getDeclaredField("map").getGenericType();
            for(Type type : pt.getActualTypeArguments()) {
                System.out.println(type.toString());
            }
        } catch(NoSuchFieldException e) {
            e.printStackTrace();
        }*/
        map.put("H","jjjk");
        map.put("jjjj",7);
        map.put("jj6jj",7.06);
        actMap.put("H","jjjk");
        actMap.put("jjjj",7);
        actMap.put("jj6jj",7);
        Iterator<Map.Entry<String, Object>> ecpectedIterator = map.entrySet().iterator();
        while (ecpectedIterator.hasNext()) {
            Map.Entry expectedEntry = ecpectedIterator.next();
            //System.out.println(expectedEntry.getValue() + " = " + expectedEntry.getValue().getClass());
            if(!expectedEntry.getValue().getClass().equals(actMap.get(expectedEntry.getKey()).getClass())){
                System.out.println(expectedEntry.getValue() + " = " + expectedEntry.getValue().getClass());
            }
        }

    }
}
