/* *****************************************************************************
 *  Name: Starfly
 *  Date: 2020/12/20
 *  Description: Brute Force Implementation
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    // initialize a treeSet to store the points
    private final TreeSet<Point2D> treeSet;

    public PointSET() {                                                          // construct and empty set of points
        treeSet = new TreeSet<>();
    }

    public boolean isEmpty() {                                                   // is the set empty?
        return treeSet.isEmpty();
    }

    public int size() {                                                          // number of points in the set
        return treeSet.size();
    }

    public void insert(
            Point2D p) {                                              // add the point to the set, if it is not already in the set
        if (p == null) {
            throw new IllegalArgumentException();
        }
        treeSet.add(p);
    }

    public boolean contains(
            Point2D p) {                                         // does the set contains Point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return treeSet.contains(p);
    }

    public void draw() {                                                         // draw all points to standard draw
        for (Point2D p : new ArrayList<>(treeSet)) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(
            RectHV rect) {                               // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> array = new ArrayList<>();
        for (Point2D p : new ArrayList<>(treeSet)) {
            if (rect.contains(p)) {
                array.add(p);
            }
        }
        return array;
    }


    public Point2D nearest(
            Point2D p) {                                // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) throw new IllegalArgumentException();
        if (treeSet.isEmpty()) {
            return null;
        }
        Point2D point = null;
        double minDis = (double) Integer.MAX_VALUE;
        for (Point2D tmpPoint : new ArrayList<>(treeSet)) {
            double tmpDis = p.distanceSquaredTo(tmpPoint);
            if (tmpDis < minDis) {
                point = tmpPoint;
                minDis = tmpDis;
            }
        }
        return point;
    }

    public static void main(String[] args) {
        PointSET ps = new PointSET();
        ps.insert(new Point2D(1, 2));
        ps.insert(new Point2D(3, 4));
        System.out.println(ps.nearest(new Point2D(10, 20)));
    }
}
