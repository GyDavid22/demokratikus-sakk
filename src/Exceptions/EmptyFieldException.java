package Exceptions;

/**
 * Kivétel arra az esetre, ha olyan mezőn végeznénk műveletet bábuval, ami üres.
 */
public class EmptyFieldException extends Exception {
    private String message;

    public EmptyFieldException() {
        this.message = "Ezen a mezőn épp nem áll bábu.";
    }

    public String getMessage() {
        return this.message;
    }
}
