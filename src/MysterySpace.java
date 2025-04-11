/**
 * Represents a mystery space on the map that will be revealed as a Treasure Chest
 * when a player moves onto it.
 */
public class MysterySpace extends Space {
    @Override
    public String getSymbol() {
        return "?";
    }
    
    /**
     * Converts this mystery space into a treasure chest
     * @return A new TreasureChest object
     */
    public TreasureChest reveal() {
        return new TreasureChest();
    }
} 