package DataStructures;

abstract class Piece {
    private Boolean isBlack;
    private String type;
    private int[] pos;
    private Board partOf;

    Piece(Boolean isBlack, int[] pos, Board partOf, String type) {
        this.isBlack = isBlack;
        this.pos = pos;
        this.partOf = partOf;
        this.type = type;
    }

    abstract void doStep();

    boolean isBlack() {
        return this.isBlack;
    }

    String getType() {
        return this.type;
    }
    
    int[] getPos() {
        return this.pos;
    }

    boolean setPos(int[] pos) {
        /**
         * Visszatérési értéke megadja a művelet sikerességét.
        */
        if (partOf.isEmpty(pos)) {
            this.pos = pos;
            return true;
        }
        return false;
    }
}
