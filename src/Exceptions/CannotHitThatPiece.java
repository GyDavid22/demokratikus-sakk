package Exceptions;

public class CannotHitThatPiece extends Exception {
    private String message;

    public CannotHitThatPiece() {
        this.message = "A mozgatandó bábu nem ütheti le az adott mezőn már álló bábut.";
    }

    public String getMessage() {
        return this.message;
    }
}
