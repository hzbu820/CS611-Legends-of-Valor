/**
 * Inventory class - the hero's backpack and equipped gear.
 * 
 * This class keeps track of all the cool stuff your heroes collect
 * during their adventures. It manages weapons, armor, potions, and
 * other items they pick up along the way. Heroes can equip weapons
 * and armor from their inventory to boost their combat abilities.
 */

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> items;
    private List<Weapon> equippedWeapons;
    private Armor equippedArmor;

    public Inventory() {
        this.items = new ArrayList<>();
        this.equippedWeapons = new ArrayList<>();
        this.equippedArmor = null;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }


    public boolean equipItem() {
        List<Item> equippableItems = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Weapon || item instanceof Armor) {
                equippableItems.add(item);
            }
        }

        if (equippableItems.isEmpty()) {
            System.out.println("No weapons or armor available to equip.");
            return false;
        }

        System.out.println("Available equitable items:");
        for (int i = 0; i < equippableItems.size(); i++) {
            System.out.println((i + 1) + ". " + equippableItems.get(i).getName());
        }

        Item itemToEquip = null;
        while (itemToEquip == null) {
            int index = InputHandler.getInstance().getIntInput("Enter the index of the item you want to equip: ") - 1;
            if (index >= 0 && index < equippableItems.size()) {
                itemToEquip = equippableItems.get(index);
            } else {
                System.out.println("Invalid selection. Please choose a valid index.");
            }
        }

        if (itemToEquip instanceof Weapon) {
            equipWeapon((Weapon) itemToEquip);
        } else if (itemToEquip instanceof Armor) {
            equipArmor((Armor) itemToEquip);
        }
        
        return true;
    }

    public void unequipItem() {
        List<Item> unequippableItems = new ArrayList<>(equippedWeapons);
        if (equippedArmor != null) {
            unequippableItems.add(equippedArmor);
        }

        if (unequippableItems.isEmpty()) {
            System.out.println("No items currently equipped.");
            return;
        }

        System.out.println("Currently equipped items:");
        for (int i = 0; i < unequippableItems.size(); i++) {
            System.out.println((i + 1) + ". " + unequippableItems.get(i).getName());
        }

        Item itemToUnequip = null;
        while (itemToUnequip == null) {
            int index = InputHandler.getInstance().getIntInput("Enter the index of the item you want to unequip: ") - 1;
            if (index >= 0 && index < unequippableItems.size()) {
                itemToUnequip = unequippableItems.get(index);
            } else {
                System.out.println("Invalid selection. Please choose a valid index.");
            }
        }

        if (itemToUnequip instanceof Weapon) {
            unequipWeapon((Weapon) itemToUnequip);
        } else if (itemToUnequip instanceof Armor) {
            unequipArmor();
        }
    }

    private void equipWeapon(Weapon weapon) {
        if (weapon.getRequiredHands() == 2) {
            equippedWeapons.clear();
            equippedWeapons.add(weapon);
            items.remove(weapon);
            System.out.println(weapon.getName() + " is now equipped as a two-handed weapon.");
        } else {
            if (equippedWeapons.size() < 2) {
                equippedWeapons.add(weapon);
                items.remove(weapon);
                System.out.println(weapon.getName() + " is now equipped as a one-handed weapon.");
            } else {
                System.out.println("Already holding two single-handed weapons.");
            }
        }
    }

    private void equipArmor(Armor armor) {
        if (equippedArmor != null) {
            System.out.println("An armor is already equipped.");
        } else {
            equippedArmor = armor;
            items.remove(armor);
            System.out.println(armor.getName() + " is now equipped as armor.");
        }
    }

    private void unequipWeapon(Weapon weapon) {
        equippedWeapons.remove(weapon);
        items.add(weapon);
        System.out.println(weapon.getName() + " has been unequipped.");
    }

    private void unequipArmor() {
        if (equippedArmor != null) {
            items.add(equippedArmor);
            System.out.println(equippedArmor.getName() + " has been unequipped.");
            equippedArmor = null;
        }
    }

    public int useWeapon() {
        if (equippedWeapons.isEmpty()) {
            return 0; // No weapon equipped, no damage
        }

        // Validate the first weapon
        Weapon weapon = equippedWeapons.get(0);
        if (weapon == null || weapon.getDamage() < 0) {
            System.out.println("DEBUG: Invalid weapon or negative damage for equipped weapon.");
            return 0; // Default to no damage if invalid
        }

        if (equippedWeapons.size() == 1) {
            // Handle single weapon case
            int damage = weapon.getDamage();
            if (weapon.getRequiredHands() == 2) {
                damage *= 1.5; // 50% more damage for double-handed weapon
            }

            // Log and validate the final damage
            if (damage > 1000) { // Arbitrary upper bound for damage
                System.out.println("DEBUG: Weapon damage out of bounds: " + damage);
                damage = 1000; // Cap the damage to a reasonable value
            }

            return (int) damage;
        } else {
            // Validate the second weapon
            Weapon secondWeapon = equippedWeapons.get(1);
            if (secondWeapon == null || secondWeapon.getDamage() < 0) {
                System.out.println("DEBUG: Invalid second weapon or negative damage.");
                return equippedWeapons.get(0).getDamage(); // Use only the first weapon's damage
            }

            // Both weapons must be one-handed
            if (weapon.getRequiredHands() != 1 || secondWeapon.getRequiredHands() != 1) {
                System.out.println("DEBUG: Invalid weapon configuration. Only one-handed weapons allowed for dual wielding.");
                return weapon.getDamage(); // Default to the first weapon's damage
            }

            // Sum the damage of both one-handed weapons
            int totalDamage = weapon.getDamage() + secondWeapon.getDamage();

            // Log and validate the final damage
            if (totalDamage > 1000) { // Arbitrary upper bound for total damage
                System.out.println("DEBUG: Total weapon damage out of bounds: " + totalDamage);
                totalDamage = 1000; // Cap the damage to a reasonable value
            }

            return totalDamage;
        }
    }


    public int useArmor() {
        return equippedArmor != null ? equippedArmor.getDamageReduction() : 0;
    }

    public void useItem(String itemName) {
        items.removeIf(item -> item.getName().equals(itemName));
    }


    public void showItems() {
        int index = 1;
        for (Item item : items) {
            System.out.println(index + ". " + item.getName());
            index++;
        }
    }

    public Item getItemByName(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public Item getCurrentArmor() {
        return equippedArmor;
    }

    public List<Weapon> getCurrentWeapon() {
        return equippedWeapons;
    }

    public Weapon getEquippedWeapon() {
        if (equippedWeapons.isEmpty()) {
            return null;
        }
        return equippedWeapons.get(0);
    }
    
    public Armor getEquippedArmor() {
        return equippedArmor;
    }

}