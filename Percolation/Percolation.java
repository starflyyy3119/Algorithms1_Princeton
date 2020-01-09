/*
1.union(),connected() is useful
2.variables: n: record the length of the 2d array;
             site: 2d array to record which site is open;
             uf: which sites are connected to other sites
             openSites: record the number of open sites
3.plan how to map a 2d pair(row, col) to a 1-dimensional union find object index
4.write a private method for validating indices
5.write the open() method and the Percolation constructor
6.test the open() method and the Percolation constructor
*/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation {

    private final WeightedQuickUnionUF ufIsFull;  // defined to address the backwash problem
    private final WeightedQuickUnionUF uf;
    private boolean[][] sites;
    private final int n;
    private int openSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n should be larger than 1");
        this.n = n;
        openSites = 0;
        sites = new boolean[n][n];
        uf = new WeightedQuickUnionUF(n * n + 2);       // set the top virtual site and bottom virtual site
        ufIsFull = new WeightedQuickUnionUF(n * n + 1); // only have top virtual site
    }

    // open the sites(row, col) if it is not open already
    public void open(int row, int col) {
        assertValid(row, col);
        if (!isOpen(row, col)) {
            sites[row-1][col-1] = true;
            openSites++;
            // judge if left neighbor and right neighbor is open?
            if (col > 1 && isOpen(row, col-1)) {
                uf.union(mapping(row, col), mapping(row, col-1));
                ufIsFull.union(mapping(row, col), mapping(row, col-1));
            }
            if (col < n && isOpen(row, col+1)) {
                uf.union(mapping(row, col), mapping(row, col+1));
                ufIsFull.union(mapping(row, col), mapping(row, col+1));
            }
            // judge if top neighbor is open?
            if (row == 1) {
                uf.union(mapping(row, col), 0);
                ufIsFull.union(mapping(row, col), 0);
            }
            if (row > 1 && isOpen(row-1, col)) {
                uf.union(mapping(row, col), mapping(row-1, col));
                ufIsFull.union(mapping(row, col), mapping(row-1, col));
            }
            // judge if down neighbor is open?
            if (row == n) uf.union(mapping(row, col), n*n+1);
            if (row < n && isOpen(row+1, col)) {
                uf.union(mapping(row, col), mapping(row+1, col));
                ufIsFull.union(mapping(row, col), mapping(row+1, col));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        assertValid(row, col);
        return sites[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        assertValid(row, col);
        if (isOpen(row, col)) {
            return ufIsFull.connected(mapping(row, col), 0);
        }
        return false;
    }

    // returns the number of the open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolates?
    public boolean percolates() {
        return uf.connected(0, n*n+1);
    }

    // map the 2-d sites to uf
    private int mapping(int row, int col) {
        return ((row - 1) * n + col);
    }

    // judge whether the index is out of bound?
    private void assertValid(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) throw new IllegalArgumentException("row or col out of bound");
    }
}
