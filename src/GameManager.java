/**
 * Manages the game initialization and replay functionality.
 * Acts as a central controller for the game flow.
 */
public class GameManager {
    /**
     * Starts the game management process, handling replay
     */
    public void startGameSession() {
        boolean playAgain = true;
        
        System.out.println("Welcome to Legends of Valor!");
        System.out.println("A MOBA-inspired adventure awaits!\n");
        
        while (playAgain) {
            // Launch Legends of Valor directly
            playLegendsOfValor();
            
            // Ask if player wants to play again
            playAgain = askPlayAgain();
        }
        
        System.out.println("Thank you for playing! Goodbye!");
    }
    
    /**
     * Starts the Legends of Valor game
     */
    private void playLegendsOfValor() {
        // Create and start a new Legends of Valor game
        Game game = new Game();
        game.start();
    }
    
    /**
     * Asks the player if they want to play again
     * @return true if the player wants to play again, false otherwise
     */
    private boolean askPlayAgain() {
        System.out.print("\nWould you like to play again? (y/n): ");
        String response = InputHandler.getInstance().getCommand().toLowerCase().trim();
        
        // Make sure we got valid input
        while (response.isEmpty()) {
            System.out.print("Please enter 'y' or 'n': ");
            response = InputHandler.getInstance().getCommand().toLowerCase().trim();
        }
        
        return response.equals("y") || response.equals("yes");
    }
} 