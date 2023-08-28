package Main;

import GUI.GUI;

public class Main {

    /**
     * A fő függvény, ami mindent elindít.
     * 
     * @param args Bemeneti argumentumok, nem használjuk.
     */
    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.start();
    }
}