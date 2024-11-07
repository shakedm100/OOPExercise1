public class SimpleDisc implements Disc {

    private Player player;

    private Position position;

    public SimpleDisc(Player player, Position position)
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
        return "⬤";
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
