import static org.junit.jupiter.api.Assertions.*;

class Index2DTest {

    @org.junit.jupiter.api.Test
    void getX() {
        Index2D p = new Index2D(3, 7);
        assertEquals(3, p.getX());
    }

    @org.junit.jupiter.api.Test
    void getY() {
        Index2D p = new Index2D(3, 7);
        assertEquals(7, p.getY());
    }

    @org.junit.jupiter.api.Test
    void distance2D() {
        Index2D p1 = new Index2D(0, 0);
        Index2D p2 = new Index2D(3, 4);
        assertEquals(5.0, p1.distance2D(p2), 1e-9);
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        Index2D p = new Index2D(5, -2);
        assertEquals("5,-2", p.toString());
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
        Index2D p1 = new Index2D(4, 6);
        Index2D p2 = new Index2D(4, 6);
        Index2D p3 = new Index2D(4, 7);

        assertTrue(p1.equals(p2));
        assertFalse(p1.equals(p3));
        assertFalse(p1.equals(null));
    }
}