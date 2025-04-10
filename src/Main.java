/**
 * The entry point of the application, contains the main method to start the game.
 */
public class Main {
    public static void main(String[] args) {
        // Create a game manager and start the game session
        GameManager gameManager = new GameManager();
        gameManager.startGameSession();
    }
}