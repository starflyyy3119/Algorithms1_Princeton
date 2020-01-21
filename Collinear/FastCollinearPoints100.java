import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;

import java.util.Arrays;

/***********************************************************************************
 * Fast, sorting based solution for collinear points
 * for every p:
 *      think of p as the origin.
 *      for each other point q, determine the slope it makes with p.
 *      sort the points according to the slopes they makes with p.
 *      check if any 3(or more) adjacent points in the sorted order have equal
 *      slopes with respect to p. If so, these points together with p, are collinear.
 ***********************************************************************************/
public class FastCollinearPoints {

    private static final int CUTOFF = 10;
    private final LineSegment[] segments;
    private int numOfSeg = 0;
    private Point[] headPoints;
    private Point[] tailPoints;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point value : points) {
            if (value == null) throw new IllegalArgumentException();
        }

        // 数组是引用型数据类型，对其的操作会改变原有的points[] 数组，所以要先复制一个
        Point[] copy = new Point[points.length];
        System.arraycopy(points, 0, copy, 0, points.length);
        Arrays.sort(copy, 0, copy.length);
        for (int i = 1; i < copy.length; i++) {
            if (copy[i].compareTo(copy[i - 1]) == 0) throw new IllegalArgumentException(); // use compareTo()
        }
        if (copy.length < 4) {
            segments = new LineSegment[0];
        }
        else {

            // headPoints record the smaller points of the segments, while tailPoints record the larger points
            headPoints = new Point[CUTOFF];
            tailPoints = new Point[CUTOFF];

            // for each p, aux record other points by slope order
            Point[] aux = new Point[copy.length];

            Point[] recordCollinearPoints = new Point[copy.length];

            Point nowPoint;

            double preSlope;
            double nowSlope;
            double[] slopeToNowPoint = new double[copy.length];

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
                int collinearNumberOfPoints = 0;
                preSlope = slopeToNowPoint[1];
                recordCollinearPoints[collinearNumberOfPoints++] = aux[1];
                for (int m = 2; m < aux.length; m++) {
                    nowSlope = slopeToNowPoint[m];
                    // It may be precise to compare two float numbers, but in this situation it is ok
                    // because it is a / b, concretely (7 / 9 = 14 / 18)
                    if (preSlope == nowSlope && m != aux.length - 1) {
                        recordCollinearPoints[collinearNumberOfPoints++] = aux[m];
                    }
                    else if (preSlope == nowSlope && m == aux.length - 1) {
                        recordCollinearPoints[collinearNumberOfPoints++] = aux[m];
                        assertInsertSegment(collinearNumberOfPoints, recordCollinearPoints, nowPoint);
                    }
                    else {  // (preSlope != nowSlope)
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
                // System.out.println(segments[k].toString());

            }
        }
    }

    private void assertInsertSegment(int collinearNumberOfPoints, Point[] recordCollinearPoints, Point nowPoint) {
        if (collinearNumberOfPoints >= 3) {
            Point[] candidateLine  = findLine(collinearNumberOfPoints, recordCollinearPoints, nowPoint);
            if (!isInSegment(candidateLine)) {
                if (numOfSeg == headPoints.length) resize(2 * headPoints.length); // solve the out of bound problem
                headPoints[numOfSeg] = candidateLine[0];
                tailPoints[numOfSeg] = candidateLine[1];
                numOfSeg++;
            }
        }
    }

    private void resize(int capacity) {
        Point[] copyHead = new Point[capacity];
        Point[] copyTail = new Point[capacity];
        assert headPoints.length == tailPoints.length;
        System.arraycopy(headPoints, 0, copyHead, 0, headPoints.length);
        System.arraycopy(tailPoints, 0, copyTail, 0, tailPoints.length);
        headPoints = copyHead;
        tailPoints = copyTail;
    }

    private Point[] findLine(int collinearNumberOfPoints, Point[] recordCollinearPoints, Point nowPoint) {
        // Arrays.sort(recordCollinearPoints, 0, collinearNumberOfPoints); use compareTo()
        Point head = recordCollinearPoints[0];
        Point tail = recordCollinearPoints[collinearNumberOfPoints - 1];
        if (nowPoint.compareTo(head) < 0)        head = nowPoint;
        else if (nowPoint.compareTo(tail) > 0)   tail = nowPoint;
        Point[] headTail = new Point[2];
        headTail[0] = head;
        headTail[1] = tail;
        return headTail;
    }
    // the number of the line segments
    public int numberOfSegments() {
        return numOfSeg;
    }

    // the line segments
    public LineSegment[] segments() {
        // don't expose the private variable in a class directly
        LineSegment[] outSegments = new LineSegment[numOfSeg];
        System.arraycopy(segments, 0, outSegments, 0, numOfSeg);
        return outSegments;
    }

    private boolean isInSegment(Point[] line) {
        assert headPoints.length == tailPoints.length;
        if (numOfSeg == 0) return false;
//        for (int i = 0; i < numOfSeg; i++) {
//
//            // bad style to write code that depends on the particular format of the output from the toString method
//            // especially if your reason for doing so is to circumvent the public API
//            // (which intentionally does not provide access to the x- and y-coordinates).
//            // but I don't know how to solve this problem
//            // I know, maintain headPoints and tailPoints to record the smaller points of each segment and bigger points
//
//            if (line[0].compareTo(headPoints[i]) == 0 && line[1].compareTo(tailPoints[i]) == 0) return true;  // use equals to compare the content of two strings
//        }
        Point head = line[0];
        Point tail = line[1];
        Point headLast = headPoints[numOfSeg - 1];
        if (head.compareTo(headLast) > 0) return false;
        if (head.compareTo(headLast) == 0) {
//            for (int j = numOfSeg - 1; j >= 0; j--) {
//                // pay attention to the logic here, only the last half is not enough
//                //                  example: (1, 2) -> (3, 4)
//                //                           (2, 3) -> (7, 8)
//                //                  if at present, the candidate line is (2, 3) -> (3, 4)
//                //                  if only have the last half, we will miss the (2, 3) -> (3, 4)
//                if (head.compareTo(headPoints[j]) == 0 && tail.compareTo(tailPoints[j]) == 0) return true;
//            }
            // the upper method is out of time, because it use too much useless compareTo()
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

        FastCollinearPoints fcp = new FastCollinearPoints(points);
        LineSegment[] lineSeg = fcp.segments();
        System.out.println(fcp.numOfSeg);

        for (LineSegment lineSegment : lineSeg) {
            lineSegment.draw();
        }

    }
}

// draw the workflow for this problem

// time: find the compareTo() method in Point is too much
// (mark: 94; 29 / 41 test passed; total number of compareTo(): 10285152731)
//       first try: to find the largest and smallest point in recordCollinearPoints using a linear method instead of sorting it.(not work)
//       second try: find that aux is already sorted using slopeOrder, therefore don't need to sort the recordCollinearPoints
// (mark: 94; 29 / 41 test passed; total number of compareTo(): 10283508395)
//       third try: something wrong with isInSegment(too much compareTo())
//                  consider the item in headPoints, it has increase order.
//
