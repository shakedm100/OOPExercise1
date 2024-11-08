import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameLogic implements PlayableLogic {

    private ArrayList<Disc> discs;
    private Disc[][] board;
    private final int boardLength = 8;
    private Player firstPlayer, secondPlayer;
    private boolean playerTurn; //True - first player, false - second player
    private Stack<Move> previousMoves;

    public GameLogic()
    {
        board = new Disc[boardLength][boardLength];
        discs = new ArrayList<>();
        previousMoves = new Stack<>();
        playerTurn = true;
    }
    @Override
    public boolean locate_disc(Position a, Disc disc)
    {
        if(board[a.row()][a.col()] == null && countFlips(a) > 0)
        {
            ArrayList<Disc> willFlip = new ArrayList<>();
            if(disc.getOwner().equals(firstPlayer))
                willFlip = checkAllFlips(a, secondPlayer);

            else
                willFlip = checkAllFlips(a, firstPlayer);
            flipDiscs(willFlip);

            playerTurn = !playerTurn; //Change turn

            board[a.row()][a.col()] = disc;
            discs.add(disc);

            previousMoves.push(new Move(willFlip,disc,a));

            return true;
        }
        return false;
    }

    private void flipDiscs(ArrayList<Disc> toFlip)
    {
        for (int i = 0; i < toFlip.size(); i++)
        {
            if(toFlip.get(i).getOwner().equals(firstPlayer))
                toFlip.get(i).setOwner(secondPlayer);
            else
                toFlip.get(i).setOwner(firstPlayer);
        }
    }

    @Override
    public Disc getDiscAtPosition(Position position)
    {
        return board[position.row()][position.col()];
    }

    @Override
    public int getBoardSize() {
        return 8;
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
     * @param move - the starting position to check
     * @param enemyPlayer - the enemy that's not currently playing
     * @return a list of all the possible moves
     */
    public ArrayList<Disc> checkAllFlips(Position move, Player enemyPlayer)
    {
        ArrayList<Disc> willFlips = new ArrayList<>();
        Position pos = new Position(move); //Copy constructor
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
        if(position.row() >= boardLength || position.col() >= boardLength)
            return true;
        if(position.row() < 0 || position.col() < 0)
            return true;
        return false;
    }

    /**
     * The method checks all the possible flips to one direction specified by the
     * row and column change
     * @param move - starting position
     * @param enemyPlayer - the player that's not currently playing
     * @param rowChange - the change in the rows
     * @param colChange - the change in the columns
     * @return a list of discs that will flip
     */
    private List<Disc> checkDiscsFlip(Position move, Player enemyPlayer, int rowChange, int colChange)
    {
        boolean check = true;
        List<Disc> tempWillFlip = new ArrayList<>();
        List<Disc> finalWillFlip = new ArrayList<>();

        Position pos = new Position(move);

        if(checkOutOfBound(pos) || getDiscAtPosition(pos) != null || enemyPlayer == null)
            return finalWillFlip;


        while(check)
        {
            pos.setCol(pos.col()+colChange);
            pos.setRow(pos.row()+rowChange);
            if(!checkOutOfBound(pos))
            {
                Disc disc = getDiscAtPosition(pos);
                if(disc != null)
                {
                    if(disc.getOwner().equals(enemyPlayer))
                    {
                        tempWillFlip.add(disc);
                    }
                    else
                    {
                        finalWillFlip.addAll(tempWillFlip);
                        //Finished checking
                        check = false;
                        tempWillFlip = new ArrayList<>();
                    }
                }
                else //If there is no disc, then
                {
                    //Finished checking
                    check = false;
                    //Delete Will Flip
                    tempWillFlip = new ArrayList<>();
                }
            }
            else //If out of bound, then reset list
            {
                check = false;
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
        return playerTurn;
    }

    @Override
    public boolean isGameFinished()
    {
        int moves = 0;
        Position position;
        for (int i = 0; i < boardLength; i++) {
            for (int j = 0; j < boardLength; j++) {
                position = new Position(i,j);
                if(playerTurn)
                    moves += checkAllFlips(position, firstPlayer).size();
                if(!playerTurn)
                    moves += checkAllFlips(position, secondPlayer).size();
            }
        }

        if(moves > 0)
            return false;
        return true;
    }

    @Override
    public void reset()
    {
        board = new Disc[boardLength][boardLength];
        discs = new ArrayList<>();
        SimpleDisc simple = new SimpleDisc(firstPlayer);
        Position position = new Position(3,3);
        if(firstPlayer != null)
        {
            //Clean previous discs
            discs = new ArrayList<>();
            //First
            discs.add(simple);
            board[position.row()][position.col()] = simple;


            //Second
            simple = new SimpleDisc(firstPlayer);
            position = new Position(4,4);
            discs.add(simple);
            board[position.row()][position.col()] = simple;
        }
        if(secondPlayer != null)
        {
            //Third
            position = new Position(3,4);
            simple = new SimpleDisc(secondPlayer);
            discs.add(simple);
            board[position.row()][position.col()] = simple;


            //Fourth
            position = new Position(4,3);
            simple = new SimpleDisc(secondPlayer);
            discs.add(simple);
            board[position.row()][position.col()] = simple;

        }
    }

    @Override
    public void undoLastMove()
    {
        previousMoves.pop().undo(firstPlayer, secondPlayer);
    }
}
