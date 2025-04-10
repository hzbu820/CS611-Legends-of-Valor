/**
 *  Manages the overall game world, containing different spaces and areas where gameplay occurs.
 *  This class include methods to initialize and update the world map, handles and validate character movement across spaces.
 */

import java.util.Random;

public class World {
    protected final int size;
    protected final int row;
    protected final int col;
    protected Space[][] grid;
    protected int heroRow;
    protected int heroCol;

    public World(int row, int col) {
        this.row = row;
        this.col = col;
        this.size = row * col;
        this.grid = new Space[row][col];
        initializeSpaces();
        placeHeroes();
    }
    //Randomize space distribution
    private void initializeSpaces() {
        Random random = new Random();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int spaceType = random.nextInt(100);  // Randomly assign space type
                if (spaceType < 20) {
                    grid[i][j] = new InaccessibleSpace();
                } else if (spaceType < 50) {
                    grid[i][j] = new Market();
                } else {
                    grid[i][j] = new CommonSpace();
                }
            }
        }
    }

    public void displayMap() {
        System.out.println("   _______________________________________");
        System.out.println(" / \\                                      \\.");
        System.out.println("|   |              World Map              |.");
        System.out.println(" \\_ |                                     |.");

        String doubleLine = "║";
        String topBorder = "╔═══╦";
        String middleBorder = "╠═══╬";
        String bottomBorder = "╚═══╩";
        for (int j = 0; j < col - 2; j++) {
            topBorder += "═══╦";
            middleBorder += "═══╬";
            bottomBorder += "═══╩";
        }
        topBorder += "═══╗  |";
        middleBorder += "═══╣  |";
        bottomBorder += "═══╝  |";

        // Print the top border of the map grid
        System.out.println("    |  " + topBorder);

        for (int i = 0; i < row; i++) {
            System.out.print("    |  " + doubleLine); // Left border
            for (int j = 0; j < col; j++) {
                if (i == heroRow && j == heroCol) {
                    System.out.print(" ⚔ " + doubleLine);  // Hero's current position
                } else {
                    System.out.print(" " + grid[i][j].getSymbol() + " " + doubleLine); // Display symbol
                }
            }
            System.out.print("  |");
            System.out.println();

            // middle or bottom border based on row
            if (i < row - 1) {
                System.out.println("    |  " + middleBorder);
            } else {
                System.out.println("    |  " + bottomBorder);
            }
        }

        System.out.println("    |                                     |.");
        System.out.println("    |   __________________________________|___");
        System.out.println("    |  /                                    /.");
        System.out.println("    \\_/____________________________________/. ");
    }

    private void placeHeroes() {
        // Place heroes in a starting accessible position
        heroRow = 0;
        heroCol = 0;

        while (grid[heroRow][heroCol] instanceof InaccessibleSpace) {
            grid[heroRow][heroCol] = new CommonSpace();  // Ensure starting position is accessible
        }
    }

    protected int[] calculateNewPosition(int row, int col, String direction) {
        int newRow = row;
        int newCol = col;
        String dir = direction.toLowerCase();

        switch (dir) {
            case "w":
                newRow = row - 1;
                break;
            case "s":
                newRow = row + 1;
                break;
            case "a":
                newCol = col - 1;
                break;
            case "d":
                newCol = col + 1;
                break;
            default:
                System.out.println("Invalid direction.");
                return null; // Indicate invalid direction
        }

        return new int[] {newRow, newCol};
    }

    public boolean moveParty(String direction) {
        int[] newPosition = calculateNewPosition(heroRow, heroCol, direction);
        if (newPosition == null) {
            return false; // Invalid direction
        }

        int newRow = newPosition[0];
        int newCol = newPosition[1];

        if (isValidMove(newRow, newCol)) {
            heroRow = newRow;
            heroCol = newCol;
            System.out.println("Moved " + direction + ".");
            return true;
        } else {
            return false;
        }
    }

    protected boolean isValidMoveNoPrint(int row, int col) {
        return row >= 0 && row < this.row && col >= 0 && col < this.col && !(grid[row][col] instanceof InaccessibleSpace);
    }

    protected boolean isValidMove(int row, int col) {
        // Check if the target row and col are within bounds
        if (row < 0 || row >= this.row || col < 0 || col >= this.col) {
           System.out.println("You have reached the edge of the world...(Out of bound)");
            return false;
        }
        // Check if the target cell is an inaccessible space
        else if (grid[row][col] instanceof InaccessibleSpace) {
            System.out.println("That area is infested by deadly poisonous gas. Best not go forward...(InaccessibleSpace)");
            return false;
        }
        // Move is valid
        return true;
    }

    public boolean isInCommonSpace() {
        return grid[heroRow][heroCol] instanceof CommonSpace;
    }

    public boolean isInMarketSpace() {
        return grid[heroRow][heroCol] instanceof Market;
    }

    public Market getMarket() {
        if (isInMarketSpace()) {
            return (Market) grid[heroRow][heroCol];
        } else {
            return null;
        }
    }



}

