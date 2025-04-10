/**
 * An abstract base class representing common attributes and behaviors for all characters in the game, such as health,
 * level, and attributes. This class is extended by more specific character types like Hero and Monster,
 * providing shared functionality such as attacking, defending, and leveling up.
 *
 */
public abstract class Character {
    protected String name;
    protected int level;
    protected int healthPoints;
    protected int manaPoints;

    public Character(String name, int level) {
        this.name = name;
        this.level = level;
        this.healthPoints = level * 100;
        this.manaPoints = level * 50;
    }
    public abstract String getSymbol();
    public String getName() { return name; }
    public int getHealthPoints() { return healthPoints; }
    public int getManaPoints() { return manaPoints; }
    public int getLevel() { return level; }

    public void takeDamage(int damage) {
        healthPoints = Math.max(healthPoints - damage, 0);
    }

    public boolean isAlive() { return healthPoints > 0; }

    @Override
    public String toString() {
        return "Name: " + name + ", Level=" + level;
    }

}
