import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Hero class - the playable characters in our game.
 * 
 * These are the good guys that you control. They have health, mana, and special
 * attributes that increase as they level up. Heroes can buy items, equip weapons
 * and armor, learn spells, and battle monsters.
 */

public class Hero extends Character implements Attackable<Monster> {
    protected String name;
    protected String heroClass;
    protected int experience;
    protected int gold;
    protected int level;
    private String heroIdentifier;
    // Base attributes
    protected int healthPoints;
    protected int manaPoints;
    protected int strength;
    protected int dexterity;
    protected int agility;

    // Current attributes
    protected int currentHealth;
    protected int currentMana;
    protected int currentStrength;
    protected int currentDexterity;
    protected int currentAgility;

    private Inventory inventory;
    private int heroRow;
    private int heroCol;
    private List<Spell> learnedSpells; // List of permanently learned spells

     /*
    Legends of Valor updates
     */
    private final int[] nexus = new int[2];

    public Hero(String name, String heroClass, int manaPoints, int strength, int agility, int dexterity, int gold, int experience) {
        super(name, 1);
        this.level = 1;
        this.heroClass = heroClass;
        this.healthPoints = 100;
        this.manaPoints = manaPoints;
        this.strength = strength;
        this.dexterity = dexterity;
        this.agility = agility;
        this.gold = gold;
        this.experience = experience;
        this.inventory = new Inventory();
        this.learnedSpells = new ArrayList<>();

        // Set current attributes equal to base attributes initially
        resetCurrentAttributes();
    }

    public Hero(String name, String heroClass) {
        super(name, 1);
        this.level = 1;
        this.gold = 2000;
        this.heroClass = heroClass;
        this.experience = 0;
        this.inventory = new Inventory();

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
        resetCurrentAttributes();
    }

    // Resets current attributes to base values
    public void resetCurrentAttributes() {
        this.currentHealth = healthPoints;
        this.currentMana = manaPoints;
        this.currentStrength = strength;
        this.currentDexterity = dexterity;
        this.currentAgility = agility;
    }

