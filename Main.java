import java.util.List;

import javax.swing.SwingUtilities;

import java.io.File;
import java.io.IOException;
import java.util.HashMap; // import the HashMap class
import fr.unistra.pelican.ByteImage;
import java.util.ArrayList;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
	static HashMap<Image, double[]> histo = new HashMap<Image, double[]>();
	private static final String DB_URL = "jdbc:mysql://localhost:3306/leblet";
	private static final String DB_USERNAME = "leblet";
	private static final String DB_PASSWORD = "leblet";
	static double[] comptageImg = new double[256];
	static String chemin_img="src/000.jpg";
	//static String chemin_img="src/lenna.jpg";
	static Image img = ImageLoader.exec(chemin_img);
	static Image img2 = ImageLoader.exec("src/img/solo_d.jpg");
	static Image imageGris=imgGris(img);
	static HashMap<String, String> name_img = new HashMap<String, String>();
	static String folderPath = "src/motos";

	public static void main(String[] args) throws IOException {
		
		
		//HSV s=new HSV();
		//s.rgbToHsv(img);
		
		//calculateHistogramSimilarity(normalise(img, discretisationHisto( s.rgbToHsv(img))),normalise(img2,s.rgbToHsv(img2)));
		
		
		
		
		

		//Image imgFiltre = filtreMedian(img);
		

		int height = img.getYDim();
		int width = img.getXDim();
		int taille = width * height;
		
		
		

		double[] rougeT = new double[256];
		double[] bleuT = new double[256];
		double[] vertT = new double[256];


		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (img.getBDim() == 1) {
					int niveauGris = img.getPixelXYBByte(x, y, 0);
					comptageImg[niveauGris]++;
				} else {
					int rouge = img.getPixelXYBByte(x, y, 0);
					rougeT[rouge]++;
					int bleu = img.getPixelXYBByte(x, y, 1);
					bleuT[bleu]++;
					int vert = img.getPixelXYBByte(x, y, 2);
					vertT[vert]++;
				}
			}
		}
		if (img.getBDim() == 1) {
			Histogramme h = new Histogramme();
			for (int a = 0; a < comptageImg.length; a++) {
				comptageImg[a] = (comptageImg[a] / taille);
			}
		h.plotHistogram(discretisationHisto(comptageImg));
		} else {
			HistoK k = new HistoK();
			for (int a = 0; a < rougeT.length; a++) {
				rougeT[a] = (rougeT[a] / taille);
				bleuT[a] = (bleuT[a] / taille);
				vertT[a] = (vertT[a] / taille);
			}
			k.plotHistogram(discretisationHisto(rougeT), discretisationHisto(bleuT), discretisationHisto(vertT));
			// k.plotHistogram(rougeT, bleuT, vertT);
		}
		
		//convertHashMap(retrieveHistogramsFromDatabase());
		

		

		
		//List<Image> trouve = findSimilarImages(normalise(imageGris, discretisationHisto(ImageAHisto(imageGris))), histoDossier(images), 200);
		/*HashMap<Image, double[]> test = new HashMap<Image, double[]>();
		Image img2 = ImageLoader.exec("src/solo.jpg");
		img2=imgGris(img2);
		;
		test.put(img2,ImageAHisto(img2));*/
		List<Image> images = loadImagesFromFolder(folderPath);
		
		
		
		printHistoSQL(ImageAHisto(imageGris));
		convertHashMap(retrieveHistogramsFromDatabase());
		histoDossier(images);
