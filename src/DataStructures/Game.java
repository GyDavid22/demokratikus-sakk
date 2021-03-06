package DataStructures;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;

import Exceptions.EmptyFieldException;
import Exceptions.NotEnoughPawnsException;
import Exceptions.OccupiedFieldException;

/**
 * A játékot vezérlő osztály.
 */
public class Game {
    private Board board;
    private Boolean blackSteps;
    private Boolean isGameOver;
    private String gameOverMessage;
    private int linesOfPawns;
    private Boolean aggressiveHit;
    private int boardSize;

    /**
     * askBoard() visszatérési értéke
     */
    public enum BoardValue {
        NONE, WHITE, BLACK
    }

    /**
     * @param linesOfPawns A játéktáblára felállítandó gyalogok sorainak száma per játékos.
     * @param boardSize Az n*n-es játéktábla kívánt oldalhossza.
     * @param aggressiveHit Amennyiben egy bábu le tud ütni valakit, megtegye-e biztosan?
     */
    public Game(int linesOfPawns, int boardSize, Boolean aggressiveHit) {
        this.linesOfPawns = linesOfPawns;
        this.board = new Board(boardSize);
        this.blackSteps = false;
        this.isGameOver = false;
        this.gameOverMessage = new String();
        this.aggressiveHit = aggressiveHit;
        try {
            if (linesOfPawns <= 0) {
                throw new NotEnoughPawnsException();
            }
            initialize();
        } catch (OccupiedFieldException e) {
            this.isGameOver = true;
            this.gameOverMessage = "A megadott paraméterekkel nem kezdhető játék.";
            System.err.println(gameOverMessage);
            e.printStackTrace();
        } catch (NotEnoughPawnsException e) {
            this.isGameOver = true;
            e.printStackTrace();
        }
        this.boardSize = boardSize;
    }

    /**
     * Játéktábla feltöltése a kezdeti játékállással. A konstruktor meghívja.
     * @throws OccupiedFieldException Akkor fordul elő, ha a gyalogok nem férnek el a táblán.
     */
    public void initialize() throws OccupiedFieldException {
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
    }

    /**
     * Az egyik fél egy lépését levezénylő függvény.
     */
    public void doRound() {
        if (this.isGameOver) {
            return;
        }
        ArrayList<Piece> possiblePieces = this.board.getList(blackSteps);
        ArrayList<Piece> ableToHit = new ArrayList<>();
        Random r = new Random();
        int chosenOne;

        for (Piece i : possiblePieces) {
            if (i.possibleHits().size() > 0) {
                ableToHit.add(i);
            }
        }

        try {
            if (ableToHit.size() > 0) {
                chosenOne = r.nextInt(0, ableToHit.size());
                ableToHit.get(chosenOne).doStep(this.aggressiveHit); // akik itt vannak, azoknak van fix lépése, nem
                                                                     // kell ellenőrizni
            } else {
                Boolean stepSucceeded = false;
                while (!stepSucceeded && possiblePieces.size() > 0) {
                    chosenOne = r.nextInt(0, possiblePieces.size());
                    stepSucceeded = possiblePieces.get(chosenOne).doStep(this.aggressiveHit);
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
            } catch (EmptyFieldException e) {
            }
        }
        for (int i = 0; i < this.board.getBoardSize(); ++i) {
            try {
                int[] testPos = { this.board.getBoardSize() - 1, i };
                if (this.board.isPieceBlack(testPos)) {
                    blackWon = true;
                    break;
                }
            } catch (EmptyFieldException e) {
            }
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

    public int getBoardSize() {
        return this.boardSize;
    }

    /**
     * Le lehet kérdezni vele, hogy a játéktábla adott mezőjén milyen bábu áll.
     * @param x Lekérdezni kívánt sor.
     * @param y Lekérdezni kívánt oszlop.
     * @return Fekete/fehér/semmi
     */
    public BoardValue askBoard(int x, int y) {
        int[] posArray = { x, y };
        if (this.board.isEmpty(posArray)) {
            return BoardValue.NONE;
        }
        try {
            if (this.board.isPieceBlack(posArray)) {
                return BoardValue.BLACK;
            }
        } catch (EmptyFieldException e) {
            System.err.println("Ennek nem kéne megtörténnie.");
            e.printStackTrace();
        }
        return BoardValue.WHITE;
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

    public String getGameOverMessage() {
        return this.gameOverMessage;
    }

    /**
     * Az osztály fájlba mentése az attribútumokkal együtt, rekurzív módon
     * @param path Mentési fájl kívánt relatív útvonala és neve, kiterjesztéssel.
     */
    public void save(String path) {
        try {
            File saveFile = new File(path);
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(saveFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            this.board.save(oos);
            oos.writeObject(this.blackSteps);
            oos.writeObject(this.isGameOver);
            oos.writeObject(this.gameOverMessage);
            oos.writeObject(this.linesOfPawns);
            oos.writeObject(this.aggressiveHit);
            oos.writeObject(this.boardSize);

            oos.close();
        } catch (Exception e) {
            System.err.println("A fájlbamentés meghiúsult.");
            e.printStackTrace();
        }
    }

    /**
     * Az osztály visszatöltése fájlból, az attribútumokkal együtt rekurzív módon.
     * @param path A betölteni kívánt fájl relatív útvonala, neve és kiterjesztése.
     * @return A betöltött objektum.
     */
    public static Game load(String path) {
        Game loadedGame = null;
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Board loaded = Board.load(ois);
            Boolean blackSteps = (Boolean) ois.readObject();
            Boolean isGameOver = (Boolean) ois.readObject();
            String gameOverMessage = (String) ois.readObject();
            int linesOfPawns = (int) ois.readObject();
            Boolean aggressiveHit = (Boolean) ois.readObject();
            int boardSize = (int) ois.readObject();

            loadedGame = new Game(linesOfPawns, boardSize, aggressiveHit);

            loadedGame.board = loaded;
            loadedGame.blackSteps = blackSteps;
            loadedGame.isGameOver = isGameOver;
            loadedGame.gameOverMessage = gameOverMessage;

            ois.close();
        } catch (Exception e) {
            System.err.println("A fájlból betöltés meghiúsult.");
            e.printStackTrace();
        } finally {
            File saveFile = new File(path);
            if (saveFile.exists()) {
                saveFile.delete();
            }
        }
        
        return loadedGame;
    }

    /**
     * Tartalmi, azaz nem referencia alapú egyezés vizsgálata
     * @param rhs Az objektum, amivel összehasonlítanánk.
     * @return True/false az egyezés függvényében.
     */
    public boolean equals(Game rhs) {
        if (!this.board.equals(rhs.board)) {
            return false;
        }
        if (!this.blackSteps.equals(rhs.blackSteps)) {
            return false;
        }
        if (!this.isGameOver.equals(rhs.isGameOver)) {
            return false;
        }
        if (!this.gameOverMessage.equals(rhs.gameOverMessage)) {
            return false;
        }
        if (this.linesOfPawns != rhs.linesOfPawns) {
            return false;
        }
        if (!this.aggressiveHit.equals(rhs.aggressiveHit)) {
            return false;
        }
        if (this.boardSize != rhs.boardSize) {
            return false;
        }

        return true;
    }
}
