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
                imgOut.pix[i][j].c0 = (short)(((int)(imgIn.pix[i][j].c0 + imgIn.pix[i][j].c1 + imgIn.pix[i][j].c2))/3);
            }
	}
    }
    
    public static void ImgClean(Img imgIn, Img imgOut) {
        //ToDo: Insert cleaning code
        imgOut.copy(imgIn);
    }
    
    //hautpfunktion
    public static Integer[] ReadBarCode(Img img) {
        int lineNr = 0;
        //verkehrt?!?!?
        int w = img.height - 1;
        int h = img.width - 1;
        Integer[] bcVector;
        Integer[] codeVector;
        Integer[] result = null;
        
        /*Line[] lines = {    new Line((int)(w/4), 0, (int)(w/4), h),
                            new Line((int)(w/2), 0, (int)(w/2), h),
                            new Line((int)(3*w/4), 0, (int)(3*w/4), h),
                            new Line(0, (int)(h/4), w, (int)(h/4)),
                            new Line(0, (int)(h/2), w, (int)(h/2)),
                            new Line(0, (int)(3*h/4), w, (int)(3*h/4)),
                            new Line(0, 0, w, h),
                            new Line((int)(w/2), 0, w, (int)(h/2)),
                            new Line(0, (int)(h/2), (int)(w/2), h),
                            new Line((int)(w/2), h, w, (int)(h/2)),
                            new Line(0, (int)(h/2), (int)(w/2), 1),
                            new Line(0, h, w, 0)};*/

        Line[] lines = {    new Line((int)(w/2), 0, (int)(w/2), h)};
                            
               
        
        while(result == null && lineNr < lines.length) {
            bcVector = PixelCount(img, lines[lineNr]);
            //TODO: remove
            System.out.println("bcVector:");
            
            for(int ii=0; ii<bcVector.length; ii++)
            {
                System.out.print(bcVector[ii].toString()+", ");
            }
            //TODO: ------------
            
            codeVector = GetCode(bcVector, 2.0);
            
            //TODO: remove
            System.out.println("\ncodeVector:"+codeVector.length);
            for(int ii=0; ii<codeVector.length; ii++)
            {
                System.out.print(codeVector[ii].toString()+", ");
            }
            System.out.println("");
            //TODO: ------------
            
            result = Decode(codeVector);
            lineNr++;
        } 
        return result;
    }
    
    public static Integer[] PixelCount(Img img, Line line) {
        int nextRow = line.Start.X;
        int nextCol = line.Start.Y;
        int endRow = line.End.X;
        int endCol = line.End.Y;
        
        int pixcount = 0;
        short currentcolor = img.pix[nextRow][nextCol].c0;
        ArrayList<Integer> bcVector = new ArrayList<Integer>();

        int deltaRow = endRow - nextRow;
        int deltaCol = endCol - nextCol;
        int stepCol = 0;
        int stepRow = 0;
        int currentStep = 0;
        int fraction = 0;

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
                if(fraction >= 0) {
                    nextRow = nextRow + stepRow;
                    fraction = fraction - deltaCol;
                }
                nextCol = nextCol + stepCol;
                fraction = fraction + deltaRow;
                currentStep = currentStep + 1;

                if(img.pix[nextRow][nextCol].c0 == currentcolor) {
                    pixcount = pixcount + 1;
                } else {
                    bcVector.add(pixcount);
                    pixcount = 1;
                    currentcolor = img.pix[nextRow][nextCol].c0;
                }
                // set it white to see the scanlines 
                // TODO: remove this line in the final version
                img.pix[nextRow-1][nextCol].c0 = 1;
            }
            bcVector.add(pixcount);
        } else {
            fraction = deltaCol*2-deltaRow;
            while (nextRow != endRow) {
                if (fraction >= 0) {
                    nextCol = nextCol + stepCol;
                    fraction = fraction - deltaRow;
                }
                nextRow = nextRow + stepRow;
                fraction = fraction + deltaCol;
                currentStep = currentStep + 1;
                
                if(img.pix[nextRow][nextCol].c0 == currentcolor) {
                    pixcount = pixcount + 1;
                } else {
                    bcVector.add(pixcount);
                    pixcount = 1;
                    currentcolor = img.pix[nextRow][nextCol].c0;
                }
                // set it white to see the scanlines 
                // TODO: remove this line in the final version
                img.pix[nextRow-1][nextCol].c0 = 1;
            }
            bcVector.add(pixcount);
        }
        return  bcVector.toArray(new Integer[bcVector.size()]);
    }  
    
    public static Integer[] GetCode(Integer[] vector, double threshold) {
        ArrayList<Integer> resVector = new ArrayList<Integer>();
        int maxSmall;
        int len;
        int color;
             
        //andere bedingung.... (return erst danach) nicht schön
        if (vector.length > 1 ) {
            maxSmall = vector[2];
            //temp_vec is smaller because the first and the last entrys don't belong
            //to the code
            len = vector.length - 4; 

            //start with black
            color = 1;  

            //coding symbols
            //1     white narrow
            //2     black narrow
            //3     white wide
            //4     black wide
            for(int i = 2; i < len+2; i++) {
                if(vector[i] < (double)maxSmall * threshold) { //narrow bar
                    if(vector[i] > maxSmall) {
                        maxSmall = vector[i];
                    }
                    resVector.add(color+1);
                } else { //wide bar
                    resVector.add(color+3);
                }                    
                color = color^1;
            }    
            return resVector.toArray(new Integer[resVector.size()]);
        }
        return null;
    }
    
    //nicht sicher
    public static Integer[] Decode(Integer[] vector) {
        ArrayList<Integer> resVector = new ArrayList<Integer>();
        
        ArrayList<Integer> whiteNumber = new ArrayList<Integer>();
        ArrayList<Integer> blackNumber = new ArrayList<Integer>();
        
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
            startVector[0] =  vector[0];
            startVector[1] =  vector[1];
            startVector[2] =  vector[2];
            startVector[3] =  vector[3];
            stopVector[0] = vector[len-3];
            stopVector[1] = vector[len-2];
            stopVector[2] = vector[len-1];
            
            stopVectorInv[0] =  vector[0];
            stopVectorInv[1] =  vector[1];
            stopVectorInv[2] =  vector[2];
            startVectorInv[0] = vector[len-4];
            startVectorInv[1] = vector[len-3];
            startVectorInv[2] = vector[len-2];
            startVectorInv[3] = vector[len-1];
            
            
            step = 1;
    
            // coding symbols
            // 1     white narrow
            // 2     black narrow
            // 3     white wide
            // 4     black wide
            //3476_rot_0.bmp => 2,1,2,1,4,1,4,1,2,3,2,1,2,3,2,1,2,3,2,3,4,1,4,1,4,1,2
            System.out.println("before loop setup vector len: "+len);

            if(Arrays.equals(startVector, startSeq) &&
               Arrays.equals(stopVector, stopSeq)) {
                loopStart = 4;
                loopEnd = len-3; // (-1 -3)
            } else if (Arrays.equals(startVectorInv, startSeqInv) &&
                       Arrays.equals(stopVectorInv, stopSeqInv)) {
                //codevector inverted
                //invert step
                step = -1;
                loopStart = len-5;
                loopEnd = 2;
            } else {
                return null;
            }
            //TODO: remove
            System.out.println("loopstart: "+loopStart);
            System.out.println("loopend: "+loopEnd);
            System.out.println("step: "+step);
            System.out.println("vector len: "+len);
            System.out.println("(len-7) % 2 = "+((len-7) % 2));
            System.out.println("");
            System.out.println("");
            
            //TODO: ----------
            // start and stop sequence identified, code has equal number of digits
            if ( ((len-7) % 2) == 0) {
                stepCount = 0;
                System.out.println("before for(loopstart to loopend");
                // fill both w_number and b_number vectors at once by using a
                // stepsize of 2
                for(int i=loopStart; i != loopEnd; i+=step*2) {
                    blackNumber.add(vector[i]-1);
                    whiteNumber.add(vector[i+step]);
                    System.out.println("black add: "+(vector[i]-1));
                    System.out.println("white add: "+(vector[i+step]));
                    if(stepCount == 4) {
                        resVector.add(CodeLookUp(blackNumber));
                        resVector.add(CodeLookUp(whiteNumber));
                        blackNumber.clear();
                        whiteNumber.clear();
                        stepCount = 0;
                    } else {
                        stepCount += 1;
                    }
                }
                return resVector.toArray(new Integer[resVector.size()]);
            } else {
                return null; //nicht benötigt...
            }
        }
        return null;
    }
    
    public static int CodeLookUp(ArrayList<Integer> orignumber) {
        int N = 1;
        int W = 3;
        
        Integer result;
        Integer[] number = new Integer[orignumber.size()];
        number = orignumber.toArray(number);
        
        
        Integer[] num0 = {N,N,W,W,N};
        Integer[] num1 = {W,N,N,N,W};
        Integer[] num2 = {N,W,N,N,W};
        Integer[] num3 = {W,W,N,N,N};
        Integer[] num4 = {N,N,W,N,W};
        Integer[] num5 = {W,N,W,N,N};
        Integer[] num6 = {N,W,W,N,N};
        Integer[] num7 = {N,N,N,W,W};
        Integer[] num8 = {W,N,N,W,N};
        Integer[] num9 = {N,W,N,W,N};
        if(Arrays.equals(number, num0)) {
            result = Integer.valueOf(0);
        } else if(Arrays.equals(number, num1)) {
            result = Integer.valueOf(1);
        } else if(Arrays.equals(number, num2)) {
            result = Integer.valueOf(2);
        } else if(Arrays.equals(number, num3)) {
            result = Integer.valueOf(3);
        } else if(Arrays.equals(number, num4)) {
            result = Integer.valueOf(4);
        } else if(Arrays.equals(number, num5)) {
            result = Integer.valueOf(5);
        } else if(Arrays.equals(number, num6)) {
            result = Integer.valueOf(6);
        } else if(Arrays.equals(number, num7)) {
            result = Integer.valueOf(7);
        } else if(Arrays.equals(number, num8)) {
            result = Integer.valueOf(8);
        } else if(Arrays.equals(number, num9)) {
            result = Integer.valueOf(9);
        } else {
            result = Integer.valueOf(-1);
        }

        return result;
    }
}

