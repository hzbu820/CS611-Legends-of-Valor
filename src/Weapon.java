

/**
 *  Defines a weapon item, providing damage increase to characters who equip it. This class include properties such as
 *  damage, required hands.
 */
public class Weapon extends Item implements Buyable {
    private int damage;
    private int requiredHands;

    public Weapon(String name, int cost, int levelRequirement, int damage, int requiredHands) {
        super(name, cost, levelRequirement, "Weapon");
        this.damage = damage;
        this.requiredHands = requiredHands;
    }

    //Getters
    //Double handed weapon deals more damage
    public int getDamage() { return requiredHands == 2 ? (int) Math.ceil(damage * 1.5) : damage; }
    public int getRequiredHands() { return requiredHands; }
}