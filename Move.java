import java.util.ArrayList;

public class Move {

    private ArrayList<Disc> flippedLast;
    private Disc lastPlaced;
    private Position position;

    public Move(ArrayList<Disc> flippedLast, Disc lastPlaced, Position position)
    {
        this.flippedLast = flippedLast;
        this.lastPlaced = lastPlaced;
        this.position = position;
    }

    public void undo(Player player1, Player player2)
    {
        if(!player1.isHuman() || !player2.isHuman())
            return ;

        for (int i = 0; i < flippedLast.size(); i++)
        {
            Player checkPlayer = flippedLast.get(i).getOwner();
            if(checkPlayer.equals(player1))
                flippedLast.get(i).setOwner(player2);
            else
                flippedLast.get(i).setOwner(player1);
        }

        lastPlaced.setOwner(null);
    }

    public Disc disc()
    {
        return lastPlaced;
    }

    public Position position()
    {
        return position;
    }
}
