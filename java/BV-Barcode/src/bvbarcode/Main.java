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
    
    /** Creates a new instance of Main */
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
boolean flag = true;

	// instantiate image im1 and attach a frame grabber	
	Img im1 = new Img(new ImgGrabber("preview"));
	
	// grab one frame to get size, etc. 
	im1.grabFrame();

	// instantiate image im2 with the same dimensions as im1 and set to
	// type gray scale
	Img im2 = new Img(im1.width, im1.height, Img.GRAY);

	// position the windows on the screen
        im1.setSingleWindowDisplay(300,10);
 	im2.setSingleWindowDisplay(300,350);
	

	// start grabbing frames from the cam
	while (flag) {
		
		// wait for 10ms (max 100 frames per sec)
		Img.sleep(10);

		im1.grabFrame();
			
		for (int i = 1; i < im1.height-1; i++) {
			for (int j = 1; j < im1.width-1; j++) {
				im2.pix[i][j].c0 = (short)(((int)(im1.pix[i][j].c0 + 
									im1.pix[i][j].c1 + im1.pix[i][j].c2))/3);
			}
		}


		im1.display();	// display (update) image im1
		im2.display();	// display (update) image im1
	}
}
}