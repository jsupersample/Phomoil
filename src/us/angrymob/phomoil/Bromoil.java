package us.angrymob.phomoil;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;
import java.util.Calendar;
import java.util.Random;

public class Bromoil {

	public static BufferedImage bromoil(BufferedImage image){
		BufferedImage tempImage;
		

		tempImage = comet(splotch(image,10,10),100,6);
		tempImage = blend(image,tempImage,1.8f,0.87f);
		return  processImages(tempImage,false);
		
		
	}
	
	
	public static BufferedImage blend(BufferedImage image1, BufferedImage image2, float brighten, float weight){
		

		
		
		BufferedImage dimg1 = null;
		BufferedImage dimg2 = null;
		
		

	 	System.out.println("Blending");
		
		
		 dimg1 =  new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_RGB);
		 dimg2 =  new BufferedImage(image2.getWidth(), image2.getHeight(), BufferedImage.TYPE_INT_RGB);

		 
		 //Random r = new Random();
		 int repeat = 1;
		 
		 
		 Graphics2D g = dimg1.createGraphics();  
		 g.drawImage(image1, null, 0, 0);  
		 g.dispose();  
		 
		 Graphics2D e = dimg2.createGraphics();  
		 e.drawImage(image2, null, 0, 0);  
		 e.dispose();  
		 
		 
		 for(int x=0; x<repeat; x++){
			 System.out.println("Pass: "+x);
			 for(int i = 0+x; i < dimg1.getHeight(); i++) {  
			        for(int j = 0+x; j < dimg1.getWidth(); j++) {  
			        	
			        	
			        	
			        	
			        	int pixel1 = dimg1.getRGB(j, i);
			        	int pixel2 = dimg2.getRGB(j, i);
			        	
			        	int red1 = (pixel1 >> 16) & 255;
			        	int red2 = (pixel2 >> 16) & 255;
			        	int red3 = (int) (red1*weight+red2*(1.0-weight));
			        	
			        	int green1 = (pixel1 >> 8) & 255;
			        	int green2 = (pixel2 >> 8) & 255;
			        	int green3 = (int) (green1*weight+green2*(1.0-weight));
			        	
			        	int blue1 = pixel1 & 255;
			        	int blue2 = pixel2 & 255;
			        	int blue3 = (int) (blue1*weight+blue2*(1.0-weight));
			        	
			        	
			        	
			        	dimg1.setRGB(j, i, ((red3 << 16) | (green3 << 8) | blue3) );
			        	
			        	
			        	
			        }
			 }
			        	
			        	
			 
		 }
		 System.out.println("Brightening...");
		 RescaleOp op = new RescaleOp(brighten, 0, null);
		 //dimg1 = op.filter(dimg1, dimg1);
		 op.filter(dimg1, dimg1);
		 System.out.println("Done with Blending.");
		 return dimg1;
		 
