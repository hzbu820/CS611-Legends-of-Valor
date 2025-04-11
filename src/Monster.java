import java.util.Random;

/**
 * Monster class - the bad guys you're fighting against.
 * 
 * Monsters have damage, defense and dodge attributes that determine how tough they are.
 * Each monster type has different strengths and weaknesses, and they're affected 
 * differently by various spell types. They get stronger as their level increases.
 */

public class Monster extends Character implements Attackable<Hero> {
    private int baseDamage;
    private String monsterIdentifier;
    private int defense;
    private int dodgeChance;

    /*
    For Legends and Valor
     */
    private int MonsterRow;
    private int MonsterCol;

    public Monster(String name, int level, int damage, int defense, int dodgeChance) {
        super(name, level);
        this.baseDamage = damage;
        this.defense = defense;
        this.dodgeChance = dodgeChance;
    }

    public void setAttributes(String name, int level, int baseDamage, int defense, int dodgeChance) {
        this.name = name;
        this.level = level;
        this.baseDamage = baseDamage;
        this.defense = defense;
        this.dodgeChance = dodgeChance;
    }

    // Getters
    public int getHealth() { return healthPoints; }
    public int getCurrentHealth() { return healthPoints; }
    public int getBaseDamage() { return baseDamage; }
    public int getBaseDefense() { return defense; }
    public double getDodgeChance() { return dodgeChance * 0.01; }  // Convert dodge chance to percentage


    public void applySpellEffect(String spellType) {
        double reductionAmount;

        switch (spellType.toLowerCase()) {
            case "ice":
                reductionAmount = baseDamage * 0.1;
                baseDamage = Math.max(0, (int)(baseDamage - reductionAmount));
                System.out.println(name + "'s base damage reduced by " + (int)reductionAmount + ". New damage: " + baseDamage);
                break;

            case "fire":
                reductionAmount = defense * 0.1;
                defense = Math.max(0, (int)(defense - reductionAmount));
                System.out.println(name + "'s defense reduced by " + (int)reductionAmount + ". New defense: " + defense);
                break;

            case "lightning":
                reductionAmount = dodgeChance * 0.1;
                dodgeChance = Math.max(0, (int)(dodgeChance - reductionAmount));
                System.out.println(name + "'s dodge chance reduced by " + (int)reductionAmount + "%.");
                break;

            default:
                System.out.println("Unknown spell type: no effect applied.");
        }
    }


    public void attack(Hero target) {
        if (target == null) {
            System.out.println("Invalid target");
            return;
        }

        Random random = new Random();
        
        // Check if hero can dodge based on agility
        double dodgeChance = target.getCurrentAgility() * 0.002; // 0.2% per agility point
        
        // Check if the hero dodges
        if (random.nextDouble() < dodgeChance) {
            // Use CombatLogger for dodge message
            CombatLogger.getInstance().logMonsterAttack(this, target, 0, true);
            return;
        }
        
        // Calculate base damage: monster damage is based on its level and damage attribute
        double attackDamage = level * baseDamage * 0.1;
        
        // Check for critical hit (10% chance)
        boolean isCritical = random.nextDouble() < 0.10;
        double damageMultiplier = isCritical ? 2.0 : 1.0;
        
        // Calculate final damage with armor reduction
        double reducedDamage = (attackDamage * damageMultiplier) - target.getInventory().useArmor();
        int finalDamage = (int) Math.max(reducedDamage, 0);
        
        // Apply damage to hero
        target.takeDamage(finalDamage);
        
        // Use appropriate CombatLogger method based on whether it was a critical hit
        if (isCritical) {
            // Use dedicated critical hit logger
            CombatLogger.getInstance().logMonsterCriticalHit(this, target, finalDamage);
        } else {
            CombatLogger.getInstance().logMonsterAttack(this, target, finalDamage, false);
        }
    }
     /*
    For Legends and Valor
     */
    public int getMonsterRow() { return MonsterRow; }
    public int getMonsterCol() { return MonsterCol; }
    public void setMonsterRow(int monsterRow) { MonsterRow = monsterRow; }
    public void setMonsterCol(int monsterCol) { MonsterCol = monsterCol; }

    @SuppressWarnings("unused")
    private void attackAdjacent(Hero target){
        double distance = Math.sqrt(Math.pow(target.getHeroRow() - this.getMonsterRow(), 2) + Math.pow(target.getHeroCol() - this.getMonsterCol(), 2));

        if (distance <= Math.sqrt(2)) {
            //monster attacks
        }
    }

    @SuppressWarnings("unused")
    private void move(lovWorld world){
        int[] down = {getMonsterRow() + 1, getMonsterCol()};
        int[] left = {getMonsterRow(), getMonsterCol() - 1};
        int[] right = {getMonsterRow(), getMonsterCol() + 1};

        if(world.isValidMove(down[0], down[1])){
            setMonsterRow(down[0]);
            setMonsterCol(down[1]);
        } else if (world.isValidMove(left[0], left[1])) {
            setMonsterRow(left[0]);
            setMonsterCol(left[1]);
        } else if (world.isValidMove(right[0], right[1])) {
            setMonsterRow(right[0]);
            setMonsterCol(right[1]);
        }
    }



    public void setMonsterIdentifier(String identifier) {
        this.monsterIdentifier = identifier;
    }


    public String getMonsterIdentifier() {
        return monsterIdentifier;
    }

    @Override
    public String getSymbol() {
        return monsterIdentifier; // This will return M1, M2, M3 depending on the monster
    }
}