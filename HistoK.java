import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import org.jfree.chart.ChartUtilities;

import java.awt.*;

public class HistoK {
	public static void plotHistogram(double[] histogramR, double[] histogramG, double[] histogramB) {
        // Création des données pour les histogrammes
        DefaultCategoryDataset dataset = createDataset(histogramR, histogramG, histogramB);

        // Création du graphique
        JFreeChart chart = createChart(dataset);

        // Création du panneau de graphique
        ChartPanel chartPanel = new ChartPanel(chart);

        // Configuration de la fenêtre
        JFrame frame = new JFrame("Histogrammes des canaux RGB");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 3));
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private static DefaultCategoryDataset createDataset(double[] histogramR, double[] histogramG, double[] histogramB) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < histogramR.length; i++) {
            dataset.addValue(histogramR[i], "Canal Rouge", Integer.toString(i));
            dataset.addValue(histogramG[i], "Canal Vert", Integer.toString(i));
            dataset.addValue(histogramB[i], "Canal Bleu", Integer.toString(i));
        }
        return dataset;
    }

    private static JFreeChart createChart(DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart(
                "Histogrammes des canaux RGB", // Titre du graphique
                "Niveaux de gris", // Étiquette de l'axe des X
                "Nombre de pixels", // Étiquette de l'axe des Y
                dataset, // Données du graphique
                PlotOrientation.VERTICAL, // Orientation du graphique
                true, // Inclure la légende
                true, // Inclure les tooltips
                false // Inclure les URLs
        );

        CategoryPlot plot = chart.getCategoryPlot();

        // Personnalisation de l'axe des X
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLowerMargin(0);
        domainAxis.setUpperMargin(0);

        // Personnalisation de l'axe des Y
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // Personnalisation des rendus des barres pour chaque série
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setItemMargin(0.02);

        return chart;
    }
    
    public static void saveHistogram(double[] histogramR, double[] histogramG, double[] histogramB, String pathToSave) throws IOException   {
        // Création des données pour les histogrammes
        DefaultCategoryDataset dataset = createDataset(histogramR, histogramG, histogramB);

        // Création du graphique
        JFreeChart chart = createChart(dataset);

        // Personnalisation du graphique
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);
        CategoryAxis domainAxis = plot.getDomainAxis();
        
        domainAxis.setLowerMargin(0);
        domainAxis.setUpperMargin(0);

        // Sauvegarde du graphique
        if (pathToSave != null) {
            File file = new File(pathToSave);
            ChartUtilities.saveChartAsPNG(file, chart, 900, 600);
        }
    }
}

