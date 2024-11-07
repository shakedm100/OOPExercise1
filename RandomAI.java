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

        SimpleDisc simpleDisc = new SimpleDisc(this);

        return new Move(willFlip, simpleDisc, chosenMove);
    }
}
