package Exceptions;

public class EmptyFieldException extends Exception {
    private String message;

    public EmptyFieldException() {
        this.message = "Ezen a mezőn épp nem áll bábu.";
    }

    public String getMessage() {
        return this.message;
    }
}
