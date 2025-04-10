import java.util.List;

/**
 * Manages the positioning of characters (heroes and monsters) on the game board.
 * Handles setting positions, validating movements, and updating character locations.
 */
public class CharacterPositioner {
    private GridManager gridManager;

    /**
     * Creates a new CharacterPositioner with a reference to the GridManager.
     * @param gridManager The GridManager to use for position validation
     */
    public CharacterPositioner(GridManager gridManager) {
        this.gridManager = gridManager;
    }

    /**
     * Sets the location for a hero on the grid.
     * @param hero The hero to position
     * @param targetRow Target row position
     * @param targetCol Target column position
     * @return true if positioning was successful, false otherwise
     */
    public boolean setHeroLocation(Hero hero, int targetRow, int targetCol) {
        if (hero == null) {
            System.out.println("Invalid Hero");
            return false;
        }

        // Check if the target position is within the grid boundaries
        if (!gridManager.isValidPosition(targetRow, targetCol)) {
            System.out.println("Hero location out of bounds: [" + targetRow + "," + targetCol + "]");
            return false;
        }

        hero.setHeroRow(targetRow);
        hero.setHeroCol(targetCol);
        gridManager.getGrid()[targetRow][targetCol].setOccupant(hero);
        return true;
    }

    /**
     * Sets the location for a monster on the grid.
     * @param monster The monster to position
     * @param targetRow Target row position
     * @param targetCol Target column position
     * @return true if positioning was successful, false otherwise
     */
    public boolean setMonsterLocation(Monster monster, int targetRow, int targetCol) {
        if (monster == null) {
            System.out.println("Invalid Monster");
            return false;
        }

        // Check if the target position is within the grid boundaries
        if (!gridManager.isValidPosition(targetRow, targetCol)) {
            System.out.println("Monster location out of bounds: [" + targetRow + "," + targetCol + "]");
            return false;
        }

        monster.setMonsterRow(targetRow);
        monster.setMonsterCol(targetCol);
        gridManager.getGrid()[targetRow][targetCol].setOccupant(monster);
        return true;
    }

    /**
     * Sets up heroes and monsters on the game board.
     * @param heroes List of heroes to place
     * @param monsters List of monsters to place
     */
    public void initializeHeroesAndMonsters(List<Hero> heroes, List<Monster> monsters) {
        Space[][] grid = gridManager.getGrid();

        // Setting up monsters in the top row: M1, M1, inaccessible, M2, M2, inaccessible, M3, M3
        for (int i = 0; i < monsters.size() && i < 3; i++) {  // Limit to at most 3 monsters initially
            Monster monster = monsters.get(i);
            monster.setMonsterIdentifier("M" + (i + 1));

            // Set initial monster positions, occupying two consecutive cells
            int col = i * 3; // Each monster takes two spaces, with inaccessible space between groups
            
            // Ensure we're not exceeding grid boundaries
            if (col + 1 < grid[0].length) {  // Check if there's space for this monster
                setMonsterLocation(monster, 0, col);       // First space for the monster
                setMonsterLocation(monster, 0, col + 1);   // Second space for the monster

                // Update the grid to reflect the monsters' positions
                grid[0][col].setOccupant(monster);
                grid[0][col + 1].setOccupant(monster);
            } else {
                System.out.println("Not enough space to place monster " + (i + 1) + " on the grid.");
            }
        }

        // Setting up heroes in the bottom row: H1, H1, inaccessible, H2, H2, inaccessible, H3, H3
        for (int i = 0; i < heroes.size() && i < 3; i++) {  // Limit to at most 3 heroes
            Hero hero = heroes.get(i);
            hero.setHeroIdentifier("H" + (i + 1));

            // Set initial hero positions, occupying two consecutive cells
            int col = i * 3; // Each hero takes two spaces, with inaccessible space between groups
            
            // Ensure we're not exceeding grid boundaries
            if (col + 1 < grid[0].length) {  // Check if there's space for this hero
                setHeroLocation(hero, 7, col);       // First space for the hero
                setHeroLocation(hero, 7, col + 1);   // Second space for the hero

                hero.setNexus(7, col + 1);
                // Update the grid to reflect the heroes' positions
                grid[7][col].setOccupant(hero);
                grid[7][col + 1].setOccupant(hero);
            } else {
                System.out.println("Not enough space to place hero " + (i + 1) + " on the grid.");
            }
        }

        // Set inaccessible spaces between the heroes and monsters
        // Only set these if they're within the grid bounds
        if (2 < grid[0].length) grid[0][2] = new InaccessibleSpace(); // Third cell from the left in the top row
        if (5 < grid[0].length) grid[0][5] = new InaccessibleSpace(); // Sixth cell from the left in the top row
        if (2 < grid[0].length) grid[7][2] = new InaccessibleSpace(); // Third cell from the left in the bottom row
        if (5 < grid[0].length) grid[7][5] = new InaccessibleSpace(); // Sixth cell from the left in the bottom row
    }
    
    /**
     * Updates the grid to reflect current positions of heroes and monsters.
     * @param heroes List of heroes
     * @param monsters List of monsters
     */
    public void updateBoard(List<Hero> heroes, List<Monster> monsters) {
        Space[][] grid = gridManager.getGrid();
        
        // Clear all occupants first
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                // Do not clear Inaccessible spaces
                if (!(grid[i][j] instanceof InaccessibleSpace)) {
                    grid[i][j].setOccupant(null);
                }
            }
        }

        // Set hero locations
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                int row = hero.getHeroRow();
                int col = hero.getHeroCol();
                if (gridManager.isValidPosition(row, col) && !(grid[row][col] instanceof InaccessibleSpace)) {
                    grid[row][col].setOccupant(hero);
                }
            }
        }

        // Set monster locations
        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                int row = monster.getMonsterRow();
                int col = monster.getMonsterCol();
                if (gridManager.isValidPosition(row, col) && !(grid[row][col] instanceof InaccessibleSpace)) {
                    grid[row][col].setOccupant(monster);
                }
            }
        }
    }
} 