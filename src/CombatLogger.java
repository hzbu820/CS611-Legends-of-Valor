import java.util.ArrayList;
import java.util.List;

/**
 * Handles combat logging with improved formatting and color coding.
 * Maintains a history of recent combat events for reference.
 */
public class CombatLogger {
    private static final int MAX_LOG_SIZE = 10;
    private List<String> combatHistory;
    private static CombatLogger instance;

    // Color constants
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    /**
     * Private constructor for singleton pattern
     */
    private CombatLogger() {
        this.combatHistory = new ArrayList<>();
    }

    /**
     * Gets the singleton instance of CombatLogger
     * @return The CombatLogger instance
     */
    public static CombatLogger getInstance() {
        if (instance == null) {
            instance = new CombatLogger();
        }
        return instance;
    }

    /**
     * Logs a hero attack on a monster with detailed information
     * @param hero The attacking hero
     * @param monster The monster being attacked
     * @param damage The damage dealt
     * @param wasDodged Whether the attack was dodged
     */
    public void logHeroAttack(Hero hero, Monster monster, int damage, boolean wasDodged) {
        String message;
        if (wasDodged) {
            message = BOLD + GREEN + hero.getName() + RESET + " attacks " + 
                      BOLD + RED + monster.getName() + RESET + " but " + 
                      YELLOW + "the attack was dodged!" + RESET;
        } else {
            message = BOLD + GREEN + hero.getName() + RESET + " attacks " + 
                      BOLD + RED + monster.getName() + RESET + " for " + 
                      BOLD + RED + damage + RESET + " damage!";
            
            // Add monster health update
            if (monster.isAlive()) {
                message += " " + RED + monster.getName() + " HP: " + monster.getCurrentHealth() + "/" + monster.getHealth() + RESET;
            } else {
                message += " " + RED + BOLD + monster.getName() + " has been defeated!" + RESET;
            }
        }
        
        logAndPrint(message);
    }

    /**
     * Logs a monster attack on a hero with detailed information
     * @param monster The attacking monster
     * @param hero The hero being attacked
     * @param damage The damage dealt
     * @param wasDodged Whether the attack was dodged
     */
    public void logMonsterAttack(Monster monster, Hero hero, int damage, boolean wasDodged) {
        String message;
        if (wasDodged) {
            message = BOLD + RED + monster.getName() + RESET + " attacks " + 
                      BOLD + GREEN + hero.getName() + RESET + " but " + 
                      YELLOW + "the attack was dodged!" + RESET;
        } else {
            message = BOLD + RED + monster.getName() + RESET + " attacks " + 
                      BOLD + GREEN + hero.getName() + RESET + " for " + 
                      BOLD + RED + damage + RESET + " damage!";
            
            // Add hero health update
            if (hero.isAlive()) {
                message += " " + GREEN + hero.getName() + " HP: " + hero.getCurrentHealth() + "/" + hero.getHealthPoints() + RESET;
            } else {
                message += " " + GREEN + BOLD + hero.getName() + " has been defeated!" + RESET;
            }
        }
        
        logAndPrint(message);
    }

    /**
     * Logs a spell cast with detailed information
     * @param hero The hero casting the spell
     * @param spell The spell being cast
     * @param monster The monster target
     * @param damage The damage dealt
     */
    public void logSpellCast(Hero hero, Spell spell, Monster monster, int damage) {
        String spellColor;
        
        // Color based on spell type
        switch(spell.getSpellType().toLowerCase()) {
            case "ice": spellColor = CYAN; break;
            case "fire": spellColor = RED; break;
            case "lightning": spellColor = YELLOW; break;
            default: spellColor = PURPLE; break;
        }
        
        String message = BOLD + GREEN + hero.getName() + RESET + " casts " + 
                         BOLD + spellColor + spell.getName() + RESET + " on " + 
                         BOLD + RED + monster.getName() + RESET + " for " + 
                         BOLD + spellColor + damage + RESET + " damage!";
        
        // Add monster health update
        if (monster.isAlive()) {
            message += " " + RED + monster.getName() + " HP: " + monster.getCurrentHealth() + "/" + monster.getHealth() + RESET;
        } else {
            message += " " + RED + BOLD + monster.getName() + " has been defeated!" + RESET;
        }
        
        // Add spell effect information
        message += "\n  " + PURPLE + "→ Effect: Reduced " + spell.getSpellType() + " attributes" + RESET;
        
        logAndPrint(message);
    }
    