    // Level-up method
    public void levelUp() {

        resetCurrentAttributes(); // Restore to full health and mana on level up
        if(level == 10){ //Max level is 10
            return;
        }
        level++;
        System.out.println(this.getName() + " leveled up to " + level + "!");
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

    public void gainExperience(int xp) {
        experience += xp;
        if (experience >= level * 10) {
            levelUp();
        }
    }

    public void reduceMana(int manaCost) {
        currentMana = Math.max(currentMana - manaCost, 0);
    }
    @Override
    public void takeDamage(int damage) {
        currentHealth = Math.max(currentHealth - damage, 0);
    }


    @Override
    public boolean isAlive(){
        return currentHealth > 0;
    }

    public void revive() {
        currentHealth = (int) Math.ceil(healthPoints * 0.5);
        currentMana = (int) Math.ceil(manaPoints * 0.5);
    }

    // restore some health and mana each turn
    public void restorePerTurn(){
        increaseHealth((int) (healthPoints*0.1));
        increaseMana((int) (manaPoints*0.1));
    }


    // Increase attribute methods with checks to not exceed base values
    public void increaseHealth(int increase) {
        if (currentHealth != healthPoints) {
            System.out.println(this.getName() + " heals " + increase + " HP");
            currentHealth = Math.min(currentHealth + increase, healthPoints);
        }
    }

    public void increaseMana(int increase) {
        if (currentMana != manaPoints) {
            System.out.println(this.getName() + " restores " + increase + " MP");
            currentMana = Math.min(currentMana + increase, manaPoints);
        }
    }

    public void increaseStrength(int increase) {
        currentStrength += increase;
        System.out.println(this.getName() + " gains " + increase + " strength");
    }

    public void increaseDexterity(int increase) {
        currentDexterity += increase;
        System.out.println(this.getName() + " gains " + increase + " dexterity");
    }

    public void increaseAgility(int increase) {
        currentAgility += increase;
        System.out.println(this.getName() + " gains " + increase + " agility");
    }

    // Resets temporary attribute boosts to base values after battle
    public void resetTemporaryAttributes() {
        currentStrength = strength;
        currentDexterity = dexterity;
        currentAgility = agility;
    }

    // Getters for base and current attributes
    public int getHealthPoints() { return healthPoints; }
    public int getManaPoints() { return manaPoints; }
    public int getStrength() { return strength; }
    public int getDexterity() { return dexterity; }
    public int getAgility() { return agility; }

    public int getCurrentHealth() { return currentHealth;  }
    public int getCurrentMana() { return currentMana; }
    public int getCurrentStrength() { return currentStrength; }
    public int getCurrentDexterity() { return currentDexterity; }
    public int getCurrentAgility() { return currentAgility; }


    public Inventory getInventory() {
        return inventory;
    }

    public int getGold() { return gold; }

    public void addGold(int amount) { gold += amount; }

    public void deductGold(int amount) {
        if (amount <= gold) {
            gold -= amount;
        }
    }


    /**
     * Attacks a target monster based on the hero's equipped weapons.
     * @param monster The monster to attack
     */
    public void attack(Monster monster) {
        if (monster == null || !monster.isAlive()) {
            System.out.println("Target is not valid.");
            return;
        }
        
        Random random = new Random();
        double dodgeChance = monster.getDodgeChance();
        
        // Check if monster dodges
        if (random.nextDouble() < dodgeChance) {
            // Use CombatLogger for the dodge message
            CombatLogger.getInstance().logHeroAttack(this, monster, 0, true);
            return;
        }
        
        // Calculate base damage based on hero's strength and weapons
        int weaponDamage = inventory.useWeapon();
        double damageMultiplier = 0.05; // 5% of strength per point
        int baseDamage = (int)(getCurrentStrength() * damageMultiplier * (weaponDamage > 0 ? weaponDamage : 1));
        
        // Check for critical hit (15% chance)
        boolean isCritical = random.nextDouble() < 0.15;
        int finalDamage = baseDamage;
        if (isCritical) {
            // Double damage on critical hit
            finalDamage *= 2;
        }
        
        // Apply damage to monster
        monster.takeDamage(finalDamage);
        
        // Use appropriate CombatLogger method based on whether it was a critical hit
        if (isCritical) {
            // Critical hit - use regular logging for now
            CombatLogger.getInstance().logHeroAttack(this, monster, finalDamage, false);
            System.out.println("CRITICAL HIT! " + this.getName() + " deals double damage!");
        } else {
            CombatLogger.getInstance().logHeroAttack(this, monster, finalDamage, false);
        }
    }


    /*
    Legends of Valor updates
     */

    public void setNexus(int nexusRow, int nexusCol) {
        nexus[0] = nexusRow;
        nexus[1] = nexusCol;
    }

    public int[] getNexus(){
        return nexus;
    }

    public int getHeroRow() { return heroRow; }
    public int getHeroCol() { return heroCol; }

    public void setHeroRow(int row) {
        heroRow = row;
    }

    public void setHeroCol(int col) {
        heroCol = col;
    }

    @Override
    public String toString() {
        return super.toString() + ", HP=" + currentHealth + "/" + healthPoints + ", MP=" + currentMana + "/" + manaPoints +
                ", STR=" + currentStrength + ", DEX=" + currentDexterity + ", AGI=" + currentAgility;
    }

    public void printExperienceProgress() {
        int experienceToNextLevel = ((level) * 10) - this.experience ;
        int nextLevel = level + 1;
        System.out.println(this.getName() + " is " + experienceToNextLevel + " experiences points away to level " + nextLevel + "." );
    }

    public int calculateDamage() {
        // Base damage from the hero's current strength
        int baseDamage = this.currentStrength;

        // Use weapon damage from the inventory
        int weaponDamage = this.inventory.useWeapon(); // Uses the `useWeapon()` method to determine total weapon damage

        // Add weapon damage to the base damage
        baseDamage += weaponDamage;

        return baseDamage;
    }




    public void setHeroIdentifier(String identifier) {
        this.heroIdentifier = identifier;
    }

    public String getHeroIdentifier() {
        return heroIdentifier;
    }

    @Override
    public String getSymbol() {
        return heroIdentifier; // This will return H1, H2, H3 depending on the hero
    }

    public void learnSpell(Spell spell) {
        if (spell == null) {
            return;
        }
        
        // Check if the spell is already learned
        for (Spell learnedSpell : learnedSpells) {
            if (learnedSpell.getName().equals(spell.getName())) {
                System.out.println(this.getName() + " already knows " + spell.getName() + ".");
                return;
            }
        }
        
        // Learn the new spell
        learnedSpells.add(spell);
        System.out.println(this.getName() + " has learned the spell " + spell.getName() + "!");
    }
    
    public List<Spell> getLearnedSpells() {
        return learnedSpells;
    }
    
    public boolean hasLearnedSpells() {
        return !learnedSpells.isEmpty();
    }
    
    public void displayLearnedSpells() {
        if (learnedSpells.isEmpty()) {
            System.out.println(this.getName() + " hasn't learned any spells yet.");
            return;
        }
        
        System.out.println("\n\u001B[35m*** " + this.getName() + "'s Learned Spells ***\u001B[0m");
        for (int i = 0; i < learnedSpells.size(); i++) {
            Spell spell = learnedSpells.get(i);
            System.out.println((i+1) + ". " + spell.getName() + 
                " (Type: " + spell.getSpellType() + 
                ", Damage: " + spell.getDamage() + 
                ", Mana Cost: " + spell.getManaCost() + ")");
        }
    }
    
    public void castLearnedSpell(Spell spell, Monster target) {
        if (currentMana < spell.getManaCost()) {
            System.out.println("Not enough mana to cast " + spell.getName() + ".");
            return;
        }
        
        if (target == null || !target.isAlive()) {
            System.out.println("Invalid target for spell.");
            return;
        }
        
        // Reduce mana
        reduceMana(spell.getManaCost());
        
        // Calculate damage
        int damage = spell.getDamage();
        
        // Apply damage to target
        target.takeDamage(damage);
        System.out.println(this.getName() + " cast " + spell.getName() + " on " + target.getName() + " for " + damage + " damage!");
        
        // Apply spell effects based on type
        if (spell.getSpellType() != null) {
            target.applySpellEffect(spell.getSpellType());
        }
    }

    public String getHeroClass() {
        return heroClass;
    }

}