//       
	
		
		
	}
	
	static double[] ImageAHisto(Image img) {
		double[]tab=new double[256];

		int height = img.getYDim();
		int width = img.getXDim();
		int taille = width * height;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
					int niveauGris = img.getPixelXYBByte(x, y, 0);
					tab[niveauGris]++;
				
			}
		}
		
		return tab;
	}
	
	static Image imgGris(Image img) {
        int height=img.getYDim();
        int width=img.getXDim(); 
        int canal=img.getBDim();
        
       

           ByteImage imageGris=new ByteImage(width,height,1,1,1);
           if(canal==3) {
        	 
			       for (int y = 0; y < height; y++) {
			           for (int x = 0; x < width; x++) {
			
			               int red = img.getPixelXYBByte(x, y,0);
			               int green = img.getPixelXYBByte(x, y,1);
			               int blue = img.getPixelXYBByte(x, y,2);
			              
			               int niveauGris = (red + green + blue) / 3;
			               imageGris.setPixelXYBByte(x, y,0,niveauGris);
			
			
			   }
			           
			          
       }
			       
			      
           }
           
           else {
        	   for (int y = 0; y < height; y++) {
		           for (int x = 0; x < width; x++) {
		        	   int gris = img.getPixelXYBByte(x, y,0);
		        	   imageGris.setPixelXYBByte(x, y,0,gris);
		        	   
		           }
		           
		        }
		
	       }
          // Viewer2D.exec(imageGris);
       return(imageGris);

   }
	
	static double[] normalise(Image i,double []tab ) {
		int height = i.getYDim();
		int width = i.getXDim();
		int taille = width * height;

		
		for (int a = 0; a < tab.length; a++) {
			tab[a] = (tab[a] / taille);
		}
		return tab;
	}
	

	/*public static List<Image> findSimilarImages(double[] mainHistogram, HashMap<Image, double[]> imageHistograms,
			double similarityThreshold) throws IOException {
		List<Image> similarImages = new ArrayList<>();
		
		
		for (HashMap.Entry<Image, double[]> entry : imageHistograms.entrySet()) {
			Image image = entry.getKey();
			System.out.println(" AAAAAAAAA "+image);

			
			double[] histogram = entry.getValue();
			

			double similarity = calculateHistogramSimilarity(discretisationHisto(mainHistogram) , histogram);
			
			

			System.out.println(" similarity "+similarity+" image "+name_img.get(image.toString()));

				similarImages.add(image);
				
		}

		return similarImages;
	}
*/
	public static double calculateHistogramSimilarity(double[] histogram1, double[] histogram2) {

		int numBins = histogram1.length;
		
		//System.out.println("taille 1 "+numBins+" taille 2 : "+histogram2.length);

		double distance = 0.0;
		for (int i = 0; i < numBins; i++) {
			distance =+ Math.sqrt(Math.pow(histogram1[i] - histogram2[i], 2));
			

		}
		
		return distance;
	}

	private static List<Image> loadImagesFromFolder(String folderPath) {
		List<Image> images = new ArrayList<>();
		

		File folder = new File(folderPath);
		File[] files = folder.listFiles();

		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					Image image = ImageLoader.exec(file.getAbsolutePath());
					image=imgGris(image);
					//image=imgGris(image);
					name_img.put(image.toString(), file.getName());
					//System.out.println("name " + file.getName() + " " + image);
					images.add(image);
					//System.out.println(" test "+name_img.get(image.toString()));
				}
			}
		}
		return images;
	}

	static void histoDossier(List<Image> img) throws IOException {
		HashMap<Image, double[]> histo = new HashMap<>();
		
		

		for (int i = 0; i < img.size(); i++) {
			double[] comptagePixels;
			Image image = img.get(i);
			int height = image.getYDim();
			int width = image.getXDim();
			int taille=width*height;

		
				comptagePixels = new double[256];
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int niveauGris = image.getPixelXYBByte(x, y, 0);
						comptagePixels[niveauGris]++;
					}
					

				}
			
			
			
			//Envoyer dans la base de données
				//uploadHistogramToDatabase(image.toString(),discretisationHisto(comptagePixels));
			
		}

		
	}

	public static double[] discretisationHisto(double[] comptageImg) throws IOException {

		int niveau = 20;
		int taille = comptageImg.length / 20;
		double[] newComptageImg = new double[taille];
		double ad = 0;
		int a = 0;

		for (int j = 0; j < comptageImg.length; j++) {
			ad = +comptageImg[j];

			if (niveau == 0) {
				newComptageImg[a] = ad;
				// System.out.println("valeur'"+ad);
				ad = 0;
				a++;
				niveau = 20;
			}
			niveau--;
		}

		Histogramme h = new Histogramme();

		return newComptageImg;

	}

	public static Image filtreMedian(Image img) {
		int largeur = img.getXDim();
		int hauteur = img.getYDim();

		ByteImage new_img = new ByteImage(largeur, hauteur, 1, 1, 1);

		for (int x = 1; x < largeur - 1; x++) {
			for (int y = 1; y < hauteur - 1; y++) {
				int[] values = new int[9];
				int index = 0;
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						values[index] = img.getPixelXYBByte(x + i, y + j, 0);
						index++;
					}
				}
				int median = getMedian(values);
				new_img.setPixelXYBByte(x, y, 0, median);
			}
		}

		new_img.setColor(false);
		return new_img;
	}

	private static int getMedian(int[] values) {
		for (int i = 0; i < values.length - 1; i++) {
			for (int j = i + 1; j < values.length; j++) {
				if (values[j] < values[i]) {
					int temp = values[i];
					values[i] = values[j];
					values[j] = temp;
				}
			}
		}
		int median;
		int middle = values.length / 2;
		if (values.length % 2 == 0) {
			median = (values[middle - 1] + values[middle]) / 2;
		} else {
			median = values[middle];
		}

		return median;
	}

	public static void uploadHistogramToDatabase(String imageName, double[] histogram) {
		
		
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			String insertQuery = "INSERT INTO histogramme (image_name, histogram) VALUES (?, ?)";
			

			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(" detail img " + imageName);
			//System.out.println("name " + name_img.get(imageName));

			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			//System.out.println("String "+name_img.get(imageName));
			preparedStatement.setString(1,  name_img.get(imageName));

			StringBuilder sb = new StringBuilder();
			for (double value : histogram) {
				sb.append(value).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			///System.out.println("String "+sb.toString());
			preparedStatement.setString(2, sb.toString());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap<Image, double[]> convertHashMap(HashMap<String, double[]> originalMap) {
        HashMap<Image, double[]> convertedMap = new HashMap<>();

        for (String key : originalMap.keySet()) {
            // Convertir la clé en Image (utilisez votre propre logique pour obtenir l'image à partir de la clé)
        	//System.out.println("key "+key);
            Image image = ImageLoader.exec(folderPath+"/"+key);
            image=imgGris(image);
            
     

            // Obtenir la valeur correspondante dans le HashMap d'origine
            double[] value = originalMap.get(key);

            // Ajouter la paire clé-valeur convertie dans le nouveau HashMap
            convertedMap.put(image, value);
        }

        return convertedMap;
    }

	public static HashMap<String, double[]> retrieveHistogramsFromDatabase() {
		HashMap<String, double[]> histogramMap = new HashMap<>();

		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			String selectQuery = "SELECT image_name, histogram FROM histogramme";

			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(selectQuery);

			while (resultSet.next()) {
				String imageName = resultSet.getString("image_name");
				String histogramString = resultSet.getString("histogram");

				String[] histogramArray = histogramString.split(",");
				double[] histogram = new double[histogramArray.length];
				for (int i = 0; i < histogramArray.length; i++) {
					histogram[i] = Double.parseDouble(histogramArray[i]);
				}
				
				histogramMap.put(imageName, histogram);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return histogramMap;
	}

	public static void printHistoSQL(double histo[]) throws IOException {
		HashMap<String, Double> ha = new HashMap<>();

		for (HashMap.Entry<String, double[]> entry : retrieveHistogramsFromDatabase().entrySet()) {
			String image = entry.getKey();
			double[] histogram = entry.getValue();
			double similarity = calculateHistogramSimilarity(discretisationHisto(histo),(histogram));

			String imageString = image;
		//	System.out.println("image string "+imageString);
			ha.put(folderPath+"/"+imageString, similarity);
			
			
		

			Histogramme h = new Histogramme();

			// h.plotHistogram(histogram);

		
			System.out.println("Similarité "+similarity+" name "+imageString);

		
		
        

	}
		//System.out.println("taille hashmap "+ha);
		SwingUtilities.invokeLater(() -> new SimilarImagesInterface(chemin_img, ha));
	}
	

}
