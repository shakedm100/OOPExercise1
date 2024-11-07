public class BombDisc extends Disc {

    private Player player;

    public BombDisc(Player owner) {
        super(owner);
    }

    @Override
    public String getType()
    {
        return "💣";
    }
}
