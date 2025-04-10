public interface Buyable {
    int getCost();
    int getSellValue();
    void buy(Hero hero);
    void sell(Hero hero);
}
