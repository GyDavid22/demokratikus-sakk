package GUI;

import javax.swing.*;
import javax.swing.border.*;

public class GameButtons {
    private JPanel buttons;
    private JButton startButton;
    private JButton other;

    GameButtons() {
        this.buttons = new JPanel();
        this.buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        this.startButton = new JButton("Start");
        this.startButton.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.buttons.add(startButton);
        this.other = new JButton("másik");
        this.other.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.buttons.add(other);
    }

    JPanel getPanel() {
        return this.buttons;
    }

    void eventLoop() {

    }
}
