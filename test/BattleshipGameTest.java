import org.junit.Test;
import java.util.Random;
import static org.junit.Assert.*;

public class BattleshipGameTest {
    @Test
    public void hittingEveryShipCellWinsTheGame() {
        BattleshipGame game = new BattleshipGame(new Board(new Random(7)));
        game.newGame();
        Board board = game.getBoard();
        for (int r = 0; r < Board.SIZE; r++)
            for (int c = 0; c < Board.SIZE; c++)
                if (board.getShipAt(r, c) != null) game.fire(r, c);
        assertTrue(game.isWon());
        assertTrue(game.isOver());
        assertEquals(17, game.getStats().getTotalHits());
    }

    @Test
    public void fifteenMissesInARowLosesTheGame() {
        BattleshipGame game = new BattleshipGame(new Board(new Random(7)));
        game.newGame();
        Board board = game.getBoard();
        int misses = 0;
        for (int r = 0; r < Board.SIZE && misses < 15; r++)
            for (int c = 0; c < Board.SIZE && misses < 15; c++)
                if (board.getShipAt(r, c) == null) { game.fire(r, c); misses++; }
        assertTrue(game.isLost());
        assertTrue(game.isOver());
        assertEquals(ShotResult.ALREADY_FIRED, game.fire(9, 9)); // no moves after game over
    }

    @Test
    public void newGameResetsTheCounters() {
        BattleshipGame game = new BattleshipGame();
        game.newGame();
        game.fire(0, 0);
        game.newGame();
        assertEquals(0, game.getStats().getTotalHits() + game.getStats().getTotalMisses());
        assertEquals(CellState.BLANK, game.getBoard().getState(0, 0));
    }
}
