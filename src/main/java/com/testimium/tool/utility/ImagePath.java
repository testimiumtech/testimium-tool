package com.testimium.tool.utility;

public class ImagePath {

    public static String    getToolImagePath(){
        return "images/" +  ScreenResolution.getScreenResolution() + "/";
    }

    public static String getToolProfileImagePath(){
        return "/profile/" + PropertyUtility.getToolProfile() + "/";
    }
}
