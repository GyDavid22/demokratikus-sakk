package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.Image;

import DataStructures.Game;

public class GameField {
    private JPanel all;
    private String separator;
    private ImageIcon black, white, blackonwhite, blackonblack, whiteonwhite, whiteonblack;

    GameField() {
        this.all = new JPanel();
        this.separator = System.getProperty("file.separator");
        this.black = new ImageIcon("files" + this.separator + "black.jpg");
        this.white = new ImageIcon("files" + this.separator + "white.jpg");
        this.blackonwhite = new ImageIcon("files" + this.separator + "blackonwhite.jpg");
        this.blackonblack = new ImageIcon("files" + this.separator + "blackonblack.jpg");
        this.whiteonwhite = new ImageIcon("files" + this.separator + "whiteonwhite.jpg");
        this.whiteonblack = new ImageIcon("files" + this.separator + "whiteonblack.jpg");
    }

    void refresh(Game toDraw) {
        // enum értékkel kérdezd le
        this.all.removeAll();
        this.all.setLayout(new BoxLayout(this.all, BoxLayout.Y_AXIS));

        int boardSize = toDraw.getBoardSize();
        for (int i = 0; i < boardSize; ++i) {
            JPanel rows = new JPanel();
            for (int j = 0; j < boardSize; ++j) {
                rows.setLayout(new BoxLayout(rows, BoxLayout.X_AXIS));
                ImageIcon currentField = null;
                if (i % 2 == j % 2) {
                    Game.BoardValue current = toDraw.askBoard(i, j);
                    if (current == Game.BoardValue.NONE) {
                        currentField = this.white;
                    } else if (current == Game.BoardValue.WHITE) {
                        currentField = this.whiteonwhite;
                    } else {
                        currentField = this.blackonwhite;
                    }
                } else {
                    Game.BoardValue current = toDraw.askBoard(i, j);
                    if (current == Game.BoardValue.NONE) {
                        currentField = this.black;
                    } else if (current == Game.BoardValue.WHITE) {
                        currentField = this.whiteonblack;
                    } else {
                        currentField = this.blackonblack;
                    }
                }
                currentField.setImage(currentField.getImage().getScaledInstance(400 / boardSize, 400 / boardSize,
                        Image.SCALE_DEFAULT));
                JLabel current = new JLabel(currentField);
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
