# Legends of Valor


## Project Introduction

Legends of Valor is an advanced, console-based role-playing game that brings the excitement of MOBA (Multiplayer Online Battle Arena) gameplay to a text-based environment. 
This project demonstrates object-oriented design principles through an engaging fantasy world where heroes and monsters battle for supremacy.

The game combines strategic combat, character progression, and resource management in a grid-based world filled with diverse terrain types and interactive elements. Players control a team of three heroes—each with unique abilities and attributes—as they navigate the world, collect treasures, defeat monsters, and ultimately attempt to reach the enemy's base.

What sets Legends of Valor apart is its deep integration of terrain effects, market economies, and tactical decision-making, all within the constraints of a text-based interface. The game showcases how complex gameplay systems can be implemented effectively even without graphical elements.

### Key Features
- **Team-based Gameplay**: Control three unique heroes, each with different strengths and abilities
- **Dynamic Map**: Interact with special terrain types that provide bonuses and challenges
- **Progressive Difficulty**: Monster difficulty increases over time
- **Economy System**: Buy and sell items at markets to improve your heroes
- **Tactical Combat**: Turn-based combat system with various attack options, including spells
- **Character Development**: Level up heroes to increase their attributes and combat effectiveness
- **Special Encounters**: Discover treasure chests and mystery spaces as you explore
- **Critical Hit System**: Heroes can land devastating blows based on dexterity
- **Interactive Environment**: Remove obstacles, gain terrain bonuses, and discover hidden treasures
- **Lane-based Strategy**: Choose hero positions wisely to control territory
- **Cross-Platform Compatibility**: Play on any system with Java support

## Gameplay Demo

### Game Initiation and Setup

When you first launch Legends of Valor, you're greeted with a welcome message and information about the data directory being used:

```
Welcome to Legends of Valor!
A MOBA-inspired adventure awaits!

Using data directory: src/data
```

The game immediately immerses you in its world by asking you to choose a difficulty level, which affects how frequently new monsters spawn:

```
1. Easy (New monsters spawn every 6 rounds)
2. Medium (New monsters spawn every 4 rounds)
3. Hard (New monsters spawn every 2 rounds)
Enter your choice (1-3): 1
Difficulty set to Easy
```

### Detailed Hero Selection Process

After setting the difficulty, you'll embark on the critical task of building your team of three heroes. Each hero will be assigned to one of the three lanes on the map, so your choices here will significantly impact gameplay:

```
Let's select your three heroes!

Selecting Hero 1 (for lane 1):
Choose a hero class:
1. Warrior (High Strength and Agility)
2. Sorcerer (High Mana and Dexterity)
3. Paladin (High Strength and Dexterity)
Enter your choice (1-3): 1
```

For each hero, you can either choose from pre-defined characters with established attributes or create your own custom hero:

```
Would you like to choose a pre-defined hero or create a custom one?
1. Choose a hero from the list
2. Create a custom hero
Enter choice: 1
```

When selecting a pre-defined hero, you'll see a list of available characters of your chosen class, complete with their attributes:

```
Available Warrior heroes:
1. Name: Gaerdal_Ironhand, Level=1, HP=100/100, MP=100/100, STR=700, DEX=500, AGI=600
2. Name: Sehanine_Monnbow, Level=1, HP=100/100, MP=600/600, STR=700, DEX=800, AGI=500
3. Name: Muamman_Duathall, Level=1, HP=100/100, MP=300/300, STR=900, DEX=500, AGI=750
Enter the index of the hero you'd like to choose: 1

Hero Gaerdal_Ironhand selected for lane 1!
```

This process repeats for all three heroes, allowing you to create a balanced team or specialize in certain strategies:

