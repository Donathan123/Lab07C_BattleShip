import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.*;

public class BoardTest {
    @Test
    public void newBoardIsAllBlank() {
        Board board = new Board();
        for (int r = 0; r < Board.SIZE; r++)
            for (int c = 0; c < Board.SIZE; c++)
                assertEquals(CellState.BLANK, board.getState(r, c));
    }

    @Test
    public void placeShipsPutsFiveShipsOn17CellsWithNoOverlap() {
        for (int seed = 0; seed < 200; seed++) {
            Board board = new Board(new Random(seed));
            board.placeShips();
            assertEquals(5, board.getShips().size());
            int occupied = 0;
            for (int r = 0; r < Board.SIZE; r++)
                for (int c = 0; c < Board.SIZE; c++)
                    if (board.getShipAt(r, c) != null) occupied++;
            assertEquals(17, occupied); // 5+4+3+3+2, so no overlaps
        }
    }

    @Test
    public void placedShipsAreStraightAndContiguous() {
        Board board = new Board(new Random(42));
        board.placeShips();
        for (Ship ship : board.getShips()) {
            int[] first = ship.getCells().get(0);
            int[] last = ship.getCells().get(ship.getSize() - 1);
            boolean horizontal = first[0] == last[0];
            boolean vertical = first[1] == last[1];
            assertTrue(horizontal || vertical);
            int span = horizontal ? last[1] - first[1] : last[0] - first[0];
            assertEquals(ship.getSize() - 1, span);
        }
    }

    @Test
    public void putShipRejectsOverlap() {
        Board board = new Board();
        assertTrue(board.putShip(new Ship(3), 0, 0, true));
        assertFalse(board.putShip(new Ship(3), 1, 0, false)); // column 1 crosses row 0
        assertFalse(board.putShip(new Ship(3), 0, 8, true));  // runs off the board
    }

    @Test
    public void fireAtReportsMissHitSunkAndAlreadyFired() {
        Board board = new Board();
        board.putShip(new Ship(2), 0, 0, true); // cells (0,0) and (0,1)
        assertEquals(ShotResult.MISS, board.fireAt(5, 5));
        assertEquals(CellState.MISS, board.getState(5, 5));
        assertEquals(ShotResult.HIT, board.fireAt(0, 0));
        assertEquals(CellState.HIT, board.getState(0, 0));
        assertEquals(ShotResult.SUNK, board.fireAt(0, 1));
        assertEquals(ShotResult.ALREADY_FIRED, board.fireAt(0, 1));
        assertTrue(board.allShipsSunk());
    }
}
