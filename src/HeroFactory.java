/**
 * A factory class for creating instances of Hero characters. Using the Factory design pattern. Hero instances are either
 * created from attributes from files or using default attributes
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HeroFactory {
    private static final List<Hero> availableHeroes = new ArrayList<>();

    // Load heroes from files upon initialization
    static {
        loadHeroesFromFile("Warriors.txt", "Warrior");
        loadHeroesFromFile("Sorcerers.txt", "Sorcerer");
        loadHeroesFromFile("Paladins.txt", "Paladin");
    }

    private static void loadHeroesFromFile(String filename, String heroClass) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/data/"+ filename))) {
            // Skip the first line
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 7) {
                    String name = parts[0];
                    int manaPoints = Integer.parseInt(parts[1]);
                    int strength = Integer.parseInt(parts[2]);
                    int agility = Integer.parseInt(parts[3]);
                    int dexterity = Integer.parseInt(parts[4]);
                    int gold = Integer.parseInt(parts[5]);
                    int experience = Integer.parseInt(parts[6]);

                    Hero hero = createHeroFromAttributes(name, heroClass, manaPoints, strength, dexterity, agility, gold, experience);
                    availableHeroes.add(hero);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading heroes from " + filename + ": " + e.getMessage());
        }
    }

    private static Hero createHeroFromAttributes(String name, String heroClass, int manaPoints, int strength, int agility, int dexterity, int gold, int experience) {
        return new Hero(name, heroClass, manaPoints, strength, agility, dexterity, gold, experience);
    }

    public static Hero createHero(String heroClass) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to choose a pre-defined hero or create a custom one?");
        System.out.println("1. Choose a hero from the list");
        System.out.println("2. Create a custom hero");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                return chooseHeroFromList(heroClass);
            case 2:
                return createCustomHero(heroClass, scanner);
            default:
                System.out.println("Invalid choice. Selecting a default hero.");
                return availableHeroes.isEmpty() ? null : availableHeroes.get(0);
        }
    }

    private static Hero chooseHeroFromList(String heroClass) {
        System.out.println("Available " + heroClass + " heroes:");
        List<Hero> classSpecificHeroes = new ArrayList<>();
        int index = 1;
        
        // Create a filtered list of heroes that match the class and haven't been selected yet
        for (Hero hero : availableHeroes) {
            if (hero.heroClass.equalsIgnoreCase(heroClass)) {
                classSpecificHeroes.add(hero);
                System.out.println(index + ". " + hero);
                index++;
            }
        }

        if (classSpecificHeroes.isEmpty()) {
            System.out.println("No heroes available for the specified class.");
            return null;
        }

        int heroChoice = -1;
        while (heroChoice <= 0 || heroChoice > classSpecificHeroes.size()) {
            heroChoice = InputHandler.getInstance().getIntInput("Enter the index of the hero you'd like to choose: ");
            if (heroChoice <= 0 || heroChoice > classSpecificHeroes.size()) {
                System.out.println("Invalid choice. Please enter a number between 1 and " + classSpecificHeroes.size());
            }
        }

        // Get the selected hero and remove it from the available heroes list
        Hero selectedHero = classSpecificHeroes.get(heroChoice - 1);
        availableHeroes.remove(selectedHero);
        
        return selectedHero;
    }

    private static Hero createCustomHero(String heroClass, Scanner scanner) {
        String customName;
        while (true) {
            System.out.print("Enter a name for your hero (cannot be empty): ");
            customName = scanner.nextLine().trim();
            if (!customName.isEmpty()) {
                break;
            }
            System.out.println("Invalid input. Please enter a non-empty name.");
        }
        return new Hero(customName, heroClass);
    }
}
