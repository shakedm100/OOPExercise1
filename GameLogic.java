import java.util.ArrayList;
import java.util.List;

public class GameLogic implements PlayableLogic {

    ArrayList<Disc> discs;
    Player firstPlayer, secondPlayer;

    @Override
    public boolean locate_disc(Position a, Disc disc) {

        for (int i = 0; i < discs.size(); i++)
        {
            if(discs.get(i).getPosition().equals(a))
                return false;
        }

        return true;
    }

    @Override
    public Disc getDiscAtPosition(Position position)
    {
        for (int i = 0; i < discs.size(); i++)
        {
            if(discs.get(i).getPosition().equals(position))
                return discs.get(i);
        }
        return null;
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
        for (int i = 1; i < 9; i++)
        {
            for (int j = 1; j < 9; j++)
            {
                Position position = new Position(i,j);
                if(countFlips(position) > 0)
                    valid.add(new Position(i,j));
            }
        }

        for (int i = 0; i < discs.size(); i++)
        {
            valid.remove((Position) discs.get(i));
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

        int sum = 0;
        sum += checkFlipsHorizontal(pos, enemyPlayer);
        sum += checkFlipsVertical(pos, enemyPlayer);
        sum += checkFlipsDiagonal(pos, enemyPlayer);


        return sum;
    }

    private int checkFlipsDiagonal(Position pos, Player enemyPlayer)
    {
        int count = 0; // count counts potential flips
        int sum = 0; // sum sums up all the flips only when it's confirmed
        boolean check = true;
        Position checkedPosition = new Position(pos); //Copy constructor
        while(check)
        {
            checkedPosition.setY(checkedPosition.getY()-1);
            checkedPosition.setX(checkedPosition.getX()-1);
            if(checkedPosition.getY() < 1 || checkedPosition.getX() < 1)
                check = false;
            else if(getDiscAtPosition(checkedPosition).getOwner().equals(enemyPlayer))
                count++;
            else
            {
                check = false;
                sum += count;
                count = 0;
            }
        }

        //Now for the other side
        check = true;
        count = 0;
        checkedPosition = new Position(pos); //Reset the checking position
        while(check)
        {
            checkedPosition.setY(checkedPosition.getY()+1);
            checkedPosition.setX(checkedPosition.getX()+1);
            if(checkedPosition.getY() > 8 || checkedPosition.getX() > 8)
                check = false;
            else if(getDiscAtPosition(checkedPosition).getOwner().equals(enemyPlayer))
                count++;
            else
            {
                check = false;
                sum += count;
                count = 0;
            }
        }

        return sum;
    }

    private int checkFlipsVertical(Position pos, Player enemyPlayer)
    {
        int count = 0; // count counts potential flips
        int sum = 0; // sum sums up all the flips only when it's confirmed
        boolean check = true;
        Position checkedPosition = new Position(pos); //Copy constructor
        while(check)
        {
            checkedPosition.setY(checkedPosition.getY()-1);
            if(checkedPosition.getY() < 1)
                check = false;
            else if(getDiscAtPosition(checkedPosition).getOwner().equals(enemyPlayer))
                count++;
            else
            {
                check = false;
                sum += count;
                count = 0;
            }
        }

        //Now for the other side
        check = true;
        count = 0;
        checkedPosition = new Position(pos); //Reset the checking position
        while(check)
        {
            checkedPosition.setY(checkedPosition.getY()+1);
            if(checkedPosition.getY() > 8)
                check = false;
            else if(getDiscAtPosition(checkedPosition).getOwner().equals(enemyPlayer))
                count++;
            else
            {
                check = false;
                sum += count;
                count = 0;
            }
        }

        return sum;
    }

    private int checkFlipsHorizontal(Position pos, Player enemyPlayer)
    {
        //check horizontally
        int count = 0; // count counts potential flips
        int sum = 0; // sum sums up all the flips only when it's confirmed
        boolean check = true;
        Position checkedPosition = new Position(pos); //Copy constructor
        while(check)
        {
            checkedPosition.setX(checkedPosition.getX()-1);
            if(checkedPosition.getX() < 1)
                check = false;
            else if(getDiscAtPosition(checkedPosition).getOwner().equals(enemyPlayer))
                count++;
            else
            {
                check = false;
                sum += count;
                count = 0;
            }
        }

        //Now for the other side
        check = true;
        count = 0;
        checkedPosition = new Position(pos); //Reset the checking position
        while(check)
        {
            checkedPosition.setX(checkedPosition.getX()+1);
            if(checkedPosition.getX() > 8)
                check = false;
            else if(getDiscAtPosition(checkedPosition).getOwner().equals(enemyPlayer))
                count++;
            else
            {
                check = false;
                sum += count;
                count = 0;
            }
        }

        return sum;
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
        Position placement = new Position(3,3);
        SimpleDisc simple = new SimpleDisc(firstPlayer, placement);
        discs.add(simple);

        //Second
        placement = new Position(4,4);
        simple.setPosition(placement);
        discs.add(simple);

        //Third
        placement = new Position(4,3);
        simple = new SimpleDisc(secondPlayer, placement);
        discs.add(simple);

        //Fourth
        placement = new Position(3,4);
        simple.setPosition(placement);
        discs.add(simple);
    }

    @Override
    public void undoLastMove() {

    }
}
