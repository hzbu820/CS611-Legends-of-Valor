/**
 * A factory class responsible for creating instances of various items from data files. Using the Factory design pattern,
 * this class simplifies item creation and may handle item type differentiation, enabling efficient management of
 * different item categories.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemFactory {
    private static final Random random = new Random();

    public static Item createItem(String itemType, int level) {
        List<ItemData> itemDataList;

        // Load all spells if itemType is Spell
        if (itemType.equals("Spell")) {
            itemDataList = loadAllSpells(level);
        } else {
            String filename = getFilenameForItemType(itemType);
            if (filename == null) {
                System.out.println("Invalid item type: " + itemType);
                return null;
            }
            itemDataList = loadItemData(filename, level);
        }

        if (itemDataList.isEmpty()) {
            System.out.println("No items found for type " + itemType + " at level " + level);
            return null;
        }

        ItemData selectedData = itemDataList.get(random.nextInt(itemDataList.size()));

        switch (itemType) {
            case "Weapon":
                return new Weapon(selectedData.name, selectedData.cost, selectedData.levelRequirement, selectedData.damage, selectedData.requiredHands);
            case "Spell":
                return new Spell(selectedData.name, selectedData.cost, selectedData.levelRequirement, selectedData.damage, selectedData.manaCost, selectedData.spellType);
            case "Armor":
                return new Armor(selectedData.name, selectedData.cost, selectedData.levelRequirement, selectedData.damageReduction);
            case "Potion":
                return new Potion(selectedData.name, selectedData.cost, selectedData.levelRequirement, selectedData.attributeIncrease, selectedData.attributeAffected);
            default:
                throw new IllegalArgumentException("Unknown item type: " + itemType);
        }
    }

    // Load spells from all three spell files
    private static List<ItemData> loadAllSpells(int level) {
        String[] spellFiles = { "FireSpells.txt", "IceSpells.txt", "LightningSpells.txt" };
        List<ItemData> allSpells = new ArrayList<>();

        for (String filename : spellFiles) {
            allSpells.addAll(loadItemData(filename, level));
        }
        return allSpells;
    }

    // Map item types to specific filenames
    private static String getFilenameForItemType(String itemType) {
        switch (itemType) {
            case "Weapon":
                return "Weaponry.txt";
            case "Armor":
                return "Armory.txt";
            case "Potion":
                return "Potions.txt";
            default:
                return null;
        }
    }

    // Load items from a specified file and filter by level
    private static List<ItemData> loadItemData(String filename, int level) {
        List<ItemData> items = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FilePathHelper.getDataFilePath(filename)))) {
            // Skip the first line
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 3) {
                    String name = parts[0];
                    int cost = Integer.parseInt(parts[1]);
                    int levelRequirement = Integer.parseInt(parts[2]);

                    if (levelRequirement <= level) {
                        switch (filename) {
                            case "Weaponry.txt":
                                int damage = Integer.parseInt(parts[3]);
                                int requiredHands = Integer.parseInt(parts[4]);
                                items.add(new ItemData(name, cost, levelRequirement, damage, requiredHands, 0, 0, 0, null ,null));
                                break;
                            case "Armory.txt":
                                int damageReduction = Integer.parseInt(parts[3]);
                                items.add(new ItemData(name, cost, levelRequirement, 0, 0, 0, damageReduction, 0, null,null));
                                break;
                            case "Potions.txt":
                                int attributeIncrease = Integer.parseInt(parts[3]);
                                String attributeAffected = parts[4];
                                items.add(new ItemData(name, cost, levelRequirement, 0, 0, 0,0, attributeIncrease, attributeAffected, null));
                                break;
                            case "FireSpells.txt":
                                damage = Integer.parseInt(parts[3]);
                                int manaCost = Integer.parseInt(parts[4]);
                                String spellType = "Fire";
                                items.add(new ItemData(name, cost, levelRequirement, damage, 0, manaCost, 0, 0 ,null, spellType));
                                break;
                            case "IceSpells.txt":
                                damage = Integer.parseInt(parts[3]);
                                manaCost = Integer.parseInt(parts[4]);
                                spellType = "Ice";
                                items.add(new ItemData(name, cost, levelRequirement, damage, 0, manaCost, 0, 0,null, spellType));
                                break;
                            case "LightningSpells.txt":
                                damage = Integer.parseInt(parts[3]);
                                manaCost = Integer.parseInt(parts[4]);
                                spellType = "Lightning";
                                items.add(new ItemData(name, cost, levelRequirement, damage, 0, manaCost, 0, 0,null, spellType));
                                break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file " + filename + ": " + e.getMessage());
        }

        return items;
    }
}

class ItemData {
    String name;
    int cost, levelRequirement, damage, requiredHands, manaCost, damageReduction, attributeIncrease;
    String attributeAffected, spellType;

    public ItemData(String name, int cost, int levelRequirement, int damage, int requiredHands, int manaCost, int damageReduction, int attributeIncrease, String attributeAffected, String spellType) {
        this.name = name;
        this.cost = cost;
        this.levelRequirement = levelRequirement;
        this.damage = damage;
        this.requiredHands = requiredHands;
        this.manaCost = manaCost;
        this.damageReduction = damageReduction;
        this.attributeIncrease = attributeIncrease;
        this.attributeAffected = attributeAffected;
        this.spellType = spellType;
    }
}