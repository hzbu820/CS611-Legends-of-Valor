import java.util.List;
import java.util.stream.Collectors;

/**
 * SpellSystem handles all the magic in the game.
 * 
 * This class manages the casting of spells, their effects on monsters,
 * and keeps track of mana costs. It's basically the wizardry department
 * of our game.
 */
public class SpellSystem {
    private List<Hero> heroes;
    private List<Monster> monsters;
    private lovWorld world;

    public SpellSystem(List<Hero> heroes, List<Monster> monsters, lovWorld world) {
        this.heroes = heroes;
        this.monsters = monsters;
        this.world = world;
    }

    /**
     * Let a hero cast a spell on a monster.
     * 
     * @param hero The spell-slinging hero
     * @return true if we killed the monster, false if it's still kicking
     */
    public boolean castSpell(Hero hero) {
        // First check if the hero has any learned spells
        List<Spell> learnedSpells = hero.getLearnedSpells();
        
        // Then check inventory for one-time use spells
        List<Item> inventorySpells = hero.getInventory().getItems().stream()
                .filter(item -> item instanceof Spell)
                .collect(Collectors.toList());
        
        if (learnedSpells.isEmpty() && inventorySpells.isEmpty()) {
            System.out.println("\u001B[31m" + hero.getName() + " doesn't know any spells.\u001B[0m");
            return false;
        }
        
        System.out.println("\n\u001B[1;35m=== Choose a Spell to Cast ===\u001B[0m");
        System.out.println("\u001B[3mCurrent mana: " + hero.getCurrentMana() + "/" + hero.getManaPoints() + "\u001B[0m");
        int index = 1;
        
        // Show learned spells first with color coding by type
        if (!learnedSpells.isEmpty()) {
            System.out.println("\n\u001B[1;35m*** Learned Spells (Permanent) ***\u001B[0m");
            for (Spell spell : learnedSpells) {
                String spellColor = getSpellTypeColor(spell.getSpellType());
                System.out.println(index + ". " + spellColor + spell.getName() + 
                    " [Type: " + spell.getSpellType() + 
                    ", Damage: " + spell.getDamage() + 
                    ", Mana Cost: " + spell.getManaCost() + 
                    "]\u001B[0m " + getSpellEffectDescription(spell.getSpellType()));
                index++;
            }
        }
        
        // Show inventory spells second (one-time use)
        if (!inventorySpells.isEmpty()) {
            System.out.println("\n\u001B[1;33m*** Inventory Spells (One-time use) ***\u001B[0m");
            for (Item item : inventorySpells) {
                Spell spell = (Spell) item;
                String spellColor = getSpellTypeColor(spell.getSpellType());
                System.out.println(index + ". " + spellColor + spell.getName() + 
                    " [Type: " + spell.getSpellType() + 
                    ", Damage: " + spell.getDamage() + 
                    ", Mana Cost: " + spell.getManaCost() + 
                    "]\u001B[0m " + getSpellEffectDescription(spell.getSpellType()));
                index++;
            }
        }

        int choice = InputHandler.getInstance().getIntInput("Enter the index of the spell you'd like to cast (0 to cancel): ");
        if (choice == 0) return false;
        
        Spell spellToCast = null;
        boolean isLearnedSpell = false;
        
        if (choice <= learnedSpells.size()) {
            // A learned spell was chosen
            spellToCast = learnedSpells.get(choice - 1);
            isLearnedSpell = true;
        } else if (choice <= learnedSpells.size() + inventorySpells.size()) {
            // An inventory spell was chosen
            spellToCast = (Spell) inventorySpells.get(choice - learnedSpells.size() - 1);
        } else {
            System.out.println("\u001B[31mInvalid spell selection.\u001B[0m");
            return false;
        }
        
        if (hero.getCurrentMana() < spellToCast.getManaCost()) {
            System.out.println("\u001B[31mNot enough mana to cast " + spellToCast.getName() + 
                ". Need " + spellToCast.getManaCost() + " mana (you have " + 
                hero.getCurrentMana() + ").\u001B[0m");
            return false;
        }
        
        Monster target = world.selectTargetMonster(hero, monsters);
        if (target != null) {
            String spellTypeColor = getSpellTypeColor(spellToCast.getSpellType());
            
            if (isLearnedSpell) {
                // For learned spells, just use mana
                hero.reduceMana(spellToCast.getManaCost());
                System.out.println("\u001B[36m" + hero.getName() + " uses " + 
                    spellToCast.getManaCost() + " mana. Remaining: " + 
                    hero.getCurrentMana() + "/" + hero.getManaPoints() + "\u001B[0m");
            } else {
                // For inventory spells, use the spell which consumes it
                spellToCast.use(hero);
                hero.getInventory().useItem(spellToCast.getName());
                System.out.println("\u001B[33mOne-time spell consumed from inventory.\u001B[0m");
            }
            
            target.takeDamage(spellToCast.getDamage());
            System.out.println(spellTypeColor + ">>> " + hero.getName() + " cast " + 
                spellToCast.getName() + " on " + target.getName() + " dealing " + 
                spellToCast.getDamage() + " damage! <<<\u001B[0m");
            
            // Apply spell effects based on the spell type
            if (spellToCast.getSpellType() != null) {
                System.out.println(spellTypeColor + getSpellEffectActionDescription(spellToCast.getSpellType()) + "\u001B[0m");
                target.applySpellEffect(spellToCast.getSpellType());
            }
            
            // Log the spell cast using CombatLogger
            CombatLogger.getInstance().logSpellCast(hero, spellToCast, target, spellToCast.getDamage());
            
            if (!target.isAlive()) {
                System.out.println("\u001B[32m" + target.getName() + " has been defeated!\u001B[0m");
                world.distributeMonsterRewards(target, heroes);
                return true; // Return true if a monster was defeated
            }
            return false; // Spell was cast but monster wasn't defeated
        }
        return false; // No target selected
    }
    
