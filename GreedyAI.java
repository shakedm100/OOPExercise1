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
        ArrayList<Position> highestPositions = new ArrayList<>();
        for (int i = 0; i < validMoves.size(); i++)
        {
            int currentFlips = gameStatus.countFlips(validMoves.get(i));
            if(currentFlips > highestFlips)
            {
                highestPositions = new ArrayList<>();
                highestFlips = currentFlips;
                highestPositions.add(validMoves.get(i));
            }
            else if(currentFlips == highestFlips)
                highestPositions.add(validMoves.get(i));
        }

        Position idealPosition;
        if(highestPositions.size() > 1)
        {
            for (int i = 0; i < highestPositions.size(); i++)
            {
                if(highestPositions.get(i+1) != null)
                {
                    if(highestPositions.get(i).col() >
                }
            }
        }
        else
        {
            idealPosition = highestPositions.getFirst();
        }

        Disc[][] currentBoard = new Disc[gameStatus.getBoardSize()][gameStatus.getBoardSize()];

        if(gameStatus instanceof GameLogic)
        {
            currentBoard = ((GameLogic) gameStatus).getBoard();
        }

        SimpleDisc simpleDisc = new SimpleDisc(this);

        return new Move(currentBoard, simpleDisc, idealPosition);

    }
}
