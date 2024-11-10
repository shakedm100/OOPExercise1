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

        Position idealPosition = highestPositions.getFirst();
        if(highestPositions.size() > 1)
        {
            for (int i = 0; i < highestPositions.size()-1; i++)
            {
                if(highestPositions.get(i+1).col() > highestPositions.get(i).col())
                    idealPosition = highestPositions.get(i+1);
                else if(highestPositions.get(i+1).col() < highestPositions.get(i).col())
                    idealPosition = highestPositions.get(i);
                else //If the columns are equal compare the rows
                {
                    if(highestPositions.get(i+1).row() >= highestPositions.get(i).row())
                        idealPosition = highestPositions.get(i+1);
                    else
                        idealPosition = highestPositions.get(i);
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
