/**
 * Represents a marketplace where players can buy and sell items. This class provide methods to display items for sale,
 * resells, and manage player interactions with the market.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Market extends Space {
    private List<Item> availableWeapons;
    private List<Item> availableArmor;
    private List<Item> availablePotions;
    private List<Item> availableSpells;

    public Market() {
        loadAvailableItems();
    }

    @Override
    public String getSymbol() {
        return "$";
    }

    // Load items from all categories with multiple level options
    private void loadAvailableItems() {
        availableWeapons = new ArrayList<>();
        availableArmor = new ArrayList<>();
        availablePotions = new ArrayList<>();
        availableSpells = new ArrayList<>();
        
        // Load items once at each level to ensure we have a full inventory
        // Level 1-8 covers most playable content
        for (int level = 1; level <= 8; level++) {
            // Load weapons - just once per level
            Item weapon = ItemFactory.createItem("Weapon", level);
            if (weapon != null) {
                availableWeapons.add(weapon);
            }
            
            // Load armor - just once per level
            Item armor = ItemFactory.createItem("Armor", level);
            if (armor != null) {
                availableArmor.add(armor);
            }
            
            // Load potions - just once per level
            Item potion = ItemFactory.createItem("Potion", level);
            if (potion != null) {
                availablePotions.add(potion);
            }
            
            // Load spells - once for each spell type
            Item spell = ItemFactory.createItem("Spell", level);
            if (spell != null) {
                availableSpells.add(spell);
            }
        }
        
        // Add level 9-10 items to make high-level content available
        for (int level = 9; level <= 10; level++) {
            Item armor = ItemFactory.createItem("Armor", level);
            if (armor != null) {
                availableArmor.add(armor);
            }
        }
    }

    public void enterMarket(Hero hero) {
        InputHandler inputHandler = InputHandler.getInstance();
        boolean inMarket = true;

        while (inMarket) {
            System.out.println("\n\u001B[33m" + "Merchant: Everything's for sale, my friend. Everything. If I had a brother, I'd sell him in a second." + "\u001B[0m");

            System.out.println("\n1. Buy an item");
            System.out.println("2. Sell an item");
            System.out.println("3. View learned spells");
            System.out.println("4. Exit market");

            int choice = inputHandler.getIntInput("Enter choice: ");

            switch (choice) {
                case 1:
                    buyItem(hero, inputHandler);
                    break;
                case 2:
                    sellItem(hero, inputHandler);
                    break;
                case 3:
                    if (hero.hasLearnedSpells()) {
                        hero.displayLearnedSpells();
                    } else {
                        System.out.println("\nYou haven't learned any spells yet.");
                        System.out.println("Buy a spell to learn it permanently!");
                    }
                    break;
                case 4:
                    System.out.println("\n\u001B[33m" + "Merchant: Do come back..." + "\u001B[0m");
                    inMarket = false;
                    break;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void buyItem(Hero hero, InputHandler inputHandler) {
        System.out.println("\nWhat type of item would you like to buy?");
        System.out.println("1. \u001B[31mWeapons\u001B[0m");
        System.out.println("2. \u001B[34mArmor\u001B[0m");
        System.out.println("3. \u001B[32mPotions\u001B[0m");
        System.out.println("4. \u001B[35mSpells\u001B[0m");
        System.out.println("5. Back to main menu");
        
        int categoryChoice = inputHandler.getIntInput("Enter your choice: ");
        
        if (categoryChoice == 5) {
            return;
        }
        
        List<Item> itemsToShow = new ArrayList<>();
        String categoryName = "";
        String categoryColor = "";
        
        switch (categoryChoice) {
            case 1:
                itemsToShow = availableWeapons;
                categoryName = "Weapons";
                categoryColor = "\u001B[31m"; // Red
                break;
            case 2:
                itemsToShow = availableArmor;
                categoryName = "Armor";
                categoryColor = "\u001B[34m"; // Blue
                break;
            case 3:
                itemsToShow = availablePotions;
                categoryName = "Potions";
                categoryColor = "\u001B[32m"; // Green
                break;
            case 4:
                itemsToShow = availableSpells;
                categoryName = "Spells";
                categoryColor = "\u001B[35m"; // Purple
                break;
            default:
                System.out.println("Invalid category choice.");
                return;
        }
        
        if (itemsToShow.isEmpty()) {
            System.out.println("No " + categoryName + " available.");
            return;
        }
        
        System.out.println("\n" + categoryColor + categoryName + " available for sale:\u001B[0m");
        
        // Group items by level
        Map<Integer, List<Item>> itemsByLevel = new HashMap<>();
        for (Item item : itemsToShow) {
            itemsByLevel
                .computeIfAbsent(item.getLevelRequirement(), k -> new ArrayList<>())
                .add(item);
        }
        
        // Display items grouped by level
        int index = 1;
        for (int level = 1; level <= 10; level++) {
            List<Item> levelItems = itemsByLevel.get(level);
            if (levelItems != null && !levelItems.isEmpty()) {
                System.out.println("\n\u001B[1m" + "Level " + level + " " + categoryName + ":\u001B[0m");
                for (Item item : levelItems) {
                    if (item.getLevelRequirement() > hero.getLevel()) {
                        // Dim items that are too high level
                        System.out.print("\u001B[90m"); // Gray
                    } else {
                        System.out.print(categoryColor);
                    }
                    
                    // Specialized display for different item types
                    String itemDetails = "";
                    if (item instanceof Weapon) {
                        Weapon weapon = (Weapon) item;
                        itemDetails = " [DMG: " + weapon.getDamage() + ", Hands: " + weapon.getRequiredHands() + "]";
                    } else if (item instanceof Armor) {
                        Armor armor = (Armor) item;
                        itemDetails = " [Protection: " + armor.getDamageReduction() + "]";
                    } else if (item instanceof Spell) {
                        Spell spell = (Spell) item;
                        itemDetails = " [" + spell.getSpellType() + " DMG: " + spell.getDamage() + ", Mana: " + spell.getManaCost() + "]";
                    } else if (item instanceof Potion) {
                        Potion potion = (Potion) item;
                        itemDetails = " [+" + potion.getAttributeIncrease() + " " + potion.getAttributeAffected() + "]";
                    }
                    
                    System.out.println(index + ": " + item.getName() + itemDetails + 
                        " (Cost: " + item.getCost() + " gold, Lvl Req: " + item.getLevelRequirement() + ")\u001B[0m");
                    index++;
                }
            }
        }
        
        int itemIndex = inputHandler.getIntInput("Enter the index of the item you want to buy (0 to cancel): ");
        if (itemIndex == 0) {
            return;
        }
        
        if (itemIndex < 1 || itemIndex > itemsToShow.size()) {
            System.out.println("Invalid item index.");
            return;
        }
        
        Item itemToBuy = itemsToShow.get(itemIndex - 1);
        
        if (hero.getLevel() < itemToBuy.getLevelRequirement()) {
            System.out.println("You do not meet the level requirement to buy this item. You need to be level " + 
                itemToBuy.getLevelRequirement() + " (You are level " + hero.getLevel() + ")");
        } else if (hero.getGold() < itemToBuy.getCost()) {
            System.out.println("You do not have enough gold to buy this item. You need " + 
                itemToBuy.getCost() + " gold (You have " + hero.getGold() + " gold)");
        } else {
            hero.deductGold(itemToBuy.getCost());
            
            // Handle spells differently - learn them instead of adding to inventory
            if (itemToBuy instanceof Spell) {
                Spell spell = (Spell) itemToBuy;
                hero.learnSpell(spell);
                
                // Add a visual feedback for spell learning
                System.out.println("\n\u001B[35m*** " + hero.getName() + " has learned " + spell.getName() + "! ***\u001B[0m");
                System.out.println("\u001B[35mSpell Type: " + spell.getSpellType() + 
                    " | Damage: " + spell.getDamage() + 
                    " | Mana Cost: " + spell.getManaCost() + "\u001B[0m");
                System.out.println("\u001B[35mThis spell can now be cast anytime in battle as long as you have enough mana!\u001B[0m");
            } else {
                hero.getInventory().addItem(itemToBuy);
            }
            
            itemsToShow.remove(itemToBuy);
            System.out.println("\u001B[32mYou have successfully bought " + itemToBuy.getName() + "!\u001B[0m");
            System.out.println("You now have " + hero.getGold() + " gold.");
        }
    }

    private void sellItem(Hero hero, InputHandler inputHandler) {
        System.out.println("\n\u001B[33mYour inventory:\u001B[0m");
        List<Item> items = hero.getInventory().getItems();

        if (items.isEmpty()) {
            System.out.println("You don't have any items to sell.");
            return;
        }

        // Display the inventory items
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            String itemDetails = "";
            String itemColor = "";
            
            // Determine color and details based on item type
            if (item instanceof Weapon) {
                Weapon weapon = (Weapon) item;
                itemDetails = " [DMG: " + weapon.getDamage() + ", Hands: " + weapon.getRequiredHands() + "]";
                itemColor = "\u001B[31m"; // Red for weapons
            } else if (item instanceof Armor) {
                Armor armor = (Armor) item;
                itemDetails = " [Protection: " + armor.getDamageReduction() + "]";
                itemColor = "\u001B[34m"; // Blue for armor
            } else if (item instanceof Potion) {
                Potion potion = (Potion) item;
                itemDetails = " [+" + potion.getAttributeIncrease() + " " + potion.getAttributeAffected() + "]";
                itemColor = "\u001B[32m"; // Green for potions
            }
            
            System.out.println((i + 1) + ". " + itemColor + item.getName() + itemDetails + 
                " - Sell value: " + (item.getCost() / 2) + " gold\u001B[0m");
        }

        int index = inputHandler.getIntInput("Enter the index of the item you want to sell (0 to cancel): ");
        if (index == 0) {
            return;
        }
        
        if (index < 1 || index > items.size()) {
            System.out.println("Invalid index. Please enter a number between 1 and " + items.size() + ".");
            return;
        }
        
        Item itemToSell = items.get(index - 1);
        int sellPrice = itemToSell.getCost() / 2;
        hero.addGold(sellPrice);
        hero.getInventory().useItem(itemToSell.getName());
        System.out.println("\u001B[32mYou have sold " + itemToSell.getName() + " for " + sellPrice + " gold.\u001B[0m");
        System.out.println("You now have " + hero.getGold() + " gold.");
    }
}