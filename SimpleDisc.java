public class SimpleDisc implements Disc {

    private Player owner;

    public SimpleDisc(Player owner)
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
        return "⬤";
    }

}
