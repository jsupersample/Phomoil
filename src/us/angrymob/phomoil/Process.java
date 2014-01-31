package us.angrymob.phomoil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;


public class Process {
	final static String path = "/Users/wurzel/Desktop/phototest/500px/";
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		//String[] names = {"bird","hun","car","boat","path","robbie","kait"};
		//String[] names = {"parking","fish","branch","chair","fount","man"};
		String[] names = {"bier"};

		for(String name:names){
			
			String inPath = path+name+".png";
			String outPath = path+name+"_Final"+".png";
			try {
				System.out.println("Processing "+name);
				BufferedImage sourceImage =  ImageIO.read(new File(inPath));
				

								
				ImageIO.write(Bromoil.bromoil(sourceImage), "png", new File(outPath));
				//ImageIO.write(Koda.kodachrome(sourceImage, 1.3f, -25.00f,19,8,0), "png", new File(outPath));
				
				System.out.println("Finishd with "+name);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Finished batch");
	}
	

}
