import java.util.ArrayList;
import java.util.List;

/**
 * Implements the original Monsters and Heroes game.
 * This class handles the game logic for the classic RPG adventure mode.
 */
public class MonstersAndHeroesGame {
    private List<Hero> heroes;
    private World world;
    private Market market;
    private boolean isGameOver;

    /**
     * Creates a new Monsters and Heroes game instance
     */
    public MonstersAndHeroesGame() {
        this.heroes = new ArrayList<>();
        this.market = new Market();
        this.isGameOver = false;
        
        // Initialize heroes
        setupHeroes();
        
        // Create a standard world (8x8 is typical for the original game)
        this.world = new World(8, 8);
    }

    /**
     * Starts the Monsters and Heroes game
     */
    public void start() {
        System.out.println("Welcome to Monsters and Heroes!");
        System.out.println("Explore the world, battle monsters, and become legendary heroes.");

        // Main game loop
        while (!isGameOver) {
            // Display the current state of the map
            world.displayMap();
            
            // Display hero status
            displayHeroesStatus();
            
            // Get player command
            System.out.println("Use W/A/S/D for movement, I for info, M for market, and Q to quit.");
            String command = InputHandler.getInstance().getCommand().toLowerCase();
            
            // Process the command
            processCommand(command);
        }
        
        System.out.println("Game Over. Thank you for playing Monsters and Heroes!");
    }
    
    /**
     * Processes a player command
     * @param command The command to process
     */
    private void processCommand(String command) {
        switch (command) {
            case "w":
            case "a":
            case "s":
            case "d":
                // Move the party in the specified direction
                if (world.moveParty(command)) {
                    // Check if the party is in a special space
                    if (world.isInMarketSpace()) {
                        System.out.println("You see a marketplace. Press M to enter it or anything else to continue.");
                        command = InputHandler.getInstance().getCommand().toLowerCase();
                        if (command.equals("m")) {
                            enterMarket();
                        } else {
                            System.out.println("You passed by the marketplace.");
                        }
                    } else if (world.isInCommonSpace()) {
                        // In the original game, common spaces had a chance to trigger battles
                        boolean encounterMonster = Math.random() < 0.4; // 40% chance to encounter monsters
                        if (encounterMonster) {
                            System.out.println("You encountered monsters!");
                            startBattle();
                        } else {
                            System.out.println("You travel safely through the area.");
                        }
                    }
                }
                break;
                
            case "q":
                isGameOver = true;
                System.out.println("You have decided to end your adventure. Farewell, heroes!");
                break;
                
            case "i":
                showHeroStats();
                break;
                
            case "m":
                if (world.isInMarketSpace()) {
                    enterMarket();
                } else {
                    System.out.println("There is no market here. Find a market space to trade.");
                }
                break;
                
            default:
                System.out.println("Invalid command. Use W/A/S/D for movement, I for info, M for market, and Q to quit.");
                break;
        }
    }
    
    /**
     * Sets up the hero party for the game
     */
    private void setupHeroes() {
        System.out.println("=== HERO SELECTION ===");
        int partySize = InputHandler.getInstance().getIntInput("How many heroes do you want in your party? (1-3): ");
        
        // Validate party size
        partySize = Math.max(1, Math.min(3, partySize));
        
        for (int i = 0; i < partySize; i++) {
            System.out.println("\nSelecting Hero " + (i + 1) + ":");
            
            // Choose hero class
            String heroClass = "";
            boolean validClassChoice = false;
            
            while (!validClassChoice) {
                System.out.println("Choose a hero class:");
                System.out.println("1. Warrior (High Strength and Agility)");
                System.out.println("2. Sorcerer (High Mana and Dexterity)");
                System.out.println("3. Paladin (High Strength and Dexterity)");
                
                int classChoice = InputHandler.getInstance().getIntInput("Enter your choice (1-3): ");
                
                switch (classChoice) {
                    case 1:
                        heroClass = "Warrior";
                        validClassChoice = true;
                        break;
                    case 2:
                        heroClass = "Sorcerer";
                        validClassChoice = true;
                        break;
                    case 3:
                        heroClass = "Paladin";
                        validClassChoice = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                        break;
                }
            }
            
            // Choose a hero from the selected class
            Hero hero = HeroFactory.createHero(heroClass);
            heroes.add(hero);
            
            System.out.println(hero.getName() + " has joined your party!");
        }
        
        System.out.println("\nYour party is ready for adventure!");
    }
    
