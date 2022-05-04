package Exceptions;

public class NotEnoughPawnsException extends Exception {
    private String message;

    public NotEnoughPawnsException() {
        this.message = "Minimum 1 sor gyalogra szükség van!";
    }

    public String getMessage() {
        return this.message;
    }

}
