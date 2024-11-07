public class SimpleDisc extends Disc {

    private Player player;

    public SimpleDisc(Player player)
    {
        super(player);
    }

    @Override
    public String getType()
    {
        return "⬤";
    }

}
