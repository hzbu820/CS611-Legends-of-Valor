import java.util.List;

/**
 * This class handles the user interface elements of the game,
 * including displaying hero stats, menus, and game messages.
 */
public class GameUI {
    
    /**
     * Displays a welcome message for the game.
     */
    public void displayWelcomeMessage() {
        System.out.println("+---------------------------------------------------------+");
        System.out.println("|                 WELCOME TO LEGENDS OF VALOR            |");
        System.out.println("+---------------------------------------------------------+");
        System.out.println("| A MOBA-style game where heroes battle against monsters |");
        System.out.println("| Fight your way to the monsters' Nexus for victory!     |");
        System.out.println("+---------------------------------------------------------+");
    }
    
    /**
     * Displays the main game controls.
     */
    public void displayMainControls() {
        System.out.println("\nGAME CONTROLS:");
        System.out.println("W/A/S/D - Movement (up/left/down/right)");
        System.out.println("I - Hero information and inventory");
        System.out.println("M - Enter market (when in a Nexus space)");
        System.out.println("Q - Quit game");
    }
    
    /**
     * Displays the status of all heroes in a compact format.
     * @param heroes The list of heroes to display
     */
    public void displayHeroesStatus(List<Hero> heroes) {
        System.out.println("\n----- HERO STATUS -----");
        for (Hero hero : heroes) {
            displayCompactHeroStats(hero);
        }
        System.out.println("----------------------");
    }
    
    /**
     * Displays compact stats for a single hero.
     * @param hero The hero to display stats for
     */
    private void displayCompactHeroStats(Hero hero) {
        // Calculate health and mana percentages for colorization
        int healthPercent = (int) ((double) hero.getCurrentHealth() / hero.getHealthPoints() * 100);
        int manaPercent = (int) ((double) hero.getCurrentMana() / hero.getManaPoints() * 100);
        
        // Set colors based on percentages
        String healthColor = healthPercent > 70 ? "\u001B[32m" : // Green
                             healthPercent > 30 ? "\u001B[33m" : // Yellow
                             "\u001B[31m";                         // Red
        
        String manaColor = manaPercent > 70 ? "\u001B[34m" : // Blue
                           manaPercent > 30 ? "\u001B[36m" : // Cyan
                           "\u001B[35m";                       // Purple
        
        // Display hero identifier and name
        System.out.print("\u001B[1m" + hero.getHeroIdentifier() + ": " + hero.getName() + "\u001B[0m");
        
        // Status indicator (Alive/Dead)
        if (hero.isAlive()) {
            System.out.print(" [ACTIVE] ");
        } else {
            System.out.print(" [\u001B[31mDEAD\u001B[0m] ");
        }
        
        // HP bar
        System.out.print(" HP: " + healthColor + hero.getCurrentHealth() + "/" + hero.getHealthPoints() + "\u001B[0m");
        
        // MP bar
        System.out.print(" MP: " + manaColor + hero.getCurrentMana() + "/" + hero.getManaPoints() + "\u001B[0m");
        
        // Level and gold
        System.out.println(" | Lvl: " + hero.getLevel() + " | Gold: " + hero.getGold());
    }
    
