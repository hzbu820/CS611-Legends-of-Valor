/**
 * Handles the application and removal of terrain effects on heroes.
 * Manages how different terrain types affect hero attributes.
 */
public class TerrainEffectHandler {
    
    /**
     * Creates a new TerrainEffectHandler.
     */
    public TerrainEffectHandler() {
        // No initialization needed
    }
    
    /**
     * Applies terrain effects to a hero based on the space they're on.
     * @param hero The hero to apply effects to
     * @param space The space the hero is on
     */
    public void applyTerrainEffect(Hero hero, Space space) {
        if (space == null || hero == null) {
            return;
        }
        
        if (space instanceof BushSpace) {
            hero.increaseDexterity(10);  // Increase dexterity by 10 in Bush space
            System.out.println(hero.getName() + " gains a dexterity boost in the Bush.");
        } else if (space instanceof CaveSpace) {
            hero.increaseAgility(10);  // Increase agility by 10 in Cave space
            System.out.println(hero.getName() + " gains an agility boost in the Cave.");
        } else if (space instanceof KoulouSpace) {
            hero.increaseStrength(10);  // Increase strength by 10 in Koulou space
            System.out.println(hero.getName() + " gains a strength boost in the Koulou.");
        }
    }
    
    /**
     * Removes terrain effects from a hero based on the space they're leaving.
     * @param hero The hero to remove effects from
     * @param space The space the hero is leaving
     */
    public void removeTerrainEffect(Hero hero, Space space) {
        if (space == null || hero == null) {
            return;
        }
        
        if (space instanceof BushSpace) {
            hero.increaseDexterity(-10);  // Remove dexterity boost from Bush space
            System.out.println(hero.getName() + " loses a dexterity boost from the Bush.");
        } else if (space instanceof CaveSpace) {
            hero.increaseAgility(-10);  // Remove agility boost from Cave space
            System.out.println(hero.getName() + " loses an agility boost from the Cave.");
        } else if (space instanceof KoulouSpace) {
            hero.increaseStrength(-10);  // Remove strength boost from Koulou space
            System.out.println(hero.getName() + " loses a strength boost from the Koulou.");
        }
    }
} 