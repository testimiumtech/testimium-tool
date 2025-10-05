package com.testimium.tool.comparator.image;

import com.testimium.tool.comparator.IToolComparator;
import com.testimium.tool.domain.ComparatorResponse;
import com.testimium.tool.exception.ToolComparatorException;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.ImageUtil;
import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.lang3.RandomUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

public class ImageExactComparator extends IToolComparator<String, String, ComparatorResponse> {
    @Override
    public ComparatorResponse compare(String actualFilePath, String expectedFilePath, String assertkey) throws ToolComparatorException {
        ComparatorResponse response = null;
        try {
            Color imgColor = Color.MAGENTA;
            BufferedImage actualImage = ImageIO.read(new File(actualFilePath));
            //BufferedImage expectedImage = ImageIO.read(new File(expectedFilePath));
            BufferedImage expectedImage = ImageIO.read(new File(expectedFilePath));
            int randomNum = RandomUtils.nextInt();
            String diffFile = FileUtility.getAbsolutePath(PropertyUtility.getScreenshotLocation()) + "/Image_diff" + randomNum + ".png";
            //String diffFile = "C:\\Users\\globa\\Downloads\\Image_diff" + randomNum + ".png";
            boolean isImageMatch = ImageUtil.compareAndHighlight(actualImage, expectedImage, diffFile, true, imgColor.getRGB());

            if (!isImageMatch) {
                return new ComparatorResponse("FAIL", "Found difference in actual and expected images. \n"
                        + "<img src=\"../../" + PropertyUtility.getScreenshotLocation()
                        + "/Image_diff" + randomNum + ".png" + "\" " + "alt=\"Italian Trulli\"> \n "
                        +"\n<a href='../../" + PropertyUtility.getScreenshotLocation()
                        + "/Image_diff" + randomNum + ".png" + "'>Click to open to see the difference in new tab.</a> \n","");
            }

            response = new ComparatorResponse((isImageMatch)? "PASS" : "FAIL",
                    (isImageMatch)? "Comparison Successful" : "Comparison Failed", "");
        } catch (FileNotFoundException ex) {
            response = new ComparatorResponse("FAIL", ex.getMessage(), "");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ToolComparatorException("Image Exact Comparison Failed: " + ex.getMessage(), ex);
        }

        return response;
    }

    /*public static void main(String[] args) throws ToolComparatorException {
      new ImageExactComparator().compare("C:\\Users\\globa\\Downloads\\GetCatalogs.png", "C:\\Users\\globa\\Downloads\\Activitydiagram.png","");
    }*/
}
