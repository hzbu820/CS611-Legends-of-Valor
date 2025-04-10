/**
 * Manages the combat interactions between characters, including heroes and monsters. This class handles the
 * battle sequence, determining attack order, applying damage, apply items,and checking for win/loss conditions.
 * Include methods to process attack, defense, and special abilities, and handle turn-based mechanics.
 *
 */

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Battle {
    private List<Hero> heroes;
    private List<Monster> monsters;
    private Random random = new Random();

    public Battle(List<Hero> heroes, List<Monster> monsters) {
        this.heroes = heroes;
        this.monsters = monsters;
        heroes.sort(Comparator.comparingInt(Hero::getCurrentAgility).reversed());  // Sort heroes by agility for turn order
    }

    public boolean startBattle() {
        System.out.println("Battle begins!");

        printBattleStatus();

        boolean heroesTurn = random.nextBoolean();  // Randomly determine initial turn

        while (heroes.stream().anyMatch(Hero::isAlive) && monsters.stream().anyMatch(Monster::isAlive)) {
            if (heroesTurn) {
                for (Hero hero : heroes) {
                    if (hero.isAlive()) {
                        heroTurn(hero);
                        //restore health and mana
                        hero.restorePerTurn();
                    }
                }
            } else {
                for (Monster monster : monsters) {
                    if (monster.isAlive()) {
                        monsterTurn(monster);
                    }
                }
                if (monsters.stream().noneMatch(Monster::isAlive)) {
                    break;  // Exit the loop if no monsters are left alive
                }
            }
            heroesTurn = !heroesTurn;  // Alternate turns

            printBattleStatus();  // Display status after each round
        }

        boolean heroesWon = heroes.stream().anyMatch(Hero::isAlive);
        System.out.println(heroesWon ? "Heroes won the battle!" : "Monsters defeated the heroes.");

        if (heroesWon) {
            rewardHeroes();
        }

        return heroesWon;
    }

    private void heroTurn(Hero hero) {
        System.out.println("\n" + hero.getName() + "'s turn! Choose an action:");
        System.out.println("1. Attack\n2. Cast Spell\n3. Use Potion\n4. Equip Item");
        int choice = InputHandler.getInstance().getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                attack(hero);
                break;
            case 2:
                castSpell(hero);
                break;
            case 3:
                usePotion(hero);
                break;
            case 4:
                hero.getInventory().equipItem();
                break;
            default:
                System.out.println("Invalid choice. Skipping turn.");
        }
    }

    private void attack(Hero hero) {
        Monster target = selectTargetMonster();
        hero.attack(target);
    }

    private void castSpell(Hero hero) {
        List<Item> spells = hero.getInventory().getItems().stream().filter(item -> item instanceof Spell).collect(Collectors.toList());
        if (spells.isEmpty()) {
            System.out.println("No spells in inventory.");
            return;
        }

        System.out.println("Select a spell:");
        for (int i = 0; i < spells.size(); i++) {
            System.out.println((i + 1) + ". " + spells.get(i).getName());
        }

        int spellIndex = InputHandler.getInstance().getIntInput("Enter spell number: ") - 1;
        if (spellIndex >= 0 && spellIndex < spells.size()) {
            Spell spell = (Spell) spells.get(spellIndex);
            if (hero.getCurrentMana() >= spell.getManaCost()) {
                Monster target = selectTargetMonster();
                if (target != null) {
                    double damage = spell.getDamage() + (hero.getCurrentDexterity() / 10000.0) * spell.getDamage();
                    target.takeDamage((int) Math.ceil(damage));
                    spell.use(hero);
                    System.out.println(hero.getName() + " cast " + spell.getName() + " on " + target.getName() + " for " + (int) Math.ceil(damage) + " damage.");
                    System.out.println(target.getName() + "'s remaining HP: " + target.getHealth());

                    if (!target.isAlive()) {
                        System.out.println(target.getName() + " has been defeated!");
                    }

                    target.applySpellEffect(spell.getSpellType());  // Apply spell effect
                }
            } else {
                System.out.println("Not enough mana.");
            }
        } else {
            System.out.println("Invalid spell selection.");
        }
    }

    private void usePotion(Hero hero) {
        List<Item> potions = hero.getInventory().getItems().stream().filter(item -> item instanceof Potion).collect(Collectors.toList());
        if (potions.isEmpty()) {
            System.out.println("No potions in inventory.");
            return;
        }

        System.out.println("Select a potion:");
        for (int i = 0; i < potions.size(); i++) {
            System.out.println((i + 1) + ". " + potions.get(i).getName());
        }

        int potionIndex = InputHandler.getInstance().getIntInput("Enter potion number: ") - 1;
        if (potionIndex >= 0 && potionIndex < potions.size()) {
            Potion potion = (Potion) potions.get(potionIndex);
            potion.use(hero);
//            hero.getInventory().useItem(potion.getName());
            System.out.println(hero.getName() + " used " + potion.getName() + ".");
        } else {
            System.out.println("Invalid potion selection.");
        }
    }


    private void monsterTurn(Monster monster) {
        System.out.println("\n" + monster.getName() + "'s turn!");
        Hero target = selectTargetHero();
        monster.attack(target);
    }

    private Monster selectTargetMonster() {
        List<Monster> aliveMonsters = monsters.stream().filter(Monster::isAlive).collect(Collectors.toList());
        if (aliveMonsters.isEmpty()) return null;

        System.out.println("Select a monster to attack:");
        for (int i = 0; i < aliveMonsters.size(); i++) {
            System.out.println((i + 1) + ". " + aliveMonsters.get(i).getName());
        }
        int choice = InputHandler.getInstance().getIntInput("Enter monster number: ") - 1;
        return (choice >= 0 && choice < aliveMonsters.size()) ? aliveMonsters.get(choice) : null;
    }

    private Hero selectTargetHero() {
        List<Hero> aliveHeroes = heroes.stream().filter(Hero::isAlive).collect(Collectors.toList());
        return aliveHeroes.isEmpty() ? null : aliveHeroes.get(random.nextInt(aliveHeroes.size()));
    }

    private void rewardHeroes() {
        int goldReward = monsters.stream().mapToInt(Monster::getLevel).sum() * 100;
        int experienceReward = monsters.stream().mapToInt(Monster::getLevel).sum();

        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                hero.gainExperience(experienceReward);
                hero.addGold(goldReward / heroes.size());
                System.out.println(hero.getName() + " gained " + experienceReward + " experience and " + (goldReward / heroes.size()) + " gold.");
            } else {
                hero.revive();
                System.out.println(hero.getName() + " is revived with half HP and mana.");
            }
            hero.printExperienceProgress();
            //reset the effects of the potions
            hero.resetCurrentAttributes();
        }
    }

    private void printBattleStatus() {
        System.out.println("\n--- Battle Status ---");
        System.out.println("Heroes:");
        for (Hero hero : heroes) {
            System.out.println(hero.getName() + " - HP: " + hero.getCurrentHealth() + "/" + hero.getHealthPoints() + ", Mana: " + hero.getCurrentMana() + "/" + hero.getManaPoints());
        }
        System.out.println("Monsters:");
        for (Monster monster : monsters) {
            System.out.println(monster.getName() + " - HP: " + monster.getHealth());
        }
        System.out.println("----------------------");
    }
}