// Algorithm Assignemtn 4 
// 8 puzzle - Make Board

public class Board {
    private int N; // dimension of board
    private int[] board; // use an array to represent a board
    
    public Board(int[][] blocks) {
        // assume blocks is a square
        N = blocks.length;
        board = new int[N*N];
        // copy values
        int k = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) 
                board[k++] = blocks[i][j];
        }
        // Q: add code to recognize if this blocks is suitble for Board? 
    }
    
    public int dimension() {
        return N;
    }
    
    /** number of blocks out of place */
    public int hamming() {
        // time ~N
        int ham = 0;
        for (int i = 0; i < N*N; i++) {
            if (board[i] != i + 1)
                ham++;
        }
        return --ham;  // 0 doesn't count
    }
    
    /** sum of Manhattan distance between this Board and goal Board */
    public int manhattan() {
        // time ~N
        int manh = 0;
        int n, goalRow, goalCol, k = 0;
        for (int row = 0;  row < N; row++) {
            for (int col = 0; col < N; col++) {
                n = board[k++];
                if (n > 0 && n != k) {
                    goalRow = (n - 1) / N;
                    goalCol = (n - 1) % N;
                    manh += Math.abs(goalCol - col) + Math.abs(goalRow - row);
                }
            }
        }
        return manh;
    }
    
    /** is this board the goal board? */
    public boolean isGoal() {
        for (int i = 0; i < N*N - 1; i++) {
            if (board[i] != i + 1)
                return false;
        }
        return true;
    }
    
    /** Make a twin board by swapping two adjencent values in a row */
    public Board twin() {
        // chekc if 0 is in the first two position
        if (board[0] == 0 || board[1] == 0)
            // swap the first two elements in the second row
            return swappedBoard(N, N+1);   
        else
            // swap the first two elements
            return swappedBoard(0, 1);
    }
    
    /** return whether two boards are equal */
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (that.N != N) return false;
        for (int i = 0; i < N*N; i++) {
            if (board[i] != that.board[i]) return false;
        }
        return true;
    }
    
    /** return an iterable type of all neighboring boards 
      * tip: add items to a stack or queue and return that */
    public Iterable<Board> neighbors() {
        Stack<Board> nbs = new Stack<Board>(); 
        // Find 0 
        int index0 = 0;
        for (int i = 0; i < N*N; i++) {
            if (board[i] == 0) {
                index0 = i;
                break;
            }
        }
        // Add neighbor boards
        if (index0 / N != 0) // not on the first row, swap up
            nbs.push(swappedBoard(index0, index0 - N));
        if (index0 / N != N-1) // not on the last row, swap down
            nbs.push(swappedBoard(index0, index0 + N));
        if (index0 % N != 0) // not on the first colomn, swap left
            nbs.push(swappedBoard(index0, index0 - 1));
        if (index0 % N != N-1)  // not on the last column, swap right
            nbs.push(swappedBoard(index0, index0 + 1));
        return (Iterable<Board>) nbs; 
    }
    
    // Return a new board that swaps item at i and j from this board
    private Board swappedBoard(int i, int j) {
        // copy values to a new int[][] blocks
        int[][] blocks = new int[N][N];
        int k = 0;
        for (int p = 0; p < N; p++) {
            for (int q = 0; q < N; q++) 
                blocks[p][q] = board[k++];
        }
        // swap values at specified position
        int tmp = blocks[i/N][i%N];
        blocks[i/N][i%N] = blocks[j/N][j%N];
        blocks[j/N][j%N] = tmp;
        // return the new swapped board
        return new Board(blocks);
    }
    
    /** string representation of the board */
    public String toString() {
        String res = Integer.toString(N) + "\n";
        int k = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) 
                res += " " + board[k++] + " ";
            res += "\n";
        }
        return res;
    }
    
}