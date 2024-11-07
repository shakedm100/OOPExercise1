public class BombDisc implements Disc {

    private Player player;
    private Position position;

    public BombDisc(Player player, Position position)
    {
        this.player = player;
        this.position = position;
    }

    @Override
    public Player getOwner()
    {
        return player;
    }

    @Override
    public void setOwner(Player player)
    {
        this.player = player;
    }

    @Override
    public String getType()
    {
        return "\uD83D\uDCA3";
    }

    @Override
    public Position getPosition()
    {
        return position;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

}
