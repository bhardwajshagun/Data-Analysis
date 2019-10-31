/**
 * Import List package.
 */
import java.util.List;

/**
 * This interface contains all operations that all DataSet classes should contain. It should contain
 * these methods: addData, getData, fitLine, and kmeans.
 */
public interface DataSet {

  /**
   * Method that adds a Point2D object to the list that takes in an x and y coordinate that
   * are given as parameters.
   *
   * @param x the x coordinate of the data point as a double data type.
   * @param y the y coordinate of the data point as a double data type.
   */
  void addData(double x, double y);

  /**
   * Getter method that returns the data list.
   *
   * @return the data list as a List data type.
   */
  List<Point2D> getData();

  /**
   * Method that returns a best-fit line based off the lists of data points. This is done by
   * using the least-squares line fitting that will find the line that minimizes the distance
   * between the data points and the line.
   *
   * @return the best-fit line in the string format: ax + by + c = 0.
   */
  String fitLine();

  /**
   * Method that takes an integer 'k' and performs k-means clustering on the data and returns a
   * lists of integers of the assigned clusters of each data point. The parameter taken in is
   * the number of clusters that will be formed when implementing the algorithm. RANSAC was
   * implemented by looping the k-means algorithm 10 times and returns the one with the lowest
   * percentage error.
   *
   * @param k the number of clusters as a integer that will result when implementing kmeans.
   * @return a list of integers of the clusters as a list data structure.
   * @throws IllegalArgumentException if k given is not a positive integer.
   */
  List kmeans(int k) throws IllegalArgumentException;

}
