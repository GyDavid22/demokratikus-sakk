package DataStructures;

import java.util.ArrayList;

import Exceptions.*;

abstract class Piece {
    private Boolean isBlack;
    private String type;
    private int[] pos;
    private Board partOf;
    private Boolean canHit;

    Piece(Boolean isBlack, int[] pos, String type, Board partOf) {
        this.isBlack = isBlack;
        this.pos = pos;
        this.type = type;
        this.partOf = partOf;
        this.canHit = false;
    }

    abstract boolean doStep() throws EmptyFieldException, CannotHitThatPiece; // annak megfelelően tér vissza, hogy a lépés sikeres volt-e

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

    Boolean canHit() {
        return this.canHit;
    }

    void nowAbleToHit() {
        this.canHit = true;
    }

    void nowCantHit() {
        this.canHit = false;
    }

    abstract ArrayList<int[]> possibleSteps();
    abstract ArrayList<int[]> possibleHits();
}
