/**
 * A factory class to create instances of Monster characters. This class uses the Factory design pattern to
 * dynamically generate monsters with specific attributes from data files.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonsterFactory {
    private static final Random random = new Random();

    public Monster createRandomMonster(int level) {
        int monsterType = random.nextInt(3);
        String monsterTypeName;

        switch (monsterType) {
            case 0:
                monsterTypeName = "Dragon";
                break;
            case 1:
                monsterTypeName = "Spirit";
                break;
            case 2:
                monsterTypeName = "Exoskeleton";
                break;
            default:
                throw new IllegalStateException("Unexpected monster type: " + monsterType);
        }

        // Load monster data and filter by level
        MonsterData monsterData = loadMonsterData(monsterTypeName, level);
        if (monsterData == null) {
            // If no monster of exact level is found, create a scaled one
            monsterData = createScaledMonsterData(monsterTypeName, level);
        }

        // Create the specific monster instance based on the loaded data
        return new Monster(monsterData.name, monsterData.level, monsterData.damage, monsterData.defense, monsterData.dodgeChance);
    }

    private MonsterData createScaledMonsterData(String monsterType, int level) {
        // Create a scaled monster based on existing ones
        String name = monsterType + " Lvl " + level;
        int damage = level * 100;
        int defense = level * 70;
        int dodgeChance = level * 5;
        
        return new MonsterData(name, level, damage, defense, dodgeChance);
    }

    public Monster createMonster(int heroLevel) {
        int monsterType = random.nextInt(3);
        String monsterTypeName;

        switch (monsterType) {
            case 0:
                monsterTypeName = "Dragon";
                break;
            case 1:
                monsterTypeName = "Spirit";
                break;
            case 2:
                monsterTypeName = "Exoskeleton";
                break;
            default:
                throw new IllegalStateException("Unexpected monster type: " + monsterType);
        }

        // Load monster data and filter by level
        MonsterData monsterData = loadMonsterData(monsterTypeName, heroLevel);
        if (monsterData == null) {
            System.out.println("No matching monster found for level " + heroLevel);
            return null;
        }

        // Create the specific monster instance based on the loaded data
        return new Monster(monsterData.name, monsterData.level, monsterData.damage, monsterData.defense, monsterData.dodgeChance);
    }

    private MonsterData loadMonsterData(String monsterType, int level) {
        String filename = monsterType + "s.txt";  // e.g., Dragons.txt
        List<MonsterData> matchingMonsters = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FilePathHelper.getDataFilePath(filename)))) {
            // Skip the first line
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 5) {
                    String name = parts[0];
                    int monsterLevel = Integer.parseInt(parts[1]);
                    int damage = Integer.parseInt(parts[2]);
                    int defense = Integer.parseInt(parts[3]);
                    int dodgeChance = Integer.parseInt(parts[4]);

                    if (monsterLevel == level) {
                        matchingMonsters.add(new MonsterData(name, monsterLevel, damage, defense, dodgeChance));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file " + filename + ": " + e.getMessage());
        }

        // Return a random monster from the matching list
        if (!matchingMonsters.isEmpty()) {
            return matchingMonsters.get(random.nextInt(matchingMonsters.size()));
        }
        return null;
    }
}

class MonsterData {
    String name;
    int level, damage, defense, dodgeChance;

    public MonsterData(String name, int level, int damage, int defense, int dodgeChance) {
        this.name = name;
        this.level = level;
        this.damage = damage;
        this.defense = defense;
        this.dodgeChance = dodgeChance;
    }
}