    /**
     * Displays detailed stats for a specific hero.
     * @param hero The hero to display
     */
    public void displayHeroStats(Hero hero) {
        System.out.println("\n===== " + hero.getName() + " (" + hero.getHeroIdentifier() + ") =====");
        System.out.println("Class: " + hero.getHeroClass());
        System.out.println("Level: " + hero.getLevel());
        
        // Display HP and MP with colorization
        int hpPercent = (int)((double)hero.getCurrentHealth() / hero.getHealthPoints() * 100);
        int mpPercent = (int)((double)hero.getCurrentMana() / hero.getManaPoints() * 100);
        
        String hpColor = hpPercent > 50 ? "\u001B[32m" : hpPercent > 25 ? "\u001B[33m" : "\u001B[31m";
        String mpColor = mpPercent > 50 ? "\u001B[34m" : mpPercent > 25 ? "\u001B[36m" : "\u001B[35m";
        
        System.out.println("HP: " + hpColor + hero.getCurrentHealth() + "/" + hero.getHealthPoints() + "\u001B[0m");
        System.out.println("MP: " + mpColor + hero.getCurrentMana() + "/" + hero.getManaPoints() + "\u001B[0m");
        
        // Display attributes with modifiers
        System.out.println("Strength: " + hero.getCurrentStrength() + 
                (hero.getCurrentStrength() != hero.getStrength() ? " (Base: " + hero.getStrength() + ")" : ""));
        System.out.println("Dexterity: " + hero.getCurrentDexterity() + 
                (hero.getCurrentDexterity() != hero.getDexterity() ? " (Base: " + hero.getDexterity() + ")" : ""));
        System.out.println("Agility: " + hero.getCurrentAgility() + 
                (hero.getCurrentAgility() != hero.getAgility() ? " (Base: " + hero.getAgility() + ")" : ""));
        
        // Gold
        System.out.println("Gold: " + hero.getGold());
        
        // Experience
        hero.printExperienceProgress();
        
        // Equipped items
        System.out.println("\nEquipped Items:");
        displayEquippedItems(hero.getInventory());
        
        // Display inventory items
        System.out.println("\nInventory Items:");
        displayInventoryItems(hero.getInventory());
        
        // Learned spells
        if (hero.hasLearnedSpells()) {
            System.out.println("\nLearned Spells:");
            hero.displayLearnedSpells();
        }
    }
    
    /**
     * Displays a hero's equipped items.
     * @param inventory The inventory to check
     */
    private void displayEquippedItems(Inventory inventory) {
        // Display equipped weapons
        List<Weapon> weapons = inventory.getCurrentWeapon();
        if (weapons != null && !weapons.isEmpty()) {
            for (Weapon weapon : weapons) {
                System.out.println("Weapon: " + weapon.getName() + " (Damage: " + weapon.getDamage() + 
                        ", Hands: " + weapon.getRequiredHands() + ")");
            }
        } else {
            System.out.println("No weapons equipped");
        }
        
        // Display equipped armor
        Armor armor = inventory.getEquippedArmor();
        if (armor != null) {
            System.out.println("Armor: " + armor.getName() + " (Defense: " + armor.getDamageReduction() + ")");
        } else {
            System.out.println("No armor equipped");
        }
    }
    
    /**
     * Displays a hero's inventory items.
     * @param inventory The inventory to display
     */
    private void displayInventoryItems(Inventory inventory) {
        List<Item> items = inventory.getItems();
        if (items.isEmpty()) {
            System.out.println("Inventory is empty");
            return;
        }
        
        // Group items by type
        System.out.println("Weapons:");
        boolean hasWeapons = false;
        for (Item item : items) {
            if (item instanceof Weapon) {
                Weapon weapon = (Weapon) item;
                System.out.println("  • " + weapon.getName() + " (Damage: " + weapon.getDamage() + 
                        ", Hands: " + weapon.getRequiredHands() + ", Level: " + item.getLevelRequirement() + ")");
                hasWeapons = true;
            }
        }
        if (!hasWeapons) {
            System.out.println("  None");
        }
        
        System.out.println("Armor:");
        boolean hasArmor = false;
        for (Item item : items) {
            if (item instanceof Armor) {
                Armor armor = (Armor) item;
                System.out.println("  • " + armor.getName() + " (Defense: " + armor.getDamageReduction() + 
                        ", Level: " + item.getLevelRequirement() + ")");
                hasArmor = true;
            }
        }
        if (!hasArmor) {
            System.out.println("  None");
        }
        
        System.out.println("Potions:");
        boolean hasPotions = false;
        for (Item item : items) {
            if (item instanceof Potion) {
                Potion potion = (Potion) item;
                System.out.println("  • " + potion.getName() + " (+" + potion.getAttributeIncrease() + " " + 
                        potion.getAttributeAffected() + ", Level: " + item.getLevelRequirement() + ")");
                hasPotions = true;
            }
        }
        if (!hasPotions) {
            System.out.println("  None");
        }
    }
    
