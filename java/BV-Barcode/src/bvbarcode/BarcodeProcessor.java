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
import java.util.*;

/**
 *
 * @author kummedan, stussman
 */
public class BarcodeProcessor {
    private static BarcodeProcessor instance = null;
    public static Img debugImg;
    
    /** Creates a new instance of BarcodeProcessor */
    protected BarcodeProcessor() {
    }
    
    public static BarcodeProcessor getInstance() {
      if(instance == null) {
         instance = new BarcodeProcessor();
      }
      return instance;
   }
    
    public static void ImgToGrayScale(Img imgIn, Img imgOut) {       
        for (int i = 1; i < imgIn.height-1; i++) {
            for (int j = 1; j < imgIn.width-1; j++) {
//                imgOut.pix[i][j].c0 = (short)(((int)(imgIn.pix[i][j].c0 + imgIn.pix[i][j].c1 + imgIn.pix[i][j].c2))/3);
                 if( (((int)(imgIn.pix[i][j].c0 + imgIn.pix[i][j].c1 + imgIn.pix[i][j].c2))/3) < 128 ){
                     imgOut.pix[i][j].c0 = 0;
                 }else{
                     imgOut.pix[i][j].c0 = 255;
                 }
            }
	}
    }
    
    public static void ImgClean(Img imgIn, Img imgOut) {
        //ToDo: Insert cleaning code
        imgOut.copy(imgIn);
    }
    
    //hautpfunktion
    public static int[] ReadBarCode(Img img) {
        int lineNr = 0;
        //verkehrt?!?!?
        int w = img.height - 1;
        int h = img.width - 1;
        int[] bcVector;
        int[] codeVector;
        int[] result = null;
        
        int[][] lines = {    {(int)(w/4), 0, (int)(w/4), h},
                            {(int)(w/2), 0, (int)(w/2), h},
                            {(int)(3*w/4), 0, (int)(3*w/4), h},
                            {0, (int)(h/4), w, (int)(h/4)},
                            {0, (int)(h/2), w, (int)(h/2)},
                            {0, (int)(3*h/4), w, (int)(3*h/4)},
                            {0, 0, w, h},
                            {(int)(w/2), 0, w, (int)(h/2)},
                            {0, (int)(h/2), (int)(w/2), h},
                            {(int)(w/2), h, w, (int)(h/2)},
                            {0, (int)(h/2), (int)(w/2), 1},
                            {0, h, w, 0}};

//        int[][] lines = {    {0, 0, w, h}};
                            
               
        
        while( result == null && lineNr < lines.length) {
            bcVector = new int[img.width+1];
            int bcLen = PixelCount(img, lines[lineNr], bcVector);
            //TODO: remove
//            System.out.println("bcVector: "+bcVector.length);
/*            
            for(int ii=0; ii<bcLen; ii++)
            {
                System.out.print(bcVector[ii]+", ");
            }
            //TODO: ------------
 */
            
            codeVector = GetCode(bcVector, bcLen, 1.6);
            
            if(codeVector != null){
            //TODO: remove
/*                System.out.println("\ncodeVector:"+codeVector.length);
                for(int ii=0; ii<codeVector.length; ii++)
                {
                    System.out.print(codeVector[ii]+", ");
                }
                System.out.println("");
 */
            //TODO: ------------
            
                result = Decode(codeVector);
            }
            lineNr++;
        } 
        return result;
    }
    
