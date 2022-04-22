package DataStructures;

abstract class Piece {
    private Boolean isBlack;
    private String type;
    private int[] pos;

    Piece(Boolean isBlack, int[] pos, String type) {
        this.isBlack = isBlack;
        this.pos = pos;
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

    void setPos(int[] pos) {
        this.pos = pos;
    }

    abstract boolean canStep();
}
