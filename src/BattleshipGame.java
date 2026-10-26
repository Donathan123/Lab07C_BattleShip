/**
 * Game logic: ties the Board to the GameStats and decides when the player wins or loses.
 * Has no Swing code so it can be unit tested.
 */
public class BattleshipGame {
    private final Board board;
    private final GameStats stats = new GameStats();
    private boolean over;

    public BattleshipGame() {
        this(new Board());
    }

    public BattleshipGame(Board board) {
        this.board = board;
    }

    /** Starts a new game with freshly placed ships and zeroed counters. */
    public void newGame() {
        board.placeShips();
        stats.reset();
        over = false;
    }

    /** Fires at a cell and updates the counters. */
    public ShotResult fire(int row, int col) {
        if (over) {
            return ShotResult.ALREADY_FIRED;
        }
        ShotResult result = board.fireAt(row, col);
        switch (result) {
            case HIT, SUNK -> stats.recordHit();
            case MISS -> stats.recordMiss();
            default -> { }
        }
        if (isWon() || isLost()) {
            over = true;
        }
        return result;
    }

    public boolean isWon() {
        return board.allShipsSunk();
    }

    public boolean isLost() {
        return stats.isOutOfStrikes();
    }

    public boolean isOver() {
        return over;
    }

    public Board getBoard() { return board; }
    public GameStats getStats() { return stats; }
}
