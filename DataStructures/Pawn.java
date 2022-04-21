package DataStructures;

public class Pawn extends Piece {
    private Boolean isFirstStep;

    Pawn(Boolean isBlack, int[] pos, Board partOf) {
        super(isBlack, pos, partOf, "gyalog");
        this.isFirstStep = true;
    }

    void doStep() {
        // todo

        if (this.isFirstStep) {
            this.isFirstStep = false;
        }
    }
}
