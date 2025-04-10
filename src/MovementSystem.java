/**
 * Handles movement logic for heroes and monsters in the game.
 * Validates movements, calculates new positions, and manages teleportation and recall.
 */
public class MovementSystem {
    private GridManager gridManager;
    private CharacterPositioner positioner;
    private TerrainEffectHandler terrainHandler;

    /**
     * Creates a new MovementSystem with references to required systems.
     * @param gridManager The GridManager for grid access
     * @param positioner The CharacterPositioner for updating positions
     * @param terrainHandler The TerrainEffectHandler for applying terrain effects
     */
    public MovementSystem(GridManager gridManager, CharacterPositioner positioner, TerrainEffectHandler terrainHandler) {
        this.gridManager = gridManager;
        this.positioner = positioner;
        this.terrainHandler = terrainHandler;
    }

    /**
     * Moves a hero in the specified direction.
     * @param hero The hero to move
     * @param direction Direction to move (w, a, s, d)
     * @return true if movement was successful, false otherwise
     */
    public boolean moveHero(Hero hero, String direction) {
        if (hero == null) {
            System.out.println("Invalid Hero");
            return false;
        }

        int currentRow = hero.getHeroRow();
        int currentCol = hero.getHeroCol();
        Space[][] grid = gridManager.getGrid();

        // Validate current position
        if (!gridManager.isValidPosition(currentRow, currentCol)) {
            System.out.println("Hero is at an invalid position: [" + currentRow + "," + currentCol + "]");
            // Try to recover by setting to a valid position (nexus)
            int[] nexus = hero.getNexus();
            if (nexus != null && nexus.length == 2) {
                hero.setHeroRow(nexus[0]);
                hero.setHeroCol(nexus[1]);
                System.out.println("Hero has been teleported back to their nexus.");
            }
            return false;
        }

        int[] newPosition = calculateNewPosition(currentRow, currentCol, direction);
        if (newPosition == null) {
            System.out.println("Invalid direction.");
            return false; // Invalid direction
        }

        int newRow = newPosition[0];
        int newCol = newPosition[1];

        // Check new position is within grid boundaries
        if (!gridManager.isValidPosition(newRow, newCol)) {
            System.out.println("Cannot move outside the game area.");
            return false;
        }

        if (isValidMove(newRow, newCol)) {
            // Check if the space is an obstacle - heroes cannot enter obstacle spaces
            if (grid[newRow][newCol] instanceof ObstacleSpace) {
                System.out.println("There's an obstacle in the way. You need to remove it first before moving there.");
                return false;
            }
            
            // Remove current terrain effect before moving
            try {
                terrainHandler.removeTerrainEffect(hero, grid[currentRow][currentCol]);
            } catch (Exception e) {
                System.out.println("Error removing terrain effect: " + e.getMessage());
                // Continue with the move anyway
            }

            // Set new location for the hero
            if (!positioner.setHeroLocation(hero, newRow, newCol)) {
                System.out.println("Failed to set hero location to [" + newRow + "," + newCol + "]");
                return false;
            }
            
            // Set occupant in the grid
            try {
                grid[newRow][newCol].setOccupant(hero);
            } catch (Exception e) {
                System.out.println("Error setting grid occupant: " + e.getMessage());
                // The hero position was already updated, so we should continue
            }

            // Apply terrain effect after moving
            try {
                terrainHandler.applyTerrainEffect(hero, grid[newRow][newCol]);
            } catch (Exception e) {
                System.out.println("Error applying terrain effect: " + e.getMessage());
                // Continue anyway
            }

            System.out.println("Moved " + hero.getName() + " " + direction + ".");
            return true;
        } else {
            System.out.println("Move failed: Invalid position.");
            return false;
        }
    }

