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

    /**
     * TODO:
     * 1. Game finish logic doesn't work well with AI
     * 2. Towards the end of the game aiMove receives null and an exception occurs
     * 3. Implement Unflippable and bomb logic
     * 4. Make sure all the prints are placed and correct
     */
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
            //Create a deep copy
            Disc[][] copy = new Disc[board.length][];
            for (int i = 0; i < board.length; i++) {
                copy[i] = new Disc[board[i].length];
                for (int j = 0; j < board[i].length; j++) {
                    if(board[i][j] != null)
                    {
                        if(board[i][j] instanceof SimpleDisc)
                            copy[i][j] = new SimpleDisc(board[i][j].getOwner());
                        else if(board[i][j] instanceof UnflippableDisc)
                            copy[i][j] = new UnflippableDisc(board[i][j].getOwner());
                        else
                            copy[i][j] = new BombDisc(board[i][j].getOwner());
                    }
                }
            }
            previousMoves.push(new Move(copy,disc,a));

            ArrayList<Disc> willFlip;
            if(disc.getOwner().equals(firstPlayer))
                willFlip = checkAllFlips(a, secondPlayer);

            else
                willFlip = checkAllFlips(a, firstPlayer);
            flipDiscs(willFlip);

            playerTurn = !playerTurn; //Change turn

            board[a.row()][a.col()] = disc;
            discs.add(disc);

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

        int countFirstPlayer = countDisc(firstPlayer);
        int countSecondPlayer = countDisc(secondPlayer);
        if(countFirstPlayer > countSecondPlayer)
        {
            System.out.println("Game over, the winner is the first player with: "+countFirstPlayer+" discs, the second player is with: "+countSecondPlayer+" discs.");
            firstPlayer.addWin();
        }
        else if(countSecondPlayer > countFirstPlayer)
        {
            System.out.println("Game over, the winner is the second player with: "+countSecondPlayer+" discs, the first player is with: "+countFirstPlayer+" discs.");
            secondPlayer.addWin();
        }
        else
            System.out.println("Game over, it's a tie! Both players have: "+countFirstPlayer+" discs.");

        return true;
    }

    /**
     * This function checks how many discs a player currently has on the board
     * @param player - the player to check
     * @return how many discs owned
     */
    private int countDisc(Player player)
    {
        int count = 0;
        for (int i = 0; i < discs.size(); i++)
        {
            if(discs.get(i).getOwner().equals(player))
                count++;
        }

        return count;
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
        if(!firstPlayer.isHuman() || !secondPlayer.isHuman())
            return ;
        if(!previousMoves.isEmpty())
        {
            Move move = previousMoves.pop();

            for (int i = 0; i < board.length; i++)
            {
                for (int j = 0; j < board[i].length; j++)
                {
                     board[i][j] = move.getBoard()[i][j];
                }
            }

            move.setDisc(null);

            playerTurn = !playerTurn;
        }
        else
        {
            System.out.println("No more turns to undo");
        }
    }

    public Disc[][] getBoard()
    {
        return board;
    }
}