		// ImageIO.write(dimg1, "png", new File(outPath));
				

		

	}
	
	
	
	public static BufferedImage splotch(BufferedImage image,  int repeat, int bound){
		
		//String inPath = path+fileName+".png";
		//String outPath = path+fileOut+".png";
		
		//BufferedImage image = null;
		BufferedImage dimg = null;
		
	

	 	System.out.println("Splotching");
		//image = ImageIO.read(new File(inPath));
		 dimg =  new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		 

		 double threshold = 50.0;
		 //int baseOffset = 10;
		 int changed = 0;
		 Random r = new Random();
		 //int repeat = 10;
		 
		 
		 Graphics2D g = dimg.createGraphics();  
		 g.setComposite(AlphaComposite.Src);  
		 g.drawImage(image, null, 0, 0);  
		 g.dispose();  
		 
		 
		 for(int x=0; x<repeat; x++){
			 System.out.println("Pass: "+x);
			 for(int i = 0+x; i < dimg.getHeight(); i++) {  
			        for(int j = 0+x; j < dimg.getWidth(); j++) {  
			        	
			        	
			        	int randOffset = (int) Math.floor((r.nextGaussian())*20);
			        	double roll =  Math.abs(r.nextGaussian()*100);
			        	//int bound = 10;
			        	
			        	
			        	int pixel = dimg.getRGB(j, i);
			        	
			        	
			        	if(roll<=threshold){
			        		changed++;
			        		//int calcOffset = ((baseOffset+randOffset)/5);
			        		
			        		
			        		
			        		if( (j+randOffset < dimg.getWidth()) && (i+randOffset < dimg.getHeight()) && (randOffset >= 0)){
			        			
			        			for(int k=i;k<=i+randOffset;k++){
			        				if(i+randOffset > i+bound) continue;
			        				dimg.setRGB(j, k, pixel);
			        				
			        				
			        			}
			        			for(int l=j; l<=j+randOffset;l++){
		        					if(j+randOffset > j+bound) continue;
		        					dimg.setRGB(l, i, pixel);
		        				}

			        		}
			        		
			        		if( (j+randOffset < dimg.getWidth()) && (i+randOffset < dimg.getHeight()) && (randOffset >= 0)){
			        			
			        			for(int k=i;k<i+randOffset;k++){
			        				if(i+randOffset > i+bound) continue;
			        				dimg.setRGB(j, k, pixel);
			        				
			        				
			        			}
			        			for(int l=j; l<j+randOffset;l++){
		        					if(j+randOffset > j+bound) continue;
		        					dimg.setRGB(l, i, pixel);
		        				}
			        			
			        			
			        			for(int k=i;k<i+randOffset;k++){ //j++,i++
			        				if(i+randOffset > i+bound) continue;
			        				for(int l=j;l<j+randOffset;l++){
			        					if(j+randOffset > j+bound) continue;
			        					dimg.setRGB(l, k, pixel);
			        				}
			        			}
			        			
			        			
			        			

			        		}
			        		if( (j-randOffset < dimg.getWidth()) && (i-randOffset < dimg.getHeight()) && (randOffset >= 0) && (j-randOffset>0) && (i-randOffset>0)){
			        			
			        			for(int k=i;k>i-randOffset;k--){
			        				if(i-randOffset < i-bound) continue;
			        				dimg.setRGB(j, k, pixel);
			        				
			        				
			        			}
			        			for(int l=j; l>j-randOffset;l--){
		        					if(j-randOffset < j-bound) continue;
		        					dimg.setRGB(l, i, pixel);
		        				}
			        			
			        			for(int k=i;k>i-randOffset;k--){
			        				if(i-randOffset < i-bound) continue;
			        				for(int l=j; l>j-randOffset;l--){ //j--,i--
			        					if(j-randOffset < j-bound) continue;
			        					dimg.setRGB(l, k, pixel);
			        				}
			        			}
			        			
			        			
			        		}
			        		
			        		if( (j+randOffset < dimg.getWidth()) && (i-randOffset < dimg.getHeight()) && (randOffset >= 0) && (j+randOffset>0) && (i-randOffset>0)){
			        			for(int k=i;k>i-randOffset;k--){
			        				if(i-randOffset > i-bound) continue;
			        				for(int l=j; l>j+randOffset;l++){  //j++,i--
			        					if(j+randOffset > j+bound) continue;
			        					dimg.setRGB(l, k, pixel);
			        				}
			        			}
			        		
			        		}
			        		if( (j-randOffset < dimg.getWidth()) && (i+randOffset < dimg.getHeight()) && (randOffset >= 0) && (j-randOffset>0) && (i+randOffset>0)){
			        			
			        			for(int k=i;k>i+randOffset;k++){
			        				if(i+randOffset > i+bound) continue;
			        				for(int l=j; l>j-randOffset;l--){  //j--,i++
			        					if(j-randOffset < j-bound) continue;
			        					dimg.setRGB(l, k, pixel);
			        				}
			        			}
			        			
			        		}
			        		
			        		
			        		
			        		
			        	}
				        if( (j+randOffset < dimg.getWidth()) && (randOffset >= 0)){
				        	j+=randOffset;
				        	
				        }
			        }

			        
			 }
			 
			 
		 }
		 System.out.println("Done with splotches!");
		 return dimg;
		// ImageIO.write(dimg, "png", new File(outPath));
				 

	}
	
	
	public static BufferedImage comet(BufferedImage image, int right, int left){
		
		
		//BufferedImage image = null;
		BufferedImage dimg = null;
		BufferedImage blurImg = null;
		
		 
		
		float[] matrix = {
				0.644774f, 0.297421f, 0.0539044f, 0.00379789f, 0.000102766f,	
		};
		
		float[] matrixRev = {
				 0.000102766f, 	0.00379789f,  0.0539044f, 0.297421f, 0.644774f,

		};
		
		 BufferedImageOp op = new ConvolveOp( new Kernel(5, 1, matrixRev),ConvolveOp.EDGE_NO_OP,null );
		 BufferedImageOp op2 = new ConvolveOp( new Kernel(5, 1, matrix),ConvolveOp.EDGE_NO_OP,null );
		 

		 dimg =  new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		 blurImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		 
		 Graphics2D g = dimg.createGraphics();  
		 g.setComposite(AlphaComposite.Src);  
		 g.drawImage(image, null, 0, 0);  
		 g.dispose();  
		 
		 
		 
		 op.filter(dimg, blurImg);
		 
		 for(int i=0; i<right;i++){
			 System.out.println("Blur pass: "+i);
			 blurImg = op.filter(blurImg, new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB));
		 }
		 
		
		 
		 for(int i=0; i<left;i++){
			 System.out.println("Blur pass: "+i);
			 blurImg = op2.filter(blurImg, new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB));
		 }
		 
		 System.out.println("Done with comet");
		 return blurImg;
		 //ImageIO.write(blurImg, "png", new File(outPath));

		
	}

	
	public static BufferedImage processImages(BufferedImage image, boolean red){
		
		
		
		
		BufferedImage dimg = null;
		BufferedImage blurImg = null;

		 double noiseThreshold = 50.0;
		 int baseOffset = 10;
		 int changed = 0;
		 Random r = new Random();
		 

		 
		 
		 //--------------- blur
		 float[] matrix = {
			        0.111f, 0.111f, 0.111f, 
			        0.111f, 0.111f, 0.111f, 
			        0.111f, 0.111f, 0.111f, 
			    };
		 


			    //BufferedImageOp op = new ConvolveOp( new Kernel(4, 4, matrix2) );
			    BufferedImageOp op = new ConvolveOp( new Kernel(3, 3, matrix) );
			    //BufferedImageOp op3 = new ConvolveOp( new Kernel(3, 2, matrix3) );
				
		 //--------------------
		 
		 
		 ColorConvertOp op2 =  new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
		

			
		   
	   
	    
	    dimg =  new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    blurImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	   
	    
	    Graphics2D g = dimg.createGraphics();  
	    g.setComposite(AlphaComposite.Src);  
	    g.drawImage(image, null, 0, 0);  
	    g.dispose();  
	    
	    r.setSeed((long) blackCount(image)+Calendar.getInstance().getTimeInMillis());
	    
	    int redMask = 0xFFFF0000; //red
	    int greenMask = 0xFF00FF00;
	    int blueMask = 0xFF0000FF;
	    
	    int value;
	    int redValue;
	    int greenValue;
	    int blueValue;
	    
	    int targetValue;
	    
	    if(red){
		    for(int i = 0; i < dimg.getHeight(); i++) {  
		        for(int j = 0; j < dimg.getWidth(); j++) {  
		        	
		        	
		        	value = dimg.getRGB(j, i);
		        	
		        	
		        	redValue = value & redMask;
		        	greenValue = value & greenMask;
		        	blueValue = value & blueMask;
		        	targetValue = redValue|greenValue|blueValue;
		        	dimg.setRGB(j, i,  redValue);

		        }
		    }
	    
	    }
	    
	  
	    for(int i = 0; i < dimg.getHeight(); i++) {  
	        for(int j = 0; j < dimg.getWidth(); j++) {  
	        	
	        	int randOffset = (int) Math.floor((r.nextGaussian())*10);
	        	double roll =  Math.abs(r.nextGaussian()*100);

	        	
	        	int pixel = dimg.getRGB(j, i);
	        	
	        	
	        	//if the random roll is within the threshold, add noise at this pixel
	        	if((roll<=noiseThreshold) && (pixel!=Color.BLACK.getRGB()) && (pixel!=Color.WHITE.getRGB())){
	        		changed++;
	        		//int calcOffset = ((baseOffset+randOffset)/5);
	        		
	        		
	        		dimg.setRGB(j, i, pixel << ((baseOffset+randOffset)/2));
	        		if( (j+randOffset < dimg.getWidth()) && (i+randOffset < dimg.getHeight()) && (randOffset >= 0)){
	        			
	        			dimg.setRGB(j+randOffset, i+randOffset, pixel >> ((baseOffset+randOffset)/2));

	        		}
	        		
	        		if( (j+randOffset+2 < dimg.getWidth()) && (i+randOffset+2 < dimg.getHeight()) && (randOffset >= 0)){
	        	
	        			dimg.setRGB(j+randOffset+2, i+randOffset+2, Color.WHITE.getRGB());
	        			dimg.setRGB(j+randOffset+2, i+randOffset+2, Color.OPAQUE);
	        		}
	        		
	        		
	        	}
	        	
	        }  
	    }  

		
	    

	 
	    
	  op2.filter(dimg,dimg);
	  op.filter(dimg, blurImg);
	  
	  for(int i=0; i<=4; i++){
		  
		  blurImg =  op.filter(blurImg, new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB));
	  }
	  System.out.println("Done with blurring");
	  return blurImg;
	  
	  //ImageIO.write(blurImg, "png", new File(outPath));
		    

		
		
	}


	public static int blackCount(BufferedImage img){
		int count = 0;
		 for(int i = 0; i < img.getHeight(); i++) {  
		        for(int j = 0; j < img.getWidth(); j++) {  
		        	if(img.getRGB(j, i)==Color.BLACK.getRGB()) count++;
		        }
		 }
		System.out.println("COunt: "+count);
		
		return count;
	}
	
	
}
