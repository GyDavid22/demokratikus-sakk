package Exceptions;

public class OccupiedFieldException extends Exception {
    private String message;

    public OccupiedFieldException() {
        this.message = "Ez a mező épp foglalt.";
    }

    public String getMessage() {
        return this.message;
    }
    
}
