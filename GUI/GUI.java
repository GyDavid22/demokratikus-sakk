package GUI;

import javax.swing.*;

public class GUI {
    static int minBoardSize = 4;
    static int maxBoardSize = 20;

    public static void gui() {
        JFrame window = new JFrame("Demokratikus sakk");
        window.setResizable(false);
        window.setSize(600, 600);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setVisible(true);

        JPanel whole = new JPanel();
        whole.setLayout(new BoxLayout(whole, BoxLayout.Y_AXIS));

        JTabbedPane tabs = new JTabbedPane();
        whole.add(tabs);

        GameTab game = new GameTab();
        tabs.addTab("Játékmező", game.getPanel());

        SettingsTab settings = new SettingsTab();
        tabs.addTab("Beállítások", settings.getPanel());

        About about = new About();
        tabs.addTab("Névjegy", about.getPanel());

        window.add(whole);

        Boolean run = true;
        while (run) {
            if (!window.isVisible()) {
                run = false;
            }
            game.eventLoop();
            settings.eventLoop();
            try {
                Thread.sleep(20); // 50 Hz
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
