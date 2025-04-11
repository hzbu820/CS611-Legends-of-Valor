/**
 * Character class - the foundation for all living things in our game.
 * 
 * This is the parent class that both heroes and monsters inherit from.
 * It handles the basic stuff that all characters need: a name, health,
 * level, and the ability to take damage and die. Think of it as the 
 * template that defines what makes a character in our world.
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
