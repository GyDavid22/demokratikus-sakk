package GUI;

import javax.swing.*;

import DataStructures.Game;

public class GameTab {
    private JPanel game;
    private GameButtons gButtons;
    private GameField field;

    GameTab() {
        this.game = new JPanel();
        this.game.setLayout(new BoxLayout(this.game, BoxLayout.Y_AXIS));

        this.field = new GameField();
        this.game.add(this.field.getPanel());

        this.gButtons = new GameButtons();
        this.game.add(this.gButtons.getPanel());
    }

    JPanel getPanel() {
        return this.game;
    }

    void eventLoop(Game toDraw) {
        this.field.refresh(toDraw);
        this.gButtons.eventLoop();
    }
}
