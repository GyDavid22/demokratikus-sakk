package DataStructures;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Tests.*;

@Category({GlobalTestsInterface.class})
public class PawnTest {
    Board testField;
    Pawn black;

    @Before
    public void init() {
        this.testField = new Board(8);
        int[] pos = {0, 0};
        this.black = new Pawn(true, pos, this.testField);
        try {
            this.testField.placePiece(this.black);
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public void stepTest() {
        int[] pos = this.black.getPos();
        for (int i = 0; i < 3; ++i) {
            try {
                this.black.doStep(true);
            } catch (Exception e) {
                fail(e.toString());
            }
            assertFalse("A bábu nem lépett.", this.black.getPos().equals(pos));
            pos = this.black.getPos();
        }
    }

    @Test
    public void hitTest() {
        int[] pos = {2, 0};
        Pawn toHit = new Pawn(false, pos, this.testField);
        try {
            this.testField.placePiece(toHit);
            assertTrue(this.testField.getList(true).size() > 0);
            assertTrue(this.testField.getList(false).size() > 0);
            assertFalse(this.testField.isPieceBlack(pos));
            assertTrue(this.black.possibleHits().size() > 0);
            this.black.doStep(true);
            assertTrue(this.testField.isPieceBlack(pos));
            assertTrue(this.testField.getList(true).size() > 0);
            assertTrue(this.testField.getList(false).size() == 0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void wrongHitTest() {
        int[] pos = {2, 0};
        Pawn toHit = new Pawn(true, pos, this.testField);
        try {
            this.testField.placePiece(toHit);
            assertTrue(this.testField.getList(true).size() > 0);
            assertTrue(this.testField.getList(false).size() == 0);
            assertTrue(this.testField.isPieceBlack(pos));
            assertTrue(this.black.possibleHits().size() == 0);
            assertTrue(this.black.possibleSteps(true).size() > 0);
            this.black.doStep(true);
            assertTrue(this.testField.isPieceBlack(pos));
            assertTrue(this.testField.getList(true).size() > 0);
            assertTrue(this.testField.getList(false).size() == 0);
        } catch (Exception e) {
            fail();
        }
    }
}
