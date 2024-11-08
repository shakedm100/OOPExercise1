import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class RandomAI extends AIPlayer{

    public RandomAI(boolean isPlayerOne)
    {
        super(isPlayerOne);
    }
    @Override
    public Move makeMove(PlayableLogic gameStatus)
    {
        List<Position> validMoves = gameStatus.ValidMoves();
        if(validMoves.isEmpty())
            return null;

        Random random = new Random();

        int rand = random.nextInt(validMoves.size()-1);
        Position chosenMove = validMoves.get(rand);

        ArrayList<Disc> willFlip = new ArrayList<>();
        Player enemy;
        if(gameStatus.getFirstPlayer().isPlayerOne())
            enemy = gameStatus.getSecondPlayer();
        else
            enemy = gameStatus.getFirstPlayer();

        if(gameStatus instanceof GameLogic)
            willFlip = ((GameLogic) gameStatus).checkAllFlips(chosenMove, enemy);

        //Choose Disc
        Disc disc = chooseRandomDisc();

        return new Move(willFlip, disc, chosenMove);
    }

    private Disc chooseRandomDisc()
    {
        boolean validDisc = false;
        Random random = new Random();
        int rand;
        Disc chosenDisc = null;

        while(!validDisc)
        {
            if(this.number_of_bombs == 3) //To minimize choosing runtime
                rand = random.nextInt(5);
            else rand = random.nextInt(6);
            if(rand <= 3) //Simple disc has a higher chance to be chosen
            {
                chosenDisc = new SimpleDisc(this);
            }
            else if(rand == 4 && this.number_of_unflippedable < 2)
            {
                chosenDisc = new UnflippableDisc(this);
            }
            else if(this.number_of_bombs < 3)
            {
                chosenDisc = new BombDisc(this);
            }

            //Exit condition: Disc has been initialized by one of the above
            if(chosenDisc != null)
                validDisc = true;
        }

        return chosenDisc;
    }
}