```
Selecting Hero 2 (for lane 2):
Choose a hero class:
1. Warrior (High Strength and Agility)
2. Sorcerer (High Mana and Dexterity)
3. Paladin (High Strength and Dexterity)
Enter your choice (1-3): 2

Would you like to choose a pre-defined hero or create a custom one?
1. Choose a hero from the list
2. Create a custom hero
Enter choice: 1

Available Sorcerer heroes:
1. Name: Rillifane_Rallathil, Level=1, HP=100/100, MP=1300/1300, STR=550, DEX=900, AGI=500
2. Name: Segojan_Earthcaller, Level=1, HP=100/100, MP=900/900, STR=650, DEX=500, AGI=800
3. Name: Reign_Havoc, Level=1, HP=100/100, MP=800/800, STR=800, DEX=800, AGI=800
Enter the index of the hero you'd like to choose: 1

Hero Rillifane_Rallathil selected for lane 2!

Selecting Hero 3 (for lane 3):
Choose a hero class:
1. Warrior (High Strength and Agility)
2. Sorcerer (High Mana and Dexterity)
3. Paladin (High Strength and Dexterity)
Enter your choice (1-3): 3

Would you like to choose a pre-defined hero or create a custom one?
1. Choose a hero from the list
2. Create a custom hero
Enter choice: 1

Available Paladin heroes:
1. Name: Parzival, Level=1, HP=100/100, MP=300/300, STR=750, DEX=700, AGI=650
2. Name: Sehanine_Moonbow, Level=1, HP=100/100, MP=300/300, STR=750, DEX=700, AGI=700
3. Name: Skoraeus_Stonebones, Level=1, HP=100/100, MP=250/250, STR=650, DEX=600, AGI=350
Enter the index of the hero you'd like to choose: 2

Hero Sehanine_Moonbow selected for lane 3!
```

### The Game World

After hero selection, the game displays the map—a rich 8x8 grid world with various terrain types, special spaces, and starting positions:

```
+--------+--------+--------+--------+--------+--------+--------+--------+
|  MN    |  M1    |  ##    |  MN    |  M2    |  ##     |  MN    |  M3   |
+--------+--------+--------+--------+--------+--------+--------+--------+
|  P     |  ?     |  ##    |  K     |  P     |  ##     |  ?     |  O    |
+--------+--------+--------+--------+--------+--------+--------+--------+
|  C     |  ?     |  ##    |  P     |  P     |  ##     |  P     |  P    |
+--------+--------+--------+--------+--------+--------+--------+--------+
|  K     |  C     |  ##    |  P     |  K     |  ##     |  O     |  P    |
+--------+--------+--------+--------+--------+--------+--------+--------+
|  P     |  P     |  ##    |  C     |  C     |  ##     |  B     |  ?    |
+--------+--------+--------+--------+--------+--------+--------+--------+
|  P     |  B     |  ##    |  C     |  P     |  ##     |  ?     |  B    |
+--------+--------+--------+--------+--------+--------+--------+--------+
|  C     |  K     |  ##    |  P     |  P     |  ##     |  P     |  P    |
+--------+--------+--------+--------+--------+--------+--------+--------+
|  HN    |  H1    |  ##    |  HN    |  H2    |  ##     |  HN    |  H3   |
+--------+--------+--------+--------+--------+--------+--------+--------+

MAP LEGEND:
HN - Hero Nexus | MN - Monster Nexus | ## - Inaccessible Space
P - Plain Space | B - Bush Space (+Dexterity) | C - Cave Space (+Agility)
K - Koulou Space (+Strength) | O - Obstacle Space (Can be removed)
H1/H2/H3 - Heroes | M1/M2/M3 - Monsters
? - Mystery Space | $ - Treasure Chest | TC - Opened Treasure Chest
```

