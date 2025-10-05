package com.testimium.tool.domain;

public class ComparatorResponse {
    private String result;
    private String messge;
    private Object object;

    public ComparatorResponse(String result, String messge, Object object) {
        this.result = result;
        this.messge = messge;
        this.object = object;
    }

    public String getResult() {
        return result;
    }

    public String getMessge() {
        return messge;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String toString() {
        return "ComparatorResponse{" +
                "result='" + result + '\'' +
                ", messge='" + messge + '\'' +
                ", object=" + ((null == object) ? null : object.toString()) +
                '}';
    }
}
