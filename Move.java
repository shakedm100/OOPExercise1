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
