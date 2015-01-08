// Algorithm I Assignment 4
// 8 puzzle

public class Solver {
    private Stack<Board> path = new Stack<Board>();
    private boolean solvable;

    /** Solve the 8 puzzle board. */
    public Solver(Board initial) {
        // Solve a twin board at the same time in case this board is insolvable.
        Board twin = initial.twin();
        // Use a game tree to keep track of board moves. Add the initial board. 
        MinPQ<Node> gameTree = new MinPQ<Node>(); 
        gameTree.insert(new Node(initial, null, 0));        
        MinPQ<Node> twinTree = new MinPQ<Node>();
        twinTree.insert(new Node(twin, null, 0));
        
        Node popNode, twinNode;
        Board popBoard = initial, twinBoard = twin; 
        int moves, twinMoves; 
        
        while (true) {
            // remove the board with lowest priority, check if it's goal board
            popNode = gameTree.delMin();
            popBoard = popNode.board; 
            /* path.push(popBoard);  <-- Mistake! Could save intermediate boards ... 
            ... that do not leade to the goal boards. */
            if (popBoard.isGoal()) {
                solvable = true;
                addToPath(popNode);  // trace the path from goal to initial board
                break;
            }
            // do the same thing for twin board, but no need to save its path
            twinNode = twinTree.delMin();
            twinBoard = twinNode.board;   
            if (twinBoard.isGoal()) {
                solvable = false;
                break;
            }
            // if not goal board, add neighboring boards to game tree
            moves = popNode.moves + 1;
            for (Board board : popBoard.neighbors()) {
                if (moves == 1 || !board.equals(popNode.previous.board)) 
                    gameTree.insert(new Node(board, popNode, moves));
            }
            twinMoves = twinNode.moves + 1;
            for (Board board : twinBoard.neighbors()) {
                if (moves == 1 || !board.equals(twinNode.previous.board))
                    twinTree.insert(new Node(board, twinNode, moves));
            }
        }
    }
    
    // add or del nodes to the game tree. 
    private class Node implements Comparable<Node> {
        // each node has four fields
        private Board board;
        private Node previous;
        private int moves, priority;
        // constructor
        private Node(Board b, Node prev, int mov) {
            board = b;
            previous = prev;
            moves = mov;
            priority = moves + board.manhattan();                
        }
        // nodes are compared by the current board's priority
        public int compareTo(Node that) {
            return this.priority - that.priority;
        }
    }
    
    // add to path the sequence of boards that lead to goal board 
    private void addToPath(Node goal) {
        do {            
            path.push(goal.board);
            goal = goal.previous;
        } while (goal != null);
    }
    
    /** return whether the initial board is solvable or not */
    public boolean isSolvable() {
        return solvable;
    }
    
    /** return the number of moves needed to the goal board 
      * return 0 if the initial board is unsolvable */
    public int moves() {
        return path.size() - 1;
    }
    
    /** retuan an iterator of boards in the path */
    public Iterable<Board> solution() {
        if (solvable) return path;
        return null;
    }
    
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
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