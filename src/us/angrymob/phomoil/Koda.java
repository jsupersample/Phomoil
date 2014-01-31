package us.angrymob.phomoil;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Kernel;
import java.awt.image.Raster;
import java.awt.image.RescaleOp;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

//Warm, colorful, but not garish
//high contrast
//brilliant reds and yellows


public class Koda {
	final static int redMask = 0xFFFF0000; 
    final static  int greenMask = 0xFF00FF00;
    final static int blueMask = 0xFF0000FF;

	
	public static BufferedImage kodachrome(BufferedImage image, float contrast, float brighten, int redOffset, int greenOffset, int blueOffset){
		
		
		BufferedImage dimg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		
		
		Graphics2D g = dimg.createGraphics();  
		 g.setComposite(AlphaComposite.SrcOver);  
		 g.drawImage(image, null, 0, 0);  
		 g.dispose();  
		 
		
		
		 

    	 
    	 //byte[] pixels = new byte[dim];
    	
    	 BufferedImage dimg2 = new BufferedImage(dimg.getWidth(), dimg.getHeight(), BufferedImage.TYPE_INT_ARGB);
    	
    	 
 		
		 int cr=0;int cy=0;

		 System.out.println("Adjusting contrast and brightening");
    	 RescaleOp op = new RescaleOp(contrast, brighten, null);
    	 System.out.println("applying filter");
    	 op.filter(dimg, dimg);

	    
	    System.out.println("splitting color");
		 for(int i = 0; i < dimg.getHeight(); i++) {  
		        for(int j = 0; j < dimg.getWidth(); j++) {  
		        	
		        	
		        	Color color = new Color(dimg.getRGB(j, i));
		        	Color targetColor = color;
		        	
		        	
		        	Color red = new Color(color.getRed(),0,0);
		        	Color green = new Color(0,color.getGreen(),0);
		        	Color blue = new Color(0,0,color.getBlue());
		        	
		        	
		        	
		        	
			        	if( (red.getRed()+redOffset) <= 255 &&  (green.getGreen()+greenOffset) <= 255 ) 
			        		targetColor = new Color(red.getRed()+redOffset,green.getGreen()+greenOffset,blue.getBlue());
			        	else
			        		targetColor = color;
			        	
			        	/*
			        	if(targetColor == Color.WHITE){
			        		targetColor = color;
			        	}
			        	
			        	
			        	
			        	if( (color.getRGB() >= Color.GRAY.getRGB()) && (color.getRGB() <= Color.BLACK.getRGB())  ) targetColor = color;
		        	*/
		       
		        	/*
		        	
		        	System.out.println("Red: "+Integer.toBinaryString(red.getRGB()));
		        	System.out.println("green: "+Integer.toBinaryString(green.getRGB()));
		        	System.out.println("blue: "+Integer.toBinaryString(blue.getRGB()));
		        	
		        	
		        	*/
		        	
		        	
		        	
		        	dimg2.setRGB(j, i,targetColor.getRGB());

		        	
		        }
		 }
		 
		 System.out.println("Scaling red and green.");
		 RescaleOp redOp = new RescaleOp(0.9f, -0.9f, null);
		 RescaleOp greenOp = new RescaleOp(1.0f, 1.7f, null);
		 //redOp.filter(redImg, redImg);
		 //greenOp.filter(greenImg, greenImg);
		 
		 
		

		 
		 
		 System.out.println("Blending channels");
			
		
		 
		 
		  
		 
		

		 
    	 System.out.println("Done with color");
 		
		 
		
    	 System.out.println("Done with kodachrome");
    	 
    	 System.out.println("Blurring a little");
    	 dimg2 = blur(dimg2,1);
    	 
    	 
    	 
    	 return dimg2;
    	 
		
		
	}
	
	public static ColorModel createColorModel(double redFactor, double greenFactor, double blueFactor,IndexColorModel orig) {
		byte[] origRed  = new byte[256];
		byte[] origGreen  = new byte[256] ;
		byte[] origBlue  = new byte[256];
		
		orig.getReds(origRed);
		orig.getGreens(origGreen);
		orig.getBlues(origBlue);
		
		byte[] newReds = new byte[256];
		byte[] newGreens = new byte[256];
		byte[] newBlues = new byte[256];
		
		
		for (int i = 0; i < 256; i++) {
			//newReds[i] = (byte)(redFactor*(double)origRed[i]);
			newReds[i] = (byte)( origRed[i] + redFactor);
			newGreens[i] = (byte)( origGreen[i] + greenFactor);
			newBlues[i] = (byte)( origBlue[i] + blueFactor);
		}
		
		//return new IndexColorModel(4, 16, r, g, b);
		return new IndexColorModel(8, 256, newReds,newGreens , newBlues);
	}
	
	
	
	public static BufferedImage blur(BufferedImage image, int iterations){
		
		BufferedImage dimg = null;
		BufferedImage blurImg = null;
		
		float[] matrix = {
		        0.111f, 0.111f, 0.111f, 
		        0.111f, 0.111f, 0.111f, 
		        0.111f, 0.111f, 0.111f, 
		    };
	 


		dimg =  new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    blurImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    
	    
	    BufferedImageOp op = new ConvolveOp( new Kernel(3, 3, matrix) );
	    
	    
	    Graphics2D g = dimg.createGraphics();  
	    g.setComposite(AlphaComposite.Src);  
	    g.drawImage(image, null, 0, 0);  
	    g.dispose();  
		
	    
	    op.filter(dimg, blurImg);
	    
	    for(int i=0; i<=iterations; i++){
			  
			  blurImg =  op.filter(blurImg, new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB));
		  }
	    