    public static int PixelCount(Img img, int[] line, int[] bcVector) {
        int nextRow = line[0];
        int nextCol = line[1];
        int endRow = line[2];
        int endCol = line[3];
        
        int pixcount = 0;
        int currentcolor;
        int threshold = 100;
        if((short)(((int)(img.pix[nextRow][nextCol].c0 + img.pix[nextRow][nextCol].c1 + img.pix[nextRow][nextCol].c2))/3) < threshold){
            currentcolor = 0;
        }else{
            currentcolor = 255;
        }
//        int bcVector[] = new int[img.width+1];

        int deltaRow = endRow - nextRow;
        int deltaCol = endCol - nextCol;
        int stepCol = 0;
        int stepRow = 0;
        int currentStep = 0;
        int bcVecPos = 0;
        int fraction = 0;
        int[] tmp;

        if (deltaRow < 0) {
            stepRow = -1;
        } else {
            stepRow = 1;
        }

        if (deltaCol < 0) {
            stepCol = -1;
        } else {
            stepCol = 1;
        }

        deltaRow = Math.abs(deltaRow*2);
        deltaCol = Math.abs(deltaCol*2);

        currentStep = currentStep + 1;

        if (deltaCol > deltaRow ) {
            fraction = deltaRow*2-deltaCol;
            while (nextCol != endCol) {

                tmp = pixcounter(img, currentcolor, nextRow, nextCol, pixcount, bcVector, threshold, bcVecPos);
                pixcount = tmp[0];
                currentcolor = tmp[1];
                bcVecPos = tmp[2];
//                System.out.println("Pixcount = "+pixcount);
//                System.out.println("bcv2: "+bcVector[currentStep]+" pixcount2: "+pixcount);

                
                if(fraction >= 0) {
                    nextRow = nextRow + stepRow;
                    fraction = fraction - deltaCol;
                }
                nextCol = nextCol + stepCol;
                fraction = fraction + deltaRow;
                currentStep = currentStep + 1;
            }
            bcVector[bcVecPos] = pixcount;
        } else {
            fraction = deltaCol*2-deltaRow;
            while (nextRow != endRow) {

                tmp = pixcounter(img, currentcolor, nextRow, nextCol, pixcount, bcVector, threshold, bcVecPos);
                pixcount = tmp[0];
                currentcolor = tmp[1];
                bcVecPos = tmp[2];
                
                if (fraction >= 0) {
                    nextCol = nextCol + stepCol;
                    fraction = fraction - deltaRow;
                }
                nextRow = nextRow + stepRow;
                fraction = fraction + deltaCol;
                currentStep = currentStep + 1;
            }
            bcVector[bcVecPos] = pixcount;
        }
        return bcVecPos+1;
    }  
    
    private static int[] pixcounter(Img imgIn, 
                                int currentcolor, 
                                int i,
                                int j, 
                                int pixcount, 
                                int[] bcVector,
                                int threshold,
                                int bcVecPos){
        short actual_color;
        if( (((int)(imgIn.pix[i][j].c0 + imgIn.pix[i][j].c1 + imgIn.pix[i][j].c2))/3) < threshold){
            actual_color = 0;
        }else{
            actual_color = 255;
        }; 
        if(actual_color == currentcolor) {
            pixcount = pixcount + 1;
        } else {
            bcVector[bcVecPos] = pixcount;
            bcVecPos++;
//            System.out.println("bcv: "+bcVector[currentStep]+" pixcount: "+pixcount);
            pixcount = 1;
            currentcolor = actual_color;
        }
//        imgIn.pix[i][j].c0 = (short)(255-currentcolor);
//        debugImg.pix[i][j].c0 = (short)(255-currentcolor);
        
        int[] t = {pixcount, currentcolor, bcVecPos};
        return t;
    }
    
    public static int[] GetCode(int[] vector, int vLen, double threshold) {
        int[] resVector = new int[vLen];
        int maxSmall;
        int len;
        int color;
             
        // Bedingung für gültigen Code
        // 2 linien aussen fallen weg
        // 4 start und 3 stopplinien
        // 1 zeichen = 5 linien
        if (vLen >= 14 ) {
            maxSmall = vector[1];
//            System.out.println("\nmaxSmall is: "+maxSmall);
            //temp_vec is smaller because the first and the last entrys don't belong
            //to the code
            len = vLen; 

            //start with black
            color = 1;  

            //coding symbols
            //1     white narrow
            //2     black narrow
            //3     white wide
            //4     black wide
            for(int i = 1; i < len-1; i++) {
                if(vector[i] < (double)maxSmall * threshold) { //narrow bar
                    if(vector[i] > maxSmall) {
                        maxSmall = vector[i];
//                        System.out.println("adjusting maxSmall to: "+maxSmall);
                    }
                    resVector[i] = color+1;
                } else { //wide bar
                    resVector[i] = color+3;
                }                    
                color = color^1;
            }    
            return resVector;
        }
        return null;
    }
    
