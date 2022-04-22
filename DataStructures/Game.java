package DataStructures;

import Exceptions.OccupiedFieldException;

public class Game {
    private Board board;
    private Boolean blackSteps;

    public Game() {
        this.board = new Board();
        initialize();
        this.blackSteps = false;
    }

    public void initialize() {
        try {
            for (int i = 0; i < 2; ++i) {
                for (int j = 0; j < board.getBoardSize(); ++j) {
                    int[] actpos = {i, j};
                    board.placePiece(new Pawn(true, actpos, board));
                }
            }
            for (int i = board.getBoardSize() - 2; i < board.getBoardSize(); ++i) {
                for (int j = 0; j < board.getBoardSize(); ++j) {
                    int[] actpos = {i, j};
                    board.placePiece(new Pawn(false, actpos, board));
                }
            }
        } catch (OccupiedFieldException e) {
            System.err.println(e.getMessage());
            System.err.println("Ennek nem kéne megtörténnie...");
        }
    }

    public String toString() {
        return this.board.toString();
    }
}
