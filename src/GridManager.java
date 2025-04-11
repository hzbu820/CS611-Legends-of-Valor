import java.util.Random;

/**
 * Manages the grid structure, initializing different types of spaces and handling
 * the creation and management of the game board.
 */
public class GridManager {
    private Space[][] grid;

    /**
     * Creates a new GridManager with an initialized grid.
     * @param gridRows Number of rows in the grid
     * @param gridCols Number of columns in the grid
     */
    public GridManager(int gridRows, int gridCols) {
        this.grid = new Space[gridRows][gridCols];
    }

    /**
     * Initializes the game spaces with a custom board setup for Legends of Valor.
     */
    public void initializeSpaces() {
        // Create a 8x8 grid
        grid = new Space[8][8];

        // Set Nexus spaces for heroes and monsters
        for (int j = 0; j < 8; j++) {
            grid[0][j] = new NexusSpace("Monster Nexus");  // Top row for monsters
            grid[7][j] = new NexusSpace("Hero Nexus");     // Bottom row for heroes
        }

        // Set lanes and walls
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                if (j == 2 || j == 5) {
                    grid[i][j] = new InaccessibleSpace();  // Walls separating lanes
                } else {
                    grid[i][j] = createRandomTerrainSpace();  // Randomly assign terrain in lanes
                }
            }
        }

        // Update specific cells to be inaccessible
        grid[0][2] = new InaccessibleSpace(); // Third cell from the left in the top row
        grid[0][5] = new InaccessibleSpace(); // Sixth cell from the left in the top row
        if (grid[0][5].getOccupant() != null) {
            grid[0][5].setOccupant(null); // Clear the previous occupant if necessary
        }
        grid[7][2] = new InaccessibleSpace(); // Third cell from the left in the bottom row
        grid[7][5] = new InaccessibleSpace(); // Sixth cell from the left in the bottom row
        if (grid[7][5].getOccupant() != null) {
            grid[7][5].setOccupant(null); // Clear the previous occupant if necessary
        }
        
        // Add treasure chests to the map (15% chance for eligible cells)
        placeTreasureChests();
    }
    
    /**
     * Places treasure chests randomly on the map
     */
    private void placeTreasureChests() {
        Random random = new Random();
        
        // Loop through the grid (excluding nexus rows and inaccessible spaces)
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                // Skip inaccessible spaces
                if (grid[i][j] instanceof InaccessibleSpace) {
                    continue;
                }
                
                // 15% chance to place a mystery space (hidden treasure chest)
                if (random.nextDouble() < 0.15) {
                    grid[i][j] = new MysterySpace();
                }
            }
        }
    }

    /**
     * Helper method to create random terrain spaces (Bush, Cave, Koulou, Plain, Obstacle).
     * @return A randomly created Space object
     */
    private Space createRandomTerrainSpace() {
        int rand = new Random().nextInt(100);
        if (rand < 15) {
            return new BushSpace();
        } else if (rand < 30) {
            return new CaveSpace();
        } else if (rand < 45) {
            return new KoulouSpace();
        } else if (rand < 55) {
            return new ObstacleSpace();
        } else {
            return new PlainSpace();
        }
    }

    /**
     * Sets customized spaces at specific positions in the grid.
     * @param row Row position
     * @param col Column position
     * @param space The space to set
     */
    public void setSpace(int row, int col, Space space) {
        if (isValidPosition(row, col)) {
            grid[row][col] = space;
        }
    }

    /**
     * Returns the grid of spaces.
     * @return The 2D array of spaces
     */
    public Space[][] getGrid() {
        return grid;
    }

    /**
     * Checks if a position is valid within the grid.
     * @param row Row to check
     * @param col Column to check
     * @return true if position is within grid boundaries, false otherwise
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    /**
     * Updates the grid with spaces from another grid.
     * @param newGrid New grid to replace current grid
     */
    public void updateGrid(Space[][] newGrid) {
        if (newGrid != null && newGrid.length > 0) {
            this.grid = newGrid;
        }
    }
} 