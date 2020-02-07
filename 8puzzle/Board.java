import edu.princeton.cs.algs4.Stack;
/*************************************************************************
 *
 *  Board is a data type that models an n-by-n board with sliding tiles.
 *
 *************************************************************************/
public class Board {

    private final int n;
    private final int[][] tiles;
    private int zeroRow;
    private int zeroCol;

    // create a board from an n-by-n array of tiles, where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles)
    {
        if (tiles == null) throw new IllegalArgumentException();
        n = tiles.length;
        this.tiles = new int[n][n];
        for (int row = 0; row < n; row++)
        {
            for (int col = 0; col < n; col++)
            {
                this.tiles[row][col] = tiles[row][col];
                if (tiles[row][col] == 0)
                {
                    zeroRow = row;
                    zeroCol = col;
                }
            }
        }
    }

    // string representation of this board
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(tiles.length).append("\n");
        for (int[] tile : tiles) {
            for (int col = 0; col < tiles.length; col++) {
                s.append(String.format("%2d", tile[col])).append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension()
    {
        return n;
    }

    // number of tiles out of place
    public int hamming()
    {
        int totalNum = n * n;  // totalNum records the total number of the grids in the board
        int count = 0;
        for (int row = 0; row < n; row++)
        {
            for (int col = 0; col < n; col++)
            {
                if ((row+1) * (col+1) < totalNum && tiles[row][col] != row * n + col + 1) count++;
            }
        }
        return count;
    }

    // sum of Manhattan distance between tiles and goal
    public int manhattan()
    {
        int mDistance = 0;
        for (int row = 0; row < n; row++)
        {
            for (int col = 0; col < n; col++)
            {
                int nowItem = tiles[row][col];
                if (!(nowItem == 0))
                {
                    int goalRow = (nowItem-1) / n;
                    int goalCol = nowItem - goalRow * n - 1;
                    int rowDis = (goalRow > row) ? (goalRow - row) : (row - goalRow);
                    int colDis = (goalCol > col) ? (goalCol - col) : (col - goalCol);
                    mDistance += (rowDis + colDis);
                }
            }
        }
        return mDistance;
    }

    // is this board the goal board?
    public boolean isGoal()
    {
        return (manhattan() == 0);
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y)
    {
        // a quick test to see if the objects are identical
        if (this == y) return true;

        // must return false if the explicit parameter is null
        if (y == null) return false;

        // if the classes don't match, then can't be equal
        if (getClass() != y.getClass()) return false;

        // now we know y is a non-null Board
        Board that = (Board) y;

        // test whether this and that are identical
        if (n != that.n) return false;
        for (int row = 0; row < n; row++)
        {
            for (int col = 0; col < n; col++)
            {
                if (tiles[row][col] != that.tiles[row][col]) return false;
            }
        }
        return true;
    }

    // defining a nested class in the program suggested poor design, can use stack or queue
    // all neighboring boards
    /* public Iterable<Board> neighbors()
    {
        return new IterableNeighbor();
    }

    private class IterableNeighbor implements Iterable<Board>
    {
        @Override
        public Iterator<Board> iterator() {
            return new IteratorNeighbor();
        }
    }

    private class IteratorNeighbor implements Iterator<Board>
    {
        private int total = 0, count = 0;
        private Board[] boards = new Board[4];
        private IteratorNeighbor()
        {
            int[][] tilesInner = new int[n][n];
            for (int i = 0; i < tiles.length; i++)
                System.arraycopy(tiles[i], 0, tilesInner[i], 0, tiles.length);

            // left
            if (zeroCol > 0)
            {
                exch(tilesInner, zeroRow, zeroCol, zeroRow, zeroCol-1);
                boards[total++] = new Board(tilesInner);
                exch(tilesInner, zeroRow, zeroCol, zeroRow, zeroCol-1);
            }
            // right
            if (zeroCol < n-1)
            {
                exch(tilesInner, zeroRow, zeroCol, zeroRow, zeroCol+1);
                boards[total++] = new Board(tilesInner);
                exch(tilesInner, zeroRow, zeroCol, zeroRow, zeroCol+1);
            }
            // up
            if (zeroRow > 0)
            {
                exch(tilesInner, zeroRow, zeroCol, zeroRow-1, zeroCol);
                boards[total++] = new Board(tilesInner);
                exch(tilesInner, zeroRow, zeroCol, zeroRow-1, zeroCol);
            }
            // down
            if (zeroRow < n-1)
            {
                exch(tilesInner, zeroRow, zeroCol, zeroRow+1, zeroCol);
                boards[total++] = new Board(tilesInner);
                exch(tilesInner, zeroRow, zeroCol, zeroRow+1, zeroCol);
            }
        }
        @Override
        public boolean hasNext() {
            return count < total;
        }

        @Override
        public Board next() {
            if (!hasNext()) throw new NoSuchElementException();
            return boards[count++];
        }
    } */

    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        Stack<Board> neighbors = new Stack<>();
        int[][] tilesInner = new int[n][n];
        for (int row = 0; row < n; row++)
        {
            System.arraycopy(tiles[row], 0, tilesInner[row], 0, n);
        }
        // left
        if (zeroCol > 0)
        {
            exch(tilesInner, zeroRow, zeroCol, zeroRow, zeroCol-1);
            neighbors.push(new Board(tilesInner));
            exch(tilesInner, zeroRow, zeroCol, zeroRow, zeroCol-1);
        }
        // right
        if (zeroCol < n-1)
        {
            exch(tilesInner, zeroRow, zeroCol, zeroRow, zeroCol+1);
            neighbors.push(new Board(tilesInner));
            exch(tilesInner, zeroRow, zeroCol, zeroRow, zeroCol+1);
        }
        // up
        if (zeroRow > 0)
        {
            exch(tilesInner, zeroRow, zeroCol, zeroRow-1, zeroCol);
            neighbors.push(new Board(tilesInner));
            exch(tilesInner, zeroRow, zeroCol, zeroRow-1, zeroCol);
        }
        // down
        if (zeroRow < n-1)
        {
            exch(tilesInner, zeroRow, zeroCol, zeroRow+1, zeroCol);
            neighbors.push(new Board(tilesInner));
            exch(tilesInner, zeroRow, zeroCol, zeroRow+1, zeroCol);
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin()
    {
        int firstNum = 1;
        int secondNum = n*n - 1;
        int firstNumRow = 0;
        int firstNumCol = 0;
        int secondNumRow = 0;
        int secondNumCol = 0;
        boolean flag1 = false, flag2 = false;
        for (int row = 0; row < n; row++)
        {
            for (int col = 0; col < n; col++)
            {
                if (flag1 && flag2) break;
                if (tiles[row][col] == firstNum)
                {
                    firstNumRow = row;
                    firstNumCol = col;
                    flag1 = true;
                }
                else if (tiles[row][col] == secondNum)
                {
                    secondNumRow = row;
                    secondNumCol = col;
                    flag2 = true;
                }
            }
        }
        exch(tiles, firstNumRow, firstNumCol, secondNumRow, secondNumCol);
        Board b = new Board(tiles);
        exch(tiles, firstNumRow, firstNumCol, secondNumRow, secondNumCol);
        return b;
    }

    private void exch(int[][] tilesArray, int row1, int col1, int row2, int col2)
    {

        int tmp = tilesArray[row1][col1];
        tilesArray[row1][col1] = tilesArray[row2][col2];
        tilesArray[row2][col2] = tmp;
    }

    public static void main(String[] args)
    {
        int[][] tiles = new int[3][3];
        tiles[0][0] = 0;
        tiles[0][1] = 1;
        tiles[0][2] = 3;
        tiles[1][0] = 4;
        tiles[1][1] = 2;
        tiles[1][2] = 5;
        tiles[2][0] = 7;
        tiles[2][1] = 8;
        tiles[2][2] = 6;
        Board b = new Board(tiles);
        System.out.println(b.toString());
        System.out.println("----------------------------------");
        System.out.println(b.isGoal());
        System.out.println(b.manhattan());
        System.out.println(b.isGoal());
    }
}
