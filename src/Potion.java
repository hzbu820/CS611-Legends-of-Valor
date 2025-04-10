/**
 * Represents a potion item that can restore health, mana, or grant other temporary boosts to characters. This class
 * includes methods for applying its effects when used by characters.
 */

public class Potion extends Item implements Buyable, Consumable {
    private int attributeIncrease;
    private String attributeAffected;

    public Potion(String name, int cost, int levelRequirement, int attributeIncrease, String attributeAffected) {
        super(name, cost, levelRequirement,"Potion");
        this.attributeIncrease = attributeIncrease;
        this.attributeAffected = attributeAffected;
    }

    public int getAttributeIncrease() { return attributeIncrease; }
    public String getAttributeAffected() { return attributeAffected; }

    @Override
    public void use(Hero hero) {
        switch (attributeAffected.toLowerCase()) {
            case "health":
                hero.increaseHealth(attributeIncrease);
                System.out.println(hero.getName() + " gained " + attributeIncrease + " HP.");
                break;
            case "strength":
                hero.increaseStrength(attributeIncrease);
                System.out.println(hero.getName() + " gained " + attributeIncrease + " Strength.");
                break;
            case "mana":
                hero.increaseMana(attributeIncrease);
                System.out.println(hero.getName() + " gained " + attributeIncrease + " Mana.");
                break;
            case "dexterity":
                hero.increaseDexterity(attributeIncrease);
                System.out.println(hero.getName() + " gained " + attributeIncrease + " Dexterity.");
                break;
            case "agility":
                hero.increaseAgility(attributeIncrease);
                System.out.println(hero.getName() + " gained " + attributeIncrease + " Agility.");
                break;
            default:
                System.out.println("Potion effect not applicable to this attribute.");
        }

        hero.getInventory().useItem(getName());
    }
}