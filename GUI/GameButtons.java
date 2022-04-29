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

        JPanel startButtonPanel = new JPanel();
        startButtonPanel.setLayout(new BoxLayout(startButtonPanel, BoxLayout.LINE_AXIS));
        this.startButton = new JButton("Start");
        startButtonPanel.add(this.startButton);
        startButtonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel otherButtonPanel = new JPanel();
        otherButtonPanel.setLayout(new BoxLayout(otherButtonPanel, BoxLayout.LINE_AXIS));
        this.other = new JButton("másik");
        otherButtonPanel.add(this.other);
        otherButtonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        this.buttons.add(startButtonPanel);
        this.buttons.add(otherButtonPanel);
    }

    JPanel getPanel() {
        return this.buttons;
    }

    void eventLoop() {

    }
}