    //nicht sicher
    public static int[] Decode(int[] vector) {
        int[] resVector = new int[(vector.length-9)/5];
        
        int[] whiteNumber = new int[5];
        int[] blackNumber = new int[5];
        
        int[] startVector = new int[4];
        int[] stopVector = new int[3];
        
        int[] startSeq  = {2,1,2,1};
        int[] stopSeq   = {4,1,2};
        
        //invertierte sequenzen
        int[] startVectorInv = new int[4];
        int[] stopVectorInv = new int[3];
        
        int[] startSeqInv  = {1,2,1,2};
        int[] stopSeqInv   = {2,1,4};
        
        int len = vector.length;
        int step;
        int stepCount;
        
        int loopStart;
        int loopEnd;
        
        if ( len > 5 ) { //better value?
            startVector[0] =  vector[1];
            startVector[1] =  vector[2];
            startVector[2] =  vector[3];
            startVector[3] =  vector[4];
            stopVector[0] = vector[len-4];
            stopVector[1] = vector[len-3];
            stopVector[2] = vector[len-2];
            
            stopVectorInv[0] =  vector[1];
            stopVectorInv[1] =  vector[2];
            stopVectorInv[2] =  vector[3];
            startVectorInv[0] = vector[len-5];
            startVectorInv[1] = vector[len-4];
            startVectorInv[2] = vector[len-3];
            startVectorInv[3] = vector[len-2];
            
            
            step = 1;
    
            // coding symbols
            // 1     white narrow
            // 2     black narrow
            // 3     white wide
            // 4     black wide
            //3476_rot_0.bmp => 2, 1, 2, 1, 4, 1, 4, 1, 2, 3, 2, 1, 2, 3, 2, 1, 2, 3, 2, 3, 4, 1, 4, 1, 4, 1, 2
//            System.out.println("before loop setup vector len: "+len);

            if(Arrays.equals(startVector, startSeq) &&
               Arrays.equals(stopVector, stopSeq)) {
                loopStart = 5;
                loopEnd = len-4; // (-1 -3)
            } else if (Arrays.equals(startVectorInv, startSeqInv) &&
                       Arrays.equals(stopVectorInv, stopSeqInv)) {
                //codevector inverted
                //invert step
                step = -1;
                loopStart = len-5;
                loopEnd = 4;
            } else {
                return null;
            }
            //TODO: remove
/*            
            System.out.println("loopstart: "+loopStart);
            System.out.println("loopend: "+loopEnd);
            System.out.println("step: "+step);
            System.out.println("vector len: "+len);
            System.out.println("(len-7) % 2 = "+((len-7) % 2));
            System.out.println("");
            System.out.println("");
*/            
            //TODO: ----------
            // start and stop sequence identified, code has equal number of digits
            int resVecCount = 0;
            if ( ((len-9) % 2) == 0) {
                stepCount = 0;
//                System.out.println("before for(loopstart to loopend");
                // fill both w_number and b_number vectors at once by using a
                // stepsize of 2
                for(int i=loopStart; i != loopEnd; i+=step*2) {
                    blackNumber[stepCount] = vector[i]-1;
                    whiteNumber[stepCount] = vector[i+step];
//                    System.out.println("black add: "+(vector[i]-1));
//                    System.out.println("white add: "+(vector[i+step]));
                    if(stepCount == 4) {
                        int r = CodeLookUp(blackNumber);
                        if(r==-1){return null;}
                        resVector[resVecCount]=r;
                        resVecCount++;
                        r = CodeLookUp(whiteNumber);
                        if(r==-1){return null;}
                        resVector[resVecCount]=r;
                        resVecCount++;
                        stepCount = 0;
                    } else {
                        stepCount += 1;
                    }
                }
                return resVector;
            } else {
                return null; //nicht benötigt...
            }
        }
        return null;
    }
    
    public static int CodeLookUp(int[] orignumber) {
        int N = 1;
        int W = 3;
        
        int result;
        int[] number = orignumber;
        
        
        int[] num0 = {N,N,W,W,N};
        int[] num1 = {W,N,N,N,W};
        int[] num2 = {N,W,N,N,W};
        int[] num3 = {W,W,N,N,N};
        int[] num4 = {N,N,W,N,W};
        int[] num5 = {W,N,W,N,N};
        int[] num6 = {N,W,W,N,N};
        int[] num7 = {N,N,N,W,W};
        int[] num8 = {W,N,N,W,N};
        int[] num9 = {N,W,N,W,N};
        if(Arrays.equals(number, num0)) {
            result = 0;
        } else if(Arrays.equals(number, num1)) {
            result = 1;
        } else if(Arrays.equals(number, num2)) {
            result = 2;
        } else if(Arrays.equals(number, num3)) {
            result = 3;
        } else if(Arrays.equals(number, num4)) {
            result = 4;
        } else if(Arrays.equals(number, num5)) {
            result = 5;
        } else if(Arrays.equals(number, num6)) {
            result = 6;
        } else if(Arrays.equals(number, num7)) {
            result = 7;
        } else if(Arrays.equals(number, num8)) {
            result = 8;
        } else if(Arrays.equals(number, num9)) {
            result = 9;
        } else {
            result = -1;
        }

        return result;
    }
}