    /**
     * Logs a potion use with detailed information
     * @param hero The hero using the potion
     * @param potion The potion being used
     */
    public void logPotionUse(Hero hero, Potion potion) {
        String message = BOLD + GREEN + hero.getName() + RESET + " uses " + 
                         BOLD + BLUE + potion.getName() + RESET + " and gains " +
                         BOLD + CYAN + "+" + potion.getAttributeIncrease() + " " + potion.getAttributeAffected() + RESET;
        
        logAndPrint(message);
    }
    
    /**
     * Logs monster respawn information
     * @param monsters The list of new monsters
     */
    public void logMonsterSpawn(List<Monster> monsters) {
        if (monsters == null || monsters.isEmpty()) return;
        
        String message = RED + BOLD + monsters.size() + " new monsters have appeared!" + RESET + "\n";
        
        for (Monster monster : monsters) {
            message += "  " + RED + "• " + monster.getName() + " (Level " + monster.getLevel() + ")" + RESET + "\n";
        }
        
        logAndPrint(message);
    }
    
    /**
     * Logs hero respawn information
     * @param hero The hero that respawned
     */
    public void logHeroRespawn(Hero hero) {
        String message = GREEN + BOLD + hero.getName() + " has respawned at the Nexus with full health and mana!" + RESET;
        logAndPrint(message);
    }
    
    /**
     * Logs terrain effect application
     * @param hero The hero affected
     * @param effect The terrain effect description
     */
    public void logTerrainEffect(Hero hero, String terrainType, String effect) {
        String terrainColor;
        switch(terrainType.toLowerCase()) {
            case "bush": terrainColor = GREEN; break;
            case "cave": terrainColor = BLUE; break;
            case "koulou": terrainColor = YELLOW; break;
            default: terrainColor = RESET;
        }
        
        String message = BOLD + GREEN + hero.getName() + RESET + " gains " + 
                         terrainColor + BOLD + effect + RESET + " from " + 
                         terrainColor + terrainType + RESET + " terrain!";
        
        logAndPrint(message);
    }
    
    /**
     * Logs a critical hit from a hero to a monster with dramatic formatting
     * @param hero The hero that landed the critical hit
     * @param monster The monster that was hit
     * @param damage The damage dealt
     */
    public void logCriticalHit(Hero hero, Monster monster, int damage) {
        String message = BOLD + GREEN + "CRITICAL HIT! " + hero.getName() + RESET + " strikes " + 
                         BOLD + RED + monster.getName() + RESET + " for " + 
                         BOLD + RED + damage + RESET + " damage!";
        
        // Add monster health update
        if (monster.isAlive()) {
            message += " " + RED + monster.getName() + " HP: " + monster.getCurrentHealth() + "/" + monster.getHealth() + RESET;
        } else {
            message += " " + RED + BOLD + monster.getName() + " has been defeated!" + RESET;
        }
        
        logAndPrint(message);
    }
    
    /**
     * Logs a critical hit from a monster to a hero with dramatic formatting
     * @param monster The monster that landed the critical hit
     * @param hero The hero that was hit
     * @param damage The damage dealt
     */
    public void logMonsterCriticalHit(Monster monster, Hero hero, int damage) {
        String message = BOLD + RED + "CRITICAL HIT! " + monster.getName() + RESET + " strikes " + 
                         BOLD + GREEN + hero.getName() + RESET + " for " + 
                         BOLD + RED + damage + RESET + " damage!";
        
        // Add hero health update
        if (hero.isAlive()) {
            message += " " + GREEN + hero.getName() + " HP: " + hero.getCurrentHealth() + "/" + hero.getHealthPoints() + RESET;
        } else {
            message += " " + GREEN + BOLD + hero.getName() + " has fainted!" + RESET;
        }
        
        logAndPrint(message);
    }
    
    /**
     * Logs and prints a message, adding it to combat history
     * @param message The message to log
     */
    private void logAndPrint(String message) {
        // Add to history
        combatHistory.add(message);
        
        // Trim history if it gets too long
        if (combatHistory.size() > MAX_LOG_SIZE) {
            combatHistory.remove(0);
        }
        
        // Print the message
        System.out.println(message);
    }
    
    /**
     * Displays the combat history
     */
    public void displayCombatHistory() {
        System.out.println("\n===== RECENT COMBAT LOG =====");
        if (combatHistory.isEmpty()) {
            System.out.println("No combat has occurred yet.");
        } else {
            for (int i = 0; i < combatHistory.size(); i++) {
                System.out.println((i+1) + ". " + combatHistory.get(i));
            }
        }
        System.out.println("=============================\n");
    }
    
    /**
     * Clears the combat history
     */
    public void clearHistory() {
        combatHistory.clear();
    }
} 