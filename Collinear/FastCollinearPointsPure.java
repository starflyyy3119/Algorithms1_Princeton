import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import java.util.Arrays;

/***********************************************************************************
 *
 * Fast, sorting based solution for collinear points
 * for every p:
 *      think of p as the origin.
 *      for each other point q, determine the slope it makes with p.
 *      sort the points according to the slopes they makes with p.
 *      check if any 3(or more) adjacent points in the sorted order have equal
 *      slopes with respect to p. If so, these points together with p, are collinear.
 *
 ***********************************************************************************/

public class FastCollinearPointsPure {
    private int numOfSeg = 0;                             // record the number of segments
    private static final int CUTOFF = 10;                 // give the original length of the headPoints and tailPoints
    private final LineSegment[] segments;                 // record the collinear segments of the given data
    private Point[] headPoints;                           // record the smaller endpoint of each segment
    private Point[] tailPoints;                           // record teh larger endpoint of each segment

    /** finds all line segments containing 4 or more points
     *
     * @param points an array of points
     * @throws IllegalArgumentException if points is null
     *                                  if any of the value in points is null
     *                                  if there are duplicate value in points
     */
    public FastCollinearPointsPure(Point[] points) {

        // check whether points is null
        if (points == null) throw new IllegalArgumentException();

        // check if any of the value in points is null
        for (Point value : points) { if (value == null) throw new IllegalArgumentException(); }

        // check if there are duplicate value in points
        Point[] copy = new Point[points.length]; // copy the original points array to avoid making changes of it
        System.arraycopy(points, 0, copy, 0, points.length);
        Arrays.sort(copy, 0, copy.length);
        for (int i = 1; i < copy.length; i++) {
            if (copy[i].compareTo(copy[i - 1]) == 0) throw new IllegalArgumentException();
        }

        if (copy.length < 4) { segments = new LineSegment[0]; }
        else {
            // for each p, aux array record other points by slope with p
            Point[] aux = new Point[copy.length];

            // recordCollinearPoints used to record the collinear points in every situation
            Point[] recordCollinearPoints = new Point[copy.length];

            // nowPoint denotes the point being considered now
            Point nowPoint;

            // temporary variable used to find collinear points
            double preSlope;
            double nowSlope;

            // slopeToNowPoint record the slopes of nowPoint and other points
            double[] slopeToNowPoint = new double[copy.length];

            // initialize headPoints and tailPoints
            headPoints = new Point[CUTOFF];
            tailPoints = new Point[CUTOFF];

            for (Point point : copy) {

                // think of p as an origin
                nowPoint = point;

                // sort the other points according to the slope to p
                System.arraycopy(copy, 0, aux, 0, copy.length);
                Arrays.sort(aux, 0, aux.length, nowPoint.slopeOrder()); // use compareTo()

                // get the specific slope of p to other points
                for (int k = 0; k < aux.length; k++) {
                    slopeToNowPoint[k] = nowPoint.slopeTo(aux[k]);
                }

                // check if any 3(or more) adjacent points in the sorted order have equal slopes with respect to p.
                int collinearNumberOfPoints = 0;         // temporary variable used to record the present number of collinear points

                preSlope = slopeToNowPoint[1];
                recordCollinearPoints[collinearNumberOfPoints++] = aux[1];
                for (int m = 2; m < aux.length; m++) {
                    nowSlope = slopeToNowPoint[m];
                    if (preSlope == nowSlope && m != aux.length - 1) {
                        recordCollinearPoints[collinearNumberOfPoints++] = aux[m];
                    } else if (preSlope == nowSlope && m == aux.length - 1) {
                        recordCollinearPoints[collinearNumberOfPoints++] = aux[m];
                        assertInsertSegment(collinearNumberOfPoints, recordCollinearPoints, nowPoint);
                    } else {  // (preSlope != nowSlope)
                        assertInsertSegment(collinearNumberOfPoints, recordCollinearPoints, nowPoint);
                        collinearNumberOfPoints = 0;
                        preSlope = nowSlope;
                        recordCollinearPoints[collinearNumberOfPoints++] = aux[m];
                    }
                }
            }
            segments = new LineSegment[numOfSeg];
            for (int k = 0; k < numOfSeg; k++) {
                segments[k] = new LineSegment(headPoints[k], tailPoints[k]);
            }
        }
    }

