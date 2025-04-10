import java.util.ArrayList;
import java.util.List;

/**
 * This class handles game initialization, hero selection, 
 * and difficulty settings.
 */
public class GameSetup {
    private InputHandler inputHandler;
    
    public GameSetup() {
        this.inputHandler = InputHandler.getInstance();
    }
    
    /**
     * Allows the player to select a game difficulty level.
     * @return The selected difficulty level (1 = Easy, 2 = Medium, 3 = Hard)
     */
    public int selectDifficulty() {
        System.out.println("Select game difficulty:");
        System.out.println("1. Easy (New monsters spawn every 6 rounds)");
        System.out.println("2. Medium (New monsters spawn every 4 rounds)");
        System.out.println("3. Hard (New monsters spawn every 2 rounds)");
        
        int choice = inputHandler.getIntInput("Enter your choice (1-3): ");
        while (choice < 1 || choice > 3) {
            System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            choice = inputHandler.getIntInput("Enter your choice (1-3): ");
        }
        
        System.out.println("Difficulty set to " + 
            (choice == 1 ? "Easy" : choice == 2 ? "Medium" : "Hard"));
        return choice;
    }
    
    /**
     * Sets up heroes for the game by allowing the player to select them.
     * @return A list of selected heroes
     */
    public List<Hero> setupHeroes() {
        List<Hero> heroes = new ArrayList<>();
        System.out.println("Let's select your three heroes!");
        
        // Create heroes for each lane
        for (int i = 0; i < 3; i++) {
            System.out.println("\nSelecting Hero " + (i + 1) + " (for lane " + (i + 1) + "):");
            Hero hero = selectHero();
            heroes.add(hero);
        }
        
        return heroes;
    }
    
    /**
     * Allows the player to select a hero from available classes.
     * @return The selected hero
     */
    private Hero selectHero() {
        System.out.println("Choose a hero class:");
        System.out.println("1. Warrior (High Strength and Agility)");
        System.out.println("2. Sorcerer (High Mana and Dexterity)");
        System.out.println("3. Paladin (High Strength and Dexterity)");
        
        int classChoice = inputHandler.getIntInput("Enter your choice (1-3): ");
        while (classChoice < 1 || classChoice > 3) {
            System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            classChoice = inputHandler.getIntInput("Enter your choice (1-3): ");
        }
        
        String heroClass = "";
        switch (classChoice) {
            case 1:
                heroClass = "Warrior";
                break;
            case 2:
                heroClass = "Sorcerer";
                break;
            case 3:
                heroClass = "Paladin";
                break;
        }
        
        // Use the static method from HeroFactory
        return HeroFactory.createHero(heroClass);
    }
    
    /**
     * Creates initial monsters for the game.
     * @param heroLevel The level to base monster levels on
     * @return A list of monsters
     */
    public List<Monster> spawnMonsters(int heroLevel) {
        List<Monster> monsters = new ArrayList<>();
        MonsterFactory monsterFactory = new MonsterFactory();
        
        // Create up to three monsters, one for each lane
        int maxInitialMonsters = 3; // This matches the grid design (3 lanes)
        for (int i = 0; i < maxInitialMonsters; i++) {
            Monster monster = monsterFactory.createMonster(heroLevel);
            if (monster != null) {
                monsters.add(monster);
            }
        }
        
        return monsters;
    }
    
    /**
     * Creates new monsters to spawn during gameplay.
     * @param heroLevel The highest level among heroes
     * @return A list of new monsters
     */
    public List<Monster> spawnNewMonsters(int heroLevel) {
        // Limit to only one new monster per spawn event to avoid overcrowding
        List<Monster> newMonsters = new ArrayList<>();
        MonsterFactory monsterFactory = new MonsterFactory();
        
        // Just spawn one monster to avoid grid overflow
        Monster monster = monsterFactory.createMonster(heroLevel);
        if (monster != null) {
            newMonsters.add(monster);
        }
        
        return newMonsters;
    }
} 