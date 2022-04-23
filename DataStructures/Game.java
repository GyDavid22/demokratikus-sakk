package DataStructures;

import java.util.ArrayList;
import java.util.Random;

import Exceptions.EmptyFieldException;
import Exceptions.OccupiedFieldException;

public class Game {
    private Board board;
    private Boolean blackSteps;
    private Boolean isGameOver;
    private String gameOverMessage;
    private int linesOfPawns;

    public Game(int linesOfPawns, int boardSize) {
        this.linesOfPawns = linesOfPawns;
        this.board = new Board(boardSize);
        initialize();
        this.blackSteps = false;
        this.isGameOver = false;
        this.gameOverMessage = new String();
    }

    public void initialize() {
        try {
            for (int i = 0; i < this.linesOfPawns; ++i) {
                for (int j = 0; j < board.getBoardSize(); ++j) {
                    int[] actpos = { i, j };
                    board.placePiece(new Pawn(true, actpos, board));
                }
            }
            for (int i = board.getBoardSize() - this.linesOfPawns; i < board.getBoardSize(); ++i) {
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
            int chosenOne;

            for (Piece i : possiblePieces) {
                i.possibleHits();
                if (i.canHit()) {
                    ableToHit.add(i);
                    i.nowCantHit();
                }
            }

            try {
                if (ableToHit.size() > 0) {
                    chosenOne = r.nextInt(0, ableToHit.size());
                    ableToHit.get(chosenOne).doStep(); // akik itt vannak, azoknak van fix lépése, nem kell ellenőrizni
                } else {
                    Boolean stepSucceeded = false;
                    while (!stepSucceeded && possiblePieces.size() > 0) {
                        chosenOne = r.nextInt(0, possiblePieces.size());
                        stepSucceeded = possiblePieces.get(chosenOne).doStep();
                        if (!stepSucceeded) {
                            possiblePieces.remove(chosenOne);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Boolean whiteWon = false;
            Boolean blackWon = false;
            for (int i = 0; i < this.board.getBoardSize(); ++i) {
                try {
                    int[] testPos = { 0, i };
                    if (!this.board.isPieceBlack(testPos)) {
                        whiteWon = true;
                        break;
                    }
                } catch (EmptyFieldException e) { }
            }
            for (int i = 0; i < this.board.getBoardSize(); ++i) {
                try {
                    int[] testPos = { this.board.getBoardSize() - 1, i };
                    if (this.board.isPieceBlack(testPos)) {
                        blackWon = true;
                        break;
                    }
                } catch (EmptyFieldException e) { }
            }

            if (this.board.numOfPieces(true) == 0 || whiteWon) {
                this.isGameOver = true;
                this.gameOverMessage = "A fehér nyert.";
            } else if (this.board.numOfPieces(false) == 0 || blackWon) {
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
