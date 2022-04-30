package GUI;

import javax.swing.*;
import javax.swing.border.*;

import DataStructures.Game;

public class Sliders {
    private JPanel sliders;

    private JSlider boardSetter;
    private int boardSize;
    private JLabel boardcount;

    private JSlider pawnSetter;
    private int pawns;
    private JLabel pawncount;

    private JSlider speed;
    private int speedMs;
    private JLabel speedLabel;

    Sliders() {
        this.sliders = new JPanel();
        this.sliders.setLayout(new BoxLayout(sliders, BoxLayout.Y_AXIS));
        this.sliders.setBorder(new EmptyBorder(20, 20, 20, 20));

        this.boardSetter = new JSlider(GUI.minBoardSize, GUI.maxBoardSize);
        this.boardSetter.setName("A tábla mérete");
        this.boardSetter.setValue(8);
        this.boardSize = boardSetter.getValue();
        this.boardSetter.setBorder(new EmptyBorder(5, 5, 5, 5));
        JPanel boardTexts = new JPanel();
        boardTexts.setLayout(new BoxLayout(boardTexts, BoxLayout.X_AXIS));
        boardTexts.add(new JLabel("Tábla mérete: "));
        this.boardcount = new JLabel(Integer.toString(boardSize));
        boardTexts.add(boardcount);
        this.sliders.add(boardTexts);
        this.sliders.add(boardSetter);

        this.pawnSetter = new JSlider(1, 4);
        this.pawnSetter.setName("A gyalogok sorainak száma");
        this.pawnSetter.setValue(2);
        this.pawns = pawnSetter.getValue();
        this.pawnSetter.setBorder(new EmptyBorder(5, 5, 5, 5));
        JPanel pawnTexts = new JPanel();
        pawnTexts.setLayout(new BoxLayout(pawnTexts, BoxLayout.X_AXIS));
        pawnTexts.add(new JLabel("Gyalogok sorainak száma: "));
        this.pawncount = new JLabel(Integer.toString(pawns));
        pawnTexts.add(this.pawncount);
        this.sliders.add(pawnTexts);
        this.sliders.add(pawnSetter);

        this.speed = new JSlider(20, 1000);
        this.speed.setName("A szimuláció sebessége (ms)");
        this.speed.setValue(1000);
        this.speedMs = this.speed.getValue();
        this.speed.setBorder(new EmptyBorder(5, 5, 5, 5));
        JPanel speedTexts = new JPanel();
        speedTexts.setLayout(new BoxLayout(speedTexts, BoxLayout.X_AXIS));
        speedTexts.add(new JLabel("A szimuláció sebessége (ms): "));
        this.speedLabel = new JLabel(Integer.toString(this.speedMs));
        speedTexts.add(this.speedLabel);
        this.sliders.add(speedTexts);
        this.sliders.add(this.speed);
    }

    JPanel getPanel() {
        return this.sliders;
    }

    int getBoardSize() {
        return this.boardSize;
    }

    int getRowOfPawns() {
        return this.pawns;
    }

    Boolean eventLoop(GUI toSet) {
        Boolean changeHappened = false;
        if (this.boardSetter.getValue() != this.boardSize) {
            this.boardSize = this.boardSetter.getValue();
            this.pawnSetter.setMaximum(this.boardSize / 2);
            if (this.pawnSetter.getValue() > this.pawnSetter.getMaximum()) {
                this.pawnSetter.setValue(this.pawnSetter.getMaximum());
            }
            this.boardcount.setText(Integer.toString(this.boardSize));
            changeHappened = true;
        }
        if (this.pawnSetter.getValue() != this.pawns) {
            this.pawns = this.pawnSetter.getValue();
            this.pawncount.setText(Integer.toString(this.pawns));
            changeHappened = true;
        }
        if (this.speed.getValue() != this.speedMs) {
            this.speedMs = this.speed.getValue();
            this.speedLabel.setText(Integer.toString(this.speedMs));
        }
        if (changeHappened) {
            toSet.thisGame = new Game(this.pawns, this.boardSize, true);
        }
        return changeHappened;
    }
}
