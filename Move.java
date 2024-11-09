public class Move {

    private final Disc[][] board;
    private Disc lastPlaced;
    private final Position position;

    public Move(Disc[][] board, Disc lastPlaced, Position position)
    {
        this.board = board;
        this.lastPlaced = lastPlaced;
        this.position = position;
    }

    /**
    public void undo(Disc[][] currentBoard)
    {
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++)
            {
                currentBoard[i][j] = board[i][j];
            }
        }

        lastPlaced = null;
    }
*/

    public Disc disc()
    {
        return lastPlaced;
    }

    public Position position()
    {
        return position;
    }

    public Disc[][] getBoard(){ return board; }

    public void setDisc(Disc disc) { lastPlaced = disc; }
}
