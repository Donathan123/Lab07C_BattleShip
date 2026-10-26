import java.util.ArrayList;
import java.util.List;

/**
 * A ship of a given size. Knows which cells it occupies and how many times it has been hit.
 */
public class Ship {
    private final int size;
    private final List<int[]> cells = new ArrayList<>();
    private int hits;

    public Ship(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Ship size must be at least 1");
        }
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    /** Records that this ship occupies the given cell. */
    public void addCell(int row, int col) {
        cells.add(new int[]{row, col});
    }

    public List<int[]> getCells() {
        return cells;
    }

    public int getHits() {
        return hits;
    }

    /** Registers a hit on this ship. */
    public void hit() {
        if (hits < size) {
            hits++;
        }
    }

    public boolean isSunk() {
        return hits >= size;
    }
}
