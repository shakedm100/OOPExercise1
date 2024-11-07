public class UnflippableDisc extends Disc {

    private Player player;

    public UnflippableDisc(Player player)
    {
        super(player);
    }

    @Override
    public String getType()
    {
        return "⭕";
    }

}
