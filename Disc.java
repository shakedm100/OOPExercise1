/**
 * The Disc interface defines the characteristics of a game in a chess-like game.
 * Implementing classes should provide information about the player who owns the Disc.
 */
public abstract class Disc {

    private Player owner;

    public Disc(Player owner)
    {
        this.owner = owner;
    }
    /**
     * Get the player who owns the Disc.
     *
     * @return The player who is the owner of this game disc.
     */
    Player getOwner()
    {
        return owner;
    }

    /**
     * Set the player who owns the Disc.
     *
     */
    void setOwner(Player player)
    {
        owner = player;
    }

    /**
     * Get the type of the disc.
     * use the:
     *          "⬤",         "⭕"                "💣"
     *      Simple Disc | Unflippedable Disc | Bomb Disc |
     * respectively.
     */
    abstract String getType();

}