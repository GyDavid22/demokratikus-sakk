package DataStructures;

import Exceptions.*;
import java.util.ArrayList;

class Board {
    private Piece[][] field;
    private ArrayList<Piece> whites;
    private ArrayList<Piece> blacks;
    private int boardSize;

    Board(int boardSize) {
        this.whites = new ArrayList<>();
        this.blacks = new ArrayList<>();
        this.boardSize = boardSize;
        this.field = new Piece[this.boardSize][this.boardSize];
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

    void movePiece(Piece that, int[] destPos) throws EmptyFieldException, CannotHitThatPiece {
        if (isEmpty(destPos)) {
            removePiece(that.getPos(), true);
            field[destPos[0]][destPos[1]] = that;
            that.setPos(destPos);
        } else {
            if (that.isBlack() == isPieceBlack(destPos)) {
                throw new CannotHitThatPiece();
            }
            removePiece(that.getPos(), true);
            removePiece(destPos, false);
            field[destPos[0]][destPos[1]] = that;
            that.setPos(destPos);
        }
        that.nowCantHit();
    }

    private Piece removePiece(int[] from, boolean onlyMoved) throws EmptyFieldException {
        if (isEmpty(from)) {
            throw new EmptyFieldException();
        }
        Piece toRemove = this.field[from[0]][from[1]];
        this.field[from[0]][from[1]] = null;
        if (!onlyMoved) {
            if (toRemove.isBlack()) {
                blacks.remove(toRemove);
            } else {
                whites.remove(toRemove);
            }
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
                    returnValue += " B ";
                } else {
                    returnValue += " W ";
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

    ArrayList<Piece> getList(boolean blackWanted) {
        ArrayList<Piece> shallowCopy = new ArrayList<>();
        if (blackWanted) {
            for (Piece i : this.blacks) {
                shallowCopy.add(i);
            }
        } else {
            for (Piece i : this.whites) {
                shallowCopy.add(i);
            }
        }
        return shallowCopy;
    }
}
