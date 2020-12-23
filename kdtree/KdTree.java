/* *****************************************************************************
 *  Name: Starfly
 *  Date: 2020/12/22
 *  Description: 2-d Tree(BST implementation)
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;


public class KdTree {
    private class Node {
        private final Point2D key;
        private final int coordinate;
        // coordinate indicates which coordinate should its children be focused on.
        // 1 indicates that x-coordinate and -1 indicates that y-coordinate
        private Node left, right;

        public Node(Point2D key, int coordinate) {
            this.key = key;
            this.coordinate = coordinate;
        }
    }

    private Node root;
    private int numOfPoints;

    public KdTree() {                                                           // construct and empty set of points
        numOfPoints = 0;
    }

    public boolean isEmpty() {                                                   // is the set empty?
        return numOfPoints == 0;
    }

    public int size() {                                                          // number of points in the set
        return numOfPoints;
    }

    public void insert(
            Point2D p) {                                                         // add the point to the set, if it is not already in the set
        if (p == null) throw new IllegalArgumentException();
        if (!this.contains(p)) {
            numOfPoints++;
            root = insert(-1, root, p);
        }
    }

    private Node insert(int preCoordinate, Node x, Point2D p) {
        if (x == null) return new Node(p, -preCoordinate);
        double cmp = compare(p, x.key, x.coordinate);
        if (cmp < 0) {
            x.left = insert(x.coordinate, x.left, p);
        }
        else {
            x.right = insert(x.coordinate, x.right, p);
        }
        return x;
    }

    private double compare(Point2D a, Point2D b, int coordinate) {
        if (coordinate > 0) {
            return a.x() - b.x();
        }
        else {
            return a.y() - b.y();
        }
    }

    public boolean contains(
            Point2D p) {                                                         // does the set contains Point p?
        if (p == null) throw new IllegalArgumentException();
        return contains(root, p);
    }

    private boolean contains(Node x, Point2D p) {
        if (x == null) return false;
        double cmp = compare(p, x.key, x.coordinate);
        if (cmp < 0) {
            return contains(x.left, p);
        }
        else if (cmp > 0) {
            return contains(x.right, p);
        }
        else {
            return compare(p, x.key, -x.coordinate) == 0.0 || contains(x.right, p);
        }
    }

    public void draw() {                                                         // draw all points to standard draw
        draw(root, 0, 1, 0, 1);
    }

    private void draw(Node p, double xMin, double xMax, double yMin, double yMax) {
        if (p == null) return;
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(p.key.x(), p.key.y());
        StdDraw.setPenRadius(0.005);
        if (p.coordinate > 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(p.key.x(), yMin, p.key.x(), yMax);
            draw(p.left, xMin, p.key.x(), yMin, yMax);
            draw(p.right, p.key.x(), xMax, yMin, yMax);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(xMin, p.key.y(), xMax, p.key.y());
            draw(p.left, xMin, xMax, yMin, p.key.y());
            draw(p.right, xMin, xMax, p.key.y(), yMax);
        }
    }

    public Iterable<Point2D> range(
            RectHV rect) {                               // all points that are inside the rectangle (or on the boundary)
        if (this.isEmpty()) {
            return new ArrayList<Point2D>();
        }
        if (rect == null) throw new IllegalArgumentException();
        return range(rect, root, new RectHV(0, 0, 1, 1));

    }

    private ArrayList<Point2D> range(RectHV rect, Node p, RectHV nowRect) {
        if (p == null) return new ArrayList<Point2D>();
        ArrayList<Point2D> arrayList = new ArrayList<>();
        if (rect.contains(p.key)) arrayList.add(p.key);
        if (nowRect.intersects(rect)) {
            if (p.coordinate > 0) {
                RectHV newRectLeft = new RectHV(nowRect.xmin(), nowRect.ymin(), p.key.x(),
                                                nowRect.ymax());
                RectHV newRectRight = new RectHV(p.key.x(), nowRect.ymin(), nowRect.xmax(),
                                                 nowRect.ymax());

                if (newRectLeft.intersects(rect))
                    arrayList.addAll(range(rect, p.left, newRectLeft));
                if (newRectRight.intersects(rect))
                    arrayList.addAll(range(rect, p.right, newRectRight));
            }
            else {
                RectHV newRectDown = new RectHV(nowRect.xmin(), nowRect.ymin(), nowRect.xmax(),
                                                p.key.y());
                RectHV newRectUp = new RectHV(nowRect.xmin(), p.key.y(), nowRect.xmax(),
                                              nowRect.ymax());
                if (newRectDown.intersects(rect))
                    arrayList.addAll(range(rect, p.left, newRectDown));
                if (newRectUp.intersects(rect))
                    arrayList.addAll(range(rect, p.right, newRectUp));
            }
        }
        return arrayList;
    }


    public Point2D nearest(
            Point2D p) {                                // a nearest neighbor in the set to point p; null if the set is empty
        if (this.isEmpty()) {
            return null;
        }
        if (p == null) throw new IllegalArgumentException();
        if (this.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return nearest(p, new RectHV(0, 0, 1, 1), root, root.key);
    }

    private Point2D nearest(Point2D target, RectHV rectHV, Node now,
                            Point2D candidate) { // rectHV is the rectangle of corresponding to now Node
        if (now == null) return candidate;

        if (candidate.distanceSquaredTo(target) > rectHV.distanceSquaredTo(target)) {
            if (candidate.distanceSquaredTo(target) > now.key.distanceSquaredTo(target))
                candidate = now.key; // only if the current node is closer then update

            if (now.coordinate > 0) {
                RectHV newRectLeft = new RectHV(rectHV.xmin(), rectHV.ymin(), now.key.x(),
                                                rectHV.ymax());
                RectHV newRectRight = new RectHV(now.key.x(), rectHV.ymin(), rectHV.xmax(),
                                                 rectHV.ymax());

                if (newRectLeft.distanceSquaredTo(target) < newRectRight
                        .distanceSquaredTo(target)) {
                    candidate = nearest(target, newRectLeft, now.left, candidate);
                    return nearest(target, newRectRight, now.right, candidate);
                }
                else {
                    candidate = nearest(target, newRectRight, now.right, candidate);
                    return nearest(target, newRectLeft, now.left, candidate);
                }
            }
            else {
                RectHV newRectDown = new RectHV(rectHV.xmin(), rectHV.ymin(), rectHV.xmax(),
                                                now.key.y());
                RectHV newRectUp = new RectHV(rectHV.xmin(), now.key.y(), rectHV.xmax(),
                                              rectHV.ymax());
                if (newRectDown.distanceSquaredTo(target) < newRectUp.distanceSquaredTo(target)) {
                    candidate = nearest(target, newRectDown, now.left, candidate);
                    return nearest(target, newRectUp, now.right, candidate);
                }
                else {
                    candidate = nearest(target, newRectUp, now.right, candidate);
                    return nearest(target, newRectDown, now.left, candidate);
                }

            }
        }
        return candidate;
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        System.out.println(kdTree.nearest(new Point2D(0.1, 0.2)));
    }
}
