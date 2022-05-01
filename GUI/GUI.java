package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Image;
import java.awt.event.*;

import DataStructures.Game;

public class GUI implements ActionListener {
    // Ablak és a GUI használt változói
    private JFrame window;
    private JPanel whole;
    private JTabbedPane tabs;
    private Game thisGame;
    private int minBoardSize;
    private int maxBoardSize;

    // Beállító csúszkák változói
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

    // Beállítások fül
    private JPanel settingsTab;

    // Játékmező
    private JPanel allOfTheField;
    private String separator;
    private ImageIcon black, white, blackonwhite, blackonblack, whiteonwhite, whiteonblack;
    private JLabel gameMessage;
    private JPanel messagePanel;
    private JPanel field;

    // Játékvezérlő gombok
    private JPanel buttons;
    private JButton startButton;
    private JButton reset;
    private Boolean gameInProgress;
    private Boolean paused;
    private Boolean doReset;

    // Játék fül
    private JPanel gameTab;

    // Névjegy fül
    private JPanel aboutTab;

    public GUI() {
        this.window = new JFrame("Demokratikus sakk");
        this.window.setResizable(false);
        this.window.setSize(600, 600);
        this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.minBoardSize = 4;
        this.maxBoardSize = 20;

        this.whole = new JPanel();
        this.whole.setLayout(new BoxLayout(this.whole, BoxLayout.Y_AXIS));

        this.tabs = new JTabbedPane();
        this.whole.add(this.tabs);

        initGameTab();
        this.tabs.addTab("Játékmező", this.gameTab);

        initSettingsTab();
        this.tabs.addTab("Beállítások", this.settingsTab);

        initAboutTab();
        this.tabs.addTab("Névjegy", this.aboutTab);

        this.window.add(this.whole);

        this.thisGame = new Game(2, 8, true);
    }

    public void start() {
        this.window.setVisible(true);
        Boolean run = true;
        Boolean changed = true;
        gameTabEventLoop();
        while (run) {
            if (!window.isVisible()) {
                run = false;
            }
            if (!thisGame.isGameOver() && this.gameInProgress && !this.paused) {
                thisGame.doRound();
                changed = true;
            }
            if (thisGame.isGameOver()) {
                this.gameInProgress = false;
                this.paused = true;
            }
            if (settingsTabLoop()) {
                changed = true;
            }
            gameTabEventLoop();
            if (this.gameTab.isShowing() && changed) {
                this.gameTab.revalidate();
                changed = false;
            }
            if (!this.gameTab.isShowing()) {
                this.paused = true;
            }
            if (this.doReset) {
                this.thisGame = new Game(this.pawns, this.boardSize, true);
                this.gameInProgress = false;
                this.paused = true;
                this.doReset = false;
                changed = true;
            }
            try {
                Thread.sleep(this.speedMs);
            } catch (InterruptedException e) {
            }
        }
    }

    private void initSliders() {
        this.sliders = new JPanel();
        this.sliders.setLayout(new BoxLayout(sliders, BoxLayout.Y_AXIS));
        this.sliders.setBorder(new EmptyBorder(20, 20, 20, 20));

        this.boardSetter = new JSlider(this.minBoardSize, this.maxBoardSize);
        this.boardSetter.setName("A tábla mérete");
        this.boardSetter.setValue(8);
        this.boardSize = boardSetter.getValue();
        this.boardSetter.setBorder(new EmptyBorder(5, 5, 5, 5));
        JPanel boardTexts = new JPanel();
        boardTexts.setLayout(new BoxLayout(boardTexts, BoxLayout.X_AXIS));
        boardTexts.add(new JLabel("Tábla mérete: "));
        this.boardcount = new JLabel(Integer.toString(this.boardSize));
        boardTexts.add(this.boardcount);
        this.sliders.add(boardTexts);
        this.sliders.add(this.boardSetter);

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
        this.speed.setValue(20);
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

    private Boolean sliderLoop() {
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
            this.thisGame = new Game(this.pawns, this.boardSize, true);
        }
        return changeHappened;
    }

    private void initSettingsTab() {
        this.settingsTab = new JPanel();
        this.settingsTab.setLayout(new BoxLayout(settingsTab, BoxLayout.Y_AXIS));
        initSliders();
        this.settingsTab.add(this.sliders);
    }

    private Boolean settingsTabLoop() {
        return sliderLoop();
    }