    /**
     * find whether the collinear points in this situation make a new segment
     * @param collinearNumberOfPoints temporary variable used to record the number of collinear points at this situation
     * @param recordCollinearPoints temporary array used to record the collinear points at this situation
     * @param nowPoint the point being considered now
     */
    private void assertInsertSegment(int collinearNumberOfPoints, Point[] recordCollinearPoints, Point nowPoint) {
        if (collinearNumberOfPoints >= 3) {
            Point[] candidateLine = findLine(collinearNumberOfPoints, recordCollinearPoints, nowPoint);
            if (!isInSegment(candidateLine)) {
                if (numOfSeg == headPoints.length) resize(2 * headPoints.length); // solve the out of bound problem
                headPoints[numOfSeg] = candidateLine[0];
                tailPoints[numOfSeg] = candidateLine[1];
                numOfSeg++;
            }
        }
    }

    /**
     * resize the headPoints and tailPoints array
     * @param capacity an int number which is the length of the target length of headPoints and tailPoints
     */
    private void resize(int capacity) {
        Point[] copyHead = new Point[capacity];
        Point[] copyTail = new Point[capacity];
        System.arraycopy(headPoints, 0, copyHead, 0, headPoints.length);
        System.arraycopy(tailPoints, 0, copyTail, 0, tailPoints.length);
        headPoints = copyHead;
        tailPoints = copyTail;
    }

    /**
     * find two endpoints from the recordCollinearPoints array
     * @param collinearNumberOfPoints temporary variable used to record the number of collinear points at this situation
     * @param recordCollinearPoints temporary array used to record the collinear points at this situation
     * @param nowPoint the point being considered now
     * @return a point array with two entries, the first one is the smaller endpoint and the second one is the larger endpoint
     */
    private Point[] findLine(int collinearNumberOfPoints, Point[] recordCollinearPoints, Point nowPoint) {
        Point head = recordCollinearPoints[0];
        Point tail = recordCollinearPoints[collinearNumberOfPoints - 1];
        if (nowPoint.compareTo(head) < 0) head = nowPoint;
        else if (nowPoint.compareTo(tail) > 0) tail = nowPoint;
        Point[] headTail = new Point[2];
        headTail[0] = head;
        headTail[1] = tail;
        return headTail;
    }

    /**
     * number of segments
     * @return return the number of segments
     */
    public int numberOfSegments() {
        return numOfSeg;
    }

    /**
     * the line segments
     * @return return the collinear segments of the given data
     */
    public LineSegment[] segments() {
        LineSegment[] outSegments = new LineSegment[numOfSeg];
        System.arraycopy(segments, 0, outSegments, 0, numOfSeg);
        return outSegments;
    }

    /**
     * check whether line is already in the segments
     * @param line a line, represented by its two endpoints
     * @return true if line is in segments; false if not
     */
    private boolean isInSegment(Point[] line) {
        assert headPoints.length == tailPoints.length;
        if (numOfSeg == 0) return false;
        Point head = line[0];
        Point tail = line[1];
        Point headLast = headPoints[numOfSeg - 1];
        if (head.compareTo(headLast) > 0) return false;
        if (head.compareTo(headLast) == 0) {
            for (int j = numOfSeg - 1; j >= 0 && headPoints[j] == head; j--) {
                if (tail.compareTo(tailPoints[j]) == 0) return true;
            }
            return false;
        }
        return true;
    }
    public static void main(String[] args) {
        StdDraw.setScale(0, 32768);
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.RED);
        Point[] points = new Point[StdIn.readInt()];
        int index = 0;
        while (!StdIn.isEmpty()) {
            points[index] = new Point(StdIn.readInt(), StdIn.readInt());
            points[index].draw();
            index++;
        }
        FastCollinearPointsPure fcp = new FastCollinearPointsPure(points);
        LineSegment[] lineSeg = fcp.segments();
        System.out.println(fcp.numberOfSegments());

        for (LineSegment lineSegment : lineSeg) {
            lineSegment.draw();
        }
    }
}
