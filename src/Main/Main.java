package Main;

//import TestingTools.*;
import GUI.GUI;

public class Main {
    public static void main(String[] args) {
        /** A fő függvény, ami mindent elindít.
         * @param args Bemeneti argumentumok, nem használjuk.
         */
        GUI gui = new GUI();
        gui.start();
    }
}