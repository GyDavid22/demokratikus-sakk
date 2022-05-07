package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.util.TreeMap;

import DataStructures.Game;

/**
 * A grafikus felületet megvalósító osztály, Game osztályt is példányosít
 * magának.
 */
public class GUI implements ActionListener, WindowListener {
    // Ablak és a GUI használt változói
    private String separator;
    private JFrame window;
    private JPanel whole;
    private JTabbedPane tabs;
    private Game thisGame;
    private int minBoardSize;
    private int maxBoardSize;
    private int defaultPawns, defaultBoardSize, defaultSpeed;

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
    private JLabel gameMessage;
    private JPanel messagePanel;
    private JPanel field;
    private static TreeMap<Game.BoardValue, ImageIcon> originalWhites = new TreeMap<>();
    private static TreeMap<Game.BoardValue, ImageIcon> originalBlacks = new TreeMap<>();
    private TreeMap<Game.BoardValue, ImageIcon> miniWhites, miniBlacks;

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

    /**
     * A konstruktor inicializál minden változót, az egyes fülek betöltése külön
     * függvénybe van kiszervezve
     */
    public GUI() {
        this.separator = System.getProperty("file.separator");

        if (GUI.originalBlacks.size() == 0) {
            originalBlacks.put(Game.BoardValue.NONE, new ImageIcon("files" + this.separator + "black.jpg"));
            originalBlacks.put(Game.BoardValue.WHITE, new ImageIcon("files" + this.separator + "whiteonblack.jpg"));
            originalBlacks.put(Game.BoardValue.BLACK, new ImageIcon("files" + this.separator + "blackonblack.jpg"));
        }
        if (GUI.originalWhites.size() == 0) {
            originalWhites.put(Game.BoardValue.NONE, new ImageIcon("files" + this.separator + "white.jpg"));
            originalWhites.put(Game.BoardValue.BLACK, new ImageIcon("files" + this.separator + "blackonwhite.jpg"));
            originalWhites.put(Game.BoardValue.WHITE, new ImageIcon("files" + this.separator + "whiteonwhite.jpg"));
        }

        this.miniBlacks = new TreeMap<>();
        this.miniWhites = new TreeMap<>();

        this.defaultBoardSize = 8;
        this.defaultPawns = 2;
        this.defaultSpeed = 20;

        this.window = new JFrame("Demokratikus sakk");
        this.window.setResizable(false);
        this.window.setSize(600, 600);
        this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.window.addWindowListener(this);

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

        newGameInstance();

        this.window.add(this.whole);
    }

    /**
     * Megjeleníti a játékablakot, illetve futtatja az eseményhurkot. A függvény a
     * főablak bezárásával áll le.
     */
    public void start() {
        this.window.setVisible(true);
        File saveFile = new File("savefile.bin");
        if (saveFile.exists()) {
            loadConfirm();
        }
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
                newGameInstance();
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

    /**
     * A beállítások lapon lévő beállító csúszkák inicializálása, initSettingsTab()
     * hívja meg.
     */
    private void initSliders() {
        this.sliders = new JPanel();
        this.sliders.setLayout(new BoxLayout(sliders, BoxLayout.Y_AXIS));
        this.sliders.setBorder(new EmptyBorder(20, 20, 20, 20));

        this.boardSetter = new JSlider(this.minBoardSize, this.maxBoardSize);
        this.boardSetter.setValue(this.defaultBoardSize);
        this.boardSize = boardSetter.getValue();
        this.boardSetter.setBorder(new EmptyBorder(5, 5, 5, 5));
        JPanel boardTexts = new JPanel();
        boardTexts.setLayout(new BoxLayout(boardTexts, BoxLayout.X_AXIS));
        boardTexts.add(new JLabel("Tábla mérete: "));
        this.boardcount = new JLabel(Integer.toString(this.boardSize));
        boardTexts.add(this.boardcount);
        this.sliders.add(boardTexts);
        this.sliders.add(this.boardSetter);

        this.pawnSetter = new JSlider(1, this.defaultBoardSize / 2);
        this.pawnSetter.setValue(this.defaultPawns);
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
        this.speed.setValue(this.defaultSpeed);
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

    /**
     * A csúszkákra vonatkozó rész az eseményhurokból, a visszatérési érték megadja,
     * hogy változott-e valamelyik érték. settingsTabLoop() hívja meg.
     * 
     * @return Történt-e változás a beállításokban?
     */
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
            newGameInstance();
        }
        return changeHappened;
    }

    /**
     * A beállítások fül inicializálása, a konstruktor hívja meg.
     */
    private void initSettingsTab() {
        this.settingsTab = new JPanel();
        this.settingsTab.setLayout(new BoxLayout(settingsTab, BoxLayout.Y_AXIS));
        initSliders();
        this.settingsTab.add(this.sliders);
    }

    /**
     * A beállítások fül része az eseményhurokból, azt adja tovább, hogy a
     * csúszkákkal állítva változott-e valamelyik érték. start() hívja meg.
     * 
     * @return Történt-e változás a beállított értékekben?
     */
    private Boolean settingsTabLoop() {
        return sliderLoop();
    }

