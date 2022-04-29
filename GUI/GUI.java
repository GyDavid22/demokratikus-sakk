package GUI;

import javax.swing.*;

import DataStructures.Game;

public class GUI {
    private JFrame window;
    private JPanel whole;
    private JTabbedPane tabs;
    private GameTab game;
    private SettingsTab settings;
    private About about;
    Game thisGame;

    static int minBoardSize = 4;
    static int maxBoardSize = 20;

    public GUI() {
        this.window = new JFrame("Demokratikus sakk");
        this.window.setResizable(false);
        this.window.setSize(600, 600);
        this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.whole = new JPanel();
        this.whole.setLayout(new BoxLayout(this.whole, BoxLayout.Y_AXIS));

        this.tabs = new JTabbedPane();
        this.whole.add(this.tabs);

        this.game = new GameTab();
        this.tabs.addTab("Játékmező", this.game.getPanel());

        this.settings = new SettingsTab();
        this.tabs.addTab("Beállítások", this.settings.getPanel());

        this.about = new About();
        this.tabs.addTab("Névjegy", this.about.getPanel());

        this.window.add(this.whole);

        this.thisGame = new Game(2, 8, true);
    }

    public void start() {
        this.window.setVisible(true);
        Boolean run = true;
        while (run) {
            if (!window.isVisible()) {
                run = false;
            }
            game.eventLoop(this.thisGame);
            settings.eventLoop(this);
        }
    }
}
