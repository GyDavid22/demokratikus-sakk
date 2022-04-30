package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.Image;

import DataStructures.Game;

public class GameField {
    private JPanel all;
    private String separator;
    private ImageIcon black, white, blackonwhite, blackonblack, whiteonwhite, whiteonblack;
    private JLabel gameMessage;
    private JPanel messagePanel;
    private JPanel field;
    private JLabel fieldImage;

    GameField() {
        this.all = new JPanel();
        this.all.setLayout(new BoxLayout(this.all, BoxLayout.Y_AXIS));
        this.separator = System.getProperty("file.separator");
        this.black = new ImageIcon("files" + this.separator + "black.jpg");
        this.white = new ImageIcon("files" + this.separator + "white.jpg");
        this.blackonwhite = new ImageIcon("files" + this.separator + "blackonwhite.jpg");
        this.blackonblack = new ImageIcon("files" + this.separator + "blackonblack.jpg");
        this.whiteonwhite = new ImageIcon("files" + this.separator + "whiteonwhite.jpg");
        this.whiteonblack = new ImageIcon("files" + this.separator + "whiteonblack.jpg");
        this.gameMessage = new JLabel();
        this.messagePanel = new JPanel();
        this.messagePanel.setLayout(new BoxLayout(this.messagePanel, BoxLayout.X_AXIS));
        this.messagePanel.add(this.gameMessage);
        this.messagePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.field = new JPanel();
        this.field.setLayout(new BoxLayout(this.field, BoxLayout.X_AXIS));
        this.fieldImage = new JLabel();
        this.field.add(this.fieldImage);

        this.all.add(this.messagePanel);
        this.all.add(this.field);
        this.all.setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    void refresh(Game toDraw) {
        JPanel currentState = new JPanel();
        currentState.setLayout(new BoxLayout(currentState, BoxLayout.Y_AXIS));
        this.field.removeAll();

        this.gameMessage.setText(toDraw.getGameOverMessage());

        int boardSize = toDraw.getBoardSize();
        for (int i = 0; i < boardSize; ++i) {
            JPanel rows = new JPanel();
            rows.setLayout(new BoxLayout(rows, BoxLayout.X_AXIS));
            for (int j = 0; j < boardSize; ++j) {
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
                JLabel current = new JLabel(new ImageIcon(currentField.getImage().getScaledInstance(400 / boardSize, 400 / boardSize, Image.SCALE_FAST)));
                rows.add(current);
            }
            currentState.add(rows);
        }
        this.field.add(currentState);
    }

    JPanel getPanel() {
        return this.all;
    }
}