    /**
     * A játéktáblát kirajzoló elemek inicializálása. A konstruktor hívja meg.
     */
    private void initGameField() {
        this.allOfTheField = new JPanel();
        this.allOfTheField.setLayout(new BoxLayout(this.allOfTheField, BoxLayout.Y_AXIS));

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

    /**
     * A játéktábla frissítése az aktuális állás alapján. gameTabEventLoop() hívja
     * meg.
     */
    private void refreshGameField() {
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
                    currentField = this.miniWhites.get(current);
                } else {
                    Game.BoardValue current = this.thisGame.askBoard(i, j);
                    currentField = this.miniBlacks.get(current);
                }
                JLabel current = new JLabel(currentField);
                rows.add(current);
            }
            currentState.add(rows);
        }
        this.field.add(currentState);
    }

    /**
     * A játékot vezérlő gombokat megvalósító változók inicializálása
     */
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

    /**
     * A játékot vezérlő gombokhoz tartozó rész az eseményhurokból,
     * gameTabEventLoop() hívja meg
     */
    private void gameButtonsEventLoop() {
        if (!this.paused && this.gameInProgress) {
            this.startButton.setText("Szünet");
        } else {
            this.startButton.setText("Start");
        }
    }

    /**
     * A játéktáblát és a játékot vezérlő gombokat inicializáló függvény, a
     * konstruktor hívja meg
     */
    private void initGameTab() {
        this.gameTab = new JPanel();
        this.gameTab.setLayout(new BoxLayout(this.gameTab, BoxLayout.Y_AXIS));

        initGameField();
        this.gameTab.add(this.allOfTheField);

        initGameButtons();
        this.gameTab.add(this.buttons);
    }

    /**
     * A játék fül része az eseményhurokból, start() hívja meg
     */
    private void gameTabEventLoop() {
        refreshGameField();
        gameButtonsEventLoop();
    }

    /**
     * Névjegy fül inicializálása, a konstruktor hívja meg
     */
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

    /**
     * Megtalált játékállás esetén megkérdezi, hogy betöltse-e azt, és el is végzi a
     * betöltést
     */
    private void loadConfirm() {
        JFrame dialog = new JFrame();
        dialog.setVisible(true);
        int result = JOptionPane.showConfirmDialog(dialog, "Mentett játékállást találtam. Szeretnéd betölteni?",
                "Betöltés", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            Game loaded = Game.load("savefile.bin");
            this.gameInProgress = true;
            this.boardSize = loaded.getBoardSize();
            this.boardSetter.setValue(this.boardSize); // különben érzékeli a különbséget maga és az előző változó
                                                       // között és resetel mindent
            newGameInstance(); // hogy átméretezze a tábla elemeit
            this.thisGame = loaded;
        }
        dialog.setVisible(false);
    }

    /**
     * Bezáráskor rákérdezés a félbeszakított játék elmentésére, el is végzi a
     * mentést.
     */
    private void saveConfirm() {
        JFrame dialog = new JFrame();
        dialog.setVisible(true);
        int result = JOptionPane.showConfirmDialog(dialog, "Szeretnéd fájlbamenteni az aktuális játékállást?", "Mentés",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            this.thisGame.save("savefile.bin");
        }
        dialog.setVisible(false);
    }

    /**
     * Amennyiben this.thisGame-t egy új példánnyal írjuk felül, ezt a függvényt
     * kell használni, ugyanis
     * a tábla egyes mezőinek a szükséges méretre igazítása is itt történik meg, az
     * új példány létrehozása mellett.
     */
    private void newGameInstance() {
        this.miniBlacks.clear();
        this.miniBlacks.put(Game.BoardValue.NONE, new ImageIcon(GUI.originalBlacks.get(Game.BoardValue.NONE).getImage()
                .getScaledInstance(400 / this.boardSize, 400 / this.boardSize, Image.SCALE_SMOOTH)));
        this.miniBlacks.put(Game.BoardValue.BLACK, new ImageIcon(GUI.originalBlacks.get(Game.BoardValue.BLACK)
                .getImage().getScaledInstance(400 / this.boardSize, 400 / this.boardSize, Image.SCALE_SMOOTH)));
        this.miniBlacks.put(Game.BoardValue.WHITE, new ImageIcon(GUI.originalBlacks.get(Game.BoardValue.WHITE)
                .getImage().getScaledInstance(400 / this.boardSize, 400 / this.boardSize, Image.SCALE_SMOOTH)));

        this.miniWhites.clear();
        this.miniWhites.put(Game.BoardValue.NONE, new ImageIcon(GUI.originalWhites.get(Game.BoardValue.NONE).getImage()
                .getScaledInstance(400 / this.boardSize, 400 / this.boardSize, Image.SCALE_SMOOTH)));
        this.miniWhites.put(Game.BoardValue.BLACK, new ImageIcon(GUI.originalWhites.get(Game.BoardValue.BLACK)
                .getImage().getScaledInstance(400 / this.boardSize, 400 / this.boardSize, Image.SCALE_SMOOTH)));
        this.miniWhites.put(Game.BoardValue.WHITE, new ImageIcon(GUI.originalWhites.get(Game.BoardValue.WHITE)
                .getImage().getScaledInstance(400 / this.boardSize, 400 / this.boardSize, Image.SCALE_SMOOTH)));

        this.thisGame = new Game(this.pawns, this.boardSize, true);
    }

    /**
     * Játékvezérlő gombok funkcionalitása
     */
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

    /**
     * Ablak bezárásának érzékelése
     */
    @Override
    public void windowClosing(WindowEvent we) {
        this.paused = true;
        if (this.gameInProgress) {
            saveConfirm();
        }
    }

    // Alább WindowsListener interfész kötelezően implementálandó, de itt nem
    // használt függvényei vannak.

    @Override
    public void windowOpened(WindowEvent we) {
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
    }

    @Override
    public void windowActivated(WindowEvent we) {
    }

    @Override
    public void windowClosed(WindowEvent we) {
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
    }

    @Override
    public void windowIconified(WindowEvent we) {
    }

}
