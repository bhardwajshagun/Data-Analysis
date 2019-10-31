/**
 * Import Collections, LinkedList, and List packages.
 */

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a single DataAnalysis that contains a list of Point2D objects.
 * Methods that can be performed on this class are adding new data points, returning data from the
 * list, finding the best-fit line using linear regression and clustering the data using
 * kmeans and RANSAC. This class implements the DataSet Interface.
 */
public class DataAnalysis implements DataSet {

  /**
   * List containing Point2D objects that have x and y coordinates.
   */
  private List<Point2D> dataList;

  /**
   * Constructs the DataAnalysis object that takes in no parameters and initializes it with an empty
   * list.
   */
  public DataAnalysis() {
    dataList = new LinkedList<>();
  }

  /**
   * Public method that adds a Point2D object to the list that takes in an x and y coordinate
   * that are given as parameters.
   *
   * @param x the x coordinate of the data point as a double data type.
   * @param y the y coordinate of the data point as a double data type.
   */
  public void addData(double x, double y) {
    dataList.add(new Point2D(x, y));
  }

  /**
   * Public getter method that returns the data list.
   *
   * @return the data list as a List data type.
   */
  public List<Point2D> getData() {
    return dataList;
  }

  /**
   * Public method that returns a best-fit line based off the lists of data points. This is
   * done by using the least-squares line fitting that will find the line that minimizes the
   * distance between the data points and the line.
   *
   * @return the best-fit line in the string format: ax + by + c = 0.
   */
  public String fitLine() {
    List listX = getListCoordinate('x');
    double sumX = findSum(listX);
    double averageX = findAverage(sumX, listX.size());
    double sxx = findSumSquares(listX, listX, averageX, averageX);
    List listY = getListCoordinate('y');
    double sumY = findSum(listY);
    double averageY = findAverage(sumY, listY.size());
    double syy = findSumSquares(listY, listY, averageY, averageY);
    double sxy = findSumSquares(listX, listY, averageX, averageY);
    double distance = (2 * sxy) / (sxx - syy);
    double theta = Math.toDegrees(Math.atan(distance));
    theta = findFT(theta, sxx, syy, sxy);
    double a = Math.cos(Math.toRadians(theta) / 2);
    double b = Math.sin(Math.toRadians(theta) / 2);
    double c = (-a * averageX) - (b * averageY);
    return round2Deci(a) + "x" + signValStr(b) + "y" + signValStr(c) + " = 0";
  }

  /**
   * Private helper method that taken in a number as a double and returns the correct string based
   * on if the number is negative or not rounded to 2 decimal points.
   *
   * @param v the number rounded as a double that is being returned as a string.
   * @return the correctly formatted String of the number rounded in the parameter.
   */
  private String signValStr(double v) {
    if (v > 0) {
      return " + " + round2Deci(v);
    } else {
      return " - " + round2Deci(Math.abs(v));
    }
  }

  /**
   * Private helper method that rounded the number given as a parameter and returns it rounded to 2
   * decimal places.
   *
   * @param d the number as a double that is being rounded
   * @return the number given as a double in the parameter rounded to 2 decimal places.
   */
  private double round2Deci(double d) {
    return (double) Math.round(d * 100) / 100;
  }

  /**
   * Private helper getter method that takes in a character of 'x' or 'y' and returns a new
   * list that contains only the x or y coordinates of the datalist containing Point2D objects.
   *
   * @param symbol the character that determines if the new list will contain 'x' or 'y'
   *               coordinates.
   * @return a list containing 'x' or 'y' coordinates based off the data list.
   */
  private List getListCoordinate(char symbol) {
    List<Double> listCoordinate = new LinkedList<>();
    for (int i = 0; i < dataList.size(); i++) {
      if (symbol == 'x') {
        listCoordinate.add(dataList.get(i).getX());
      } else {
        listCoordinate.add(dataList.get(i).getY());
      }
    }
    return listCoordinate;
  }

  /**
   * Private helper method that takes in a list of doubles and returns the sum of all the
   * numbers in the list.
   *
   * @param listCoor the list containing the doubles that are being summed up.
   * @return the sum of all the numbers being taken as a double data type.
   */
  private double findSum(List<Double> listCoor) {
    double sum = 0;
    for (int i = 0; i < listCoor.size(); i++) {
      sum += listCoor.get(i);
    }
    return sum;
  }

  /**
   * Private helper method that is finding the average based off a sum of numbers and the number of
   * numbers that were summed together.
   *
   * @param sumOfData the sum of the numbers we are taking the average of as a double data type.
   * @param num       the number of numbers that we are taking the average of as a double data
   *                  type.
   * @return the average as a double data type.
   */
  private double findAverage(double sumOfData, int num) {
    return sumOfData / num;
  }

  /**
   * Private helper method that is finding the sum of squares, which is the sum of the squared
   * deviations of the predicted values from the mean value, from the two lists containing numbers.
   *
   * @param list1    first list containing numbers as doubles.
   * @param list2    second list containing numbers as doubles.
   * @param average1 average value of the first data list of numbers as a double.
   * @param average2 average value of the second data list of numbers as a double.
   * @return the sum of squares as a double data type.
   */
  private double findSumSquares(List<Double> list1, List<Double> list2,
                                double average1, double average2) {
    double sumSquare = 0;

    for (int i = 0; i < list1.size(); i++) {
      sumSquare += ((list1.get(i) - average1) * (list2.get(i) - average2));
    }
    return sumSquare;
  }

