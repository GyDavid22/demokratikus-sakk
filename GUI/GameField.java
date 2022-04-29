package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.Image;

public class GameField {
    private JPanel all;

    GameField() {
        refresh();
    }

    void refresh() {
        this.all = new JPanel();
        this.all.setLayout(new BoxLayout(this.all, BoxLayout.Y_AXIS));
        for (int i = 0; i < 8; ++i) {
            JPanel rows = new JPanel();
            for (int j = 0; j < 8; ++j) {
                rows.setLayout(new BoxLayout(rows, BoxLayout.X_AXIS));
                ImageIcon black = new ImageIcon("files\\fekete.png");
                black.setImage(black.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
                JLabel current = new JLabel(black);
                current.setSize(20, 20);
                rows.add(current);
            }
            all.add(rows);
        }
        all.setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    JPanel getPanel() {
        return this.all;
    }
}
