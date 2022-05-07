package Exceptions;

/**
 * Kivétel arra az esetre, ha olyan mezőre helyeznénk új bábut, ami már foglalt.
 */
public class OccupiedFieldException extends Exception {
    private String message;

    public OccupiedFieldException() {
        this.message = "Ez a mező épp foglalt.";
    }

    public String getMessage() {
        return this.message;
    }
    
}