    /**
     * Displays a game over message based on who won.
     * @param heroesWon True if heroes won, false if monsters won
     */
    public void displayGameOverMessage(boolean heroesWon) {
        if (heroesWon) {
            System.out.println("\n\u001B[32m+-------------------------------------------+");
            System.out.println("| VICTORY! YOUR HEROES HAVE WON THE BATTLE! |");
            System.out.println("|  The heroes have reached the monster Nexus  |");
            System.out.println("|   and successfully stopped the invasion!   |");
            System.out.println("+-------------------------------------------+\u001B[0m");
        } else {
            System.out.println("\n\u001B[31m+--------------------------------------------+");
            System.out.println("|  DEFEAT! THE MONSTERS HAVE OVERRUN YOU!    |");
            System.out.println("| The monsters have reached your hero Nexus   |");
            System.out.println("|      and the invasion was successful!      |");
            System.out.println("+--------------------------------------------+\u001B[0m");
        }
        
        System.out.println("\nThank you for playing Legends of Valor!");
    }
    
    /**
     * Displays an error message.
     * @param message The error message to display
     */
    public void displayErrorMessage(String message) {
        System.out.println("\u001B[31mERROR: " + message + "\u001B[0m");
    }
    
    /**
     * Displays a success message.
     * @param message The success message to display
     */
    public void displaySuccessMessage(String message) {
        System.out.println("\u001B[32m" + message + "\u001B[0m");
    }
    
    /**
     * Displays an informational message.
     * @param message The info message to display
     */
    public void displayInfoMessage(String message) {
        System.out.println("\u001B[34m" + message + "\u001B[0m");
    }
    
    /**
     * Displays the game map and highlights special spaces and characters
     * @param world The game world
     * @param heroes List of heroes
     * @param monsters List of monsters
     */
    public void displayMap(lovWorld world, List<Hero> heroes, List<Monster> monsters) {
        System.out.println("\n=== World Map ===");
        
        Space[][] grid = world.grid;
        
        // Detect treasure chests and inform player
        boolean hasTreasureChests = false;
        
        // Print top border
        System.out.print("  ");
        for (int j = 0; j < grid[0].length; j++) {
            System.out.print("--");
        }
        System.out.println("-");
        
        for (int i = 0; i < grid.length; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] instanceof TreasureChest) {
                    hasTreasureChests = true;
                    // Print in yellow for treasure chests
                    System.out.print("\u001B[33m" + grid[i][j].getSymbol() + "\u001B[0m ");
                } else if (grid[i][j].getOccupant() instanceof Hero) {
                    // Print in green for heroes
                    System.out.print("\u001B[32m" + grid[i][j].getSymbol() + "\u001B[0m ");
                } else if (grid[i][j].getOccupant() instanceof Monster) {
                    // Print in red for monsters
                    System.out.print("\u001B[31m" + grid[i][j].getSymbol() + "\u001B[0m ");
                } else if (grid[i][j] instanceof NexusSpace) {
                    // Print in cyan for nexus spaces
                    System.out.print("\u001B[36m" + grid[i][j].getSymbol() + "\u001B[0m ");
                } else if (grid[i][j] instanceof InaccessibleSpace) {
                    // Print in dark gray for inaccessible
                    System.out.print("\u001B[90m" + grid[i][j].getSymbol() + "\u001B[0m ");
                } else {
                    System.out.print(grid[i][j].getSymbol() + " ");
                }
            }
            System.out.println("|");
        }
        
        // Print bottom border
        System.out.print("  ");
        for (int j = 0; j < grid[0].length; j++) {
            System.out.print("--");
        }
        System.out.println("-");
        
        // Print column numbers
        System.out.print("   ");
        for (int j = 0; j < grid[0].length; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
        
        // Show special message for treasure chests
        if (hasTreasureChests) {
            System.out.println("\u001B[33m★\u001B[0m - Treasure chests detected! Move onto them to collect rewards.");
        }
    }
} 