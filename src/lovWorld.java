
import java.util.List;

import java.util.stream.Collectors;

/**
 * Implementation of the World class for Legends of Valor game.
 * This class delegates specific functionality to dedicated component classes.
 */
public class lovWorld extends World {
    @SuppressWarnings("unused")
    private List<Hero> heroes;
    @SuppressWarnings("unused")
    private List<Monster> monsters;
    private SpellSystem spellSystem;
    
    // Component classes
    private GridManager gridManager;
    private CharacterPositioner characterPositioner;
    private MovementSystem movementSystem;
    private TerrainEffectHandler terrainEffectHandler;
    private CombatSystem combatSystem;

    /**
     * Creates a new lovWorld with the specified dimensions.
     * @param row Number of rows
     * @param col Number of columns
     */
    public lovWorld(int row, int col) {
        super(row, col);
        
        // Initialize component classes
        this.gridManager = new GridManager(row, col);
        this.terrainEffectHandler = new TerrainEffectHandler();
        this.characterPositioner = new CharacterPositioner(gridManager);
        this.movementSystem = new MovementSystem(gridManager, characterPositioner, terrainEffectHandler);
        this.combatSystem = new CombatSystem(gridManager);
        
        // Initialize the grid spaces
        gridManager.initializeSpaces();
        
        // Update the super class grid
        this.grid = gridManager.getGrid();
        
        System.out.println("lovWorld initialized with custom board setup!");
    }

    @Override
    public void displayMap() {
        System.out.println("\u001B[36m╔════════╦════════╦════════╦════════╦════════╦════════╦════════╦════════╗\u001B[0m");

        for (int i = 0; i < grid.length; i++) {
            System.out.print("\u001B[36m║\u001B[0m");
            for (int j = 0; j < grid[i].length; j++) {
                Space space = grid[i][j];
                if (space != null && space.getOccupant() != null) {
                    String symbol = space.getOccupant().getSymbol();
                    // Color heroes in green and monsters in red
                    if (symbol.startsWith("H")) {
                        System.out.print("  \u001B[32m" + symbol + "\u001B[0m    ");
                    } else if (symbol.startsWith("M")) {
                        System.out.print("  \u001B[31m" + symbol + "\u001B[0m    ");
                    } else {
                        System.out.print("  " + symbol + "    ");
                    }
                } else if (space != null) {
                    String symbol = space.getSymbol();
                    // Color special spaces
                    if (symbol.equals("▒▒")) {
                        System.out.print("  \u001B[90m" + symbol + "\u001B[0m    "); // Gray for inaccessible
                    } else if (symbol.equals("HN")) {
                        System.out.print("  \u001B[32m" + symbol + "\u001B[0m    "); // Green for hero nexus
                    } else if (symbol.equals("MN")) {
                        System.out.print("  \u001B[31m" + symbol + "\u001B[0m    "); // Red for monster nexus
                    } else if (symbol.equals("B")) {
                        System.out.print("  \u001B[92m" + symbol + "\u001B[0m     "); // Light green for bush
                    } else if (symbol.equals("C")) {
                        System.out.print("  \u001B[94m" + symbol + "\u001B[0m     "); // Light blue for cave
                    } else if (symbol.equals("K")) {
                        System.out.print("  \u001B[33m" + symbol + "\u001B[0m     "); // Yellow for koulou
                    } else if (symbol.equals("O")) {
                        System.out.print("  \u001B[91m" + symbol + "\u001B[0m     "); // Light red for obstacle
                    } else {
                        System.out.print("  " + symbol + "     "); // Default for plain
                    }
                } else {
                    System.out.print("        "); // Empty space
                }
                System.out.print("\u001B[36m║\u001B[0m");
            }
            System.out.println();
            if (i < grid.length - 1) {
                System.out.println("\u001B[36m╠════════╬════════╬════════╬════════╬════════╬════════╬════════╬════════╣\u001B[0m");
            }
        }

        System.out.println("\u001B[36m╚════════╩════════╩════════╩════════╩════════╩════════╩════════╩════════╝\u001B[0m");
        
        // Display the map legend with colors
        System.out.println("\n\u001B[1mMAP LEGEND:\u001B[0m");
        System.out.println("\u001B[32mHN\u001B[0m - Hero Nexus | \u001B[31mMN\u001B[0m - Monster Nexus | \u001B[90m▒▒\u001B[0m - Inaccessible Space");
        System.out.println("P - Plain Space | \u001B[92mB\u001B[0m - Bush Space (+Dexterity) | \u001B[94mC\u001B[0m - Cave Space (+Agility)");
        System.out.println("\u001B[33mK\u001B[0m - Koulou Space (+Strength) | \u001B[91mO\u001B[0m - Obstacle Space (Can be removed)");
        System.out.println("\u001B[32mH1/H2/H3\u001B[0m - Heroes | \u001B[31mM1/M2/M3\u001B[0m - Monsters\n");
    }

