import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A JUnit test class for the DataAnalysis class.
 */
public class DataAnalysisTest {

  @Test
  public void testGetX() {
    Point2D firstPoint = new Point2D(3.2, 6.3);
    Point2D secPoint = new Point2D(-1, 5);
    Point2D thirdPoint = new Point2D(300000, 3);

    assertEquals(3.2, firstPoint.getX(), 0.01);
    assertEquals(-1, secPoint.getX(), 0.01);
    assertEquals(300000, thirdPoint.getX(), 0.01);
  }

  @Test
  public void testGetY() {
    Point2D firstPoint = new Point2D(-4, -20.3);
    Point2D secPoint = new Point2D(4, 22);
    Point2D thirdPoint = new Point2D(0, 0);

    assertEquals(-20.3, firstPoint.getY(), 0.01);
    assertEquals(22, secPoint.getY(), 0.01);
    assertEquals(0, thirdPoint.getY(), 0.01);
  }

  @Test
  public void testAddGetData() {
    DataAnalysis testDataList = new DataAnalysis();

    testDataList.addData(20, 20.2);
    testDataList.addData(10, 2);
    testDataList.addData(0, -4);


    assertEquals(20, testDataList.getData().get(0).getX(), 0.01);
    assertEquals(10, testDataList.getData().get(1).getX(), 0.01);
    assertEquals(0, testDataList.getData().get(2).getX(), 0.01);

    assertEquals(20.2, testDataList.getData().get(0).getY(), 0.01);
    assertEquals(2, testDataList.getData().get(1).getY(), 0.01);
    assertEquals(-4, testDataList.getData().get(2).getY(), 0.01);
  }

  @Test
  public void testPositiveSlopeRegressionLine() {
    DataAnalysis zeroTest = new DataAnalysis();
    zeroTest.addData(-6, -10);
    zeroTest.addData(-1, -3);
    zeroTest.addData(-4, -7);
    zeroTest.addData(3, 2);
    zeroTest.addData(7, 8);
    zeroTest.addData(11, 10);

    assertEquals("0.78x - 0.63y - 1.29 = 0", zeroTest.fitLine());
  }

  @Test
  public void testNegativeSlopeRegressionLine() {
    DataAnalysis zeroTest = new DataAnalysis();
    zeroTest.addData(-30, 22);
    zeroTest.addData(-3, 10);
    zeroTest.addData(5, 8);
    zeroTest.addData(6, 3);
    zeroTest.addData(7, -2);
    zeroTest.addData(8, -21);

    assertEquals("0.7x + 0.72y - 1.57 = 0", zeroTest.fitLine());
  }

  @Test
  public void testZeroSlopeRegressionLine() {
    DataAnalysis zeroTest = new DataAnalysis();
    zeroTest.addData(3, 8);
    zeroTest.addData(4, 8);
    zeroTest.addData(5, 8);
    zeroTest.addData(6, 8);
    zeroTest.addData(7, 8);
    zeroTest.addData(8, 8);

    assertEquals("0.0x + 1.0y - 8.0 = 0", zeroTest.fitLine());
  }

  @Test
  public void testInfinitySlopeRegressionLine() {
    DataAnalysis infinityTest = new DataAnalysis();
    infinityTest.addData(5, 0);
    infinityTest.addData(5,1);
    infinityTest.addData(5, 2);
    infinityTest.addData(5, 3);
    infinityTest.addData(5, 4);
    infinityTest.addData(5, 5);
    infinityTest.addData(5, 6);

    assertEquals("1.0x - 0.0y - 5.0 = 0", infinityTest.fitLine());
  }

  @Test
  public void testAlmostInfinitySlopeRegressionLine() {
    DataAnalysis infinityTest = new DataAnalysis();
    infinityTest.addData(-1, 10);
    infinityTest.addData(-1,0);
    infinityTest.addData(-1, 12);
    infinityTest.addData(-1, 3);
    infinityTest.addData(-1, 5);
    infinityTest.addData(-1, -9);
    infinityTest.addData(-2, 6);

    assertEquals("1.0x + 0.01y + 1.11 = 0", infinityTest.fitLine());
  }

  @Test
  public void testAlmostZeroSlopeRegressionLine() {
    DataAnalysis infinityTest = new DataAnalysis();
    infinityTest.addData(-3, 0);
    infinityTest.addData(-2,0);
    infinityTest.addData(-1, 0);
    infinityTest.addData(0, 0);
    infinityTest.addData(5, 0);
    infinityTest.addData(10, 0);
    infinityTest.addData(8, -1);

    assertEquals("0.03x + 1.0y + 0.06 = 0", infinityTest.fitLine());
  }

