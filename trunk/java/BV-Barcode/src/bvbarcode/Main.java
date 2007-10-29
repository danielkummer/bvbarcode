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

    public static void main(String[] args) {
        boolean running = true;

        bc = BarcodeProcessor.getInstance();
        
	// instantiate image im1 and attach a frame grabber	
	Img image = new Img(new ImgGrabber("preview"));
        Img result = new Img(image.width, image.height, Img.GRAY);
	// grab one frame to get size, etc. 
	image.grabFrame();
	// position the windows on the screen
        image.setSingleWindowDisplay(300,10);
        result.setSingleWindowDisplay(350,350);
 	
	// start grabbing frames from the cam
	while (running) {
		// wait for 10ms (max 100 frames per sec)
		Img.sleep(10);
    		image.grabFrame();
                result = bc.ImgToGrayScale(image);
                result.display();
		image.display();
	}
    }
}