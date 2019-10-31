import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * This class contains a Main that plots the images for data read from the files provided.
 * It calls methods from the ImagePlotter class to create the images. It also calls methods
 * from the DataAnalysis class to analyze the data.
 */
public class DataAnalysisMain {

  /**
   * Main method that reads the data from files given using Scanner. Uses the DataAnalysis
   * class to either do linear regression or k-means clustering on the data. Uses the ImagePlotter
   * class to plot the data points and create the graph for the desired analysis.
   *
   * @param args the default parameter for a Java main.
   */
  public static void main(String[] args) {
    int counter = 0;
    String[] filenames = new String[]{"linedata-1", "linedata-2", "linedata-3", "clusterdata-2",
                                      "clusterdata-3", "clusterdata-4", "clusterdata-6"};
    Color[] availableColors = new Color[]{Color.RED, Color.BLUE, Color.GREEN,
                                          Color.MAGENTA, Color.ORANGE, Color.CYAN};
    int[] numClusters = new int[]{2, 3, 4, 6};
    for (String fname : filenames) {
      try {
        DataAnalysis inputData = new DataAnalysis();
        Scanner sc = new Scanner(new File(fname + ".txt"));
        while (sc.hasNextLine()) {
          String line = sc.nextLine();
          String[] coordinates = line.split(" ");
          double num1 = Double.parseDouble(coordinates[0]);
          double num2 = Double.parseDouble(coordinates[1]);
          inputData.addData(num1, num2);
        }
        ImagePlotter plotter = new ImagePlotter();
        plotter.setWidth(600);
        plotter.setHeight(600);
        if (counter < 3) {
          plotter.setDimensions(-450, 450, -450, 450);
        } else {
          plotter.setDimensions(-0, 450, -450, 450);
        }
        for (int i = 0; i < inputData.getData().size(); i++) {
          double x = inputData.getData().get(i).getX();
          double y = inputData.getData().get(i).getY();
          plotter.addPoint((int) Math.round(x), (int) Math.round(y));
        }
        if (counter < 3) {
          //linear regression on data from first 3 files
          String lineEquation = inputData.fitLine();
          String[] variables = lineEquation.split(" ");
          double a = Double.parseDouble(variables[0].substring(0, variables[0].length() - 1));
          double b = Double.parseDouble(variables[2].substring(0, variables[2].length() - 1));
          if (variables[1].equals("-")) {
            b *= -1;
          }
          double c = Double.parseDouble(variables[4]);
          if (variables[3].equals("-")) {
            c *= -1;
          }
          double lineY1 = (-a * -450 - c) / b;
          double lineY2 = (-a * 450 - c) / b;
          plotter.addLine(-450, (int) Math.round(lineY1), 450, (int) Math.round(lineY2), Color.RED);
        } else {
          //k-means clustering data for the last 4 files
          List<Integer> clustersList = inputData.kmeans(numClusters[counter - 3]);
          for (int i = 0; i < inputData.getData().size(); i++) {
            double x = inputData.getData().get(i).getX();
            double y = inputData.getData().get(i).getY();
            plotter.addPoint((int) Math.round(x), (int) Math.round(y),
                              availableColors[clustersList.get(i)]);
          }
        }
        sc.close();
        counter++;
        try {
          plotter.write(fname + "_Graph.png");
        } catch (IOException e) {
          //Error writing the file
        }
      } catch (FileNotFoundException e) {
        //Error reading the file
      }
    }
  }

}
