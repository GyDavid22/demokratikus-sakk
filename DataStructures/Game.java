package DataStructures;

import java.util.ArrayList;
import java.util.Random;

import Exceptions.OccupiedFieldException;

public class Game {
    private Board board;
    private Boolean blackSteps;
    private static int linesOfPawns = 2;
    private Boolean isGameOver;
    private String gameOverMessage;

    public Game() {
        this.board = new Board();
        initialize();
        this.blackSteps = false;
        this.isGameOver = false;
        this.gameOverMessage = new String();
    }

    public void initialize() {
        try {
            for (int i = 0; i < Game.linesOfPawns; ++i) {
                for (int j = 0; j < board.getBoardSize(); ++j) {
                    int[] actpos = { i, j };
                    board.placePiece(new Pawn(true, actpos, board));
                }
            }
            for (int i = board.getBoardSize() - Game.linesOfPawns; i < board.getBoardSize(); ++i) {
                for (int j = 0; j < board.getBoardSize(); ++j) {
                    int[] actpos = { i, j };
                    board.placePiece(new Pawn(false, actpos, board));
                }
            }
        } catch (OccupiedFieldException e) {
            e.printStackTrace();
        }
    }

    public void doRound() {
        if (!this.isGameOver) {
            ArrayList<Piece> possiblePieces = this.board.getList(blackSteps);
            ArrayList<Piece> ableToHit = new ArrayList<>();
            Random r = new Random();

            for (Piece i : possiblePieces) {
                i.possibleHits();
                if (i.canHit()) {
                    ableToHit.add(i);
                }
            }

            try {
                if (ableToHit.size() > 0) {
                    ableToHit.get(r.nextInt(0, ableToHit.size())).doStep(); // akik itt vannak, azoknak van fix lépése, nem kell ellenőrizni
                } else {
                    Boolean stepSucceeded = false;
                    while (!stepSucceeded && possiblePieces.size() > 0) {
                        int chosenOne = r.nextInt(0, possiblePieces.size());
                        stepSucceeded = possiblePieces.get(chosenOne).doStep();
                        if (!stepSucceeded) {
                            possiblePieces.remove(chosenOne);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (this.board.numOfPieces(true) == 0) {
                this.isGameOver = true;
                this.gameOverMessage = "A fehér nyert.";
            } else if (this.board.numOfPieces(false) == 0) {
                this.isGameOver = true;
                this.gameOverMessage = "A fekete nyert.";
            } else if (possiblePieces.size() == 0) {
                this.isGameOver = true;
                this.gameOverMessage = "Patthelyzet.";
            }
            this.blackSteps = !this.blackSteps;
        }
    }

    public String toString() {
        if (!this.isGameOver) {
            return this.board.toString();
        }
        return this.board.toString() + this.gameOverMessage;
    }

    public boolean isGameOver() {
        return this.isGameOver;
    }
}
