package com.testimium.tool.utility;

import java.awt.*;

public class ScreenResolution {
    public static String getScreenResolution() {
        Dimension size
                = Toolkit.getDefaultToolkit().getScreenSize();

        // width will store the width of the screen
        int width = (int)size.getWidth();

        // height will store the height of the screen
        int height = (int)size.getHeight();

        return width + "x" + height;
    }
}
