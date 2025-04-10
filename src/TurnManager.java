import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the turn-based mechanics of the game,
 * including hero and monster turns, action processing, and round management.
 */
public class TurnManager {
    private List<Hero> heroes;
    private List<Monster> monsters;
    private lovWorld world;
    private GameSetup gameSetup;
    private boolean isGameOver;
    private int round;
    private int difficulty;
    
    /**
     * Creates a new TurnManager with the specified game elements.
     * @param heroes The list of heroes in the game
     * @param monsters The list of monsters in the game
     * @param world The game world
     * @param difficulty The game difficulty level
     */
    public TurnManager(List<Hero> heroes, List<Monster> monsters, lovWorld world, int difficulty) {
        this.heroes = heroes;
        this.monsters = monsters;
        this.world = world;
        this.gameSetup = new GameSetup();
        this.isGameOver = false;
        this.round = 1;
        this.difficulty = difficulty;
    }
    
    /**
     * Starts a turn-based round of gameplay.
     * @return true if the game has ended, false otherwise
     */
    public boolean executeRound() {
        System.out.println("\n--- Round " + round + " ---");
        
        // Process heroes' turns
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                executeHeroTurn(hero);
                
                // Check if the game ended during hero's turn
                if (isGameOver) {
                    return true;
                }
            }
        }
        
        // Process monsters' turns
        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                executeMonsterTurn(monster);
                
                // Check if the game ended during monster's turn
                if (isGameOver) {
                    return true;
                }
            }
        }
        
        // Restore hero health and mana per turn
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                hero.restorePerTurn();
            }
        }
        
        // Respawn any dead heroes
        for (Hero hero : heroes) {
            if (!hero.isAlive()) {
                world.respawnHero(hero);
            }
        }
        
        // Spawn new monsters based on difficulty
        if (shouldSpawnMonsters()) {
            spawnNewMonsters();
        }
        
        round++;
        return false;
    }
    
    /**
     * Executes a turn for a specific hero.
     * @param hero The hero whose turn is being processed
     */
    private void executeHeroTurn(Hero hero) {
        System.out.println("\nHero " + hero.getHeroIdentifier() + "'s turn:");
        boolean inBattle = isHeroInBattle(hero);
        
        // Define actions based on mode
        List<String> actions = inBattle
                ? Arrays.asList("Move", "Attack", "Cast Spell", "Use Potion", "Change Weapon/Armor", "Remove Obstacle", "View Combat Log", "Check Status", "Skip Turn")
                : Arrays.asList("Move", "Use Potion", "Change Weapon/Armor", "Teleport", "Recall", "Shop", "Remove Obstacle", "View Combat Log", "Check Status", "Skip Turn");
        
        // Display Available Actions
        System.out.println("Available actions for " + hero.getName() + " (" + (inBattle ? "Battle Mode" : "Exploration Mode") + "):");
        for (int i = 0; i < actions.size(); i++) {
            System.out.println((i + 1) + ". " + actions.get(i));
        }
        
        // Handle Hero Action
        boolean validAction = false;
        while (!validAction) {
            int action = InputHandler.getInstance().getIntInput("Enter the action number: ");
            
            // Validate action index
            if (action < 1 || action > actions.size()) {
                System.out.println("Invalid action. Please enter a valid number.");
                continue;
            }
            
            // Process the selected action
            String selectedAction = actions.get(action - 1);
            validAction = processHeroAction(hero, selectedAction, inBattle);
        }
    }
    
    /**
     * Processes a hero's selected action.
     * @param hero The hero performing the action
     * @param action The selected action
     * @param inBattle Whether the hero is in battle
     * @return true if the action was processed successfully, false otherwise
     */
    private boolean processHeroAction(Hero hero, String action, boolean inBattle) {
        switch (action) {
            case "Move":
                String direction = InputHandler.getInstance().getStringInput("Enter direction (w = up, a = left, s = down, d = right): ");
                if (world.moveHero(hero, direction)) {
                    world.updateBoard(heroes, monsters);
                    world.displayMap();
                    return true;
                }
                return false;
                
            case "Attack":
                if (inBattle) {
                    Monster target = world.selectTargetMonster(hero, monsters);
                    if (target != null) {
                        // Attack only if the monster is alive
                        if (target.isAlive()) {
                            hero.attack(target); // Perform the attack
                            
                            // Check if monster was defeated and distribute rewards
                            if (!target.isAlive()) {
                                world.distributeMonsterRewards(target, heroes);
                                // Update and display the map after a monster is defeated
                                world.updateBoard(heroes, monsters);
                                world.displayMap();
                            }
                            
                            return true; // Mark the action as complete
                        } else {
                            System.out.println(target.getName() + " is already defeated and cannot be attacked.");
                        }
                    } else {
                        System.out.println("No valid target selected.");
                    }
                } else {
                    System.out.println("Invalid action. Attack is only available in Battle Mode.");
                }
                return false;
                
            case "Use Potion":
                world.usePotion(hero);
                return true;
                
            case "Change Weapon/Armor":
                boolean equipSuccess = hero.getInventory().equipItem();
                if (!equipSuccess) {
                    System.out.println("No weapons or armor available to equip.");
                    return false;
                }
                return true;
                
            case "Cast Spell":
                if (inBattle) {
                    boolean monsterDefeated = world.castSpell(hero);
                    if (monsterDefeated) {
                        // Update and display the map after a monster is defeated by a spell
                        world.updateBoard(heroes, monsters);
                        world.displayMap();
                    }
                    return true;
                } else {
                    System.out.println("Invalid action. Casting spells is only available in Battle Mode.");
                }
                return false;
                
            case "Teleport":
                System.out.println("\nChoose a hero you want to teleport to:");
                
                List<Hero> teleportableHeroes = new ArrayList<>();
                for (Hero target : heroes) {
                    if (target != hero) {
                        teleportableHeroes.add(target);
                    }
                }
                
                for (int i = 0; i < teleportableHeroes.size(); i++) {
                    System.out.println((i + 1) + ". " + teleportableHeroes.get(i).getName() + " (" 
                            + teleportableHeroes.get(i).getHeroIdentifier() + ")");
                }
                
                int heroIndex = InputHandler.getInstance().getIntInput("Enter the hero's number: ") - 1;
                while (heroIndex < 0 || heroIndex >= teleportableHeroes.size()) {
                    System.out.println("Invalid selection. Please try again.");
                    heroIndex = InputHandler.getInstance().getIntInput("Enter the hero's number: ") - 1;
                }
                
                // Perform the teleport
                Hero targetHero = teleportableHeroes.get(heroIndex);
                if (world.teleportHero(hero, targetHero)) {
                    world.updateBoard(heroes, monsters);
                    world.displayMap();
                    return true;
                }
                return false;
                
            case "Recall":
                if (world.recallHero(hero)) {
                    world.updateBoard(heroes, monsters);
                    world.displayMap();
                    return true;
                }
                return false;
                
            case "Shop":
                if (world.isInNexus(hero)) {
                    enterMarket(hero);
                    return true;
                } else {
                    System.out.println("You can only shop when in a Nexus.");
                }
                return false;
                
            case "Remove Obstacle":
                if (world.removeObstacle(hero)) {
                    world.updateBoard(heroes, monsters);
                    world.displayMap();
                    return true;
                }
                return false;
                
            case "View Combat Log":
                CombatLogger.getInstance().displayCombatHistory();
                return true;
                
            case "Check Status":
                displayHeroStats(hero);
                return true;
                
            case "Skip Turn":
                System.out.println(hero.getName() + " skips their turn.");
                return true;
                
            default:
                System.out.println("Invalid action.");
                return false;
        }
    }
    
    /**
     * Executes a turn for a specific monster.
     * @param monster The monster whose turn is being processed
     */
    private void executeMonsterTurn(Monster monster) {
        if (!monster.isAlive()) {
            return;
        }
        
        // Check if there's a hero in attack range
        List<Hero> heroesInRange = world.getHeroesInRange(monster, heroes);
        if (!heroesInRange.isEmpty()) {
            // Attack a random hero in range
            Hero target = heroesInRange.get(new java.util.Random().nextInt(heroesInRange.size()));
            world.monsterAttack(monster, heroes);
            System.out.println("\nMonster " + monster.getMonsterIdentifier() + " attacks " + target.getHeroIdentifier() + "!");
        } else {
            // Move toward hero nexus if no targets in range
            world.moveMonster(monster, "s");  // Monsters move down toward hero nexus
            System.out.println("\nMonster " + monster.getMonsterIdentifier() + " moves closer to the hero nexus!");
            world.updateBoard(heroes, monsters);
        }
        
        // Check win condition after monster moves
        if (checkWinCondition()) {
            isGameOver = true;
        }
    }
    
    /**
     * Enters the market for a hero to buy/sell items.
     * @param hero The hero entering the market
     */
    private void enterMarket(Hero hero) {
        Market market = new Market();
        market.enterMarket(hero);
    }
    
    /**
     * Displays detailed stats for a hero.
     * @param hero The hero whose stats to display
     */
    private void displayHeroStats(Hero hero) {
        System.out.println("\n==== " + hero.getName() + " (" + hero.getHeroIdentifier() + ") ====");
        System.out.println("Class: " + hero.getHeroClass());
        System.out.println("Level: " + hero.getLevel());
        
        // Display HP and MP with colorization based on percentage
        int hpPercent = (int)((double)hero.getCurrentHealth() / hero.getHealthPoints() * 100);
        int mpPercent = (int)((double)hero.getCurrentMana() / hero.getManaPoints() * 100);
        
        String hpColor = hpPercent > 50 ? "\u001B[32m" : hpPercent > 25 ? "\u001B[33m" : "\u001B[31m";
        String mpColor = mpPercent > 50 ? "\u001B[34m" : mpPercent > 25 ? "\u001B[36m" : "\u001B[35m";
        
        System.out.println("HP: " + hpColor + hero.getCurrentHealth() + "/" + hero.getHealthPoints() + "\u001B[0m");
        System.out.println("MP: " + mpColor + hero.getCurrentMana() + "/" + hero.getManaPoints() + "\u001B[0m");
        
        // Display attributes
        System.out.println("Strength: " + hero.getCurrentStrength() + 
                (hero.getCurrentStrength() != hero.getStrength() ? " (Base: " + hero.getStrength() + ")" : ""));
        System.out.println("Dexterity: " + hero.getCurrentDexterity() + 
                (hero.getCurrentDexterity() != hero.getDexterity() ? " (Base: " + hero.getDexterity() + ")" : ""));
        System.out.println("Agility: " + hero.getCurrentAgility() + 
                (hero.getCurrentAgility() != hero.getAgility() ? " (Base: " + hero.getAgility() + ")" : ""));
        
        // Display gold
        System.out.println("Gold: " + hero.getGold());
        
        // Experience progress
        hero.printExperienceProgress();
        
        // Equipped items
        System.out.println("\nEquipped Items:");
        displayEquippedItems(hero.getInventory());
        
        // Inventory status
        System.out.println("\nInventory Status:");
        displayInventorySummary(hero.getInventory());
        
        // Learned spells
        if (hero.hasLearnedSpells()) {
            System.out.println("\nLearned Spells:");
            hero.displayLearnedSpells();
        }
    }
    
    /**
     * Displays a summary of equipped items from inventory.
     * @param inventory The inventory to display equipped items from
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
     * Displays a summary of the inventory contents.
     * @param inventory The inventory to display
     */
    private void displayInventorySummary(Inventory inventory) {
        List<Item> items = inventory.getItems();
        
        // Count item types
        int weaponCount = 0;
        int armorCount = 0;
        int potionCount = 0;
        int spellCount = 0;
        
        for (Item item : items) {
            if (item instanceof Weapon) {
                weaponCount++;
            } else if (item instanceof Armor) {
                armorCount++;
            } else if (item instanceof Potion) {
                potionCount++;
            } else if (item instanceof Spell) {
                spellCount++;
            }
        }
        
        // Display counts
        System.out.println("Weapons: " + weaponCount);
        System.out.println("Armor pieces: " + armorCount);
        System.out.println("Potions: " + potionCount);
        System.out.println("Spells: " + spellCount);
        System.out.println("Total items: " + items.size());
    }
    
    /**
     * Determines if a hero is in battle with monsters based on proximity.
     * @param hero The hero to check
     * @return true if the hero is in battle, false otherwise
     */
    private boolean isHeroInBattle(Hero hero) {
        if (!hero.isAlive()) {
            return false;
        }
        
        for (Monster monster : monsters) {
            if (monster.isAlive()) {
                int heroRow = hero.getHeroRow();
                int heroCol = hero.getHeroCol();
                int monsterRow = monster.getMonsterRow();
                int monsterCol = monster.getMonsterCol();
                
                // If monster is within attack range (current or adjacent cell)
                if (Math.abs(heroRow - monsterRow) <= 1 && Math.abs(heroCol - monsterCol) <= 1) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Checks if new monsters should spawn this round based on difficulty.
     * @return true if monsters should spawn, false otherwise
     */
    private boolean shouldSpawnMonsters() {
        // Easy: every 6 rounds, Medium: every 4 rounds, Hard: every 2 rounds
        int spawnFrequency;
        switch (difficulty) {
            case 1: // Easy
                spawnFrequency = 6;
                break;
            case 2: // Medium
                spawnFrequency = 4;
                break;
            case 3: // Hard
                spawnFrequency = 2;
                break;
            default:
                spawnFrequency = 4; // Default to medium
        }
        
        return round % spawnFrequency == 0;
    }
    
    /**
     * Spawns new monsters for the next wave.
     */
    private void spawnNewMonsters() {
        // Find the highest level among heroes
        int highestHeroLevel = 1;
        for (Hero hero : heroes) {
            if (hero.getLevel() > highestHeroLevel) {
                highestHeroLevel = hero.getLevel();
            }
        }
        
        // Spawn new monsters at the highest hero level
        List<Monster> newMonsters = gameSetup.spawnNewMonsters(highestHeroLevel);
        if (newMonsters != null && !newMonsters.isEmpty()) {
            // Limit the number of total monsters to avoid grid overflow
            // The grid can safely handle 3 monsters (each taking 2 spaces)
            int maxTotalMonsters = 3;
            
            // Only add new monsters if there's room for them
            int currentMonsterCount = monsters.size();
            int monsterCapacity = maxTotalMonsters - currentMonsterCount;
            
            if (monsterCapacity > 0) {
                // Only add up to the capacity
                int monstersToAdd = Math.min(monsterCapacity, newMonsters.size());
                for (int i = 0; i < monstersToAdd; i++) {
                    monsters.add(newMonsters.get(i));
                }
                
                System.out.println("\n\u001B[31m!!! " + monstersToAdd + " NEW MONSTERS HAVE SPAWNED AT THE MONSTER NEXUS !!!\u001B[0m");
                
                // Set monster identifiers
                for (int i = 0; i < monsters.size(); i++) {
                    monsters.get(i).setMonsterIdentifier("M" + (i + 1));
                }
                
                // Update the board with new monsters
                world.initializeHeroesAndMonsters(heroes, monsters);
                world.updateBoard(heroes, monsters);
                world.displayMap();
            } else {
                System.out.println("\nThe battlefield is already at maximum monster capacity!");
            }
        }
    }
    
    /**
     * Checks win conditions for the game.
     * @return true if either heroes or monsters have won, false otherwise
     */
    private boolean checkWinCondition() {
        return world.checkWinCondition(heroes, monsters);
    }
    
    /**
     * Gets the current round number.
     * @return The current round number
     */
    public int getRound() {
        return round;
    }
    
    /**
     * Checks if the game is over.
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return isGameOver;
    }
} 