package TestingTools;

import DataStructures.*;

public class ConsoleUI {
    public static void startingPoint() {
        Game game = new Game(2, 8, true);
        System.out.println(game);
        while (!game.isGameOver()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) { }
            game.doRound();
            System.out.println(game);
        }
    }
}
