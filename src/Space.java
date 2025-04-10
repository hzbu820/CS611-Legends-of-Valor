/**
 * An abstract class that represents a grid within the game world, forming the base class for different
 * types of spaces like CommonSpace and InaccessibleSpace.
 */
public abstract class Space {
    private Character occupant; // Can be Hero or Monster

    public String getSymbol() {
        if (occupant != null) {
            return occupant.getSymbol(); // Get the symbol from the occupant, which could be H1, M1, etc.
        }
        return " "; // Default symbol if space is empty
    }

    public void setOccupant(Character occupant) {
        this.occupant = occupant;
    }

    public Character getOccupant() {
        return occupant;
    }
}






