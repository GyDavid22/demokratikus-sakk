package DataStructures;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import Exceptions.*;

/**
 * Az egyes bábukat reprezentáló absztrakt osztály.
 */
abstract class Piece implements Serializable {
    private Boolean isBlack;
    private String type;
    private int[] pos;
    private Board partOf;

    /**
     * @param isBlack Fekete legyen?
     * @param pos     Pozíció a tartalazó táblának megfelelően.
     * @param type    A bábu típusa szövegesen.
     * @param partOf  A tábla, amin a bábu áll.
     */
    Piece(Boolean isBlack, int[] pos, String type, Board partOf) {
        this.isBlack = isBlack;
        this.pos = pos;
        this.type = type;
        this.partOf = partOf;
    }

    /**
     * Kiválaszt egy random bábut az éppen soronkövetkező színűek közül, majd
     * lépteti,
     * ha lehet. Ennek sikerességét adja vissza a függvény
     * 
     * @param aggressiveHit True: amennyiben a bábu képes leütni valakit, megteszi.
     * @return Sikeres volt-e a lépés? (Tud-e lépni az adott bábu?)
     * @throws EmptyFieldException Hívott függvényekhez tartozik, feljebb kezeljük.
     * @throws CannotHitThatPiece  Hívott függvényekhez tartozik, feljebb kezeljük.
     */
    abstract boolean doStep(Boolean aggressiveHit) throws EmptyFieldException, CannotHitThatPiece;
    // annak megfelelően tér vissza, hogy a lépés sikeres volt-e

    boolean isBlack() {
        return this.isBlack;
    }

    String getType() {
        return this.type;
    }

    int[] getPos() {
        return this.pos;
    }

    void setPos(int[] pos) {
        this.pos = pos;
    }

    Board getBoard() {
        return this.partOf;
    }

    void save(ObjectOutputStream oos) throws IOException {
        /**
         * Függvény fájlba mentéshez.
         * 
         * @param oos Erre az ObjectOutputStreamre ír.
         * @throws IOException Kötelező kezelni, azonban a hívó függvényben több értelme
         *                     van.
         */
        oos.writeObject(this.isBlack);
        oos.writeObject(this.pos);
        oos.writeObject(this.partOf);
    }

    /**
     * Az összes lehetséges lépés vektorát adja vissza, beleértve a lehetséges
     * leütéseket is.
     * 
     * @param aggressiveHit True: amennyiben a bábu képes leütni valakit, megteszi.
     * @return A lehetséges lépések vektora. (Ha nincs, 0 elemű.)
     */
    abstract ArrayList<int[]> possibleSteps(Boolean aggressiveHit);

    /**
     * A lehetséges leütések vektoraival tér vissza.
     * 
     * @return A lehetséges ütések vektora. (Ha nincs, 0 elemű.)
     */
    abstract ArrayList<int[]> possibleHits();

    /**
     * Tartalmi, azaz nem referenciaalapú egyezés ellenőrzése
     * 
     * @param rhs A Piece, amivel összehasonlítanánk.
     * @return True vagy false egyezéstől függően.
     */
    boolean equals(Piece rhs) {
        if (!this.isBlack.equals(rhs.isBlack)) {
            return false;
        }
        if (!this.type.equals(rhs.type)) {
            return false;
        }
        if (this.pos.length != 2 || rhs.pos.length != 2) {
            return false;
        }
        if (this.pos[0] != rhs.pos[0] || this.pos[1] != rhs.pos[1]) {
            return false;
        }

        return true;
    } // a tábla tartalmát itt nem ellenőrizzük, mert végtelen rekurzió lenne belőle
}
