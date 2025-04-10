# CS611-Assignment 4

## Monster and Hero

---

- **Name**: Ronghao Tong
- **Email**: ronghaot@bu.edu
- **Student ID**: U72020135

---

## Files

| Filename                 | Description                                                                                                                                   |
|--------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| `Armor.java`             | Represents an armor item in the game, providing damage reduction to heroes by reducing incoming damage.                                       |
| `Battle.java`            | Manages the combat interactions between characters, including heroes and monsters.                                                            |
| `Character.java`         | An abstract base class representing common attributes and behaviors for all characters in the game, such as health, level, and attributes.    |
| `CommonSpace.java`       | Represents a common, area in the game world where players might move triggering battle events.                                                |
| `Game.java`              | This class handles the overall game logic, responsible for initializing game components, managing the game loop, and tracking the game state. |
| `Hero.java`              | Defines the Hero character type with specific attributes such as health, mana, and other attributes.                                          |
| `Herofactory.java`       | A factory class for creating instances of Hero characters. Using the Factory design pattern.                                                  |
| `InaccessibleSapce.java` | Represents an area in the game world that cannot be entered by characters.                                                                    |
| `InputHandler.java`      | Singleton input Handler that handles all user's input.                                                                                        |
| `Inventory.java`         | Manages the collection of items a hero possesses, such as weapons, armor, spell, and potions.                                                 |
| `Item.java`              | An abstract class representing generic items in the game, serving as the base class for all item types (e.g., Weapon, Armor, Potion).         |
| `ItemFactory.java`       | A factory class responsible for creating instances of various items from data files.                                                          |
| `Main.java`              | A concise main class.                                                                                                                         |
| `Market.java`            | Represents a marketplace where players can buy and sell items.                                                                                |
| `Monster.java`           | Represents the Monster character type, with unique attributes.                                                                                |
| `MosterFactory.java`     | A factory class to create instances of Monster characters.                                                                                    |
| `Potion.java`            | Represents a potion item that can restore health, mana, or grant other temporary boosts to characters.                                        |
| `Space.java`             | An abstract class that represents a grid within the game world.                                                                               |
| `Spell.java`             | Represents a spell that characters can cast in battle, providing magical effects.                                                             |
| `Weapon.java`            | Defines a weapon item, providing damage increase to characters who equip it.                                                                  |
| `World.java`             | Manages the overall game world, containing different spaces and areas where gameplay occurs.                                                  |

---

## Notes

1. **Visualization**: Added visualization for game title, map, and different spaces etc.
2. **Design Pattern**: Use factory classes to generate objects like heroes, items, and monsters.
3. **Singleton InputHandler**: Uses a singleton class to handle user inputs.
4. **Customized Hero**: User can choose to customize their own hero with default attributes.
5. **Target Selection**: User can choose which enemy he wants to attack.
6. **Stuck Proof**: User can move to a new map if they are blocked by inaccessible spaces.
---

## How to Compile and Run
File Structures:
MyProject/
└── src/
    ├── Main.java
    └── data/
        ├──  Armory.txt
        └──...txt

1. Navigate to the directory `"src"` after unzipping the files.
2. Run the following commands:
   ```bash
   javac *.java
   java Main
   ```

## Input/Output Example

