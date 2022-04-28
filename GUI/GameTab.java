package GUI;

import javax.swing.*;

public class GameTab {
    private JPanel game;
    private GameButtons gButtons;

    GameTab() {
        this.game = new JPanel();
        this.game.setLayout(new BoxLayout(this.game, BoxLayout.Y_AXIS));

        this.gButtons = new GameButtons();
        this.game.add(this.gButtons.getPanel());
    }

    JPanel getPanel() {
        return this.game;
    }

    void eventLoop() {
        this.gButtons.eventLoop();
    }
}
