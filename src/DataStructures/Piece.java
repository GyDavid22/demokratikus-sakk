package DataStructures;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import Exceptions.*;

abstract class Piece implements Serializable {
    private Boolean isBlack;
    private String type;
    private int[] pos;
    private Board partOf;

    Piece(Boolean isBlack, int[] pos, String type, Board partOf) {
        this.isBlack = isBlack;
        this.pos = pos;
        this.type = type;
        this.partOf = partOf;
    }

    abstract boolean doStep(Boolean aggressiveHit) throws EmptyFieldException, CannotHitThatPiece; // annak megfelelően tér vissza, hogy a lépés sikeres volt-e

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
        oos.writeObject(this.isBlack);
        oos.writeObject(this.pos);
        oos.writeObject(this.partOf);
    }

    //abstract Piece load(ObjectInputStream ois) throws IOException, ClassNotFoundException;

    abstract ArrayList<int[]> possibleSteps(Boolean aggressiveHit);
    abstract ArrayList<int[]> possibleHits();

    boolean equals(Piece rhs) {
        /** Tartalmi, azaz nem referenciaalapú egyezés ellenőrzése */
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
