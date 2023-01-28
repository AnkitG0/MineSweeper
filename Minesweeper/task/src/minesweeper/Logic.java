package minesweeper;

public class Logic {

    // Data Fields
    Game game;
    boolean result = true;
    // Constructor
    public Logic(Game game) {
        this.game = game;
        this.runGame();
    }

    // Methods
    public void runGame() {
        //System.out.println("How many mines do you want on the field?");
        game.approximateMines();
        game.printField();
        while (result) {
            game.approximateMines(); // creates a map of mine proximity.
            game.takeUserInput();
            game.printField();
            if (!game.isInProgress()) {
                result = false; // prevents the loop from running again
                System.out.println("Congratulations! You found all the mines!");
            }
            if (game.hasSteppedOnMine) {
                //game.printField();
                System.out.println("You stepped on a mine and failed!");
                result = false;
            }
        }
        System.exit(0);
    }
}