    /**
     * Custom implementation to initialize spaces using the GridManager.
     * This replaces the parent class implementation.
     */
    protected void initializeSpaces() {
        // This is now handled by the GridManager
        // We just need to make sure the super class grid is updated
        if (gridManager != null) {
            this.grid = gridManager.getGrid();
        }
    }

    /**
     * Initializes heroes and monsters on the game board.
     * @param heroes List of heroes to place
     * @param monsters List of monsters to place
     */
    public void initializeHeroesAndMonsters(List<Hero> heroes, List<Monster> monsters) {
        this.heroes = heroes;
        this.monsters = monsters;
        
        // Initialize the SpellSystem with the heroes and monsters
        this.spellSystem = new SpellSystem(heroes, monsters, this);
        
        // Delegate to CharacterPositioner
        characterPositioner.initializeHeroesAndMonsters(heroes, monsters);
        
        // Update the grid reference
        this.grid = gridManager.getGrid();
    }

    /**
     * Sets the location for a hero on the grid.
     * @param hero The hero to position
     * @param targetRow Target row position
     * @param targetCol Target column position
     * @return true if positioning was successful, false otherwise
     */
    @SuppressWarnings("unused")
    private boolean setHeroLocation(Hero hero, int targetRow, int targetCol) {
        return characterPositioner.setHeroLocation(hero, targetRow, targetCol);
    }

    /**
     * Sets the location for a monster on the grid.
     * @param monster The monster to position
     * @param targetRow Target row position
     * @param targetCol Target column position
     * @return true if positioning was successful, false otherwise
     */
    @SuppressWarnings("unused")
    private boolean setMonsterLocation(Monster monster, int targetRow, int targetCol) {
        return characterPositioner.setMonsterLocation(monster, targetRow, targetCol);
    }

    /**
     * Allows a hero to select a monster to target.
     * @param hero The hero selecting a target
     * @param monsters List of monsters that could be targeted
     * @return The selected monster, or null if no valid target
     */
    public Monster selectTargetMonster(Hero hero, List<Monster> monsters) {
        return combatSystem.selectTargetMonster(hero, monsters);
    }

    /**
     * Delegate spell casting to the SpellSystem.
     * @param hero The hero casting the spell
     * @return true if a monster was defeated by the spell, false otherwise
     */
    public boolean castSpell(Hero hero) {
        return spellSystem.castSpell(hero);
    }

    /**
     * Allows a hero to use a potion from their inventory.
     * @param hero The hero using the potion
     */
    public void usePotion(Hero hero) {
        List<Item> potions = hero.getInventory().getItems().stream()
                .filter(item -> item instanceof Potion)
                .collect(Collectors.toList());

        if (potions.isEmpty()) {
            System.out.println("No potions available in inventory.");
            return;
        }

        System.out.println("Select a potion to use:");
        for (int i = 0; i < potions.size(); i++) {
            System.out.println((i + 1) + ". " + potions.get(i).getName());
        }

        int choice = InputHandler.getInstance().getIntInput("Enter the index of the potion you'd like to use: ");
        if (choice > 0 && choice <= potions.size()) {
            Potion selectedPotion = (Potion) potions.get(choice - 1);
            selectedPotion.use(hero);
            System.out.println(hero.getName() + " used " + selectedPotion.getName() + ".");
        } else {
            System.out.println("Invalid potion selection.");
        }
    }

    /**
     * Moves a hero in the specified direction.
     * @param hero The hero to move
     * @param direction Direction to move (w, a, s, d)
     * @return true if movement was successful, false otherwise
     */
    public boolean moveHero(Hero hero, String direction) {
        boolean result = movementSystem.moveHero(hero, direction);
        // Update the grid reference after movement
        this.grid = gridManager.getGrid();
        return result;
    }

    /**
     * Teleports a hero to a position near another hero.
     * @param teleportingHero The hero teleporting
     * @param targetHero The target hero to teleport near
     * @return true if teleport was successful, false otherwise
     */
    public boolean teleportHero(Hero teleportingHero, Hero targetHero) {
        boolean result = movementSystem.teleportHero(teleportingHero, targetHero);
        // Update the grid reference after teleport
        this.grid = gridManager.getGrid();
        return result;
    }

    /**
     * Recalls a hero to their nexus.
     * @param hero The hero to recall
     * @return true if recall was successful, false otherwise
     */
    public boolean recallHero(Hero hero) {
        boolean result = movementSystem.recallHero(hero);
        // Update the grid reference after recall
        this.grid = gridManager.getGrid();
        return result;
    }

    /**
     * Gets all monsters within attack range of a hero.
     * @param hero The hero to check from
     * @param monsters List of all monsters to check
     * @return List of monsters within range
     */
    public List<Monster> getMonstersInRange(Hero hero, List<Monster> monsters) {
        return combatSystem.getMonstersInRange(hero, monsters);
    }

