package minesweeper;

import java.util.Arrays;
import java.util.*;
public class Game {
    // Data Fields
    char[][] visibleField, hiddenField; // hidden field stores the mines and visible field used as gui
    int mineNum;
    final int SIZE = 9;
    // Constructor
    public Game(int mineNum) {
        this.createField();
        this.mineNum = mineNum;
        this.addMines(mineNum);   
    }
    public void createField() {
        // Initializing the visible and hidden field with a 10x10 array
        visibleField = new char[SIZE][SIZE];
        hiddenField = new char[SIZE][SIZE];
        // Fill the array with '.'
        for (char[] chars : visibleField) {
            Arrays.fill(chars, '.');
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
    }
    public void approximateMines() {
        // Bruteforce Method: check individual neighboring cells each for mines. Assign the value after checking
        for (int row = 0; row < SIZE; row++) {
             for (int column = 0; column < SIZE; column++) {
                 int mineFound = 0;
                 if (hiddenField[row][column] != 'X') {
                     /* Logic made up of two conditions. One makes sure ArrayOutOfBounds exception doesn't occur.
                        Second condition looks for mine
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
                 // Replacing the char with mineFound
                 if (mineFound > 0) {
                     visibleField[row][column] = (char) (mineFound + '0');
                 }
             }
         }
    }
}
