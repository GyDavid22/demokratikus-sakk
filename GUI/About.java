package GUI;

import javax.swing.*;

public class About {
    private JPanel aboutPanel;

    About() {
        this.aboutPanel = new JPanel();
        this.aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
        String aboutText = "Demokratikus sakk v1.0\nKészítette: Gyenes Dávid András\nKészült a BME üzemmérnök-informatikus képzésének Objektumorientált programozás tárgyának\nnagy házi feladataként.\n2022";
        for (String i : aboutText.split("\n")) {
            JPanel currentRow = new JPanel();
            currentRow.setLayout(new BoxLayout(currentRow, BoxLayout.LINE_AXIS));
            currentRow.add(new JLabel(i));
            this.aboutPanel.add(currentRow);
        }
    }

    JPanel getPanel() {
        return this.aboutPanel;
    }
}
