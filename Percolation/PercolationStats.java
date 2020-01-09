import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {

    private static final double SIGMA = 1.96;
    private final int trails;
    private final double mean;
    private final double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trails) {
        if (n <= 0 || trails <= 0) throw new IllegalArgumentException("illegal n or trails number");
        this.trails = trails;
        double[] a = new double[trails];
        for (int t = 0; t < trails; t++) {
            Percolation p = new Percolation(n);  // every trial needs a totally new p
            do {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!p.isOpen(row, col)) p.open(row, col);
            } while (!p.percolates());
            double openSites = (double) p.numberOfOpenSites();
            a[t] = openSites / (n * n);
        }
        mean = StdStats.mean(a);
        stddev = StdStats.stddev(a);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (trails == 1) return Double.NaN;
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        if (trails == 1) return Double.NaN;
        return mean - SIGMA * stddev / Math.sqrt(trails);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        if (trails == 1) return Double.NaN;
        return mean + SIGMA * stddev / Math.sqrt(trails);
    }

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println("mean                    = "+ps.mean());
        System.out.println("stddev                  = "+ ps.stddev());
        System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
