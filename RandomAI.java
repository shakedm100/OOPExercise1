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

        int rand;
        if(validMoves.size() > 1)
            rand = random.nextInt(validMoves.size()-1);
        else
            rand = 0;

        Position chosenMove = validMoves.get(rand);

        Disc[][] currentBoard = new Disc[gameStatus.getBoardSize()][gameStatus.getBoardSize()];

        if(gameStatus instanceof GameLogic)
            currentBoard = ((GameLogic) gameStatus).getBoard();

        //Choose Disc
        Disc disc = chooseRandomDisc();

        return new Move(currentBoard, disc, chosenMove);

    }

    /**
     * The method chooses which disc the bot will play
     * 60% chance to choose simple disc
     * 20% chance to choose unflippable disc
     * 20% chance to choose bomb disc
     * @return the disc type to use
     */
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