This map shows:
- Three lanes connecting Hero Nexus (HN) at the bottom to Monster Nexus (MN) at the top
- Various terrain types offering strategic bonuses (Bush, Cave, Koulou)
- Obstacles (O) that can be destroyed to open new paths
- Special interactive spaces like Treasure Chests ($) and Mystery Spaces (?)
- Inaccessible areas (##) that define the boundaries of each lane

### Hero Turn Options and Movement

When it's a hero's turn, you'll see a menu of available actions:

```
Round 1: Hero Gaerdal_Ironhand's turn (Lane 1)
Hero Stats: Level 1 | HP: 100/100 | MP: 100/100 | XP: 0 | Gold: 500

1. Attack a monster
2. Cast a spell
3. Use a potion
4. View/Change equipment
5. View information
6. Move
7. Teleport
8. Recall to nexus
9. Quit game
Enter your choice: 6

Enter direction to move (W/A/S/D): w

Gaerdal_Ironhand moved north.
You are now on a Bush space! (+10% Dexterity)
```

The terrain effects are automatically applied as heroes move onto special spaces:

```
Terrain effect applied: Dexterity increased by 10% while on this space.
Gaerdal_Ironhand's effective Dexterity: 550
```

### Discovering Treasure and Mystery Spaces

When a hero moves onto a Treasure Chest space, they'll be rewarded with valuable items:

```
You found a treasure chest!
Opening the chest...
You found a Sword of Strength! (DMG: 700, Hands: 1)
The item has been added to your inventory.
```

Similarly, Mystery Spaces trigger random events:

```
You stepped on a Mystery Space!
Rolling the dice of fate...
You found a secret stash of 200 gold!
Your gold: 700
```

### Combat System Demonstration

When heroes encounter monsters, combat begins:

```
Hero Gaerdal_Ironhand's turn:
A Desghidorrah (Dragon) is in your lane!

1. Attack a monster
2. Cast a spell
3. Use a potion
4. View/Change equipment
5. View information
6. Move
7. Teleport
8. Recall to nexus
9. Quit game
Enter your choice: 1

Monsters in range:
1. Desghidorrah (Dragon) - Level 3, HP: 300/300
Enter the index of the monster to attack (0 to cancel): 1

Gaerdal_Ironhand attacks Desghidorrah with Sword of Strength!
CRITICAL HIT! Double damage!
Desghidorrah takes 105 damage!
Desghidorrah has 195 HP remaining.
```

The critical hit system adds excitement to combat, allowing heroes to occasionally deal devastating damage:

```
Calculating critical hit chance...
Gaerdal_Ironhand's Dexterity: 550 (including Bush space bonus)
Critical hit probability: 5.5%
Random roll: 3.2% - CRITICAL HIT LANDED!
Damage doubled from 52.5 to 105!
```

### Spell Casting

Heroes can cast spells with various effects:

```
Hero Rillifane_Rallathil's turn:
1. Attack a monster
2. Cast a spell
3. Use a potion
4. View/Change equipment
5. View information
6. Move
7. Teleport
8. Recall to nexus
9. Quit game
Enter your choice: 2

Available Spells:
1. Flame_Tornado (Fire) - DMG: 850, Mana Cost: 300
2. Breeze (Ice) - DMG: 650, Mana Cost: 250
Enter the spell index to cast (0 to cancel): 1

Target monsters:
1. Desghidorrah (Dragon) - Level 3, HP: 195/300
Enter the index of the monster to target (0 to cancel): 1

Rillifane_Rallathil casts Flame_Tornado on Desghidorrah!
The spell does 85 damage!
Desghidorrah has 110 HP remaining.
Fire spell effect: Additional 8 burn damage applied!
Desghidorrah now has 102 HP.
```

### Market Interaction

When a hero is on a market space (such as a Nexus), they can buy and sell items:

```
Welcome to the Market!
Merchant: Everything's for sale, my friend. Everything. If I had a brother, I'd sell him in a second.

1. Buy an item
2. Sell an item
3. View learned spells
4. Exit market
Enter choice: 1

What type of item would you like to buy?
1. Weapons
2. Armor
3. Potions
4. Spells
5. Back to main menu
Enter your choice: 1

Weapons available for sale:

Level 1 Weapons:
1: Sword (DMG: 800, Hands: 1) (Cost: 500 gold, Lvl Req: 1)
2: Bow (DMG: 500, Hands: 2) (Cost: 300 gold, Lvl Req: 2)
3: Axe (DMG: 550, Hands: 1) (Cost: 650 gold, Lvl Req: 5)

Enter the index of the item you want to buy (0 to cancel): 1
You have successfully bought Sword!
You now have 200 gold.
```

### Monster Turns and AI

After all heroes have taken their turns, monsters move and attack automatically:

```
Monster Turn: Desghidorrah (Dragon)
Desghidorrah moves toward the nearest hero...
Desghidorrah attacks Gaerdal_Ironhand!
Gaerdal_Ironhand takes 45 damage!
Gaerdal_Ironhand has 55 HP remaining.
```

### Leveling Up

As heroes defeat monsters, they gain experience and eventually level up:

```
Desghidorrah has been defeated!
Experience gained: 8
Gold reward: 300 (100 gold per hero)

Gaerdal_Ironhand has enough experience to level up!
LEVEL UP! Gaerdal_Ironhand is now level 2!
All attributes increased by 5%!
Health and Mana fully restored!
```

## Implementation Highlights

### Critical Hit System
We've implemented an advanced critical hit system that allows heroes to occasionally land devastating blows:
- Critical hit chance scales with hero dexterity
- Critical hits deal double damage
- Visual feedback with "CRITICAL HIT!" message during combat
- Different hero classes have varying potential for critical hits

### Treasure Chest System
The world is filled with valuable treasures waiting to be discovered:
- Treasure chests appear as "$" symbols on the map
- Contain random valuable items when opened
- Items are scaled based on the hero's level
- Possible rewards include gold, weapons, armor, potions, or spells

### Terrain Effects System
The environment plays a crucial role in combat strategy:
- Bush spaces: +10% dexterity boost
- Cave spaces: +10% agility boost 
- Koulou spaces: +10% strength boost
- Effects apply automatically when a hero is on the space
- Strategic positioning can significantly enhance combat effectiveness

### Mystery Spaces
Adding an element of surprise to exploration:
- Appear as "?" symbols on the map
- Trigger random events when a hero steps on them
- Possible effects include health restoration, attribute boosts, gold discovery, or spawning monsters
- Adds unpredictability and excitement to gameplay

### Dynamic File Path Management
We've solved cross-platform compatibility issues with our FilePathHelper utility:
- Automatically detects the correct data file locations
- Works regardless of where the game is launched from
- Handles different OS path separators
- Makes the game easily portable between systems

### Lane-Based Movement and Combat
Our implementation restricts heroes and monsters to their respective lanes:
- Each hero is assigned to one of three lanes
- Movement is constrained within lanes but can be vertical or horizontal
- Teleport ability allows heroes to assist each other across lanes
- Monsters spawn in and remain in their assigned lanes
- Creates strategic depth with lane prioritization decisions

### Object-Oriented Design Principles
The game showcases advanced OOP techniques:
- **Encapsulation**: Each game component manages its own state and behavior
- **Inheritance**: Character types inherit from base classes (Character → Hero/Monster)
- **Polymorphism**: Different space types implement the Space interface in unique ways
- **Composition**: Complex systems like combat are composed of smaller, specialized components
- **Design Patterns**: Factory, Singleton, and Strategy patterns used throughout

## Table of Contents
1. [Installation](#installation)
2. [Running the Game](#running-the-game)
3. [Game Rules & Mechanics](#game-rules--mechanics)
4. [Controls & Interface](#controls--interface)
5. [Heroes & Character Classes](#heroes--character-classes)
6. [Items & Equipment](#items--equipment)
7. [Map & World](#map--world)
8. [Combat System](#combat-system)
9. [Troubleshooting](#troubleshooting)
10. [Credits](#credits)

## Installation

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command-line terminal or PowerShell
- Approximately 10MB free disk space

### Download and Setup
1. Download the project as a ZIP file or clone the repository:
2. Extract the ZIP file to a location of your choice if you downloaded it
3. Verify file integrity - ensure you have the following key directories:
   - `src/` - Contains all source code
   - `src/data/` - Contains game data files
   - `data/` - Duplicate game data files for flexible execution

## Running the Game

### Method 1: Using Run Scripts (Recommended)

#### Windows Users
1. Double-click the `run.bat` file in the project's root directory
   - Alternatively, open Command Prompt in the project directory:
     - Run: `run.bat`
   - If using PowerShell:
     - Run: `.\run.bat` (the `.\` prefix is required in PowerShell)

#### Mac/Linux Users
1. Open Terminal in the project directory
2. Make the script executable: `chmod +x run.sh`
3. Execute the script: `./run.sh`

### Method 2: Manual Compilation and Execution

1. Open Terminal/Command Prompt in the project directory
2. Create output directory:
   - Windows: `if not exist out mkdir out`
   - Mac/Linux: `mkdir -p out`
3. Compile the source files:
   ```
   javac -d out src/*.java
   ```
4. Run the compiled program:
   ```
   java -cp out Main
   ```

### Method 3: Using an IDE

1. Import the project into your preferred Java IDE (IntelliJ IDEA, Eclipse, NetBeans, etc.)
2. Build the project using the IDE's build functionality
3. Run the `Main` class

## Game Rules & Mechanics

### Game Objective
Lead your team of three heroes to victory by strategically battling monsters, leveling up, and ultimately reaching the Monster Nexus.

### Game Flow
1. **Initialization**: Select difficulty level and choose your three heroes
2. **Exploration**: Move your heroes across the map, collecting items and gaining experience
3. **Combat**: Battle monsters using a combination of basic attacks and special abilities
4. **Progression**: Level up heroes, purchase equipment, and prepare for tougher challenges
5. **Victory**: Win by defeating monsters and reaching their nexus

### Rounds & Turns
- Each round consists of turns for each hero and monster
- Heroes can move, attack, cast spells, use potions, or access the market
- Monsters automatically move toward heroes and attack when in range
- New monsters spawn from the Monster Nexus based on the difficulty level:
  - Easy: Every 6 rounds
  - Medium: Every 4 rounds
  - Hard: Every 2 rounds

### Win/Loss Conditions
- **Win**: At least one hero reaches the Monster Nexus
- **Loss**: All heroes are defeated simultaneously

## Controls & Interface

### Basic Controls
- **W**: Move up
- **A**: Move left
- **S**: Move down
- **D**: Move right
- **Q**: Quit game (anytime)
- **I**: View inventory (during hero's turn)
- **M**: Access market (when on market space)
- **1-9**: Select options from menus

### Map Legend
- **HN**: Hero Nexus (heroes spawn here)
- **MN**: Monster Nexus (monsters spawn here)
- **##**: Inaccessible space (cannot be traversed)
- **P**: Plain space (standard terrain)
- **B**: Bush space (provides dexterity bonus)
- **C**: Cave space (provides agility bonus)
- **K**: Koulou space (provides strength bonus)
- **O**: Obstacle (can be removed by heroes)
- **$**: Treasure chest (contains valuable items)
- **?**: Mystery space (triggers random events)
- **H1/H2/H3**: Hero positions
- **M1/M2/M3**: Monster positions

## Heroes & Character Classes

### Hero Attributes
- **Health (HP)**: Determines survival in combat
- **Mana (MP)**: Required for casting spells
- **Strength**: Increases attack damage
- **Dexterity**: Improves chance to dodge attacks and land critical hits
- **Agility**: Increases the chance to dodge attacks
- **Level**: Determines overall power and access to equipment
- **Experience**: Accumulated by defeating monsters, leads to level ups
- **Gold**: Currency for buying items

### Hero Classes
1. **Warriors**: High strength and agility
   - Excel at physical combat
   - Moderate mana pool
   - Strong against groups of weaker monsters
   - Example: Gaerdal_Ironhand (STR=700, DEX=500, AGI=600)

2. **Sorcerers**: High mana and dexterity
   - Excel at casting spells
   - Lower physical attack capability
   - Strong against monsters with high defense
   - Example: Rillifane_Rallathil (MP=1300, DEX=900, INT=750)

3. **Paladins**: High strength and dexterity
   - Balanced combat capabilities
   - Can both attack and cast spells effectively
   - Versatile for different combat situations
   - Example: Sehanine_Moonbow (STR=700, DEX=800, MP=600)

### Hero Selection
- Choose from pre-defined heroes with unique attributes
- Create custom heroes with balanced starting attributes
- Each lane should have one hero (total of three)

## Items & Equipment

### Item Types
1. **Weapons**: Increase attack damage
   - Require certain level to equip
   - Some require one hand, others require two hands
   - Example: Sword of Truth (Damage: 850, Required Hands: 1)

2. **Armor**: Reduces damage taken
   - Provides damage reduction percentage
   - Example: Guardian Angel (Damage Reduction: 1000)

3. **Potions**: One-time use consumables
   - Instantly affect hero attributes
   - Types include health, mana, strength, dexterity, and agility potions
   - Example: Healing Potion (Health: +100)

4. **Spells**: Magical attacks with special effects
   - Require mana to cast
   - Different types: Fire, Ice, Lightning
   - Example: Flame Tornado (Damage: 850, Mana Cost: 300)

### The Market
- Markets are available at spaces marked with '$'
- Buy new equipment and spells
- Sell unwanted items for gold
- Items have level requirements

## Map & World

### Map Structure
- 8x8 grid with special terrain features
- Three lanes connecting Hero Nexus and Monster Nexus
- Contains inaccessible areas, markets, and special terrain

### Terrain Types
- **Plain**: Standard terrain, no special effects
- **Bush**: Increases hero's dexterity by 10%
- **Cave**: Increases hero's agility by 10%
- **Koulou**: Increases hero's strength by 10%

### Special Features
- **Hero Nexus**: Starting point for heroes, also serves as a market
- **Monster Nexus**: Starting point for monsters
- **Obstacle**: Blocks movement but can be removed by heroes
- **Treasure Chest**: Contains random items when opened
- **Mystery Space**: Triggers random events when a hero steps on it

## Combat System

### Attack Types
1. **Regular Attack**: Basic weapon damage based on strength
2. **Spell Casting**: Magical damage plus special effects
   - Fire Spells: Additional damage
   - Ice Spells: Reduce target's damage
   - Lightning Spells: Reduce target's dodge chance

### Battle Mechanics
- Heroes can attack monsters within range
- Damage calculation considers attacker's strength and defender's defense
- Dodge chance based on defender's agility
- Critical hits (double damage) chance based on attacker's dexterity

### Special Combat Features
- **Lane-based Combat**: Heroes and monsters stay in their respective lanes
- **Teleportation**: Heroes can teleport to the lane of another hero
- **Recall**: Heroes can return to their nexus when in danger

## Troubleshooting

### Common Issues

#### File Not Found Errors
- Ensure data files are located in either `src/data/` or `data/` directories
- The game automatically searches multiple locations using the `FilePathHelper` class

#### Compilation Errors
- Verify Java is correctly installed: `java -version`
- Check Java is in your PATH environment variable
- Ensure all source files are present in the `src/` directory

#### Character Display Issues
- If you see strange characters in the map, your terminal may not support UTF-8
- The game uses ASCII alternatives for compatibility

#### Performance Issues
- The game is designed to run on most modern computers
- Close other resource-intensive applications for best performance

## Development Team Achievements

Our team has successfully implemented several enhancements and features:

- **File Path Management**: Created a robust `FilePathHelper` class that makes the game run correctly regardless of execution location
- **Cross-Platform Compatibility**: Ensured the game works on Windows, Mac, and Linux systems
- **ASCII Compatibility**: Replaced Unicode characters with ASCII alternatives for better terminal compatibility
- **Critical Hit System**: Added an exciting combat mechanic that rewards high dexterity
- **Treasure Chest System**: Implemented discoverable treasures throughout the game world
- **Mystery Spaces**: Added unpredictable spaces that trigger various events
- **Enhanced UI**: Improved color coding and readability of the game interface
- **Convenience Scripts**: Created run.bat and run.sh scripts for easy execution

