import org.junit.Test;
import static org.junit.Assert.*;

public class ShipTest {
    @Test
    public void newShipIsNotSunk() {
        Ship ship = new Ship(3);
        assertEquals(3, ship.getSize());
        assertEquals(0, ship.getHits());
        assertFalse(ship.isSunk());
    }

    @Test
    public void shipSinksAfterHitsEqualToSize() {
        Ship ship = new Ship(2);
        ship.hit();
        assertFalse(ship.isSunk());
        ship.hit();
        assertTrue(ship.isSunk());
    }

    @Test
    public void hitsNeverExceedSize() {
        Ship ship = new Ship(2);
        ship.hit(); ship.hit(); ship.hit();
        assertEquals(2, ship.getHits());
    }

    @Test(expected = IllegalArgumentException.class)
    public void sizeZeroIsRejected() {
        new Ship(0);
    }
}
