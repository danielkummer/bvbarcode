/*
 * Main.java
 *
 * Created on 25. Oktober 2007, 17:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package bvbarcode;
import img.*;

/**
 *
 * @author kummedan
 */
public class Main {
    
    private static BarcodeProcessor bc;
    
    private static String parseResult(Integer[] res) {
        StringBuilder s = new StringBuilder();
        //super 3v1| hack, god killd thousand kittens 'cause of this... (3)
        if(res != null && res.length > 3) {
            for(int i = 0; i < res.length; i++) {
                s.append(res[i]);
            }
            java.awt.Toolkit.getDefaultToolkit().beep();
        } else {
            s.append("nothing found");
        }        
        System.out.println("RESULT "+s);
        return s.toString();
    }

    public static void main(String[] args) {
        boolean running = true;
        
        Integer[] result = null;

        bc = BarcodeProcessor.getInstance();
        
        ImgTextDisplay textDisp = new ImgTextDisplay(100, 100, 300,300);
        
	// instantiate image im1 and attach a frame grabber	
	Img image = new Img(new ImgGrabber("preview"));
//        Img image = new Img();
        
//        image.read("C:\\Dokumente und Einstellungen\\kummedan\\Desktop\\BV Projekt Barcode\\matlab\\testimages\\3476_rot_0.bmp");
//        image.read("C:\\Dokumente und Einstellungen\\XereX\\Eigene Dateien\\Data\\zhwin\\5.Sem\\BV\\Projekt\\Barcode\\SVN_google_Barcode\\matlab\\testimages\\3476_rot_0.bmp");
        
        Img grayImage = new Img(image.width, image.height, Img.GRAY);
        Img binImage  = new Img(image.width, image.height, Img.BINARY);
	image.grabFrame();
        image.setSingleWindowDisplay(300,10);
        grayImage.setSingleWindowDisplay(350,350);
        binImage.setSingleWindowDisplay(500,500);
 	bc.debugImg = new Img(image.width, image.height, Img.RGB);
        bc.debugImg.setSingleWindowDisplay(300,500);

        
	while (running) {
		// wait for 10ms (max 100 frames per sec)
		Img.sleep(10);
   		image.grabFrame();
                
//                bc.ImgToGrayScale(image, grayImage);
//                bc.ImgClean(grayImage, binImage);
//                grayImage.display();
                
//                binImage.gray2bin(128);
                bc.debugImg.copy(image);
                bc.debugImg.rgb2bin(100);
                
                result = bc.ReadBarCode(image);
                textDisp.displayText(parseResult(result));
                
//                image.display();
//                binImage.display();
                bc.debugImg.display();
                
                //image.read("C:\\Dokumente und Einstellungen\\kummedan\\Desktop\\BV Projekt Barcode\\matlab\\testimages\\3476_rot_0.bmp");
	}
    }
}