```
                                                              .---.
                                                              /  .  \
                                                             |\_/|   |
                                                             |   |  /|
  .----------------------------------------------------------------' |
 /  .-.                                                              |
|  /   \                                                             |
| |\_.  |                        _                                   |
|\|  | /|   /\/\   ___  _ __  ___| |_ ___ _ __                       |
| `---' |  /    \ / _ \| '_ \/ __| __/ _ \ '__|                      |
|       | / /\/\ \ (_) | | | \__ \ ||  __/ |                         |
|       | \/    \/\___/|_| |_|___/\__\___|_|                         |
|       |                                                            |
|       |   __ _ _ __   __| |   /\  /\___ _ __ ___   ___  ___        |
|       |  / _` | '_ \ / _` |  / /_/ / _ \ '__/ _ \ / _ \/ __|       |
|       | | (_| | | | | (_| | / __  /  __/ | | (_) |  __/\__ \       |
|       |  \__,_|_| |_|\__,_| \/ /_/ \___|_|  \___/ \___||___/       |
|       |                                                            | 
|       |                                                           /
|       |----------------------------------------------------------'
\       |
 \     /
  `---'

[+] Choose your party size (1-3): 2
[-] You chose a party size of 2.

[+] Choose a class for hero 1:
[-] 1. Warrior
[-] 2. Sorcerer
[-] 3. Paladin

[+] Enter your choice: 1
[-] You chose Warrior for hero 1.

[+] Would you like to choose a pre-defined hero or create a custom one?
[-] 1. Choose a hero from the list
[-] 2. Create a custom hero

[+] Enter your choice: 1
[-] You chose to select a hero from the list.

[-] Available Warrior heroes:
[-] 1. Name: Gaerdal_Ironhand, Level=1, HP=100/100, MP=100/100, STR=700, DEX=500, AGI=600
[-] 2. Name: Sehanine_Monnbow, Level=1, HP=100/100, MP=600/600, STR=700, DEX=800, AGI=500
[-] 3. Name: Muamman_Duathall, Level=1, HP=100/100, MP=300/300, STR=900, DEX=500, AGI=750

[+] Enter the index of the hero you'd like to choose: 3
[-] Muamman_Duathall has joined your party.

   _______________________________________
 / \                                      \.
|   |              World Map              |.
 \_ |                                     |.
    |  ╔═══╦═══╦═══╦═══╦═══╦═══╦═══╦═══╗  |
    |  ║ ⚔ ║ □ ║ ☠ ║ □ ║ $ ║ □ ║ □ ║ □ ║  |
    |  ╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣  |
    |  ║ $ ║ □ ║ ☠ ║ □ ║ □ ║ □ ║ $ ║ □ ║  |
    |  ╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣  |
    |  ║ $ ║ $ ║ $ ║ □ ║ □ ║ □ ║ $ ║ $ ║  |
    |  ╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣  |
    |  ║ □ ║ □ ║ $ ║ $ ║ □ ║ □ ║ $ ║ □ ║  |
    |  ╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣  |
    |  ║ ☠ ║ ☠ ║ $ ║ ☠ ║ □ ║ □ ║ □ ║ □ ║  |
    |  ╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣  |
    |  ║ ☠ ║ □ ║ $ ║ $ ║ □ ║ ☠ ║ $ ║ $ ║  |
    |  ╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣  |
    |  ║ □ ║ □ ║ $ ║ ☠ ║ ☠ ║ $ ║ □ ║ □ ║  |
    |  ╠═══╬═══╬═══╬═══╬═══╬═══╬═══╬═══╣  |
    |  ║ $ ║ $ ║ $ ║ ☠ ║ $ ║ □ ║ $ ║ $ ║  |
    |  ╚═══╩═══╩═══╩═══╩═══╩═══╩═══╩═══╝  |
    |                                     |.
    |   __________________________________|___
    |  /                                    /.
    \_/____________________________________/. 

[+] Enter command: d
[-] Moved right.
[-] Your teams spot no sign of monsters in this area...

[+] Enter command: s
[-] Moved down.
[-] You encountered monsters!
[-] Natsunomeryu Lv: 1 has spawned.
[-] Blinky Lv: 1 has spawned.

[-] Battle begins!

[-] --- Battle Status ---
[-] Heroes:
[-] Muamman_Duathall - HP: 100/100, Mana: 300/300
[-] Monsters:
[-] Natsunomeryu - HP: 100
[-] Blinky - HP: 100
[-] ----------------------

[+] Muamman_Duathall's turn! Choose an action:
[-] 1. Attack
[-] 2. Cast Spell
[-] 3. Use Potion
[-] 4. Equip Item

[+] Enter your choice: 1
[-] Select a monster to attack:
[-] 1. Natsunomeryu
[-] 2. Blinky

[+] Enter monster number: 1
[-] Muamman_Duathall attacked Natsunomeryu for 45 damage.
[-] Natsunomeryu's remaining HP: 55

```
