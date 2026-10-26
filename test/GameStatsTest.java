import org.junit.Test;
import static org.junit.Assert.*;

public class GameStatsTest {
    @Test
    public void fiveMissesInARowMakeAStrike() {
        GameStats stats = new GameStats();
        for (int i = 0; i < 4; i++) assertFalse(stats.recordMiss());
        assertTrue(stats.recordMiss());
        assertEquals(1, stats.getStrikeCount());
        assertEquals(0, stats.getMissCount());
        assertEquals(5, stats.getTotalMisses());
    }

    @Test
    public void hitResetsMissCounter() {
        GameStats stats = new GameStats();
        stats.recordMiss(); stats.recordMiss(); stats.recordMiss();
        stats.recordHit();
        assertEquals(0, stats.getMissCount());
        assertEquals(3, stats.getTotalMisses());
        assertEquals(1, stats.getTotalHits());
        assertEquals(0, stats.getStrikeCount());
    }

    @Test
    public void threeStrikesIsALoss() {
        GameStats stats = new GameStats();
        for (int i = 0; i < 15; i++) stats.recordMiss();
        assertEquals(3, stats.getStrikeCount());
        assertTrue(stats.isOutOfStrikes());
    }

    @Test
    public void resetZeroesEverything() {
        GameStats stats = new GameStats();
        stats.recordHit(); stats.recordMiss();
        stats.reset();
        assertEquals(0, stats.getMissCount());
        assertEquals(0, stats.getStrikeCount());
        assertEquals(0, stats.getTotalMisses());
        assertEquals(0, stats.getTotalHits());
    }
}
