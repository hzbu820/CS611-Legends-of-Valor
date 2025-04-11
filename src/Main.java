/**
 * Main class - this is where the adventure begins!
 * 
 * Just a simple starter class that kicks off the game.
 * Think of it as the "once upon a time" part of our story.
 */
public class Main {
    public static void main(String[] args) {
        // Start Legends of Valor directly
        System.out.println("Welcome to Legends of Valor!");
        System.out.println("A MOBA-inspired adventure awaits!\n");
        
        // Initialize the data directory by accessing it once
        String dataDir = FilePathHelper.getDataDirectory();
        System.out.println("Using data directory: " + dataDir);
        
        // Create and start a new Legends of Valor game directly
        Game game = new Game();
        game.start();
        
        System.out.println("Thank you for playing! Goodbye!");
    }
}