package DataStructures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Exceptions.CannotHitThatPiece;
import Exceptions.EmptyFieldException;

/**
 * A gyalogokat reprezentáló osztály
 */
class Pawn extends Piece {
    private Boolean isFirstStep;
    private int[][] stepDirections;

    /**
     * @param isBlack Fekete legyen?
     * @param pos     Pozíció a tartalmazó táblának megfelelően.
     * @param partOf  A bábut tartalmazó tábla.
     */
    Pawn(Boolean isBlack, int[] pos, Board partOf) {
        super(isBlack, pos, "gyalog", partOf);
        this.isFirstStep = true;
        int[][] defaultDirections = { { 2, 0 }, { 1, 0 }, { 1, -1 }, { 1, 1 } };
        // Sorban: első lépés, alapértelmezett lépés, illtve a kettő átlós, leütő lépés
        this.stepDirections = defaultDirections;
        if (!this.isBlack()) { // ha a bábu fehér, a fenti vektoroknak pont az ellentettjét kell venni.
            for (int i = 0; i < this.stepDirections.length; ++i) {
                for (int j = 0; j < this.stepDirections[i].length; ++j) {
                    this.stepDirections[i][j] *= -1;
                }
            }
        }
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
    boolean doStep(Boolean aggressiveHit) throws EmptyFieldException, CannotHitThatPiece {
        ArrayList<int[]> possibleSteps = possibleSteps(true);
        if (possibleSteps.size() == 0) {
            return false;
        }

        Random r = new Random();
        int stepToDo = r.nextInt(0, possibleSteps.size()); // ha több lehetőség is van, nem szeretnék bekódolni semmit,
                                                           // legyen random

        Board partOf = getBoard();
        partOf.movePiece(this, possibleSteps.get(stepToDo));

        if (this.isFirstStep) {
            this.isFirstStep = false;
        }

        return true;
    }

    /**
     * Az összes lehetséges lépés vektorát adja vissza, beleértve a lehetséges
     * leütéseket is.
     * 
     * @param aggressiveHit True: amennyiben a bábu képes leütni valakit, megteszi.
     * @return A lehetséges lépések vektora. (Ha nincs, 0 elemű.)
     */
    ArrayList<int[]> possibleSteps(Boolean aggressiveHit) {
        ArrayList<int[]> returnValues = possibleHits();
        if (returnValues.size() > 0 && aggressiveHit) {
            return returnValues; // nem vizsgálunk tovább, ha ütni lehet, azt preferáljuk
        }

        Board partOf = getBoard();
        int[] vectorsToView = new int[2];
        if (this.isFirstStep) {
            vectorsToView[0] = 0;
            vectorsToView[1] = 1 + 1; // mert az intervallum vége exkluzív
        } else {
            vectorsToView[0] = 1;
            vectorsToView[1] = 1 + 1;
        }

        for (int[] i : Arrays.copyOfRange(this.stepDirections, vectorsToView[0], vectorsToView[1])) {
            int[] currentPos = this.getPos();
            int[] possiblePos = { currentPos[0] + i[0], currentPos[1] + i[1] };
            try {
                if (partOf.isEmpty(possiblePos)) {
                    if (i == this.stepDirections[0]) { // kezdő dupla lépés, nem léphetjük át az előttünk lévőt
                        int[] testPos = { currentPos[0] + this.stepDirections[1][0],
                                currentPos[1] + this.stepDirections[1][1] };
                        if (partOf.isEmpty(testPos)) {
                            returnValues.add(possiblePos);
                        }
                    } else if (i != this.stepDirections[0]) { // nem dupla lépés esetén nincs mit vizsgálni
                        returnValues.add(possiblePos);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
            } // kimentünk a tábláról, de ez nem jelent most semmit
        }

        return returnValues;
    }

    /**
     * A lehetséges leütések vektoraival tér vissza.
     * 
     * @return A lehetséges ütések vektora. (Ha nincs, 0 elemű.)
     */
    ArrayList<int[]> possibleHits() {
        ArrayList<int[]> returnValues = new ArrayList<>();
        Board partOf = getBoard();

        int[] vectorsToView = new int[2];
        if (this.isFirstStep) {
            vectorsToView[0] = 0;
            vectorsToView[1] = this.stepDirections.length;
        } else {
            vectorsToView[0] = 1;
            vectorsToView[1] = this.stepDirections.length;
        }

        for (int[] i : Arrays.copyOfRange(this.stepDirections, vectorsToView[0], vectorsToView[1])) {
            int[] currentPos = this.getPos();
            int[] possiblePos = { currentPos[0] + i[0], currentPos[1] + i[1] };
            try {
                if (this.isBlack() != partOf.isPieceBlack(possiblePos)) {
                    if (i == this.stepDirections[0]) { // kezdő dupla lépés, nem léphetjük át az előttünk lévőt
                        int[] testPos = { currentPos[0] + this.stepDirections[1][0],
                                currentPos[1] + this.stepDirections[1][1] };
                        if (partOf.isEmpty(testPos)) {
                            returnValues.add(possiblePos);
                        }
                    } else if (i != this.stepDirections[0]) { // nem dupla lépés esetén nincs mit vizsgálni
                        returnValues.add(possiblePos);
                    }
                }
            } catch (IndexOutOfBoundsException e) { // kimentünk a tábláról, de ez nem jelent most semmit
            } catch (EmptyFieldException e) { // üres blokk színét ellenőriztük, ebben az esetben ez sem fontos
            }
        }
        return returnValues;
    }

    /**
     * @param oos Erre az ObjectOutputStreamre ír.
     * @throws IOException Kötelező kezelni, felsőbb szinteken fogjuk.
     */
    void save(ObjectOutputStream oos) throws IOException {
        super.save(oos);
        oos.writeObject(this.isFirstStep);
    }

    /**
     * @param ois Erről az ObjectInputStreamről olvas.
     * @return A betöltött objektum.
     * @throws IOException            Kötelező kezelni, felsőbb szinteken fogjuk.
     * @throws ClassNotFoundException Kötelező kezelni, felsőbb szinteken fogjuk.
     */
    static Pawn load(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        boolean isBlack = (boolean) ois.readObject();
        int[] pos = (int[]) ois.readObject();
        Board partOf = (Board) ois.readObject();

        Pawn loadedObject = new Pawn(isBlack, pos, partOf);

        loadedObject.isFirstStep = (boolean) ois.readObject();

        return loadedObject;
    }

    /**
     * Tartalmi, nem referenciaalapú egyezés ellenőrzése.
     * 
     * @param rhs A másik Pawn, amivel összehasonlítunk.
     * @return True vagy false, egyezéstől függően.
     */
    boolean equals(Pawn rhs) {
        if (!super.equals(rhs)) {
            return false;
        }
        if (!this.isFirstStep.equals(rhs.isFirstStep)) {
            return false;
        }
        try {
            for (int i = 0; i < this.stepDirections.length; ++i) {
                for (int j = 0; j < this.stepDirections[i].length; ++j) {
                    if (this.stepDirections[i][j] != rhs.stepDirections[i][j]) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
