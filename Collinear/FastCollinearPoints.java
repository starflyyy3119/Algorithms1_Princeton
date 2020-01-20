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

    private LineSegment[] segments;
    private int numOfSeg = 0;
    private LineSegment[] auxiliary;

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
            if (copy[i].compareTo(copy[i - 1]) == 0) throw new IllegalArgumentException();
        }
        if (copy.length < 4) {
            segments = new LineSegment[0];
        }
        else {

            auxiliary = new LineSegment[copy.length];

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
                Arrays.sort(aux, 0, aux.length, nowPoint.slopeOrder());

                // get the specific slope of p to other points
                for (int k = 0; k < aux.length; k++) {
                    slopeToNowPoint[k] = nowPoint.slopeTo(aux[k]);
                }
                int collinearNumberOfPoints = 0;
                preSlope = slopeToNowPoint[1];
                recordCollinearPoints[collinearNumberOfPoints++] = aux[1];
                for (int m = 2; m < aux.length; m++) {
                    nowSlope = slopeToNowPoint[m];
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
            System.arraycopy(auxiliary, 0, segments, 0, segments.length);

        }
    }

    private void assertInsertSegment(int collinearNumberOfPoints, Point[] recordCollinearPoints, Point nowPoint) {
        if (collinearNumberOfPoints >= 3) {
            LineSegment candidateLine  = findLine(collinearNumberOfPoints, recordCollinearPoints, nowPoint);
            if (!isInSegment(candidateLine)) {
                if (numOfSeg == auxiliary.length) resize(2 * auxiliary.length); // solve the out of bound problem
                auxiliary[numOfSeg++] = candidateLine;
            }
        }
    }

    private void resize(int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        System.arraycopy(auxiliary, 0, copy, 0, auxiliary.length);
        auxiliary = copy;
    }

    private LineSegment findLine(int collinearNumberOfPoints, Point[] recordCollinearPoints, Point nowPoint) {
        Arrays.sort(recordCollinearPoints, 0, collinearNumberOfPoints);
        Point head = recordCollinearPoints[0];
        Point tail = recordCollinearPoints[collinearNumberOfPoints-1];
        if (nowPoint.compareTo(head) < 0)        head = nowPoint;
        else if (nowPoint.compareTo(tail) > 0)   tail = nowPoint;
        return new LineSegment(head, tail);
    }
    // the number of the line segments
    public int numberOfSegments() {
        return numOfSeg;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] outSegments = new LineSegment[segments.length];
        System.arraycopy(segments, 0, outSegments, 0, segments.length);
        return outSegments;
    }

    private boolean isInSegment(LineSegment line) {
        if (numOfSeg == 0) return false;
        for (int i = 0; i < numOfSeg; i++) {

            // bad style to write code that depends on the particular format of the output from the toString method
            // especially if your reason for doing so is to circumvent the public API
            // (which intentionally does not provide access to the x- and y-coordinates).
            // but I don't know how to solve this problem

            if (line.toString().equals(auxiliary[i].toString())) return true;  // use equals to compare the content of two strings
        }
        return false;
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