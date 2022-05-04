package DataStructures;

import Exceptions.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

class Board implements Serializable {
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

    private Board() { }

    void placePiece(Piece that) throws OccupiedFieldException {
        /** Bábu elhelyezése a játéktáblán */
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
        /** Bábu mozgatása a játéktáblán */
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
    }

    private void removePiece(int[] from, boolean onlyMoved) throws EmptyFieldException {
        /** Bábu levétele a játéktábláról */
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
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < getBoardSize(); ++i) {
            sb.append("+---");
        }
        sb.append("+\n");

        for (int i = 0; i < getBoardSize(); ++i) {
            for (int j = 0; j < getBoardSize(); ++j) {
                sb.append("|");
                if (field[i][j] == null) {
                    sb.append("   ");
                } else if (field[i][j].isBlack()) {
                    sb.append(" B ");
                } else {
                    sb.append(" W ");
                }
            }
            sb.append("|\n");

            for (int j = 0; j < getBoardSize(); ++j) {
                sb.append("+---");
            }
            sb.append("+\n");
        }

        return sb.toString();
    }

    int numOfPieces(boolean blacksWanted) {
        /** Visszaadja, hogy hány darab van a kért színű bábukból a táblán */
        if (blacksWanted) {
            return this.blacks.size();
        }
        return this.whites.size();
    }

    ArrayList<Piece> getList(boolean blackWanted) {
        /** Sekély másolatot ad vissza a bábukat tartalmazó ArrayListről */
        // sekély másolatot adunk vissza, ugyanis így a visszatérési értéken keresztül magát az eredeti listát nem lehet módosítani
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
    void save(ObjectOutputStream oos) throws IOException {
        /** Az osztályt ObjectOutputStreamre író függvény */
        oos.writeObject(this.field);
        oos.writeObject(this.whites);
        oos.writeObject(this.blacks);
        oos.writeObject(this.boardSize);
    }

    static Board load(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        /** Azt osztályt ObjectInputStreamről beolvasó függvény */
        Board loadedObject = new Board();
        loadedObject.field = (Piece[][]) ois.readObject();
        loadedObject.whites = (ArrayList<Piece>) ois.readObject();
        loadedObject.blacks = (ArrayList<Piece>) ois.readObject();
        loadedObject.boardSize = (int) ois.readObject();
        return loadedObject;
    }

    boolean equals(Board rhs) {
        /** Tartalmi, azaz nem referenciaalapú egyezés ellenőrzése */
        try {
            for (int i = 0; i < this.field.length; ++i) {
                for (int j = 0; j < this.field[i].length; ++j) {
                    if (this.field[i][j] != null && (!this.field[i][j].equals(rhs.field[i][j]))) {
                        return false;
                    }
                }
            }
            if (this.whites.size() != rhs.whites.size()) {
                return false;
            }
            for (int i = 0; i < this.whites.size(); ++i) {
                if (!this.whites.get(i).equals(rhs.whites.get(i))) {
                    return false;
                }
            }
            if (this.blacks.size() != rhs.blacks.size()) {
                return false;
            }
            for (int i = 0; i < this.blacks.size(); ++i) {
                if (!this.blacks.get(i).equals(rhs.blacks.get(i))) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        if (this.boardSize != rhs.boardSize) {
            return false;
        }

        return true;
    }
}
