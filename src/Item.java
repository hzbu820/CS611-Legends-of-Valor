/**
 * An abstract class representing generic items in the game, serving as the base class for all item types (e.g., Weapon, Armor, Potion).
 * This class include properties like name, price, and effect. It also provides a toString method for generic items.
 */
public abstract class Item {
    protected String name;
    protected int cost;
    protected int levelRequirement;
    protected String categories;

    public Item(String name, int cost, int levelRequirement, String categories) {
        this.name = name;
        this.cost = cost;
        this.levelRequirement = levelRequirement;
        this.categories = categories;
    }

    //Getters
    public String getName() { return name; }
    public int getCost() { return cost; }
    public int getSellValue() {
        return cost/2; }
    public void buy(Hero hero) {
        hero.deductGold(cost);
        hero.getInventory().addItem(this);
    }
    public void sell(Hero hero) {
        hero.addGold(getSellValue());
        hero.getInventory().useItem(this.getName());
    }
    public int getLevelRequirement() { return levelRequirement; }

    @Override
    public String toString() {
        return name + ", Level=" + levelRequirement + ", Categories: " + categories + ", Cost=" + cost;
    }
}