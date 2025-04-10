import java.util.Random;

/**
 * Handles all hero combat-related actions and calculations including attacking,
 * damage calculations, and special combat abilities.
 */
public class HeroCombat {
    private final Random random = new Random();
    private final Hero hero;
    
    /**
     * Constructor that initializes the combat system for a specific hero.
     * 
     * @param hero The hero this combat system belongs to
     */
    public HeroCombat(Hero hero) {
        this.hero = hero;
    }
    
    /**
     * Attacks a monster target with the hero's current weapon and attributes.
     * 
     * @param target The monster being attacked
     */
    public void attack(Monster target) {
        if (target == null) {
            System.out.println("No target to attack.");
            return;
        }

        if (!target.isAlive()) {
            System.out.println(target.getName() + " is already defeated and cannot be attacked.");
            return;
        }

        // Calculate damage
        double weaponDamage = hero.getInventory().useWeapon();
        double damage = (hero.getCurrentStrength() + weaponDamage) * 0.05;

        // Check if monster dodges
        double dodgeChance = target.getDodgeChance();
        if (random.nextDouble() < dodgeChance) {
            System.out.println(target.getName() + " dodged " + hero.getName() + "'s attack!");
            return;
        }

        // Calculate damage reduction from defense
        double finalDamage = Math.max(0, damage - target.getBaseDefense() * 0.05);
        int damageDealt = (int) Math.ceil(finalDamage);

        // Apply damage to monster
        target.takeDamage(damageDealt);
        System.out.println(hero.getName() + " hit " + target.getName() + " for " + damageDealt + " damage!");

        // Check if monster is defeated (this information will be used by the caller)
        if (!target.isAlive()) {
            System.out.println(target.getName() + " has been defeated!");
        }
    }
    
    /**
     * Calculates the total damage the hero can deal based on attributes and equipment.
     * 
     * @return The calculated damage value
     */
    public int calculateDamage() {
        // Base damage from the hero's current strength
        int baseDamage = hero.getCurrentStrength();

        // Use weapon damage from the inventory
        int weaponDamage = hero.getInventory().useWeapon();

        // Add weapon damage to the base damage
        baseDamage += weaponDamage;

        return baseDamage;
    }
    
    /**
     * Checks if the hero can dodge an attack based on agility.
     * 
     * @return true if the attack is dodged, false otherwise
     */
    public boolean tryDodgeAttack() {
        double dodgeChance = hero.getCurrentAgility() * 0.002; // 0.2% per agility point
        return random.nextDouble() < dodgeChance;
    }
} 