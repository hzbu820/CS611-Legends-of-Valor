/**
 * Game class - the main engine that runs everything.
 * 
 * This is the brain of our RPG that handles all the major game logic.
 * It creates the world, manages heroes and monsters, tracks player
 * progress, and determines when you win or lose. Think of it as
 * the dungeon master running the whole show.
 */

import java.util.*;

public class Game {
    private List<Hero> heroes;
    private List<Monster> monsters;
    private lovWorld world;
    private Market market;
    private boolean isGameOver;
    private int difficulty; // 1 = Easy, 2 = Medium, 3 = Hard
    
    // New component references
    private GameSetup gameSetup;
    private TurnManager turnManager;
    private GameUI gameUI;

    public Game() {
        this.gameUI = new GameUI();
        this.gameSetup = new GameSetup();
        
        // Initialize game components
        this.heroes = new ArrayList<>();
        this.monsters = new ArrayList<>();
        this.market = new Market();
        this.isGameOver = false;
        
        // Set up the game
        this.difficulty = gameSetup.selectDifficulty();
        this.heroes = gameSetup.setupHeroes();
        this.monsters = gameSetup.spawnMonsters(1); // Start with level 1 monsters
        
        // Initialize the world
        this.world = new lovWorld(8, 8);
        world.initializeHeroesAndMonsters(heroes, monsters);
        
        // Initialize the turn manager
        this.turnManager = new TurnManager(heroes, monsters, world, difficulty);
    }

    public void start() {
        // Display welcome message and controls
        gameUI.displayWelcomeMessage();
        gameUI.displayMainControls();

        // Main game loop
        while (!isGameOver) {
            world.updateBoard(heroes, monsters);
            world.displayMap();
            gameUI.displayHeroesStatus(heroes);
            
            // Execute a game round
            boolean roundEnded = turnManager.executeRound();
            if (roundEnded) {
                isGameOver = true;
                continue; // Skip to the end of the loop
            }
            
            // We no longer need to ask for another command here since the turn system already handled
            // hero movement during executeRound(). Adding another input request was causing the double-input issue.
        }
        
        System.out.println("Game Over. Thank you for playing!");
    }
    
    /**
     * Handles player commands outside of battle turns.
     * @param command What the player wants to do
     */
    private void processCommand(String command) {
        switch (command) {
            case "w":
            case "a":
            case "s":
            case "d":
                if (attemptMove(command)) {
                    // Check if the hero is in a special space
                    if (world.isInMarketSpace()) {
                        System.out.println("You see a marketplace. Press M to enter it and enter anything else to quit.");
                        command = InputHandler.getInstance().getCommand().toLowerCase();
                        if (command.equals("m")) {
                            enterMarket();
                        } else {
                            System.out.println("You left the marketplace.");
                        }
                    } else if (world.isInCommonSpace()) {
                        boolean chanceOfBattle = Math.random() < 0.5; // 50% chance for battle
                        if (chanceOfBattle) {
                            gameUI.displayInfoMessage("You encountered monsters!");
                            explore();
                        } else {
                            System.out.println("Your teams spot no sign of monsters in this area...");
                        }
                    }
                }
                break;
                
            case "q":
                isGameOver = true;
                System.out.println("Thank you for playing!");
                break;
                
            case "i":
                showHeroStats();
                break;
                
            default:
                System.out.println("Invalid command. Use W/A/S/D for movement, I for info, M for market, and Q to quit.");
                break;
        }
    }
    
    /**
     * Tries to move heroes around the map.
     * @param direction Which way to go (w/a/s/d)
     * @return true if we could move, false if we hit a wall
     */
    private boolean attemptMove(String direction) {
        boolean moveSuccessful = false;
        
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                moveSuccessful = world.moveHero(hero, direction) || moveSuccessful;
            }
        }
        
        if (!moveSuccessful) {
            gameUI.displayErrorMessage("Movement failed! Ensure your heroes can move in that direction.");
            return false;
        }
        
        world.updateBoard(heroes, monsters);
        
        // Check if heroes reached monster nexus (win condition)
        if (world.checkWinCondition(heroes, monsters)) {
            isGameOver = true;
            gameUI.displayGameOverMessage(true);
            return true;
        }
        
        return true;
    }
    
    /**
     * Handles running into monsters while exploring.
     */
    private void explore() {
        // This is now handled within the turn system
        // May be removed or extended for special exploration cases
    }
    
    /**
     * Opens the market so heroes can buy and sell stuff.
     */
    private void enterMarket() {
        System.out.println("\nWelcome to the Market! Select a hero to trade with:");
        
        for (int i = 0; i < heroes.size(); i++) {
            System.out.println((i + 1) + ". " + heroes.get(i).getName() + " (" + heroes.get(i).getHeroIdentifier() + ")");
        }
        
        int heroChoice = InputHandler.getInstance().getIntInput("Select a hero (1-" + heroes.size() + "): ");
        
        if (heroChoice > 0 && heroChoice <= heroes.size()) {
            Hero selectedHero = heroes.get(heroChoice - 1);
            market.enterMarket(selectedHero);
        } else {
            gameUI.displayErrorMessage("Invalid hero selection.");
        }
    }
    
    /**
     * Shows detailed info about your heroes.
     */
    private void showHeroStats() {
        System.out.println("\nSelect a hero to view detailed information:");
        
        for (int i = 0; i < heroes.size(); i++) {
            System.out.println((i + 1) + ". " + heroes.get(i).getName() + " (" + heroes.get(i).getHeroIdentifier() + ")");
        }
        
        int heroChoice = InputHandler.getInstance().getIntInput("Select a hero (1-" + heroes.size() + ", or 0 to cancel): ");
        
        if (heroChoice > 0 && heroChoice <= heroes.size()) {
            Hero selectedHero = heroes.get(heroChoice - 1);
            gameUI.displayHeroStats(selectedHero);
        } else if (heroChoice != 0) {
            gameUI.displayErrorMessage("Invalid hero selection.");
        }
    }

    protected void displayGameInstructions() {
        System.out.println("\n=== Legends of Valor Instructions ===");
        System.out.println("In this game, heroes must defeat monsters to protect the kingdom.");
        System.out.println("Move your heroes with 'w' (up), 'a' (left), 's' (down), 'd' (right).");
        System.out.println("Enter the market at nexus spaces to buy items.");
        System.out.println("Attack monsters when they are within range.");
        System.out.println("Win by reaching the monster's nexus or defeating all monsters.");
        System.out.println("Lose if a monster reaches your nexus.");
        System.out.println("\nMap Legend:");
        System.out.println("  HN - Hero Nexus");
        System.out.println("  MN - Monster Nexus");
        System.out.println("  H1, H2, H3 - Heroes");
        System.out.println("  M1, M2, M3 - Monsters");
        System.out.println("  ▒▒ - Inaccessible");
        System.out.println("  B - Bush (Dexterity boost)");
        System.out.println("  C - Cave (Agility boost)");
        System.out.println("  K - Koulou (Strength boost)");
        System.out.println("  ★ - Treasure Chest (Contains gold, experience, and possibly items!)");
        System.out.println("  TC - Opened Treasure Chest");
        System.out.println("\nSpecial Commands:");
        System.out.println("  i - Show inventory");
        System.out.println("  m - Show map");
        System.out.println("  q - Quit game");
        System.out.println("  t - Teleport to another hero");
        System.out.println("  r - Recall to nexus");
        System.out.println("  e - Show hero stats");
        System.out.println("==================================\n");
    }
}