package DataStructures;

import java.util.ArrayList;

import Exceptions.*;

abstract class Piece {
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

    abstract ArrayList<int[]> possibleSteps(Boolean aggressiveHit);
    abstract ArrayList<int[]> possibleHits();
}
