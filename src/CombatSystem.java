import java.util.ArrayList;
import java.util.List;

/**
 * Handles combat interactions between heroes and monsters.
 * Manages targeting, range checks, and reward distribution.
 */
public class CombatSystem {
    private GridManager gridManager;

    /**
     * Creates a new CombatSystem with a reference to the GridManager.
     * @param gridManager The GridManager to use for grid access
     */
    public CombatSystem(GridManager gridManager) {
        this.gridManager = gridManager;
    }

    /**
     * Allows a hero to select a monster to target.
     * @param hero The hero selecting a target
     * @param monsters List of monsters that could be targeted
     * @return The selected monster, or null if no valid target
     */
    public Monster selectTargetMonster(Hero hero, List<Monster> monsters) {
        List<Monster> monstersInRange = getMonstersInRange(hero, monsters);
        
        if (monstersInRange.isEmpty()) {
            System.out.println("No monsters in attack range!");
            return null;
        }
        
        System.out.println("Select a monster to target:");
        for (int i = 0; i < monstersInRange.size(); i++) {
            Monster monster = monstersInRange.get(i);
            System.out.println((i + 1) + ". " + monster.getName() + " (HP: " + monster.getCurrentHealth() + ")");
        }
        
        int choice = InputHandler.getInstance().getIntInput("Enter the monster index: ");
        if (choice > 0 && choice <= monstersInRange.size()) {
            return monstersInRange.get(choice - 1);
        } else {
            System.out.println("Invalid selection!");
            return null;
        }
    }

