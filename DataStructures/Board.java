package DataStructures;

import Exceptions.*;
import java.util.ArrayList;
import java.util.Random;

class Board {
    private Piece[][] field;
    private ArrayList<Piece> whites;
    private ArrayList<Piece> blacks;
    private int boardSize = 8;

    Board() {
        this.whites = new ArrayList<>();
        this.blacks = new ArrayList<>();
        this.field = new Piece[8][8];
    }

    void placePiece(Piece that) throws OccupiedFieldException {
        int[] to = that.getPos();
        if (!isEmpty(to)) {
            throw new OccupiedFieldException();
        }
        this.field[to[0]][to[1]] = that;
        if (that.isBlack()) {
            blacks.add(that);
        } else {
            whites.add(that);
        }
    }

    Piece removePiece(int[] from) throws EmptyFieldException {
        if (isEmpty(from)) {
            throw new EmptyFieldException();
        }
        Piece toRemove = this.field[from[0]][from[1]];
        this.field[from[0]][from[1]] = null;
        if (toRemove.isBlack()) {
            blacks.remove(toRemove);
        } else {
            whites.remove(toRemove);
        }
        return toRemove;
    }

    boolean isEmpty(int[] pos) {
        return field[pos[0]][pos[1]] == null;
    }

    boolean isPieceBlack(int[] pos) throws EmptyFieldException {
        if (isEmpty(pos)) {
            throw new EmptyFieldException();
        }
        return field[pos[0]][pos[1]].isBlack();
    }

    int getBoardSize() {
        return this.boardSize;
    }

    Piece getRandomOne(boolean blackWanted) {
        Random r = new Random();
        if (blackWanted) {
            return this.blacks.get(r.nextInt(0, this.blacks.size()));
        }
        return this.whites.get(r.nextInt(0, this.whites.size()));
    }

    public String toString() {
        String returnValue = "";
        for (int i = 0; i < getBoardSize(); ++i) {
            returnValue += "+---";
        }
        returnValue += "+\n";

        for (int i = 0; i < getBoardSize(); ++i) {
            for (int j = 0; j < getBoardSize(); ++j) {
                returnValue += "|";
                if (field[i][j] == null) {
                    returnValue += "   ";
                } else if (field[i][j].isBlack()) {
                    returnValue += " X ";
                } else {
                    returnValue += " O ";
                }
            }
            returnValue += "|\n";

            for (int j = 0; j < getBoardSize(); ++j) {
                returnValue += "+---";
            }
            returnValue += "+\n";
        }

        return returnValue;
    }

    int numOfPieces(boolean blacksWanted) {
        if (blacksWanted) {
            return this.blacks.size();
        }
        return this.whites.size();
    }
}