  @Test
  public void testOneParameterKmeans() {
    DataAnalysis oneClusterKmeans = new DataAnalysis();
    oneClusterKmeans.addData(0,0);
    oneClusterKmeans.addData(10,2);
    oneClusterKmeans.addData(3,2);
    oneClusterKmeans.addData(330,20);
    oneClusterKmeans.addData(60,-21);
    oneClusterKmeans.addData(22,-0.2);
    oneClusterKmeans.addData(4,9);

    List<Integer> oneCenterList = oneClusterKmeans.kmeans(1);

    for (int i = 0; i < oneCenterList.size(); i++) {
      assertTrue(oneCenterList.get(i) == 0);
    }
  }

  @Test
  public void testTwoParameterKmeans() {
    DataAnalysis twoClusterKmeans = new DataAnalysis();
    twoClusterKmeans.addData(0,0);
    twoClusterKmeans.addData(1,0);
    twoClusterKmeans.addData(0,1);
    twoClusterKmeans.addData(10,10);
    twoClusterKmeans.addData(10,11);
    twoClusterKmeans.addData(12,10);
    twoClusterKmeans.addData(12,9);

    List<Integer> twoCenterList = twoClusterKmeans.kmeans(2);

    List<Point2D> centers = new LinkedList<>();
    centers.add(new Point2D(0, 0));
    centers.add(new Point2D(0, 0));

    //Finding the centers for each cluster and testing which center each data point is closest too
    //Test if kmeans cluster is same as test cluster
    for (int i = 0; i < twoClusterKmeans.getData().size(); i++) {
      centers.set(twoCenterList.get(i), new Point2D(centers.get(twoCenterList.get(i)).getX()
                  + twoClusterKmeans.getData().get(i).getX(),
                centers.get(twoCenterList.get(i)).getY()
                   + twoClusterKmeans.getData().get(i).getY()));
    }
    for (int i = 0; i < centers.size(); i++) {
      centers.set(i, new Point2D(centers.get(i).getX() / twoClusterKmeans.getData().size(),
              centers.get(i).getY() / twoClusterKmeans.getData().size()));
    }
    for (int i = 0; i < twoClusterKmeans.getData().size(); i++) {
      double bestDistance = Double.POSITIVE_INFINITY;
      int index = -1;
      for (int j = 0; j < centers.size(); j++) {
        double distance = Math.pow(Math.pow(twoClusterKmeans.getData().get(i).getX()
                - centers.get(j).getX(), 2)
                + Math.pow(twoClusterKmeans.getData().get(i).getY()
                - centers.get(j).getY(), 2), 0.5);
        if (distance < bestDistance) {
          bestDistance = distance;
          index = j;
        }
      }
      assertEquals(index, twoCenterList.get(i), 0.00);
    }
  }

  @Test
  public void testManyParameterKmeans() {
    DataAnalysis manyClusterKmeans = new DataAnalysis();

    manyClusterKmeans.addData(-1, 10);
    manyClusterKmeans.addData(-1,11);
    manyClusterKmeans.addData(0, 12);
    manyClusterKmeans.addData(1, 14);
    manyClusterKmeans.addData(2,13);
    manyClusterKmeans.addData(2, 2);
    manyClusterKmeans.addData(-1, 1);
    manyClusterKmeans.addData(0,1);
    manyClusterKmeans.addData(-2, 2);
    manyClusterKmeans.addData(-1,1);
    manyClusterKmeans.addData(22, -12);
    manyClusterKmeans.addData(21, -11);
    manyClusterKmeans.addData(19,-13);
    manyClusterKmeans.addData(23, -12);
    manyClusterKmeans.addData(20, -11);
    manyClusterKmeans.addData(22, 25);
    manyClusterKmeans.addData(21, 22);
    manyClusterKmeans.addData(19,23);
    manyClusterKmeans.addData(23, 21);
    manyClusterKmeans.addData(20, 20);
    manyClusterKmeans.addData(-22, 19);
    manyClusterKmeans.addData(-21, 19);
    manyClusterKmeans.addData(-19,19);
    manyClusterKmeans.addData(-23, 20);
    manyClusterKmeans.addData(-20, 20);

    List<Integer> manyCentersList = manyClusterKmeans.kmeans(5);

    for (int i = 0; i < manyCentersList.size(); i++) {
      assertTrue(manyCentersList.get(i) < 5);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalNegativeParameterKmeans() {
    DataAnalysis illegalDataKmeans = new DataAnalysis();
    illegalDataKmeans.addData(-1, 10);
    illegalDataKmeans.addData(5,30);
    illegalDataKmeans.addData(2, 2);

    illegalDataKmeans.kmeans(-20);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalZeroParameterKmeans() {
    DataAnalysis illegalDataKmeans = new DataAnalysis();
    illegalDataKmeans.addData(-90, 0);
    illegalDataKmeans.addData(52,3);
    illegalDataKmeans.addData(4, 22);

    illegalDataKmeans.kmeans(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testkTooLargeKmeans() {
    DataAnalysis illegalDataKmeans = new DataAnalysis();
    illegalDataKmeans.addData(-90, 0);
    illegalDataKmeans.addData(52,3);
    illegalDataKmeans.addData(4, 22);

    illegalDataKmeans.kmeans(4);
  }

}