    /**
     * Checks if a hero is in battle (has monsters in range).
     * @param hero The hero to check
     * @param monsters List of all monsters
     * @return true if hero is in battle, false otherwise
     */
    @SuppressWarnings("unused")
    private boolean isHeroInBattle(Hero hero, List<Monster> monsters) {
        return combatSystem.isHeroInBattle(hero, monsters);
    }

    /**
     * Moves a monster in the specified direction.
     * @param monster The monster to move
     * @param direction Direction to move (w, a, s, d)
     * @return true if movement was successful, false otherwise
     */
    public boolean moveMonster(Monster monster, String direction) {
        boolean result = movementSystem.moveMonster(monster, direction);
        // Update the grid reference after movement
        this.grid = gridManager.getGrid();
        return result;
    }

    /**
     * Checks if win conditions have been met.
     * @param heroes List of heroes
     * @param monsters List of monsters
     * @return true if game is over, false otherwise
     */
    public boolean checkWinCondition(List<Hero> heroes, List<Monster> monsters) {
        return combatSystem.checkWinCondition(heroes, monsters);
    }

    /**
     * Updates the board to reflect current positions of heroes and monsters.
     * @param heroes List of heroes
     * @param monsters List of monsters
     */
    public void updateBoard(List<Hero> heroes, List<Monster> monsters) {
        characterPositioner.updateBoard(heroes, monsters);
        // Update the grid reference
        this.grid = gridManager.getGrid();
    }
    
    /**
     * Checks if a hero is in a Nexus space.
     * @param hero The hero to check
     * @return true if the hero is in a Nexus, false otherwise
     */
    public boolean isInNexus(Hero hero) {
        return combatSystem.isInNexus(hero);
    }
    
    /**
     * Handles the logic to remove obstacles near a hero.
     * @param hero The hero removing the obstacle
     * @return true if an obstacle was removed, false otherwise
     */
    public boolean removeObstacle(Hero hero) {
        boolean result = combatSystem.removeObstacle(hero);
        // Update the grid reference
        this.grid = gridManager.getGrid();
        return result;
    }
    
    /**
     * Executes an attack from a monster to heroes in range.
     * @param monster The attacking monster
     * @param heroes List of potential hero targets
     * @return true if attack was successful, false otherwise
     */
    public boolean monsterAttack(Monster monster, List<Hero> heroes) {
        return combatSystem.monsterAttack(monster, heroes);
    }
    
    /**
     * Gets all heroes within attack range of a monster.
     * @param monster The monster to check from
     * @param heroes List of all heroes to check
     * @return List of heroes within range
     */
    public List<Hero> getHeroesInRange(Monster monster, List<Hero> heroes) {
        return combatSystem.getHeroesInRange(monster, heroes);
    }
    
    /**
     * Respawns a dead hero at their nexus.
     * @param hero The hero to respawn
     */
    public void respawnHero(Hero hero) {
        combatSystem.respawnHero(hero);
        // Update the grid reference
        this.grid = gridManager.getGrid();
    }
    
    /**
     * Distributes rewards to heroes when a monster is defeated.
     * @param monster The defeated monster
     * @param heroes List of heroes to receive rewards
     */
    public void distributeMonsterRewards(Monster monster, List<Hero> heroes) {
        combatSystem.distributeMonsterRewards(monster, heroes);
    }

    /**
     * Checks if a move to the given position is valid.
     * @param row Target row
     * @param col Target column
     * @return true if move is valid, false otherwise
     */
    protected boolean isValidMove(int row, int col) {
        return movementSystem.isValidMove(row, col);
    }
    
    /**
     * Checks if a move is valid without printing error messages.
     * @param row Target row
     * @param col Target column
     * @return true if move is valid, false otherwise
     */
    protected boolean isValidMoveNoPrint(int row, int col) {
        return movementSystem.isValidMoveNoPrint(row, col);
    }
    
    /**
     * Calculates a new position based on the current position and direction.
     * @param currentRow Current row
     * @param currentCol Current column
     * @param direction Direction to move (w, a, s, d)
     * @return New position as [row, col] or null if invalid direction
     */
    protected int[] calculateNewPosition(int currentRow, int currentCol, String direction) {
        return movementSystem.calculateNewPosition(currentRow, currentCol, direction);
    }
}



// Define the specific terrain space classes
class NexusSpace extends Space {
    private String type;

    public NexusSpace(String type) {
        this.type = type;
    }

    @Override
    public String getSymbol() {
        return type.equals("Hero Nexus") ? "HN" : "MN";
    }
}

class BushSpace extends Space {
    @Override
    public String getSymbol() {
        return "B";
    }
}

class CaveSpace extends Space {
    @Override
    public String getSymbol() {
        return "C";
    }
}

class KoulouSpace extends Space {
    @Override
    public String getSymbol() {
        return "K";
    }
}

class PlainSpace extends Space {
    @Override
    public String getSymbol() {
        return "P";
    }
}