    /**
     * Gets all monsters within attack range of a hero.
     * @param hero The hero to check from
     * @param monsters List of all monsters to check
     * @return List of monsters within range
     */
    public List<Monster> getMonstersInRange(Hero hero, List<Monster> monsters) {
        List<Monster> monstersInRange = new ArrayList<>();
        int heroRow = hero.getHeroRow();
        int heroCol = hero.getHeroCol();

        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                int monsterRow = monster.getMonsterRow();
                int monsterCol = monster.getMonsterCol();

                // Check if monster is adjacent to the hero
                if (Math.abs(heroRow - monsterRow) <= 1 && Math.abs(heroCol - monsterCol) <= 1) {
                    monstersInRange.add(monster);
                }
            }
        }
        return monstersInRange;
    }

    /**
     * Gets all heroes within attack range of a monster.
     * @param monster The monster to check from
     * @param heroes List of all heroes to check
     * @return List of heroes within range
     */
    public List<Hero> getHeroesInRange(Monster monster, List<Hero> heroes) {
        List<Hero> inRangeHeroes = new ArrayList<>();
        int monsterRow = monster.getMonsterRow();
        int monsterCol = monster.getMonsterCol();
        
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                int heroRow = hero.getHeroRow();
                int heroCol = hero.getHeroCol();
                
                // Check if hero is in one of the adjacent cells (including the current cell)
                boolean inSameCell = (heroRow == monsterRow && heroCol == monsterCol);
                boolean isAdjacent = Math.abs(heroRow - monsterRow) + Math.abs(heroCol - monsterCol) == 1;
                
                if (inSameCell || isAdjacent) {
                    inRangeHeroes.add(hero);
                }
            }
        }
        
        return inRangeHeroes;
    }

    /**
     * Executes an attack from a monster to heroes in range.
     * @param monster The attacking monster
     * @param heroes List of potential hero targets
     * @return true if attack was successful, false otherwise
     */
    public boolean monsterAttack(Monster monster, List<Hero> heroes) {
        List<Hero> heroesInRange = getHeroesInRange(monster, heroes);
        
        if (!heroesInRange.isEmpty()) {
            // Choose the hero with the lowest health to attack
            Hero target = heroesInRange.stream()
                    .filter(Hero::isAlive)
                    .min((h1, h2) -> Integer.compare(h1.getCurrentHealth(), h2.getCurrentHealth()))
                    .orElse(null);
            
            if (target != null) {
                monster.attack(target);
                System.out.println(monster.getName() + " attacks " + target.getName() + "!");
                if (!target.isAlive()) {
                    System.out.println(target.getName() + " has been defeated!");
                    // Heroes respawn at their nexus with full HP and MP
                    System.out.println(target.getName() + " will respawn at the nexus in the next round.");
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Distributes rewards to heroes when a monster is defeated.
     * @param monster The defeated monster
     * @param heroes List of heroes to receive rewards
     */
    public void distributeMonsterRewards(Monster monster, List<Hero> heroes) {
        if (monster == null || heroes == null || heroes.isEmpty()) {
            return;
        }
        
        // Calculate rewards based on monster level
        int goldReward = 500 * monster.getLevel();
        int experienceReward = 2 * monster.getLevel();
        
        System.out.println("\nMonster " + monster.getName() + " has been defeated!");
        System.out.println("All heroes receive " + goldReward + " gold and " + experienceReward + " experience points!");
        
        // Distribute rewards to all heroes
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                hero.addGold(goldReward);
                hero.gainExperience(experienceReward);
            }
        }
    }

    /**
     * Checks if a hero is in battle (has monsters in range).
     * @param hero The hero to check
     * @param monsters List of all monsters
     * @return true if hero is in battle, false otherwise
     */
    public boolean isHeroInBattle(Hero hero, List<Monster> monsters) {
        if (!hero.isAlive()) {
            return false;
        }
        
        int heroRow = hero.getHeroRow();
        int heroCol = hero.getHeroCol();

        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                int monsterRow = monster.getMonsterRow();
                int monsterCol = monster.getMonsterCol();

                // Check if a monster is in an adjacent space
                if (Math.abs(heroRow - monsterRow) <= 1 && Math.abs(heroCol - monsterCol) <= 1) {
                    return true; // Hero is in battle
                }
            }
        }
        return false; // No monsters nearby, hero is not in battle
    }

    /**
     * Checks if win conditions have been met.
     * @param heroes List of heroes
     * @param monsters List of monsters
     * @return true if game is over, false otherwise
     */
    public boolean checkWinCondition(List<Hero> heroes, List<Monster> monsters) {
        Space[][] grid = gridManager.getGrid();
        
        // Check if any monster has reached the heroes' Nexus (bottom row)
        for (Monster monster : monsters) {
            if (monster.isAlive() && monster.getMonsterRow() == 7 && grid[7][monster.getMonsterCol()] instanceof NexusSpace) {
                System.out.println("\n" + monster.getName() + " has reached the heroes' Nexus!");
                System.out.println("GAME OVER - Monsters Win!");
                return true;
            }
        }

        // Check if any hero has reached the monsters' Nexus (top row)
        for (Hero hero : heroes) {
            if (hero.isAlive() && hero.getHeroRow() == 0 && grid[0][hero.getHeroCol()] instanceof NexusSpace) {
                System.out.println("\n" + hero.getName() + " has reached the monsters' Nexus!");
                System.out.println("VICTORY - Heroes Win!");
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a hero is in a Nexus space.
     * @param hero The hero to check
     * @return true if the hero is in a Nexus, false otherwise
     */
    public boolean isInNexus(Hero hero) {
        Space[][] grid = gridManager.getGrid();
        int row = hero.getHeroRow();
        
        // A hero is in a Nexus if they are in the bottom row (row 7) for hero Nexus
        // or top row (row 0) for monster Nexus
        return row == 7 && grid[row][hero.getHeroCol()] instanceof NexusSpace;
    }

    /**
     * Handles the logic to remove obstacles near a hero.
     * @param hero The hero removing the obstacle
     * @return true if an obstacle was removed, false otherwise
     */
    public boolean removeObstacle(Hero hero) {
        int heroRow = hero.getHeroRow();
        int heroCol = hero.getHeroCol();
        Space[][] grid = gridManager.getGrid();
        
        // Get all adjacent cells and check for obstacles
        List<int[]> adjacentCells = getAdjacentCells(heroRow, heroCol);
        List<int[]> obstaclePositions = new ArrayList<>();
        
        for (int[] cell : adjacentCells) {
            int row = cell[0];
            int col = cell[1];
            
            if (gridManager.isValidPosition(row, col) && grid[row][col] instanceof ObstacleSpace) {
                obstaclePositions.add(new int[]{row, col});
            }
        }
        
        if (obstaclePositions.isEmpty()) {
            System.out.println("There are no obstacles within range to remove.");
            return false;
        }
        
        // If there's only one obstacle, remove it directly
        if (obstaclePositions.size() == 1) {
            int row = obstaclePositions.get(0)[0];
            int col = obstaclePositions.get(0)[1];
            grid[row][col] = new PlainSpace();
            System.out.println(hero.getName() + " removed an obstacle at [" + row + "," + col + "], turning it into a plain space.");
            return true;
        }
        
        // If there are multiple obstacles, let the player choose which one to remove
        System.out.println("Multiple obstacles detected within range. Choose one to remove:");
        for (int i = 0; i < obstaclePositions.size(); i++) {
            int[] pos = obstaclePositions.get(i);
            System.out.println((i + 1) + ". Obstacle at position [" + pos[0] + "," + pos[1] + "]");
        }
        
        int choice = InputHandler.getInstance().getIntInput("Enter the number of the obstacle to remove: ");
        if (choice > 0 && choice <= obstaclePositions.size()) {
            int row = obstaclePositions.get(choice - 1)[0];
            int col = obstaclePositions.get(choice - 1)[1];
            grid[row][col] = new PlainSpace();
            System.out.println(hero.getName() + " removed an obstacle at [" + row + "," + col + "], turning it into a plain space.");
            return true;
        } else {
            System.out.println("Invalid choice. No obstacle was removed.");
            return false;
        }
    }
    
    /**
     * Gets all cells adjacent to a position (including diagonals).
     * @param row Center row
     * @param col Center column
     * @return List of adjacent cell coordinates
     */
    private List<int[]> getAdjacentCells(int row, int col) {
        List<int[]> adjacentCells = new ArrayList<>();
        
        // Add the current cell
        adjacentCells.add(new int[]{row, col});
        
        // Add adjacent cells (up, down, left, right, and diagonals)
        adjacentCells.add(new int[]{row - 1, col}); // Up
        adjacentCells.add(new int[]{row + 1, col}); // Down
        adjacentCells.add(new int[]{row, col - 1}); // Left
        adjacentCells.add(new int[]{row, col + 1}); // Right
        adjacentCells.add(new int[]{row - 1, col - 1}); // Up-Left
        adjacentCells.add(new int[]{row - 1, col + 1}); // Up-Right
        adjacentCells.add(new int[]{row + 1, col - 1}); // Down-Left
        adjacentCells.add(new int[]{row + 1, col + 1}); // Down-Right
        
        return adjacentCells;
    }
    
    /**
     * Respawns a dead hero at their nexus.
     * @param hero The hero to respawn
     */
    public void respawnHero(Hero hero) {
        if (!hero.isAlive()) {
            // Reset hero stats
            hero.resetCurrentAttributes();
            
            // Move hero back to their nexus
            int[] nexus = hero.getNexus();
            if (gridManager.isValidPosition(nexus[0], nexus[1])) {
                hero.setHeroRow(nexus[0]);
                hero.setHeroCol(nexus[1]);
                gridManager.getGrid()[nexus[0]][nexus[1]].setOccupant(hero);
                System.out.println(hero.getName() + " has respawned at the nexus with full health and mana.");
            }
        }
    }
} 