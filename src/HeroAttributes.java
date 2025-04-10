/**
 * Manages hero-specific attributes and stat manipulation.
 * This class handles base and current attributes, attribute increases/decreases,
 * and applying terrain bonuses.
 */
public class HeroAttributes {
    // Base attributes
    private int healthPoints;
    private int manaPoints;
    private int strength;
    private int dexterity;
    private int agility;

    // Current attributes (may be affected by spells, items, etc.)
    private int currentHealth;
    private int currentMana;
    private int currentStrength;
    private int currentDexterity;
    private int currentAgility;

    // Hero information
    private final String heroClass;
    private final String heroName;

    /**
     * Constructor for initializing attributes based on provided values.
     */
    public HeroAttributes(String heroName, String heroClass, int healthPoints, int manaPoints, 
                         int strength, int dexterity, int agility) {
        this.heroName = heroName;
        this.heroClass = heroClass;
        this.healthPoints = healthPoints;
        this.manaPoints = manaPoints;
        this.strength = strength;
        this.dexterity = dexterity;
        this.agility = agility;
        
        // Initialize current attributes to base values
        resetCurrentAttributes();
    }

    /**
     * Constructor for initializing attributes based on hero class.
     */
    public HeroAttributes(String heroName, String heroClass) {
        this.heroName = heroName;
        this.heroClass = heroClass;
        
        // Set default attributes based on hero type
        switch (heroClass.toLowerCase()) {
            case "warrior":
                this.healthPoints = 100;
                this.manaPoints = 500;
                this.strength = 750;
                this.dexterity = 500;
                this.agility = 750;
                break;
            case "sorcerer":
                this.healthPoints = 100;
                this.manaPoints = 700;
                this.strength = 500;
                this.dexterity = 750;
                this.agility = 750;
                break;
            case "paladin":
                this.healthPoints = 100;
                this.manaPoints = 500;
                this.strength = 750;
                this.dexterity = 750;
                this.agility = 500;
                break;
            default:
                throw new IllegalArgumentException("Invalid hero type: " + heroClass);
        }
        
        // Initialize current attributes to base values
        resetCurrentAttributes();
    }

    /**
     * Resets current attributes to base values
     */
    public void resetCurrentAttributes() {
        this.currentHealth = healthPoints;
        this.currentMana = manaPoints;
        this.currentStrength = strength;
        this.currentDexterity = dexterity;
        this.currentAgility = agility;
    }

    /**
     * Increases hero's attributes based on level-up and hero class
     */
    public void applyLevelUpBonus(int level) {
        healthPoints += 100;
        manaPoints = (int) Math.ceil(manaPoints * 1.1);

        switch (heroClass.toLowerCase()) {
            case "warrior":
                strength += (int) Math.ceil(strength * 0.1);
                dexterity += (int) Math.ceil(dexterity * 0.05);
                agility += (int) Math.ceil(agility * 0.1);
                break;
            case "sorcerer":
                strength += (int) Math.ceil(strength * 0.05);
                dexterity += (int) Math.ceil(dexterity * 0.1);
                agility += (int) Math.ceil(agility * 0.1);
                break;
            case "paladin":
                strength += (int) Math.ceil(strength * 0.1);
                dexterity += (int) Math.ceil(dexterity * 0.1);
                agility += (int) Math.ceil(agility * 0.05);
                break;
        }
        resetCurrentAttributes();
    }

    /**
     * Applies damage to the hero
     */
    public void takeDamage(int damage) {
        currentHealth = Math.max(currentHealth - damage, 0);
    }

    /**
     * Checks if the hero is alive
     */
    public boolean isAlive() {
        return currentHealth > 0;
    }

    /**
     * Revives the hero with 50% health and mana
     */
    public void revive() {
        currentHealth = (int) Math.ceil(healthPoints * 0.5);
        currentMana = (int) Math.ceil(manaPoints * 0.5);
    }

    /**
     * Restores health and mana per turn (10% of max)
     */
    public void restorePerTurn() {
        increaseHealth((int) (healthPoints * 0.1));
        increaseMana((int) (manaPoints * 0.1));
    }

    // Attribute modification methods

    /**
     * Increases current health, capped at max health
     */
    public void increaseHealth(int increase) {
        if (currentHealth < healthPoints) {
            System.out.println(heroName + " heals " + increase + " HP");
            currentHealth = Math.min(currentHealth + increase, healthPoints);
        }
    }

    /**
     * Increases current mana, capped at max mana
     */
    public void increaseMana(int increase) {
        if (currentMana < manaPoints) {
            System.out.println(heroName + " restores " + increase + " MP");
            currentMana = Math.min(currentMana + increase, manaPoints);
        }
    }

    /**
     * Reduces current mana by the specified amount
     */
    public void reduceMana(int manaCost) {
        currentMana = Math.max(currentMana - manaCost, 0);
    }

    /**
     * Increases current strength
     */
    public void increaseStrength(int increase) {
        currentStrength += increase;
        System.out.println(heroName + " gains " + increase + " strength");
    }

    /**
     * Increases current dexterity
     */
    public void increaseDexterity(int increase) {
        currentDexterity += increase;
        System.out.println(heroName + " gains " + increase + " dexterity");
    }

    /**
     * Increases current agility
     */
    public void increaseAgility(int increase) {
        currentAgility += increase;
        System.out.println(heroName + " gains " + increase + " agility");
    }

    // Getters
    public int getHealthPoints() { return healthPoints; }
    public int getManaPoints() { return manaPoints; }
    public int getStrength() { return strength; }
    public int getDexterity() { return dexterity; }
    public int getAgility() { return agility; }

    public int getCurrentHealth() { return currentHealth; }
    public int getCurrentMana() { return currentMana; }
    public int getCurrentStrength() { return currentStrength; }
    public int getCurrentDexterity() { return currentDexterity; }
    public int getCurrentAgility() { return currentAgility; }
    
    public String getHeroClass() { return heroClass; }
} 