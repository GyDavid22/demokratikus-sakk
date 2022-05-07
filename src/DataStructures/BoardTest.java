package DataStructures;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Exceptions.*;
import Tests.*;

@Category({GlobalTestsInterface.class})
public class BoardTest {
    Board testSubject;

    @Before
    public void init() {
        this.testSubject = new Board(8);
    }

    @Test
    public void placeTest() {
        int[] pos = { 0, 0 };
        try {
            this.testSubject.placePiece(new Pawn(false, pos, this.testSubject));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void occupiedTest() {
        int[] pos = { 0, 0 };
        try {
            this.testSubject.placePiece(new Pawn(false, pos, this.testSubject));
        } catch (Exception e) {
            fail();
        }
        assertThrows(OccupiedFieldException.class, () -> {
            this.testSubject.placePiece(new Pawn(false, pos, this.testSubject));
        });
    }

    @Test
    public void wrongHitTest() {
        int[] pos = { 0, 0 };
        try {
            this.testSubject.placePiece(new Pawn(false, pos, this.testSubject));
        } catch (Exception e) {
            fail();
        }
        assertThrows(CannotHitThatPiece.class, () -> {
            int[] newPos = { 1, 0 };
            Pawn toMove = new Pawn(false, newPos, this.testSubject);
            int[] destPos = { 0, 0 };
            this.testSubject.movePiece(toMove, destPos);
        });
    }

    @Test
    public void correctHitTest() {
        int[] pos = { 0, 0 };
        try {
            this.testSubject.placePiece(new Pawn(false, pos, this.testSubject));
            int[] newPos = { 1, 0 };
            Pawn toMove = new Pawn(true, newPos, this.testSubject);
            this.testSubject.placePiece(toMove);
            int[] destPos = { 0, 0 };
            this.testSubject.movePiece(toMove, destPos);
        } catch (Exception e) {
            fail(e.toString());
        }
    }
}
