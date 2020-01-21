import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;

import java.util.Arrays;

/*****************************************************************
 * Brute force method for the collinear points problem:
 * To check whether the 4 points p, q, r, s are collinear, check
 * whether the three slopes between p and q, between p and r, and
 * between p and s are all equal.
 *****************************************************************/

public class BruteCollinearPoints {

    private final LineSegment[] segments;
    /**
     * Find all segments containing 4 points
     *
     * @param  points an array of point
     * @throws IllegalArgumentException if the argument to the constructor
     * is null
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException();
        }
        Point[] aux = new Point[points.length];
        System.arraycopy(points, 0, aux, 0, points.length);
        Arrays.sort(aux);
        for (int i = 1; i < aux.length; i++) {
            if (aux[i].compareTo(aux[i - 1]) == 0) throw new IllegalArgumentException();
        }
        LineSegment[] auxiliary = new LineSegment[points.length];
        Point[] fourPoint = new Point[4];
        int index = 0;
        for (int i = 0; i < aux.length - 3; i++) {
            for (int j = i + 1; j < aux.length - 2; j++) {
                for (int k = j + 1; k < aux.length - 1; k++) {
                    for (int m = k + 1; m < aux.length; m++) {
                        Point p = aux[i];
                        Point q = aux[j];
                        Point r = aux[k];
                        Point s = aux[m];
                        if (p.slopeOrder().compare(q, r) == 0 && p.slopeOrder().compare(q, s) == 0) {
                            fourPoint[0] = p;
                            fourPoint[1] = q;
                            fourPoint[2] = r;
                            fourPoint[3] = s;
                            Arrays.sort(fourPoint);
                            auxiliary[index++] = new LineSegment(fourPoint[0], fourPoint[3]);
                        }
                    }
                }
            }
        }
        segments = new LineSegment[index];
        System.arraycopy(auxiliary, 0, segments, 0, segments.length);
    }

    public LineSegment[] segments() {
        // return a defensive copy
        LineSegment[] outSegments = new LineSegment[segments.length];
        System.arraycopy(segments, 0, outSegments, 0, segments.length);
        return outSegments;
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public static void main(String[] args) {
        StdDraw.setScale(-1000, 30000);
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.RED);
        Point[] points = new Point[StdIn.readInt()];
        int index = 0;
        while (!StdIn.isEmpty()) {
            points[index] = new Point(StdIn.readInt(), StdIn.readInt());
            points[index].draw();
            index++;
        }

        BruteCollinearPoints bcp = new BruteCollinearPoints(points);
        LineSegment[] lineSeg = bcp.segments();
        System.out.println(lineSeg.length);
        for (LineSegment lineSegment : lineSeg) {
            lineSegment.draw();
        }

    }
}
