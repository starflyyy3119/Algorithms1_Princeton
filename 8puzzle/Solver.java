import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/************************************************************************************
 * A* search algorithm to solve the n-by-n slider puzzles problem.
 *
 * First, insert the initial search node (the initial board, 0 moves, and a null
 * previous search node) into a priority queue.
 *
 * Then, delete from the priority queue the search node the minimum priority, and
 * insert onto the priority queue all neighboring search nodes (those that can be
 * reached in one move from the dequeued search node).
 *
 * Repeat this procedure until the search node dequeued corresponds to the goal
 * board.
 ************************************************************************************/
public class Solver
{
    private final SearchNode goalSearchNode;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        if (initial == null) throw new IllegalArgumentException();

        Board twin = initial.twin();

        // insert the initial search node into the priority queue
        SearchNode sni = new SearchNode(initial, null);
        MinPQ<SearchNode> minPQInitial = new MinPQ<>();
        minPQInitial.insert(sni);

        SearchNode snt = new SearchNode(twin, null);
        MinPQ<SearchNode> minPQTwin = new MinPQ<>();
        minPQTwin.insert(snt);

        // delete from the priority queue the search node with the minimum priority
        // insert onto the priority queue all neighboring search nodes.
        while (true)
        {
            SearchNode nowMinNodeInitial = minPQInitial.delMin();
            SearchNode nowMinNodeTwin = minPQTwin.delMin();

            if (nowMinNodeInitial.board.isGoal())
            {
                goalSearchNode = nowMinNodeInitial;
                break;
            }
            if (nowMinNodeTwin.board.isGoal())
            {
                goalSearchNode = null;
                break;
            }
            for (Board nowBoardNeighbor : nowMinNodeInitial.board.neighbors())
            {
                if (nowMinNodeInitial.preSearchNode == null || !nowBoardNeighbor.equals(nowMinNodeInitial.preSearchNode.board))
                {
                    SearchNode nowNeighborNode = new SearchNode(nowBoardNeighbor, nowMinNodeInitial);
                    minPQInitial.insert(nowNeighborNode);
                }
            }

            for (Board nowBoardNeighbor : nowMinNodeTwin.board.neighbors())
            {
                if (nowMinNodeTwin.preSearchNode == null || !nowBoardNeighbor.equals(nowMinNodeTwin.preSearchNode.board))
                {
                    SearchNode nowNeighborNode = new SearchNode(nowBoardNeighbor, nowMinNodeTwin);
                    minPQTwin.insert(nowNeighborNode);
                }
            }
        }
    }

    private static class SearchNode implements Comparable<SearchNode>
    {
        Board board = null;
        int move, priority, manhattan;
        SearchNode preSearchNode = null;
        private SearchNode(Board now, SearchNode pre)
        {
            preSearchNode = pre;
            if (preSearchNode == null) this.move = 0;
            else this.move = preSearchNode.move + 1;   // preSearchNode != null
            board = now;
            manhattan = now.manhattan();
            priority = manhattan + this.move;
        }

        @Override
        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    // is the initial board solvable?
    public boolean isSolvable()
    {
        return goalSearchNode != null;
    }

    // min number of moves to solve initial board
    public int moves()
    {
        if (goalSearchNode == null) return -1;
        return goalSearchNode.move;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution()
    {
        if (!isSolvable()) return null;
        Stack<Board> solution = new Stack<>();
        SearchNode nowSearchNode = goalSearchNode;
        while (nowSearchNode != null)
        {
            solution.push(nowSearchNode.board);
            nowSearchNode = nowSearchNode.preSearchNode;
        }
        return solution;
    }

    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

// comparable, comparator
// iterable, iterator

// critical optimization
// The critical optimization needs to check whether a board is equal to a neighbor of its neighbor.
// That is the only place where you need equals(). Why do you need to use equals() elsewhere?