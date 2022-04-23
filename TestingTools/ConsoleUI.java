package TestingTools;

import DataStructures.*;

public class ConsoleUI {
    public static void startingPoint() {
        Game game = new Game();
        System.out.println(game);
        while (!game.isGameOver()) {
            try {
                Thread.sleep(000);
            } catch (InterruptedException e) { }
            game.doRound();
            System.out.println(game);
        }
    }
}