    private void initGameField() {
        this.allOfTheField = new JPanel();
        this.allOfTheField.setLayout(new BoxLayout(this.allOfTheField, BoxLayout.Y_AXIS));
        this.separator = System.getProperty("file.separator");
        this.black = new ImageIcon("files" + this.separator + "black.jpg");
        this.white = new ImageIcon("files" + this.separator + "white.jpg");
        this.blackonwhite = new ImageIcon("files" + this.separator + "blackonwhite.jpg");
        this.blackonblack = new ImageIcon("files" + this.separator + "blackonblack.jpg");
        this.whiteonwhite = new ImageIcon("files" + this.separator + "whiteonwhite.jpg");
        this.whiteonblack = new ImageIcon("files" + this.separator + "whiteonblack.jpg");
        this.gameMessage = new JLabel("");
        this.messagePanel = new JPanel();
        this.messagePanel.setLayout(new BoxLayout(this.messagePanel, BoxLayout.X_AXIS));
        this.messagePanel.add(this.gameMessage);
        this.messagePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.field = new JPanel();
        this.field.setLayout(new BoxLayout(this.field, BoxLayout.X_AXIS));

        this.allOfTheField.add(this.messagePanel);
        this.allOfTheField.add(this.field);
        this.allOfTheField.setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private void refreshGameField() {
        ImageIcon miniBlack = new ImageIcon(
                this.black.getImage().getScaledInstance(400 / this.boardSize, 400 / this.boardSize, Image.SCALE_FAST));
        ImageIcon miniWhite = new ImageIcon(
                this.white.getImage().getScaledInstance(400 / this.boardSize, 400 / this.boardSize, Image.SCALE_FAST));
        ImageIcon miniblackonwhite = new ImageIcon(this.blackonwhite.getImage().getScaledInstance(400 / this.boardSize,
                400 / this.boardSize, Image.SCALE_FAST));
        ImageIcon miniblackonblack = new ImageIcon(this.blackonblack.getImage().getScaledInstance(400 / this.boardSize,
                400 / this.boardSize, Image.SCALE_FAST));
        ImageIcon miniwhiteonwhite = new ImageIcon(this.whiteonwhite.getImage().getScaledInstance(400 / this.boardSize,
                400 / this.boardSize, Image.SCALE_FAST));
        ImageIcon miniwhiteonblack = new ImageIcon(this.whiteonblack.getImage().getScaledInstance(400 / this.boardSize,
                400 / this.boardSize, Image.SCALE_FAST));

        JPanel currentState = new JPanel();
        currentState.setLayout(new BoxLayout(currentState, BoxLayout.Y_AXIS));
        this.field.removeAll();

        this.gameMessage.setText(this.thisGame.getGameOverMessage());

        for (int i = 0; i < this.boardSize; ++i) {
            JPanel rows = new JPanel();
            rows.setLayout(new BoxLayout(rows, BoxLayout.X_AXIS));
            for (int j = 0; j < this.boardSize; ++j) {
                ImageIcon currentField = null;
                if (i % 2 == j % 2) {
                    Game.BoardValue current = this.thisGame.askBoard(i, j);
                    if (current == Game.BoardValue.NONE) {
                        currentField = miniWhite;
                    } else if (current == Game.BoardValue.WHITE) {
                        currentField = miniwhiteonwhite;
                    } else {
                        currentField = miniblackonwhite;
                    }
                } else {
                    Game.BoardValue current = this.thisGame.askBoard(i, j);
                    if (current == Game.BoardValue.NONE) {
                        currentField = miniBlack;
                    } else if (current == Game.BoardValue.WHITE) {
                        currentField = miniwhiteonblack;
                    } else {
                        currentField = miniblackonblack;
                    }
                }
                JLabel current = new JLabel(currentField);
                rows.add(current);
            }
            currentState.add(rows);
        }
        this.field.add(currentState);
    }

    private void initGameButtons() {
        this.buttons = new JPanel();
        this.buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        JPanel startButtonPanel = new JPanel();
        startButtonPanel.setLayout(new BoxLayout(startButtonPanel, BoxLayout.LINE_AXIS));
        this.startButton = new JButton("Start");
        this.startButton.addActionListener(this);
        startButtonPanel.add(this.startButton);
        startButtonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel resetButtonPanel = new JPanel();
        resetButtonPanel.setLayout(new BoxLayout(resetButtonPanel, BoxLayout.LINE_AXIS));
        this.reset = new JButton("Reset");
        this.reset.addActionListener(this);
        resetButtonPanel.add(this.reset);
        resetButtonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        this.buttons.add(startButtonPanel);
        this.buttons.add(resetButtonPanel);

        this.gameInProgress = false;
        this.paused = true;
        this.doReset = false;
    }

    private void gameButtonsEventLoop() {
        if (!this.paused && this.gameInProgress) {
            this.startButton.setText("Szünet");
        } else {
            this.startButton.setText("Start");
        }
    }

    private void initGameTab() {
        this.gameTab = new JPanel();
        this.gameTab.setLayout(new BoxLayout(this.gameTab, BoxLayout.Y_AXIS));

        initGameField();
        this.gameTab.add(this.allOfTheField);

        initGameButtons();
        this.gameTab.add(this.buttons);
    }

    private void gameTabEventLoop() {
        refreshGameField();
        gameButtonsEventLoop();
    }

    private void initAboutTab() {
        this.aboutTab = new JPanel();
        this.aboutTab.setLayout(new BoxLayout(aboutTab, BoxLayout.Y_AXIS));
        String aboutText = "Demokratikus sakk v1.0\nKészítette: Gyenes Dávid András\nKészült a BME üzemmérnök-informatikus képzésének Objektumorientált programozás tárgyának\nnagy házi feladataként.\n2022";
        for (String i : aboutText.split("\n")) {
            JPanel currentRow = new JPanel();
            currentRow.setLayout(new BoxLayout(currentRow, BoxLayout.LINE_AXIS));
            currentRow.add(new JLabel(i));
            this.aboutTab.add(currentRow);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.startButton) {
            if (this.gameInProgress) {
                this.paused = !this.paused;
            } else {
                this.gameInProgress = true;
                this.paused = false;
            }
        }
        if (e.getSource() == this.reset) {
            this.gameInProgress = false;
            this.paused = true;
            this.doReset = true;
        }
    }
}
