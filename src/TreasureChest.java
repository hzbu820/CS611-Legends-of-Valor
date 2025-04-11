import java.util.Random;

/**
 * Represents a treasure chest that can be found and opened by heroes during gameplay.
 * Contains random rewards like gold, potions, or items.
 */
public class TreasureChest extends Space {
    private static final Random random = new Random();
    private boolean isOpened;
    private Item reward;
    private int goldReward;
    private int experienceReward;
    
    /**
     * Creates a new treasure chest with random rewards
     */
    public TreasureChest() {
        this.isOpened = false;
        generateRewards();
    }
    
    /**
     * Generates random rewards for the chest
     */
    private void generateRewards() {
        // Gold reward: 100-500 gold
        this.goldReward = 100 + random.nextInt(401);
        
        // Experience reward: 5-20 experience
        this.experienceReward = 5 + random.nextInt(16);
        
        // 40% chance to contain an item
        if (random.nextDouble() < 0.4) {
            int level = 1 + random.nextInt(5); // Generate item suitable for levels 1-5
            String[] itemTypes = {"Weapon", "Armor", "Potion", "Spell"};
            String itemType = itemTypes[random.nextInt(itemTypes.length)];
            this.reward = ItemFactory.createItem(itemType, level);
        }
    }
    
    /**
     * Opens the chest and returns the rewards
     * @param hero The hero opening the chest
     * @return A string describing what was found
     */
    public String open(Hero hero) {
        if (isOpened) {
            return "This chest has already been opened.";
        }
        
        StringBuilder result = new StringBuilder();
        result.append("You open the treasure chest and find:\n");
        
        // Add gold
        hero.addGold(goldReward);
        result.append("• ").append(goldReward).append(" gold!\n");
        
        // Add experience
        hero.gainExperience(experienceReward);
        result.append("• ").append(experienceReward).append(" experience points!\n");
        
        // Add item if there is one
        if (reward != null) {
            hero.getInventory().addItem(reward);
            result.append("• ").append(reward.getName()).append("!\n");
            
            // If it's a spell, also learn it
            if (reward instanceof Spell) {
                hero.learnSpell((Spell) reward);
                result.append("  The hero learned a new spell: ").append(reward.getName()).append("!\n");
            }
        }
        
        isOpened = true;
        return result.toString();
    }
    
    @Override
    public String getSymbol() {
        return isOpened ? "TC" : "$";
    }
    
    /**
     * Checks if the chest has been opened already
     * @return true if chest is already opened, false otherwise
     */
    public boolean isOpened() {
        return isOpened;
    }
} 