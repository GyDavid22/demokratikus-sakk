package GUI;

import javax.swing.*;
import java.awt.*;

public class GUI {
    public static void gui() {
        JFrame window = new JFrame("Demokratikus sakk");
        window.setResizable(false);
        window.setSize(600, 600);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);

        JSlider boardSetter = new JSlider(4, 15);
        boardSetter.setName("A tábla mérete");
        boardSetter.setValue(8);
        boardSetter.setBounds(0, 0, 200, 20);
        int boardSize = boardSetter.getValue();
        JSlider pawnSetter = new JSlider(1, 4);
        pawnSetter.setName("A gyalogok sorainak száma");
        pawnSetter.setValue(2);
        int pawns = pawnSetter.getValue();
        pawnSetter.setBounds(0, 40, 200, 20);

        window.add(boardSetter);
        window.add(pawnSetter);

        Boolean run = true;
        while (run) {
            if (!window.isVisible()) {
                run = false;
            }
            if (boardSetter.getValue() != boardSize) {
                boardSize = boardSetter.getValue();
                pawnSetter.setMaximum(boardSize / 2);
                pawns = pawnSetter.getValue();
                System.out.println(boardSize);
            }
            if (pawnSetter.getValue() != pawns) {
                pawns = pawnSetter.getValue();
                System.out.println(pawns);
            }
        }
    }
}