    /**
     * Teleports a hero to a position near another hero.
     * @param teleportingHero The hero teleporting
     * @param targetHero The target hero to teleport near
     * @return true if teleport was successful, false otherwise
     */
    public boolean teleportHero(Hero teleportingHero, Hero targetHero) {
        if (teleportingHero == null || targetHero == null) {
            System.out.println("Invalid Hero");
            return false;
        }

        int targetRow = targetHero.getHeroRow();
        int targetCol = targetHero.getHeroCol();
        int teleportingHeroRow = teleportingHero.getHeroRow();
        int teleportingHeroCol = teleportingHero.getHeroCol();
        Space[][] grid = gridManager.getGrid();

        // Validate both hero positions
        if (!gridManager.isValidPosition(targetRow, targetCol)) {
            System.out.println("Target hero is at an invalid position. Teleport failed.");
            return false;
        }
        
        if (!gridManager.isValidPosition(teleportingHeroRow, teleportingHeroCol)) {
            System.out.println("Teleporting hero is at an invalid position. Teleport failed.");
            // Try to recover by setting to a valid position (nexus)
            int[] nexus = teleportingHero.getNexus();
            if (nexus != null && nexus.length == 2 && 
                gridManager.isValidPosition(nexus[0], nexus[1])) {
                teleportingHero.setHeroRow(nexus[0]);
                teleportingHero.setHeroCol(nexus[1]);
                System.out.println("Teleporting hero has been recovered to their nexus.");
            }
            return false;
        }

        if (Math.abs(teleportingHeroCol - targetCol) <= 1) {
            System.out.println("Teleport failed: Must teleport to a different lane.");
            return false;
        }

        // Remove terrain effect from current space (with error handling)
        try {
            terrainHandler.removeTerrainEffect(teleportingHero, grid[teleportingHeroRow][teleportingHeroCol]);
        } catch (Exception e) {
            System.out.println("Error removing terrain effect: " + e.getMessage());
            // Continue with teleport anyway
        }

        // Define possible adjacent positions around the target hero
        int[][] adjacentPositions = {
                {targetRow + 1, targetCol}, // Below
                {targetRow, targetCol - 1}, // Left
                {targetRow, targetCol + 1}  // Right
        };

        // Attempt teleport to any valid adjacent position
        for (int[] pos : adjacentPositions) {
            int newRow = pos[0];
            int newCol = pos[1];

            // Check if the position is valid and different from current position
            if (gridManager.isValidPosition(newRow, newCol) &&
                (newRow != teleportingHeroRow || newCol != teleportingHeroCol) &&
                !(grid[newRow][newCol] instanceof InaccessibleSpace) &&
                grid[newRow][newCol].getOccupant() == null) {
                
                // Update hero's position
                if (!positioner.setHeroLocation(teleportingHero, newRow, newCol)) {
                    continue; // Try next position if this one fails
                }
                
                System.out.println("Teleport successful.");
                
                // Apply terrain effect after teleporting (with error handling)
                try {
                    terrainHandler.applyTerrainEffect(teleportingHero, grid[newRow][newCol]);
                } catch (Exception e) {
                    System.out.println("Error applying terrain effect: " + e.getMessage());
                }
                
                return true;
            }
        }

        System.out.println("Teleport failed: No valid adjacent space.");
        return false;
    }

    /**
     * Recalls a hero to their nexus.
     * @param hero The hero to recall
     * @return true if recall was successful, false otherwise
     */
    public boolean recallHero(Hero hero) {
        if (hero == null) {
            System.out.println("Invalid Hero");
            return false;
        }

        int[] nexus = hero.getNexus();
        if (!positioner.setHeroLocation(hero, nexus[0], nexus[1])) {
            System.out.println("Recall failed.");
            return false;
        }
        System.out.println("Recalling " + hero.getName());
        terrainHandler.applyTerrainEffect(hero, gridManager.getGrid()[nexus[0]][nexus[1]]); // Apply terrain effect after recall
        return true;
    }

