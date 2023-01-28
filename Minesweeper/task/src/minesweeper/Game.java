package minesweeper;

import java.util.Arrays;
import java.util.*;
public class Game {
    // Data Fields
    char[][] visibleField, hiddenField; // hidden field stores the mines and visible field is used as gui
    int mineNum, numMinesMarked = 0;
    final int SIZE = 9;
    boolean hasSteppedOnMine = false;

    Logic logic;
    Scanner scanner = new Scanner(System.in);
    // Constructor
    public Game(int mineNum) {
        this.createField();
        this.mineNum = mineNum;
        this.addMines(mineNum);
        logic = new Logic(this); // allows to send game object to be used in logic
    }
    public void takeUserInput() {
        System.out.println("Set/unset mines marks or claim a cell as free:");
        String input = scanner.nextLine();
        String[] userInput = input.split(" "); // splits the command by space and stores each unit into array
        int userColumn = Integer.parseInt(userInput[0]) - 1; // converts the first string into int. This is the user column number
        // System.out.println(userColumn);
        int userRow = Integer.parseInt(userInput[1]) - 1; // converts the second string into int. This is the user row number
        String userMark = userInput[2]; // stores 'free/mine' user guess
        // Taking these input and marking the field
        markCell(userColumn, userRow, userMark);
    }
    public void createField() {
        // Initializing the visible and hidden field with a 10x10 array
        visibleField = new char[SIZE][SIZE];
        hiddenField = new char[SIZE][SIZE];
        // Fill the array with '.'
        for (char[] chars : visibleField) {
            Arrays.fill(chars, '.');
        }
        for (char[] chars : hiddenField) {
            Arrays.fill(chars, '/');
        }
    }
    public void addMines(int mineNum) {
        // Randomizing the mines in the game
        // Take a random value of row and column and fill the location with X. Fill the hidden field.
        Random rand = new Random();
        for (int i = 0; i < mineNum; i++) {
            int row = rand.nextInt(SIZE);
            int column = rand.nextInt(SIZE);
            // Check if the location already contains an 'X'
            while(hiddenField[row][column] == 'X') {
                // If it does, generate new random locations. The while loop terminates if location is not occupied by 'X'
                row = rand.nextInt(SIZE);
                column = rand.nextInt(SIZE);
            }
            hiddenField[row][column] = 'X';
        }
    }
    public void printField() {
        System.out.println(" |123456789|");
        System.out.println("-|---------|");
        int i = 1;
        // Using for loop to print field
        for (char[] chars: visibleField) {
            System.out.print(i++ + "|");
            for (char ch: chars) {
                System.out.print(ch);
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("-|---------|");
//        i = 1;
//        for (char[] chars: hiddenField) {
//            System.out.print(i++ + "|");
//            for (char ch: chars) {
//                System.out.print(ch);
//            }
//            System.out.print("|");
//            System.out.println();
//        }
//        System.out.println("-|---------|");
    }
    public void approximateMines() {
        // Used to hint the number of mines touching a cell
        // Bruteforce Method: check individual neighboring cells each for mines. Assign the value after checking
        for (int row = 0; row < SIZE; row++) {
             for (int column = 0; column < SIZE; column++) {
                 int mineFound = 0;
                 if (hiddenField[row][column] != 'X') {
                     /* Logic is made up of two conditions. One makes sure ArrayOutOfBounds exception doesn't occur.
                        Second condition looks for mine.
                      */
                     // Check North
                     if (column > 0 && hiddenField[row][column - 1] == 'X') ++mineFound;
                     // Check West
                     if (row > 0 && hiddenField[row - 1][column] == 'X') ++mineFound;
                     // Check South
                     if (column < (SIZE - 1) && hiddenField[row][column + 1] == 'X') ++mineFound;
                     // Check East
                     if (row < (SIZE - 1) && hiddenField[row + 1][column] == 'X') ++mineFound;
                     // Check NorthWest
                     if (row > 0 && column > 0 && hiddenField[row - 1][column - 1] == 'X') ++mineFound;
                     // Check NorthEast
                     if (row > 0 && column < (SIZE - 1) && hiddenField[row - 1][column + 1] == 'X') ++mineFound;
                     // Check SouthWest
                     if (row < (SIZE - 1) && column > 0 && hiddenField[row + 1][column - 1] == 'X') ++mineFound;
                     // Check SouthEast
                     if (column < (SIZE - 1) && row < (SIZE - 1) && hiddenField[row + 1][column + 1] == 'X') ++mineFound;
                 }
                 // Replacing the element with mineFound
                 if (mineFound > 0) {
                     // All the approximations are hidden. Will be revealed to user in visibleField as game progresses
                     hiddenField[row][column] = (char) (mineFound + '0');
                 }
             }
         }
    }
    public void markCell(int y, int x, String userMark) {
        // Check if the location has a number. If not, add an * at the location. If location is marked, un-mark it.
        // Mark/unmark a mine
        if ("mine".equals(userMark) && visibleField[x][y] == '*') {
            visibleField[x][y] = '.';
        } else if ("mine".equals(userMark) && visibleField[x][y] != '*'){
            visibleField[x][y] = '*';
        }
        // Explore a cell with "free" command
        if ("free".equals(userMark)) {
            // Case 1: cell is empty but has mines around it: Then show the number of mines it is touching
            if (hiddenField[x][y] != '/' && hiddenField[x][y] != 'X') {
                visibleField[x][y] = hiddenField[x][y];
            }
            // Case 2: cell is empty and has no mines around it (that is, it contains '/'):
            /* If the cell is empty and has no mines around, all the neighbors, including the marked ones will be
            explored automatically. Also, if next to the explored cell there is another empty one with no mines around,
            all its neighboring cells should be explored as well, and so on, until no more can be explored automatically.
            */
            // Use floodFill algorithm to accomplish this
            if (hiddenField[x][y] == '/') floodFill(y, x);
            // Case 3: User steps on a mine
            if (hiddenField[x][y] == 'X') {
                visibleField[x][y] = 'X';
                hasSteppedOnMine = true;
            }
        }

//        if (visibleField[y][x] != '.' && visibleField[y][x] != '*') {
//            System.out.println("There is a number here!");
//            markCell(); // Rerun method to ask user for different coordinates
//        } else if (visibleField[y][x] == '*') {
//            visibleField[y][x] = '.';
//            --numMinesMarked;
//        } else {
//            visibleField[y][x] = '*';
//            ++numMinesMarked;
//        }

    }

    private void checkNeighbors(int col, int row) {
        for (int rowOff = -1; rowOff <= 1; rowOff++) {
            for (int colOff = -1; colOff <= 1; colOff++) {
                int i = row + rowOff;
                int j = col + colOff;
                if (i >= 0 && j >= 0 && i < 9 && j < 9) {
                    if (hiddenField[i][j] == '/' && visibleField[i][j] != '/') {
                        visibleField[i][j] = '/';
                        checkNeighbors(j, i);
                    }
                    else if (hiddenField[i][j] != '/' && hiddenField[i][j] != 'X') {
                        visibleField[i][j] = hiddenField[i][j];
                    }
                }
            }
        }
    }
    private void floodFill(int y, int x) {
        visibleField[x][y] = hiddenField[x][y];
        checkNeighbors(y, x);
    }

    public boolean isInProgress() {
        // Check whether all fields that contain mines have been identified
        // Condition 1: Game continues if number of marked cells are not equal to number of mines
        if (numMinesMarked != mineNum) {
            return true;
        }
        // Condition 2: If above is not true, check if correct locations have been identified
        int score = 0;
        if (numMinesMarked == mineNum) {
            for (int row = 0; row < visibleField.length; row++) {
                for (int col = 0; col < visibleField[row].length; col++) {
                    if (visibleField[row][col] == '*' && hiddenField[row][col] == 'X') {
                        score++;
                    }
                }
            }
        }
        // checks if all mines are marked
        return score != mineNum;
    }

}
