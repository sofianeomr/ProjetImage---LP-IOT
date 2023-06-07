import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SimilarImagesInterface extends JFrame {

    public SimilarImagesInterface(String mainImage, HashMap<String, Double> similarImages) {
        setTitle("Similar Images");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Créer un TreeMap pour trier les images similaires par ordre croissant de similitude
        TreeMap<Double, String> sortedImages = new TreeMap<>();
        for (Map.Entry<String, Double> entry : similarImages.entrySet()) {
            sortedImages.put(entry.getValue(), entry.getKey());
        }

        // Créer un panneau pour afficher les images similaires
        JPanel panel = new JPanel(new GridLayout(2, sortedImages.size() + 1));

        // Ajouter l'image principale à afficher
        JLabel mainImageLabel = new JLabel(new ImageIcon(mainImage));
        panel.add(new JLabel("Main Image"));
        panel.add(mainImageLabel);
        int a=0;
        // Ajouter les images similaires triées par ordre croissant de similitude
        for (Map.Entry<Double, String> entry : sortedImages.entrySet()) {
        	if(a<10) {
            String imageName = entry.getValue();
            Double similarity = entry.getKey();

            // Charger l'image à partir du nom (imageName) et la convertir en ImageIcon
            ImageIcon imageIcon = new ImageIcon(imageName); // Remplacez cela par votre chargement d'image réel

            JLabel similarImageLabel = new JLabel(imageIcon);
            panel.add(similarImageLabel);
            panel.add(new JLabel("Similarity: " + similarity));
        	}
        	a++;
        }

        add(panel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }
}