    /**
     * Moves a monster in the specified direction.
     * @param monster The monster to move
     * @param direction Direction to move (w, a, s, d)
     * @return true if movement was successful, false otherwise
     */
    public boolean moveMonster(Monster monster, String direction) {
        if (monster == null) {
            System.out.println("Invalid Monster");
            return false;
        }

        int currentRow = monster.getMonsterRow();
        int currentCol = monster.getMonsterCol();
        Space[][] grid = gridManager.getGrid();

        int[] newPosition = calculateNewPosition(currentRow, currentCol, direction);
        if (newPosition == null) {
            System.out.println("Invalid direction.");
            return false; // Invalid direction
        }

        int newRow = newPosition[0];
        int newCol = newPosition[1];

        if (isValidMove(newRow, newCol)) {
            // Check if the space is an obstacle - monsters cannot enter obstacle spaces
            if (grid[newRow][newCol] instanceof ObstacleSpace) {
                System.out.println("There's an obstacle blocking " + monster.getName() + "'s path.");
                return false;
            }
            
            // Update monster position
            monster.setMonsterRow(newRow);
            monster.setMonsterCol(newCol);
            System.out.println("Moved " + monster.getName() + " " + direction + ".");
            return true;
        } else {
            System.out.println("Move failed: Invalid position.");
            return false;
        }
    }

    /**
     * Calculates a new position based on the current position and direction.
     * @param currentRow Current row
     * @param currentCol Current column
     * @param direction Direction to move (w, a, s, d)
     * @return New position as [row, col] or null if invalid direction
     */
    protected int[] calculateNewPosition(int currentRow, int currentCol, String direction) {
        // Validate current position
        if (!gridManager.isValidPosition(currentRow, currentCol)) {
            System.out.println("Invalid current position: [" + currentRow + "," + currentCol + "]");
            return null;
        }
        
        int newRow = currentRow;
        int newCol = currentCol;

        switch (direction.toLowerCase()) {
            case "w":  // Up
                newRow--;
                break;
            case "a":  // Left
                newCol--;
                break;
            case "s":  // Down
                newRow++;
                break;
            case "d":  // Right
                newCol++;
                break;
            default:
                System.out.println("Invalid direction. Use w (up), a (left), s (down), d (right).");
                return null;
        }

        // Check if the new position is within the grid boundaries
        if (!gridManager.isValidPosition(newRow, newCol)) {
            System.out.println("Cannot move outside the game area.");
            return null;
        }

        return new int[]{newRow, newCol};
    }

    /**
     * Checks if a move to the given position is valid.
     * @param row Target row
     * @param col Target column
     * @return true if move is valid, false otherwise
     */
    protected boolean isValidMove(int row, int col) {
        Space[][] grid = gridManager.getGrid();
        
        // Check if the position is within grid boundaries
        if (!gridManager.isValidPosition(row, col)) {
            System.out.println("Position [" + row + "," + col + "] is outside the game area.");
            return false;
        }
        
        // Check if the space is inaccessible
        if (grid[row][col] instanceof InaccessibleSpace) {
            System.out.println("You cannot move to an inaccessible space.");
            return false;
        }
        
        // Check if the space is already occupied by another character
        if (grid[row][col].getOccupant() != null) {
            System.out.println("This space is already occupied by another character.");
            return false;
        }
        
        return true;
    }

    /**
     * Checks if a move is valid without printing error messages.
     * @param row Target row
     * @param col Target column
     * @return true if move is valid, false otherwise
     */
    protected boolean isValidMoveNoPrint(int row, int col) {
        Space[][] grid = gridManager.getGrid();
        
        // Check grid boundaries
        if (!gridManager.isValidPosition(row, col)) {
            return false;
        }
        
        // Check for inaccessible space
        if (grid[row][col] instanceof InaccessibleSpace) {
            return false;
        }
        
        // Check if space is already occupied
        if (grid[row][col].getOccupant() != null) {
            return false;
        }
        
        return true;
    }
} 