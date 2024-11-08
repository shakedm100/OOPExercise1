public class UnflippableDisc extends Disc {

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
