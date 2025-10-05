package com.testimium.tool.utility;

import com.testimium.tool.logging.LogUtil;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class ImageUtil {
	
	private static Logger logger = Logger.getLogger(ImageUtil.class.getName());
	
	public static boolean compareAndHighlight(final BufferedImage img1, final BufferedImage img2, String fileName, boolean highlight, int colorCode) throws IOException {

	    final int w = img1.getWidth();
	    final int h = img1.getHeight();
		final int w2 = img2.getWidth();
		final int h2 = img2.getHeight();
	    final int[] p1 = img1.getRGB(0, 0, w, h, null, 0, w);
	    final int[] p2 = img2.getRGB(0, 0, w2, h2, null, 0, w2);

	    if(!(java.util.Arrays.equals(p1, p2))){
	    	logger.warning("Image compared - does not match");
	    	if(highlight){
	    	    for (int i = 0; i < p1.length; i++) {
	    	        if (i < p2.length && p1[i] != p2[i]){
	    	            p1[i] = colorCode;
	    	        } 
	    	    }
	    	    final BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    	    out.setRGB(0, 0, w, h, p1, 0, w);
	    	    saveImage(out, fileName);
	    	}
	    	return false;
	    }
	    return true;
	}

	static void saveImage(BufferedImage image, String file){
		try{
			File outputfile = new File(file);
			ImageIO.write(image, "png", outputfile);	
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * Used for Sikuli to match the images
	 * @param screen
	 * @param imagePath
	 * @return
	 * @throws IOException
	 */
	public static Pattern matchImagePatterns(Screen screen, String imagePath) throws IOException {
		Pattern patter = null;
		imagePath = ImagePath.getToolImagePath() + imagePath;
		String[] listOfImages = FileUtility.getAllFiles(imagePath);

		for (int ddlitr = 0; ddlitr < listOfImages.length; ddlitr++) {
			String file = listOfImages[ddlitr];
			patter = matchImagePattern(screen, file);
			if (patter != null && patter.isValid() == true) break;
		}
		return patter;
	}

	/**
	 *
	 * @param screen
	 * @param imagePath
	 * @return
	 */
	public static Pattern matchToolProfilePattern(Screen screen, String imagePath) {
		Pattern pattern = null;
		String toolProfile = null;
		String filePath = null;
		try {
			toolProfile  = PropertyUtility.getToolProfile();
			LogUtil.logTestCaseMsg("start image matching with configured tool profile = " + toolProfile );
			String imagePath1 =  ImagePath.getToolProfileImagePath() + imagePath;
			if(imagePath1.contains(imagePath)) {
				imagePath1 = imagePath.replace("\\images\\", ImagePath.getToolProfileImagePath() + "images/");
			}
			LogUtil.logTestCaseMsg("Profile Image Path: " + imagePath1);
			filePath = FileUtility.getAbsolutePath(imagePath1);
			LogUtil.logTestCaseMsg("Profile Image Path: " + filePath);
			pattern = new Pattern(filePath);
			screen.wait(pattern);
			//screen.wait(pattern, 2);
			LogUtil.logTestCaseMsg("Image - Found image successfully....");
			LogUtil.logTestCaseMsg("Image matched: " + filePath);
		} catch (FindFailed ffd) {
			pattern = null;
			LogUtil.logTestCaseMsg("image not matched with configured tool profile = " + filePath);
		} catch (Exception ex) {
			pattern = null;
			LogUtil.logTestCaseErrorMsg("Exception Occurred for configured tool profile = " + filePath, ex);
		}
		LogUtil.logTestCaseMsg("End image matching with configured tool profile = " + toolProfile );
		return pattern;
	}

	/**
	 *
	 * @param screen
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Pattern matchImagePattern(Screen screen, String file) throws IOException {
		Pattern patter1;
		String filePath = FileUtility.getAbsolutePath(file);
		LogUtil.logTestCaseMsg("Image Path = " + filePath);
		try {
			patter1 = new Pattern(filePath);
			screen.wait(patter1);
			//screen.wait(dropDownImg, 2);
			LogUtil.logTestCaseMsg("Image - Found image/Pattern successfully....");
			LogUtil.logTestCaseMsg("Image/Pattern matched: " + file);
		} catch (FindFailed ff) {
			LogUtil.logTestCaseMsg("Image not matched: " + filePath);
			//if(ddlitr == dropDownList.length - 1) {
			patter1 = matchToolProfilePattern(screen, file);
			//LogUtil.logTestCaseMsg("Image === " + patter1.toString());
			//if (null == patter1 || patter1.isValid() != true) return null;
		}
		return patter1;
	}
}
