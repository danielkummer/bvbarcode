/*
 * BarcodeProcessor.java
 *
 * Created on 29. Oktober 2007, 16:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package bvbarcode;
import img.*;

/**
 *
 * @author kummedan, stussman
 */
public class BarcodeProcessor {
    private static BarcodeProcessor instance = null;
    
    /** Creates a new instance of BarcodeProcessor */
    protected BarcodeProcessor() {
    }
    
    public static BarcodeProcessor getInstance() {
      if(instance == null) {
         instance = new BarcodeProcessor();
      }
      return instance;
   }
    
    public static Img ImgToGrayScale(Img img) {
        
        Img imgGray = new Img(img.width, img.height, img.GRAY);
                
        for (int i = 1; i < img.height-1; i++) {
            for (int j = 1; j < img.width-1; j++) {
                imgGray.pix[i][j].c0 = (short)(((int)(img.pix[i][j].c0 + img.pix[i][j].c1 + img.pix[i][j].c2))/3);
            }
	}
        return imgGray;
    }
    
    public static Img ImgClean(Img img) {
        return img;
    }
    
}