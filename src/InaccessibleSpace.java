/**
 * Represents an area in the game world that cannot be entered by characters.
 */
public class InaccessibleSpace extends Space {
    @Override
    public String getSymbol() {
        return "▒▒";
    }
}