/**
 * Item class - all the stuff you can collect in the game.
 * 
 * This is the parent class for weapons, armor, potions, and spells.
 * Items have a name, cost, and level requirement. You can buy them
 * at markets, sell them when you need gold, and use them to make
 * your heroes stronger.
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