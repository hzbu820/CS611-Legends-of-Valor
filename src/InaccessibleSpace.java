/**
 * Represents an inaccessible space on the game map.
 * Characters cannot move onto or through these spaces.
 */
public class InaccessibleSpace extends Space {
    @Override
    public String getSymbol() {
        return "##";
    }
}