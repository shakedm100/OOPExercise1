public class UnflippableDisc implements Disc {

    private Player owner;

    public UnflippableDisc(Player owner)
    {
        this.owner = owner;
    }

    public void setOwner(Player player)
    {
        owner = player;
    }

    public Player getOwner()
    {
        return owner;
    }

    @Override
    public String getType()
    {
        return "⭕";
    }

}
