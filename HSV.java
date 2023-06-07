import java.io.IOException;

import fr.unistra.pelican.Image;
import fr.unistra.pelican.util.Color;

public class HSV {
	 public static double[] rgbToHsv(Image img) throws IOException {
	        int width = img.getXDim();
	        int height = img.getYDim();
	        int pixelCount = width * height;
	        
	        int canal=img.getBDim();

	        double[] hueHistogram = new double[360]; // Histogramme des teintes (valeurs de 0 à 359)
	        int[] saturationHistogram = new int[101]; // Histogramme des saturations (valeurs de 0 à 100)

	        for (int y = 0; y < height; y++) {
	            for (int x = 0; x < width; x++) {
	            	if(canal==3) {
		                int rgb = img.getPixelXYBByte(x, y, 0); // Récupérer la valeur RGB du pixel
	
		                int red = img.getPixelXYBByte(x, y, 0);
		                int green = img.getPixelXYBByte(x, y, 1);
		                int blue = img.getPixelXYBByte(x, y, 2);
	
		                float[] hsv = new float[3];
		                Color.RGBtoHSB(red, green, blue, hsv);
	
		                float hue = hsv[0]; // Teinte (valeur entre 0 et 1)
		                float saturation = hsv[1]; // Saturation (valeur entre 0 et 1)
	
		                int hueIndex = Math.round(hue * (hueHistogram.length - 1)); // Index de la teinte dans l'histogramme (entre 0 et 359)
		                int saturationIndex = Math.round(saturation * (saturationHistogram.length - 1)); // Index de la saturation dans l'histogramme (entre 0 et 100)
	
		                hueHistogram[hueIndex]++;
		                saturationHistogram[saturationIndex]++;
	            	}
	            	
	            }
	        }
        // Affichage de l'histogramme des teintes
        System.out.println("Histogramme des teintes:");
        for (int i = 0; i < hueHistogram.length; i++) {
            //System.out.println("Teinte " + i + ": " + hueHistogram[i]);
        }

        // Affichage de l'histogramme des saturations
        System.out.println("Histogramme des saturations:");
        for (int i = 0; i < saturationHistogram.length; i++) {
         //   System.out.println("Saturation " + i + ": " + saturationHistogram[i]);
        }
        
        Histogramme h=new Histogramme();
        Main m=new Main();
        
        
        //h.plotHistogram(Main.normalise(img, hueHistogram));
        h.plotHistogram(hueHistogram);
        return hueHistogram;
    }
	 
	
    
}