    /**
     * Starts a battle encounter with monsters
     */
    private void startBattle() {
        // Create appropriate level monsters based on hero levels
        int highestHeroLevel = 1;
        for (Hero hero : heroes) {
            if (hero.getLevel() > highestHeroLevel) {
                highestHeroLevel = hero.getLevel();
            }
        }
        
        // Create monsters based on hero level
        List<Monster> monsters = new ArrayList<>();
        MonsterFactory monsterFactory = new MonsterFactory();
        
        // Create a random number of monsters (1-3)
        int monsterCount = 1 + (int)(Math.random() * 3);
        for (int i = 0; i < monsterCount; i++) {
            Monster monster = monsterFactory.createMonster(highestHeroLevel);
            if (monster != null) {
                monsters.add(monster);
                System.out.println("A " + monster.getName() + " (Level " + monster.getLevel() + ") appears!");
            }
        }
        
        // Begin the battle
        Battle battle = new Battle(heroes, monsters);
        boolean victory = battle.startBattle();
        
        if (victory) {
            System.out.println("Victory! Your heroes have defeated the monsters!");
            
            // In the original game, check if all heroes are dead
            boolean allHeroesDead = true;
            for (Hero hero : heroes) {
                if (hero.isAlive()) {
                    allHeroesDead = false;
                    break;
                }
            }
            
            if (allHeroesDead) {
                System.out.println("However, all your heroes have fallen in battle.");
                isGameOver = true;
            }
        } else {
            System.out.println("Defeat! Your party has been defeated by the monsters.");
            isGameOver = true;
        }
    }
    
