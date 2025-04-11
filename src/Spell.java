/**
 * Spell class - the magical abilities in our game.
 * 
 * Spells deal damage and have special effects like reducing defense,
 * damage output, or dodge chance depending on their type (fire, ice, lightning).
 * Each spell costs mana to cast.
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