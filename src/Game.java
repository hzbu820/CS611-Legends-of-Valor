/**
 * This class handles the overall game logic, responsible for initializing game components, managing
 * the game loop, and tracking the game state. It handles menu level player interactions, and oversee win/loss conditions.
 *
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
            
            // Get player command for the next round
            System.out.println("Use W/A/S/D for movement, I for info, M for market, and Q to quit.");
            String command = InputHandler.getInstance().getCommand().toLowerCase();
            
            // Process the command
            processCommand(command);
        }
        
        System.out.println("Game Over. Thank you for playing!");
    }
    
    /**
     * Processes a player command outside of the turn system.
     * @param command The command to process
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
     * Attempts to move the heroes on the map.
     * @param direction The direction to move
     * @return true if movement was successful, false otherwise
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
     * Handles encounters with monsters while exploring.
     */
    private void explore() {
        // This is now handled within the turn system
        // May be removed or extended for special exploration cases
    }
    
    /**
     * Enters the market for trading.
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
     * Displays detailed information about all heroes.
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
}