	    System.out.println("Done with blurring");
		return blurImg;
	    
	  
	}
	
	public static boolean isBlueHue(Color color){
		float threshold=0.50f;
		
		if( (color.getBlue()>color.getRed()) && (color.getBlue()>color.getGreen()) ) return true;
		else return false;
		
		
	}
	
	public static boolean isRedHue(int color){
		float threshold= 0.50f;
		
		int ired = color & redMask;
    	int igreen = color & greenMask;
    	int iblue = color & blueMask;
    	
    	int red = (ired>>16)&255;
    	int green = (igreen>>8)&255;
    	int blue = (iblue&255);
    	
    	/*
    	System.out.println("Red value: "+Integer.toBinaryString(red));
    	System.out.println("Green value: "+ Integer.toBinaryString(green));
    	System.out.println("Blue value: "+Integer.toBinaryString(blue));
    	
    	
    	if( (green==0) && (blue==0)) return true;
    	System.out.println("Red value: "+red);
    	System.out.println("Green value: "+ green);
    	if(green>0) System.out.println("red/green  "+(float)red/green);
    	if(blue>0) System.out.println("red/blue "+(float)red/blue);
    	System.out.println("Blue value: "+blue);
    	*/
    	
    	
    	if( ( (green>0) && (((float)red/(float)green) <= threshold)) && ( (blue>0)&& (((float)red/(float)blue) <= threshold)) ){
    		//System.out.println("returning on: "+((float)red/(float)green)+" and "+((float)red/(float)blue));
    		
    		return true;
    	}
    	else return false;
		
		
	}
	
	public static boolean isYellowHue(int color){
		float threshold=0.50f;
		int difference = 10;
		
		int ired = color & redMask;
    	int igreen = color & greenMask;
    	int iblue = color & blueMask;
    	
    	int red = (ired>>16)&255;
    	int green = (igreen>>8)&255;
    	int blue = (iblue&255);
		
    	if(blue==0) return true;
    	if( (Math.abs(red-green)<=difference ) && ( ((float)(red+green)/2)/(float)blue <= threshold))  return true;
    	else return false;
    	
		
		
	}
	
	public static BufferedImage blend(BufferedImage image1, BufferedImage image2, BufferedImage image3){
		

		
		
		BufferedImage dimg1 = null;
		BufferedImage dimg2 = null;
		BufferedImage dimg3 = null;
		

	 	System.out.println("Blending");
		
		
		 dimg1 =  new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_RGB);
		 dimg2 =  new BufferedImage(image2.getWidth(), image2.getHeight(), BufferedImage.TYPE_INT_RGB);
		 dimg3 = new BufferedImage(image3.getWidth(), image3.getHeight(), BufferedImage.TYPE_INT_RGB);
		 
		 
		 
		 //Random r = new Random();
		 
		 
		 
		 Graphics2D g = dimg1.createGraphics();  
		 g.drawImage(image1, null, 0, 0);  
		 g.dispose();  
		 
		 Graphics2D e = dimg2.createGraphics();  
		 e.drawImage(image2, null, 0, 0);  
		 e.dispose();  
		 
		 

		 for(int i = 0; i < dimg1.getHeight(); i++) {  
		        for(int j = 0; j < dimg1.getWidth(); j++) {  
		        	
		        	
		        	Color pixel1 = new Color(dimg1.getRGB(j, i));
		        	Color pixel2 = new Color(dimg2.getRGB(j, i));
		        	Color pixel3 = new Color(dimg3.getRGB(j, i));
		        	
		        	
		        	
		        	int red1 = pixel1.getRed();
		        	int red2 = pixel2.getRed();
		        	int red3 = pixel3.getRed();
		        	int red4 = (red1|red2|red3);
		        	
		        	//System.out.println("NEw Red: "+Integer.toBinaryString(red4));
		        	
		        	int green1 = pixel1.getGreen();
		        	int green2 = pixel2.getGreen();
		        	int green3 = pixel3.getGreen();
		        	int green4 = (green1|green2|green3);
		        	
		        	//System.out.println("New Green: "+Integer.toBinaryString(green4));
		        	
		        	int blue1 = pixel1.getBlue();
		        	int blue2 = pixel2.getBlue();
		        	int blue3 = pixel3.getBlue();
		        	int blue4 = (blue1|blue2|blue3);
		        	
		        	//Color newColor = new Color(red4,green4,blue4);
		        	Color newColor = new Color(red4,green4,blue4);
		        	
		        	//System.out.println("New Blue: "+Integer.toBinaryString(blue4)+" pixel3: "+Integer.toBinaryString(blue3));
		        	
		        	
		        	dimg1.setRGB(j, i,newColor.getRGB() );
		        	
		        	
		        	
		        }
		 }
		        	
			        	
			 

		
		 
		 System.out.println("Done with Blending.");
		 return dimg1;
		

	}
	
}


