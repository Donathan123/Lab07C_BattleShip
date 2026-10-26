import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/** Runs every JUnit test in the project at once. */
@RunWith(Suite.class)
@Suite.SuiteClasses({ShipTest.class, BoardTest.class, GameStatsTest.class, BattleshipGameTest.class})
public class AllTests {
}
