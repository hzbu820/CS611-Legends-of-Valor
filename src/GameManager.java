/**
 * Manages the game selection, initialization, and replay functionality.
 * Acts as a central controller for the game flow.
 */
public class GameManager {
    // Game modes constants
    public static final int MONSTERS_AND_HEROES = 1;
    public static final int LEGENDS_OF_VALOR = 2;
    
    /**
     * Starts the game management process, handling game selection and replay
     */
    public void startGameSession() {
        boolean playAgain = true;
        
        System.out.println("Welcome to the RPG Game Universe!");
        System.out.println("Experience incredible adventures in multiple game worlds!\n");
        
        while (playAgain) {
            // Display game selection menu
            int gameChoice = selectGame();
            
            switch (gameChoice) {
                case MONSTERS_AND_HEROES:
                    playMonstersAndHeroes();
                    break;
                case LEGENDS_OF_VALOR:
                    playLegendsOfValor();
                    break;
                default:
                    System.out.println("Invalid choice. Exiting...");
                    return;
            }
            
            // Ask if player wants to play again
            playAgain = askPlayAgain();
        }
        
        System.out.println("Thank you for playing! Goodbye!");
    }
    
    /**
     * Displays the game selection menu and gets user choice
     * @return The user's selected game mode
     */
    private int selectGame() {
        System.out.println("\n=== GAME SELECTION ===");
        System.out.println("1. Monsters and Heroes - Classic adventure mode");
        System.out.println("2. Legends of Valor - MOBA-inspired strategy game");
        System.out.println("======================\n");
        
        return InputHandler.getInstance().getIntInput("Select a game to play (1-2): ");
    }
    
    /**
     * Starts the original Monsters and Heroes game
     */
    private void playMonstersAndHeroes() {
        System.out.println("\nLaunching Monsters and Heroes...");
        System.out.println("Embark on a classic RPG adventure!");
        
        // Create and start a new Monsters and Heroes game
        MonstersAndHeroesGame game = new MonstersAndHeroesGame();
        game.start();
    }
    
    /**
     * Starts the Legends of Valor game
     */
    private void playLegendsOfValor() {
        System.out.println("\nLaunching Legends of Valor...");
        System.out.println("A MOBA-inspired adventure awaits!");
        
        // Create and start a new Legends of Valor game
        Game game = new Game();
        game.start();
    }
    
    /**
     * Asks the player if they want to play again
     * @return true if the player wants to play again, false otherwise
     */
    private boolean askPlayAgain() {
        System.out.print("\nWould you like to play another game? (y/n): ");
        String response = InputHandler.getInstance().getCommand().toLowerCase().trim();
        
        // Make sure we got valid input
        while (response.isEmpty()) {
            System.out.print("Please enter 'y' or 'n': ");
            response = InputHandler.getInstance().getCommand().toLowerCase().trim();
        }
        
        return response.equals("y") || response.equals("yes");
    }
} 