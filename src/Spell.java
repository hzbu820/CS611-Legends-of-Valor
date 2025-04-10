

/**
 * Represents a spell that characters can cast in battle, providing magical effects. This class include attributes like
 * manaCost, damage, and spellType.
 */

public class Spell extends Item implements Consumable {
    private String spellType;
    private int damage;
    private int manaCost;

    public Spell(String name, int cost, int levelRequirement, int damage, int manaCost, String spellType) {
        super(name, cost, levelRequirement, "Spell");
        this.damage = damage;
        this.manaCost = manaCost;
        this.spellType = spellType;
    }

    // Getters
    public int getDamage() { return damage; }
    public int getManaCost() { return manaCost; }
    public String getSpellType() { return spellType; }

    @Override
    public void use(Hero hero) {
        hero.reduceMana(getManaCost());
        hero.getInventory().useItem(getName());
    }
}