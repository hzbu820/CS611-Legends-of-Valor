/**
 * Represents an armor item in the game, providing damage reduction to heroes by reducing incoming damage.
 */

public class Armor extends Item implements Buyable {
    private int damageReduction;

    public Armor(String name, int cost, int levelRequirement, int damageReduction) {
        super(name, cost, levelRequirement, "Armor");
        this.damageReduction = damageReduction;
    }

    public int getDamageReduction() { return damageReduction; }


}