  /**
   * Private helper method that finds the theta that makes the f(t) positive. It will return theta
   * in the parameter if the fT found is positive
   *
   * @param theta the angle in degrees of the distance as a double data type.
   * @param sxx   the sum of the squares of the difference between each 𝑥 and the mean 𝑥 value as
   *              a double data type.
   * @param syy   the sum of the squares of the difference between each y and the mean y value as a
   *              double data type.
   * @param sxy   sum of the product of the difference between 𝑥 and its means and the difference
   *              between 𝑦 and its mean.
   * @return the correct theta (theta or theta + 180) as a double data type.
   */
  private double findFT(double theta, double sxx, double syy, double sxy) {
    double fT = (syy - sxx) * Math.cos(Math.toRadians(theta))
            - (2 * sxy * Math.sin(Math.toRadians(theta)));
    if (fT > 0) {
      return theta;
    } else {
      return theta + 180;
    }
  }

  /**
   * Public method that takes an integer 'k' and performs k-means clustering on the data and returns
   * a lists of integers of the assigned clusters of each data point. The parameter taken in
   * is the number of clusters that will be formed when implementing the algorithm. RANSAC was
   * implemented by looping the k-means algorithm 10 times and returns the one with the lowest
   * percentage error.
   *
   * @param k the number of clusters as a integer that will result when implementing kmeans.
   * @return a list of integers of the clusters as a list data structure.
   * @throws IllegalArgumentException if k given is not a positive integer or if k is greater than
   *                                  the number of data points.
   */
  public List kmeans(int k) throws IllegalArgumentException {
    if (k <= 0) {
      throw new IllegalArgumentException("k must be positive.");
    }
    if (k > dataList.size()) {
      throw new IllegalArgumentException("k cannot be greater than number of data points.");
    }
    Double bestError = Double.MAX_VALUE;
    List<Integer> bestCluster = new LinkedList<>();
    for (int a = 0; a < 10; a++) {
      List<Point2D> centers = getCenters(dataList, k);
      List<Integer> clusters = new LinkedList<>();
      double error = Double.MAX_VALUE;
      double distance;
      double temp_distance;
      int index = -1;
      double percentError = Double.MAX_VALUE;
      int counter = 0;
      while (percentError > 0.01 && counter < 100) {
        clusters.clear();
        counter++;
        for (int i = 0; i < dataList.size(); i++) {
          distance = Double.MAX_VALUE;
          for (int j = 0; j < centers.size(); j++) {
            temp_distance = findEDistance(dataList.get(i), centers.get(j));
            if (temp_distance < distance) {
              distance = temp_distance;
              index = j;
            }
          }
          clusters.add(index);
        }
        double averageX;
        double averageY;
        double sumX;
        double sumY;
        int xCounter;
        int yCounter;
        for (int i = 0; i < k; i++) {
          sumX = 0;
          sumY = 0;
          xCounter = 0;
          yCounter = 0;
          for (int j = 0; j < dataList.size(); j++) {
            if (clusters.get(j) == i) {
              xCounter++;
              yCounter++;
              sumX += dataList.get(j).getX();
              sumY += dataList.get(j).getY();
            }
          }
          averageX = findAverage(sumX, xCounter);
          averageY = findAverage(sumY, yCounter);
          centers.set(i, new Point2D(averageX, averageY));
        }
        double totalError = 0;
        for (int i = 0; i < dataList.size(); i++) {
          totalError += findEDistance(dataList.get(i), centers.get(clusters.get(i)));
        }
        double newError = findAverage(totalError, dataList.size());
        percentError = Math.abs(newError - error) / error;
        error = newError;
      }
      if (percentError < bestError) {
        bestError = percentError;
        bestCluster.clear();
        for (int b = 0; b < clusters.size(); b++) {
          bestCluster.add(clusters.get(b));
        }
      }
    }
    return bestCluster;
  }

  /**
   * Private helper method that randomizes and selects a k number of centers from a list of
   * Point2D objects. The method creates a new list with the shuffled indexes of all the data
   * points in the data list given as a parameter and returns a list with the k number of
   * Point2D objects that were selected to be centers. This method was created to ensure the cluster
   * centers were randomized and unique in every instance.
   *
   * @param data the list that the centers are being pulled from randomly.
   * @param k    the number of centers that are being pulled from the data set given.
   * @return the list of Point2D objects that were selected to be centers.
   */
  private List getCenters(List<Point2D> data, int k) {
    List<Integer> randomNums = new LinkedList<>();
    for (int i = 0; i < data.size(); i++) {
      randomNums.add(i);
    }
    Collections.shuffle(randomNums);
    List<Integer> kIndexes = new LinkedList<>();
    for (int i = 0; i < k; i++) {
      kIndexes.add(randomNums.get(i));
    }
    List<Point2D> centers = new LinkedList<>();
    for (int i = 0; i < kIndexes.size(); i++) {
      centers.add(data.get(kIndexes.get(i)));
    }
    return centers;
  }

  /**
   * Private helper method that finds the Euclidean distance of the points given in the parameter
   * and returns that value.
   *
   * @param point1 the first Point2D object that is being used to find the Euclidean distance.
   * @param point2 the second Point2D object that is being used to find the Euclidean distance.
   * @return the Euclidean distance between the two points as a double data type.
   */
  private double findEDistance(Point2D point1, Point2D point2) {
    return Math.pow(Math.pow(point1.getX() - point2.getX(), 2)
            + Math.pow(point1.getY() - point2.getY(), 2), 0.5);
  }
}
