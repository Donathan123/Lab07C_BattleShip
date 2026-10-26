import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The 10x10 Battleship board. Places the ships randomly (no overlaps) and resolves shots.
 */
public class Board {
    public static final int SIZE = 10;
    public static final int[] SHIP_SIZES = {5, 4, 3, 3, 2}; // placed largest first

    private final Ship[][] shipGrid = new Ship[SIZE][SIZE];
    private final CellState[][] states = new CellState[SIZE][SIZE];
    private final List<Ship> ships = new ArrayList<>();
    private final Random random;

    public Board() {
        this(new Random());
    }

    public Board(Random random) {
        this.random = random;
        clear();
    }

    /** Empties the board and marks every cell BLANK. */
    public void clear() {
        ships.clear();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                shipGrid[r][c] = null;
                states[r][c] = CellState.BLANK;
            }
        }
    }

    /** Clears the board and randomly places all five ships. */
    public void placeShips() {
        clear();
        for (int size : SHIP_SIZES) {
            placeShip(new Ship(size));
        }
    }

    /**
     * Places one ship: pick a random orientation and row/column, scan that line for every
     * run of empty cells long enough for the ship, and pick one of those at random.
     * If the line has no room, try another line.
     */
    private void placeShip(Ship ship) {
        while (true) {
            boolean horizontal = random.nextBoolean();
            int line = random.nextInt(SIZE);
            List<Integer> starts = new ArrayList<>();
            for (int start = 0; start <= SIZE - ship.getSize(); start++) {
                if (isRunEmpty(line, start, ship.getSize(), horizontal)) {
                    starts.add(start);
                }
            }
            if (!starts.isEmpty()) {
                int start = starts.get(random.nextInt(starts.size()));
                putShip(ship, line, start, horizontal);
                return;
            }
        }
    }

    private boolean isRunEmpty(int line, int start, int length, boolean horizontal) {
        for (int i = start; i < start + length; i++) {
            int r = horizontal ? line : i;
            int c = horizontal ? i : line;
            if (shipGrid[r][c] != null) {
                return false;
            }
        }
        return true;
    }

    /** Puts a ship on the board at an exact spot. Returns false if it doesn't fit. */
    public boolean putShip(Ship ship, int line, int start, boolean horizontal) {
        if (start < 0 || start + ship.getSize() > SIZE || line < 0 || line >= SIZE
                || !isRunEmpty(line, start, ship.getSize(), horizontal)) {
            return false;
        }
        for (int i = start; i < start + ship.getSize(); i++) {
            int r = horizontal ? line : i;
            int c = horizontal ? i : line;
            shipGrid[r][c] = ship;
            ship.addCell(r, c);
        }
        ships.add(ship);
        return true;
    }

    /** Fires a missile at (row, col). */
    public ShotResult fireAt(int row, int col) {
        if (states[row][col] != CellState.BLANK) {
            return ShotResult.ALREADY_FIRED;
        }
        Ship ship = shipGrid[row][col];
        if (ship == null) {
            states[row][col] = CellState.MISS;
            return ShotResult.MISS;
        }
        states[row][col] = CellState.HIT;
        ship.hit();
        return ship.isSunk() ? ShotResult.SUNK : ShotResult.HIT;
    }

    public boolean allShipsSunk() {
        if (ships.isEmpty()) {
            return false;
        }
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    public CellState getState(int row, int col) {
        return states[row][col];
    }

    public Ship getShipAt(int row, int col) {
        return shipGrid[row][col];
    }

    public List<Ship> getShips() {
        return ships;
    }

    /** Text map of ship locations (used for debugging). */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("   0 1 2 3 4 5 6 7 8 9\n");
        for (int r = 0; r < SIZE; r++) {
            sb.append(r).append("  ");
            for (int c = 0; c < SIZE; c++) {
                sb.append(shipGrid[r][c] == null ? "." : String.valueOf(shipGrid[r][c].getSize())).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