    /**
     * Gets the right color for each spell type.
     * 
     * @param spellType The type of spell
     * @return The ANSI color code for terminal output
     */
    public String getSpellTypeColor(String spellType) {
        switch (spellType.toLowerCase()) {
            case "fire":
                return "\u001B[31m"; // Red for fire
            case "ice":
                return "\u001B[36m"; // Cyan for ice
            case "lightning":
                return "\u001B[33m"; // Yellow for lightning
            default:
                return "\u001B[35m"; // Purple default for other spell types
        }
    }
    
    /**
     * Get a description of what each spell type does.
     * 
     * @param spellType The type of spell
     * @return A short description of the effect
     */
    public String getSpellEffectDescription(String spellType) {
        switch (spellType.toLowerCase()) {
            case "fire":
                return "\u001B[3m(Reduces target's defense)\u001B[0m";
            case "ice":
                return "\u001B[3m(Reduces target's damage)\u001B[0m";
            case "lightning":
                return "\u001B[3m(Reduces target's dodge chance)\u001B[0m";
            default:
                return "";
        }
    }
    
    /**
     * Gets a flashy description of the spell effect for battle messages.
     * 
     * @param spellType The type of spell
     * @return A dramatic description for the battle log
     */
    public String getSpellEffectActionDescription(String spellType) {
        switch (spellType.toLowerCase()) {
            case "fire":
                return "*** The flames sear the target's armor, reducing its defense! ***";
            case "ice":
                return "*** Freezing cold numbs the target, reducing its attack damage! ***";
            case "lightning":
                return "*** Electric shock stuns the target, reducing its dodge chance! ***";
            default:
                return "";
        }
    }

    /**
     * Direct casting method - might use this later for AI or special events.
     * 
     * @param hero The hero casting the spell
     * @param spell The spell to cast
     * @param target The monster target
     * @return true if the spell hit, false if something went wrong
     */
    @SuppressWarnings("unused")
    private boolean castSpellOnMonster(Hero hero, Spell spell, Monster target) {
        if (hero == null || spell == null || target == null) {
            return false;
        }
        
        if (!target.isAlive()) {
            System.out.println("Target is already defeated.");
            return false;
        }
        
        int manaCost = spell.getManaCost();
        if (hero.getCurrentMana() < manaCost) {
            System.out.println("Not enough mana to cast this spell.");
            return false;
        }
        
        // Apply spell damage
        int baseDamage = spell.getDamage();
        double dexterityMultiplier = hero.getCurrentDexterity() * 0.001;  // 0.1% per dexterity point
        int spellDamage = (int)(baseDamage * (1 + dexterityMultiplier));
        
        // Reduce hero's mana
        hero.reduceMana(manaCost);
        
        // Apply damage to monster
        target.takeDamage(spellDamage);
        
        // Apply the special effect
        target.applySpellEffect(spell.getSpellType());
        
        // Log the spell cast using CombatLogger
        CombatLogger.getInstance().logSpellCast(hero, spell, target, spellDamage);
        
        // Check if monster was defeated
        if (!target.isAlive()) {
            return true; // Indicate monster was defeated
        }
        
        return true;
    }
} 