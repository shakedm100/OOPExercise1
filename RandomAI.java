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
        Random random = new Random();
        int rand;
        Disc chosenDisc;

        rand = random.nextInt(6); //Chooses a number between [0,5]
        System.out.println("Chosen random: "+rand);
        if(rand == 0 && this.number_of_unflippedable > 0) //Simple disc has a higher chance to be chosen
        {
            chosenDisc = new UnflippableDisc(this);
        }
        else if(rand == 1 && this.number_of_bombs > 0)
        {
            chosenDisc = new BombDisc(this);
        }
        else
            chosenDisc = new SimpleDisc(this);

        return chosenDisc;
    }
}