    /**
     * Enters the market for trading
     */
    private void enterMarket() {
        System.out.println("\nWelcome to the Market!");
        
        boolean exitMarket = false;
        while (!exitMarket) {
            System.out.println("\nSelect a hero to trade with:");
            
            for (int i = 0; i < heroes.size(); i++) {
                System.out.println((i + 1) + ". " + heroes.get(i).getName() + " (Gold: " + heroes.get(i).getGold() + ")");
            }
            System.out.println((heroes.size() + 1) + ". Exit Market");
            
            int heroChoice = InputHandler.getInstance().getIntInput("Select an option: ");
            
            if (heroChoice == heroes.size() + 1) {
                exitMarket = true;
                System.out.println("You leave the market.");
            } else if (heroChoice > 0 && heroChoice <= heroes.size()) {
                Hero selectedHero = heroes.get(heroChoice - 1);
                market.enterMarket(selectedHero);
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    /**
     * Displays detailed information about heroes
     */
    private void showHeroStats() {
        System.out.println("\n=== HERO INFORMATION ===");
        
        for (int i = 0; i < heroes.size(); i++) {
            System.out.println((i + 1) + ". " + heroes.get(i).getName());
        }
        
        int heroChoice = InputHandler.getInstance().getIntInput("Select a hero to view (0 to cancel): ");
        
        if (heroChoice > 0 && heroChoice <= heroes.size()) {
            Hero selectedHero = heroes.get(heroChoice - 1);
            displayHeroStats(selectedHero);
        }
    }
    
    /**
     * Displays detailed stats for a specific hero
     * @param hero The hero to display stats for
     */
    private void displayHeroStats(Hero hero) {
        System.out.println("\n=== " + hero.getName() + " ===");
        System.out.println("Class: " + hero.getHeroClass());
        System.out.println("Level: " + hero.getLevel());
        
        // Display HP and MP with color coding
        int hpPercent = (int)((double)hero.getCurrentHealth() / hero.getHealthPoints() * 100);
        int mpPercent = (int)((double)hero.getCurrentMana() / hero.getManaPoints() * 100);
        
        String hpColor = hpPercent > 50 ? "\u001B[32m" : hpPercent > 25 ? "\u001B[33m" : "\u001B[31m";
        String mpColor = mpPercent > 50 ? "\u001B[34m" : mpPercent > 25 ? "\u001B[36m" : "\u001B[35m";
        
        System.out.println("HP: " + hpColor + hero.getCurrentHealth() + "/" + hero.getHealthPoints() + "\u001B[0m");
        System.out.println("MP: " + mpColor + hero.getCurrentMana() + "/" + hero.getManaPoints() + "\u001B[0m");
        
        // Display attributes
        System.out.println("Strength: " + hero.getCurrentStrength());
        System.out.println("Dexterity: " + hero.getCurrentDexterity());
        System.out.println("Agility: " + hero.getCurrentAgility());
        
        // Display gold
        System.out.println("Gold: " + hero.getGold());
        
        // Experience progress
        hero.printExperienceProgress();
        
        // Inventory
        System.out.println("\nInventory:");
        hero.getInventory().showItems();
        
        // Equipped items
        System.out.println("\nEquipped Items:");
        
        Weapon weapon = hero.getInventory().getEquippedWeapon();
        if (weapon != null) {
            System.out.println("Weapon: " + weapon.getName() + " (Damage: " + weapon.getDamage() + ")");
        } else {
            System.out.println("Weapon: None");
        }
        
        Armor armor = hero.getInventory().getEquippedArmor();
        if (armor != null) {
            System.out.println("Armor: " + armor.getName() + " (Defense: " + armor.getDamageReduction() + ")");
        } else {
            System.out.println("Armor: None");
        }
        
        // Actions
        System.out.println("\nActions:");
        System.out.println("1. Equip/Unequip Items");
        System.out.println("2. Use Potion");
        System.out.println("3. Back");
        
        int action = InputHandler.getInstance().getIntInput("Select an action: ");
        
        switch (action) {
            case 1:
                System.out.println("\n1. Equip Item\n2. Unequip Item\n3. Cancel");
                int equipAction = InputHandler.getInstance().getIntInput("Select an option: ");
                if (equipAction == 1) {
                    hero.getInventory().equipItem();
                } else if (equipAction == 2) {
                    hero.getInventory().unequipItem();
                }
                break;
            case 2:
                // Find potions in inventory
                List<Item> potions = new ArrayList<>();
                for (Item item : hero.getInventory().getItems()) {
                    if (item instanceof Potion) {
                        potions.add(item);
                    }
                }
                
                if (potions.isEmpty()) {
                    System.out.println("No potions in inventory.");
                } else {
                    System.out.println("\nAvailable Potions:");
                    for (int i = 0; i < potions.size(); i++) {
                        Potion potion = (Potion) potions.get(i);
                        System.out.println((i + 1) + ". " + potion.getName() + " (+" + potion.getAttributeIncrease() + " " + potion.getAttributeAffected() + ")");
                    }
                    
                    int potionChoice = InputHandler.getInstance().getIntInput("Select a potion to use (0 to cancel): ");
                    
                    if (potionChoice > 0 && potionChoice <= potions.size()) {
                        Potion selectedPotion = (Potion) potions.get(potionChoice - 1);
                        
                        // Apply potion effect
                        String attribute = selectedPotion.getAttributeAffected();
                        int increase = selectedPotion.getAttributeIncrease();
                        
                        if (attribute.equalsIgnoreCase("Health")) {
                            hero.increaseHealth(increase);
                        } else if (attribute.equalsIgnoreCase("Mana")) {
                            hero.increaseMana(increase);
                        } else if (attribute.equalsIgnoreCase("Strength")) {
                            hero.increaseStrength(increase);
                        } else if (attribute.equalsIgnoreCase("Dexterity")) {
                            hero.increaseDexterity(increase);
                        } else if (attribute.equalsIgnoreCase("Agility")) {
                            hero.increaseAgility(increase);
                        }
                        
                        // Remove the used potion from inventory
                        hero.getInventory().getItems().remove(selectedPotion);
                        System.out.println(hero.getName() + " used " + selectedPotion.getName() + "!");
                    }
                }
                break;
        }
    }
    
    /**
     * Displays the status of all heroes
     */
    private void displayHeroesStatus() {
        System.out.println("\n=== PARTY STATUS ===");
        
        for (Hero hero : heroes) {
            // Calculate health and mana percentages
            int healthPercent = (int) ((double) hero.getCurrentHealth() / hero.getHealthPoints() * 100);
            int manaPercent = (int) ((double) hero.getCurrentMana() / hero.getManaPoints() * 100);
            
            // Set colors based on percentages
            String healthColor = healthPercent > 70 ? "\u001B[32m" : // Green
                                 healthPercent > 30 ? "\u001B[33m" : // Yellow
                                 "\u001B[31m";                       // Red
            
            String manaColor = manaPercent > 70 ? "\u001B[34m" : // Blue
                               manaPercent > 30 ? "\u001B[36m" : // Cyan
                               "\u001B[35m";                     // Purple
            
            // Display hero name and level
            System.out.print("\u001B[1m" + hero.getName() + " (Lvl " + hero.getLevel() + ")\u001B[0m - ");
            
            // Status indicator
            if (hero.isAlive()) {
                System.out.print("[ACTIVE] ");
            } else {
                System.out.print("[\u001B[31mDEAD\u001B[0m] ");
            }
            
            // HP and MP bars
            System.out.print("HP: " + healthColor + hero.getCurrentHealth() + "/" + hero.getHealthPoints() + "\u001B[0m | ");
            System.out.print("MP: " + manaColor + hero.getCurrentMana() + "/" + hero.getManaPoints() + "\u001B[0m | ");
            
            // Gold
            System.out.println("Gold: " + hero.getGold());
        }
        
        System.out.println("=====================");
    }
} 