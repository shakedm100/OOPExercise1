import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {

    private ArrayList<Disc> discs;
    private Disc[][] board;
    private final int boardLength = 8;
    private Player firstPlayer, secondPlayer;
    private Stack<Move> previousMoves;

    @Override
    public boolean locate_disc(Position a, Disc disc)
    {
        if(board[a.col()][a.row()] == null && countFlips(a) > 0)
        {
            board[a.col()][a.row()] = disc;
            discs.add(disc);
            return true;
        }
        return false;
    }

    @Override
    public Disc getDiscAtPosition(Position position)
    {
        return board[position.col()][position.row()];
    }

    @Override
    public int getBoardSize() {
        return 8*8;
    }

    //Method runs at O(n) when n < 64
    @Override
    public List<Position> ValidMoves()
    {
        List<Position> valid = new ArrayList<>();
        //No valid  moves, return empty array list
        if(discs.size() == 64)
            return valid;

        //Build all the combinations for board positions
        for (int i = 0; i < boardLength; i++)
        {
            for (int j = 0; j < boardLength; j++)
            {
                Position position = new Position(i,j);
                if(countFlips(position) > 0 && board[i][j] == null)
                    valid.add(new Position(i,j));
            }
        }

        return valid;
    }

    @Override
    public int countFlips(Position pos)
    {
        Player enemyPlayer;
        if(isFirstPlayerTurn())
        {
            enemyPlayer = secondPlayer;
        }
        else
        {
            enemyPlayer = firstPlayer;
        }

        return checkAllFlips(pos, enemyPlayer).size();
    }

    /**
     * This function checks all the possible flips from every direction
     * @param pos - the starting position to check
     * @param enemyPlayer - the enemy that's not currently playing
     * @return a list of all the possible moves
     */
    public ArrayList<Disc> checkAllFlips(Position pos, Player enemyPlayer)
    {
        ArrayList<Disc> willFlips = new ArrayList<>();
        willFlips.addAll(checkDiscsFlip(pos, enemyPlayer, 0, 1));
        willFlips.addAll(checkDiscsFlip(pos, enemyPlayer, 0, -1));
        willFlips.addAll(checkDiscsFlip(pos, enemyPlayer, 1, 0));
        willFlips.addAll(checkDiscsFlip(pos, enemyPlayer, -1, 0));
        willFlips.addAll(checkDiscsFlip(pos, enemyPlayer, 1,1));
        willFlips.addAll(checkDiscsFlip(pos, enemyPlayer, -1,-1));
        willFlips.addAll(checkDiscsFlip(pos, enemyPlayer, 1,-1));
        willFlips.addAll(checkDiscsFlip(pos, enemyPlayer, -1,1));

        return willFlips;
    }

    /**
     * This function checks if a given position is out of bound
     * It depends on the final boardLength variable, so it is better
     * For "changeability" if the board size changes
     * @param position - a given position to check
     * @return true if position is out of bound
     */
    private boolean checkOutOfBound(Position position)
    {
        if(position.row() > boardLength || position.col() > boardLength)
            return true;
        if(position.row() < 0 || position.col() < 0)
            return true;
        return false;
    }

    /**
     * The method checks all the possible flips to one direction specified by the
     * row and column change
     * @param pos - starting position
     * @param enemyPlayer - the player that's not currently playing
     * @param rowChange - the change in the rows
     * @param colChange - the change in the columns
     * @return a list of discs that will flip
     */
    private List<Disc> checkDiscsFlip(Position pos, Player enemyPlayer, int rowChange, int colChange)
    {
        boolean check = true;
        List<Disc> tempWillFlip = new ArrayList<>();
        List<Disc> finalWillFlip = new ArrayList<>();

        while(check)
        {
            pos.setCol(pos.col()+colChange);
            pos.setCol(pos.row()+rowChange);
            Disc disc = getDiscAtPosition(pos);
            if(!checkOutOfBound(pos) || disc != null)
            {
                if(!disc.getOwner().equals(enemyPlayer))
                    finalWillFlip.addAll(tempWillFlip);
                else
                    tempWillFlip.add(disc);
            }
            else
            {
                //Finished checking
                check = false;
                //Delete Will Flip
                tempWillFlip = new ArrayList<>();
            }
        }

        return finalWillFlip;
    }


    @Override
    public Player getFirstPlayer()
    {
        return firstPlayer;
    }

    @Override
    public Player getSecondPlayer()
    {
        return secondPlayer;
    }

    @Override
    public void setPlayers(Player player1, Player player2)
    {
        this.firstPlayer = player1;
        this.secondPlayer = player2;
    }

    @Override
    public boolean isFirstPlayerTurn() {
        return false;
    }

    @Override
    public boolean isGameFinished()
    {
        int moves = ValidMoves().size();
        if(moves == 0)
            return false;
        return true;
    }

    @Override
    public void reset()
    {
        //Clean previous discs
        discs = new ArrayList<>();
        //First
        SimpleDisc simple = new SimpleDisc(firstPlayer);
        board[3][3] = simple;
        discs.add(simple);

        //Second
        board[4][4] = simple;
        discs.add(simple);

        //Third
        simple.setOwner(secondPlayer);
        board[4][3] = simple;
        discs.add(simple);

        //Fourth
        board[3][4] = simple;
        discs.add(simple);
    }

    @Override
    public void undoLastMove()
    {
        previousMoves.pop().undo(firstPlayer, secondPlayer);
    }
}
