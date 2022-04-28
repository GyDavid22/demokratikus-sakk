package GUI;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class SettingsTab {
    private JPanel settings;
    private Sliders sliders;

    SettingsTab() {
        this.settings = new JPanel();
        this.settings.setLayout(new BoxLayout(settings, BoxLayout.Y_AXIS));
        this.sliders = new Sliders();
        this.settings.add(this.sliders.getPanel());
    }

    JPanel getPanel() {
        return this.settings;
    }

    void eventLoop() {
        this.sliders.eventLoop();
    }
}
