# Legends of Valor - Project Design Document

## Table of Contents
1. [Introduction](#introduction)
2. [Project Architecture](#project-architecture)
3. [Design Patterns](#design-patterns)
4. [Class Structure](#class-structure)
5. [Game Mechanics Implementation](#game-mechanics-implementation)
6. [Data Management](#data-management)
7. [User Interface Design](#user-interface-design)
8. [Technical Challenges](#technical-challenges)
9. [Future Improvements](#future-improvements)

## Introduction

### Project Overview
Legends of Valor is a turn-based RPG inspired by MOBA games, implemented in Java as a console application. The game features a team of heroes battling against monsters on a grid-based map with special terrain features and items.

### Design Goals
1. Create an engaging, replayable game experience
2. Implement proper object-oriented design with clean separation of concerns
3. Ensure maintainability and extensibility of the codebase
4. Create a user-friendly console interface
5. Support different play styles through variety in hero classes and items

### Target Platform
The game is designed for any platform that supports Java 8 or higher, with primary testing on Windows and Linux environments. The console-based nature of the game ensures compatibility across different operating systems.

## Project Architecture

### High-Level Architecture
The project uses a modular architecture where each component handles a specific game aspect:

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Game Engine   │────▶│   World Model   │────▶│  Combat System  │
└─────────────────┘     └─────────────────┘     └─────────────────┘
         │                      │                        │
         ▼                      ▼                        ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   User Input    │     │  Character Mgmt │     │  Item/Inventory │
└─────────────────┘     └─────────────────┘     └─────────────────┘
         │                      │                        │
         └──────────────────────┼────────────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │    Game UI      │
                       └─────────────────┘
```

### Core Components

1. **Game Engine**: Controls the overall game flow and coordinates between other components
   - Manages rounds and turns
   - Handles game initialization and termination
   - Tracks win/loss conditions

2. **World Model**: Manages the game map and character positioning
   - Grid representation of the game world
   - Special terrain and spaces
   - Character movement logic

3. **Character System**: Manages heroes and monsters
   - Character attributes and statistics
   - Level-up mechanics
   - Experience and gold management

4. **Combat System**: Handles all aspects of combat
   - Attack mechanics
   - Spell casting
   - Damage calculation
   - Critical hit system

5. **Item System**: Manages items and inventory
   - Item creation and attributes
   - Inventory management
   - Market buying/selling logic

6. **User Interface**: Handles all user interaction
   - Display of game map and information
   - Command interpretation
   - Menu systems

## Design Patterns

### Factory Pattern
Used extensively for creating game objects like heroes, monsters, and items:

- `HeroFactory`: Creates hero instances based on class and attributes
- `MonsterFactory`: Creates monsters with appropriate attributes for current game level
- `ItemFactory`: Creates various item types (weapons, armor, potions, spells)

The factory pattern allows for centralized object creation with proper initialization and validation.

### Singleton Pattern
Applied to components that should have exactly one instance:

- `InputHandler`: Manages all user input with a single scanner instance
- `GameManager`: Controls core game logic and state

### Strategy Pattern
Used for different behaviors that can be selected at runtime:

- `SpellSystem`: Different spell types implement different effects
- `TerrainEffectHandler`: Different terrain types provide different effects

### Observer Pattern
Used for event notification:

- Combat events notify interested components about attacks, damage, and defeats
- Round changes trigger monster spawning and other time-based events

### Command Pattern
Used for handling user input and actions:

- Each command (move, attack, use item, etc.) is encapsulated in a separate command object
- Commands are executed based on user input

## Class Structure

### Core Game Classes

#### `Main`
Entry point for the application. Initializes the game and starts game loop.

#### `Game`
Manages the overall game state and flow.
- Initializes game components
- Manages game loop
- Tracks rounds

#### `GameManager`
Singleton that coordinates various game systems.
- Controls turns
- Manages difficulty settings
- Coordinates between systems

#### `GameUI`
Handles displaying information to the user.
- Renders game map
- Displays character info
- Shows combat messages

#### `InputHandler`
Singleton that processes all user input.
- Gets commands from user
- Validates input
- Converts input to game actions

### World and Map Classes

#### `World`
Base class for game worlds.
- Contains grid of spaces
- Manages basic movement

#### `lovWorld`
MOBA-specific world implementation.
- Implements lanes
- Contains specialized terrain

#### `GridManager`
Manages the grid structure of the world.
- Creates and maintains grid
- Places terrain features
- Supports spatial queries

#### `Space`
Abstract base class for grid cells.
- Defines common space properties
- Tracks occupants

##### Space Subclasses
- `CommonSpace`: Basic traversable space
- `Market`: Space where heroes can buy/sell items
- `InaccessibleSpace`: Spaces that cannot be traversed
- `NexusSpace`: Starting points for heroes/monsters
- Various terrain spaces (Bush, Cave, Koulou, etc.)

#### `MovementSystem`
Handles character movement logic.
- Validates moves
- Processes teleportation
- Handles recall to nexus

### Character Classes

#### `Character`
Abstract base class for all game characters.
- Defines common attributes (name, level)
- Provides base functionality

#### `Hero`
Represents player-controlled characters.
- Tracks hero-specific attributes
- Manages experience and leveling
- Handles equipment

#### `Monster`
Represents enemy characters.
- Defines monster behaviors
- Tracks monster-specific stats

#### `HeroFactory`
Creates and initializes hero instances.
- Loads hero data from files
- Creates heroes with appropriate attributes
- Manages custom hero creation

#### `MonsterFactory`
Creates monster instances.
- Scales monsters based on level
- Creates appropriate monster types

### Combat System

#### `CombatSystem`
Manages combat encounters.
- Processes attacks
- Calculates damage
- Determines hit/miss outcomes

#### `SpellSystem`
Handles spell casting mechanics.
- Manages different spell types
- Calculates spell effects
- Processes mana costs

#### `CombatLogger`
Records and displays combat events.
- Shows attack results
- Tracks damage and effects
- Provides battle summary

### Item System

#### `Item`
Base class for all items.
- Defines common item properties
- Implements Buyable interface

#### Item Subclasses
- `Weapon`: Increases attack damage
- `Armor`: Reduces damage taken
- `Potion`: One-use consumable items
- `Spell`: Magical attacks with special effects

#### `Inventory`
Manages hero inventories.
- Tracks owned items
- Handles equipping/unequipping
- Manages inventory capacity

#### `Market`
Allows heroes to buy and sell items.
- Tracks available items
- Processes transactions
- Displays available merchandise

## Game Mechanics Implementation

### Character Movement
Movement is restricted to adjacent cells in cardinal directions (up, down, left, right):
```java
protected int[] calculateNewPosition(int row, int col, String direction) {
    int newRow = row;
    int newCol = col;
    String dir = direction.toLowerCase();

    switch (dir) {
        case "w": newRow = row - 1; break;
        case "s": newRow = row + 1; break;
        case "a": newCol = col - 1; break;
        case "d": newCol = col + 1; break;
        default: return null; // Invalid direction
    }

    return new int[] {newRow, newCol};
}
```

### Combat System
The combat system uses a combination of attributes to determine outcomes:

1. **Attack Damage Calculation**:
   ```
   Base damage = (Strength + Weapon damage) * 0.05
   ```

2. **Damage Reduction**:
   ```
   Actual damage = Base damage * (1 - (Defense / 1000))
   ```

3. **Dodge Chance**:
   ```
   Dodge probability = Agility / 10000
   ```

4. **Critical Hit System**:
   ```
   Critical chance = Dexterity / 10000
   Critical damage = Base damage * 2
   ```

### Leveling System
Heroes gain experience from defeating monsters:
- Level up occurs when XP reaches threshold: `Level * 10`
- Upon level up:
  - All attributes increase by 5%
  - HP and Mana fully restored
  - Level-up message displayed

### Spell Effects
Different spell types have unique effects:
- **Fire Spells**: Deal additional damage
- **Ice Spells**: Reduce target's damage by 10% for 2 rounds
- **Lightning Spells**: Reduce target's dodge chance by 10% for 2 rounds

### Terrain Effects
Special terrain provides temporary attribute bonuses while a hero occupies the space:
- **Bush**: +10% Dexterity
- **Cave**: +10% Agility
- **Koulou**: +10% Strength

## Data Management

### File Structure
Game data is stored in text files located in the `data/` directory:
- `Armory.txt`: Armor items
- `Dragons.txt`, `Exoskeletons.txt`, `Spirits.txt`: Monster data
- `Warriors.txt`, `Sorcerers.txt`, `Paladins.txt`: Hero data
- `Weaponry.txt`: Weapon items
- `FireSpells.txt`, `IceSpells.txt`, `LightningSpells.txt`: Spell data
- `Potions.txt`: Potion items

### Data Loading
The game uses a robust file loading system via `FilePathHelper` that attempts multiple paths:
```java
public static String getDataFilePath(String filename) {
    // Try src/data/ directory
    String directPath = SRC_DIRECTORY + File.separator + DATA_DIRECTORY + File.separator + filename;
    if (new File(directPath).exists()) {
        return directPath;
    }
    
    // Try data/ directory in current working directory
    String currentDirPath = DATA_DIRECTORY + File.separator + filename;
    if (new File(currentDirPath).exists()) {
        return currentDirPath;
    }
    
    // Try other relative paths...
    // ...
}
```

### Data Format
Data files use a space-separated format with each line representing one item/character:
```
Name Level/Cost Attribute1 Attribute2 ...
```

Example for weapons (`Weaponry.txt`):
```
Sword 500 1 800 1
Bow 300 2 500 2
Axe 550 6 850 1
```
Format: `Name Cost LevelRequired Damage HandsRequired`

## User Interface Design

### Console-Based UI
The game uses ANSI escape codes for colored output in the terminal:
```java
// Color codes for visual clarity
public static final String ANSI_RED = "\u001B[31m";
public static final String ANSI_GREEN = "\u001B[32m";
public static final String ANSI_BLUE = "\u001B[34m";
public static final String ANSI_YELLOW = "\u001B[33m";
public static final String ANSI_RESET = "\u001B[0m";
```

### Map Representation
The map is displayed using ASCII characters with color coding:
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
...
```

### Menu System
The game uses numbered menus for selection:
```
1. Attack a monster
2. Cast a spell
3. Use a potion
4. Equip weapon/armor
5. View inventory
6. Move
7. Teleport
8. Recall to nexus
9. Quit game
```

## Technical Challenges

### Cross-Platform Compatibility
To ensure the game runs consistently across different operating systems, several challenges were addressed:

1. **File Path Handling**:
   - Implemented the `FilePathHelper` class to handle different path separators
   - Supports multiple potential data locations

2. **Character Encoding**:
   - Some terminals don't support Unicode characters
   - Implemented ASCII fallbacks for map display
   - Using simple box-drawing characters for boundaries

3. **Terminal Size Constraints**:
   - Designed UI to work with minimum 80x24 terminal size
   - Implemented line wrapping for longer messages

### Performance Optimizations

1. **Resource Management**:
   - Single Scanner instance via Singleton pattern
   - Reuse of objects where possible

2. **Algorithmic Efficiency**:
   - Optimized combat calculations
   - Efficient path finding for monster movement

### Code Maintainability

1. **Separation of Concerns**:
   - Clear division between game logic and UI
   - Modular systems with well-defined interfaces

2. **Robust Error Handling**:
   - Graceful handling of invalid user input
   - Recovery from file loading errors

## Future Improvements

### Gameplay Enhancements
1. **Team Composition Effects**: Bonuses for specific hero team combinations
2. **Weather Effects**: Random weather events affecting gameplay
3. **Quest System**: Optional objectives for additional rewards
4. **Multiple Maps**: Different map layouts for variety

### Technical Enhancements
1. **Save/Load System**: Ability to save game progress
2. **Configuration System**: User-configurable game settings
3. **Replays**: Record and replay gameplay sessions
4. **Achievement System**: Track player accomplishments

### UI Improvements
1. **Enhanced Visualization**: More detailed character and item representations
2. **Sound Effects**: Basic audio feedback for game events
3. **Animated Text**: For dramatic effect during important game moments
4. **Mini-map**: Compact map view for larger game worlds

---

## Development History

### Version 1.0 (Current)
- Core gameplay implementation
- Three hero classes
- Basic combat system
- Market and item system
- Special terrain effects
- Treasure chest system

### Planned for Version 1.1
- Critical hit system improvements
- Additional monster types
- More varied terrain effects
- Enhanced market system
- Improved UI with better visual feedback

