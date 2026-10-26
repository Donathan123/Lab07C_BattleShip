/**
 * Keeps the four counters shown in the status area and applies the strike rules.
 */
public class GameStats {
    public static final int MISSES_PER_STRIKE = 5;
    public static final int STRIKES_TO_LOSE = 3;

    private int missCount;
    private int strikeCount;
    private int totalMisses;
    private int totalHits;

    public void reset() {
        missCount = 0;
        strikeCount = 0;
        totalMisses = 0;
        totalHits = 0;
    }

    /** A hit adds to the total hits and resets the miss counter. */
    public void recordHit() {
        totalHits++;
        missCount = 0;
    }

    /**
     * A miss adds to the miss counters. Five misses in a row turns into a strike.
     * @return true if this miss caused a new strike
     */
    public boolean recordMiss() {
        missCount++;
        totalMisses++;
        if (missCount >= MISSES_PER_STRIKE) {
            strikeCount++;
            missCount = 0;
            return true;
        }
        return false;
    }

    public boolean isOutOfStrikes() {
        return strikeCount >= STRIKES_TO_LOSE;
    }

    public int getMissCount() { return missCount; }
    public int getStrikeCount() { return strikeCount; }
    public int getTotalMisses() { return totalMisses; }
    public int getTotalHits() { return totalHits; }
}
