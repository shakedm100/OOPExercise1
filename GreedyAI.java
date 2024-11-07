import java.util.ArrayList;
import java.util.List;

public class GreedyAI extends AIPlayer {

    public GreedyAI(boolean isPlayerOne)
    {
        super(isPlayerOne);
    }
    @Override
    public Move makeMove(PlayableLogic gameStatus)
    {
        List<Position> validMoves = gameStatus.ValidMoves();
        if(validMoves.isEmpty())
            return null;

        int highestFlips = 0;
        Position highestPosition = validMoves.getFirst();
        for (int i = 0; i < validMoves.size(); i++)
        {
            int currentFlips = gameStatus.countFlips(validMoves.get(i));
            if(currentFlips > highestFlips)
            {
                highestFlips = currentFlips;
                highestPosition = validMoves.get(i);
            }
        }

        Player enemy;
        if(gameStatus.getFirstPlayer().isPlayerOne())
            enemy = gameStatus.getSecondPlayer();
        else
            enemy = gameStatus.getFirstPlayer();

        ArrayList<Disc> willFlip = new ArrayList<>();

        if(gameStatus instanceof GameLogic)
        {
            willFlip = ((GameLogic) gameStatus).checkAllFlips(highestPosition, enemy);
        }

        SimpleDisc simpleDisc = new SimpleDisc(this);

        return new Move(willFlip, simpleDisc, highestPosition);
    }
}
