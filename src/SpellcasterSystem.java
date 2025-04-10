import java.util.ArrayList;
import java.util.List;

/**
 * Manages a hero's spell-related functionality including learning spells,
 * managing learned spells, and spell casting.
 */
public class SpellcasterSystem {
    private final Hero hero;
    private final List<Spell> learnedSpells;
    
    /**
     * Constructor that initializes the spell system for a specific hero.
     * 
     * @param hero The hero this spell system belongs to
     */
    public SpellcasterSystem(Hero hero) {
        this.hero = hero;
        this.learnedSpells = new ArrayList<>();
    }
    
    /**
     * Learns a new spell if the hero doesn't already know it.
     * 
     * @param spell The spell to learn
     */
    public void learnSpell(Spell spell) {
        if (spell == null) {
            return;
        }
        
        // Check if the spell is already learned
        for (Spell learnedSpell : learnedSpells) {
            if (learnedSpell.getName().equals(spell.getName())) {
                System.out.println(hero.getName() + " already knows " + spell.getName() + ".");
                return;
            }
        }
        
        // Learn the new spell
        learnedSpells.add(spell);
        System.out.println(hero.getName() + " has learned the spell " + spell.getName() + "!");
    }
    
    /**
     * Gets the list of all spells the hero has learned.
     * 
     * @return The list of learned spells
     */
    public List<Spell> getLearnedSpells() {
        return learnedSpells;
    }
    
    /**
     * Checks if the hero has learned any spells.
     * 
     * @return true if the hero knows at least one spell, false otherwise
     */
    public boolean hasLearnedSpells() {
        return !learnedSpells.isEmpty();
    }
    
    /**
     * Displays all spells the hero has learned with their details.
     */
    public void displayLearnedSpells() {
        if (learnedSpells.isEmpty()) {
            System.out.println(hero.getName() + " hasn't learned any spells yet.");
            return;
        }
        
        System.out.println("\n\u001B[35m*** " + hero.getName() + "'s Learned Spells ***\u001B[0m");
        for (int i = 0; i < learnedSpells.size(); i++) {
            Spell spell = learnedSpells.get(i);
            System.out.println((i+1) + ". " + spell.getName() + 
                " (Type: " + spell.getSpellType() + 
                ", Damage: " + spell.getDamage() + 
                ", Mana Cost: " + spell.getManaCost() + ")");
        }
    }
    
    /**
     * Casts a learned spell on a target monster.
     * 
     * @param spell The spell to cast
     * @param target The monster to target with the spell
     */
    public void castLearnedSpell(Spell spell, Monster target) {
        if (hero.getCurrentMana() < spell.getManaCost()) {
            System.out.println("Not enough mana to cast " + spell.getName() + ".");
            return;
        }
        
        if (target == null || !target.isAlive()) {
            System.out.println("Invalid target for spell.");
            return;
        }
        
        // Reduce mana
        hero.reduceMana(spell.getManaCost());
        
        // Calculate damage
        int damage = spell.getDamage();
        
        // Apply damage to target
        target.takeDamage(damage);
        System.out.println(hero.getName() + " cast " + spell.getName() + " on " + target.getName() + " for " + damage + " damage!");
        
        // Apply spell effects based on type
        if (spell.getSpellType() != null) {
            target.applySpellEffect(spell.getSpellType());
        }
    }
    
    /**
     * Gets a learned spell by its index in the learned spells list.
     * 
     * @param index The index of the spell
     * @return The spell at the specified index, or null if index is invalid
     */
    public Spell getSpellByIndex(int index) {
        if (index >= 0 && index < learnedSpells.size()) {
            return learnedSpells.get(index);
        }
        return null;
    }
} 