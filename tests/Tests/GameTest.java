package Tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import DataStructures.*;

public class GameTest {
    Game testSubject;

    @Before
    public void load() {
        this.testSubject = new Game(2, 8, true);
    }

    @Test(timeout = 1000)
    public void hasEnd() {
        while (!this.testSubject.isGameOver()) {
            this.testSubject.doRound();
        }
        assertTrue(this.testSubject.isGameOver());
    }

    @Test
    public void loading() {
        File testSave = new File("test.bin");
        for (int i = 0; i < 5; ++i) {
            this.testSubject.doRound();
        }
        this.testSubject.save(testSave.getName());
        Game loaded = Game.load(testSave.getName());
        testSave.delete();
        assertNotNull(loaded);
        assertTrue("A két játék nem egyezik.", this.testSubject.equals(loaded));
    }

    @Test
    public void winningIsCorrent() {
        while (!this.testSubject.isGameOver()) {
            this.testSubject.doRound();
        }
        Boolean wonByRules = false;
        for (int i = 0; i < this.testSubject.getBoardSize(); ++i) {
            if (this.testSubject.askBoard(0, i) == Game.BoardValue.WHITE) {
                wonByRules = true;
                break;
            }
        }
        for (int i = 0; i < this.testSubject.getBoardSize(); ++i) {
            if (this.testSubject.askBoard(this.testSubject.getBoardSize() - 1, i) == Game.BoardValue.BLACK) {
                wonByRules = true;
                break;
            }
        }
        int whites, blacks;
        whites = blacks = 0;
        for (int i = 0; i < this.testSubject.getBoardSize(); ++i) {
            for (int j = 0; j < this.testSubject.getBoardSize(); ++j) {
                if (this.testSubject.askBoard(i, j) == Game.BoardValue.WHITE) {
                    whites++;
                } else if (this.testSubject.askBoard(i, j) == Game.BoardValue.BLACK) {
                    blacks++;
                }
            }
        }
        if (whites == 0 ^ blacks == 0) {
            wonByRules = true;
        }
        if (this.testSubject.getGameOverMessage().equals("Patthelyzet.")) {
            wonByRules = true;
        }
        assertTrue("A játék nem a kívánt módon ért véget.", wonByRules);
    }
}
