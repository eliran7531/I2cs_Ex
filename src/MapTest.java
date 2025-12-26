import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Intro2CS, 2026A, this is a very
 */
class MapTest {
    /**
     */
    private int[][] _map_3_3 = {{0,1,0}, {1,0,1}, {0,1,0}};
    private Map2D _m0, _m1, _m3_3;
    @BeforeEach
    public void setuo() {
        _m0 = new Map(1);
        _m1 = new Map(1);
        _m3_3 = new Map(_map_3_3);
    }

    void init() {
        int[][] bigarr = new int [500][500];
        _m1.init(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        Pixel2D p1 = new Index2D(3,2);
        _m1.fill(p1,1, true);
    }

    @Test
    void testInit() {
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0, _m1);
    }
    @Test
    void testEquals() {
        assertEquals(_m0,_m1);
        _m0.init(_map_3_3);
        _m1.init(_map_3_3);
        assertEquals(_m0,_m1);
    }

    @Test
    void testInit1() {
        Map m = new Map(4, 3, 7);
        assertEquals(4, m.getWidth());
        assertEquals(3, m.getHeight());
        for (int x = 0; x < m.getWidth(); x++) {
            for (int y = 0; y < m.getHeight(); y++) {
                assertEquals(7, m.getPixel(x, y));
            }
        }
    }

    @Test
    void testInit2() {
        int[][] a = {
                {1, 2, 3},
                {4, 5, 6}
        };
        Map m = new Map(a);
        assertEquals(2, m.getWidth());
        assertEquals(3, m.getHeight());
        assertEquals(1, m.getPixel(0,0));
        assertEquals(6, m.getPixel(1,2));

        a[0][0] = 99;
        assertEquals(1, m.getPixel(0,0));
    }

    @Test
    void getMap() {
        Map m = new Map(3, 2, 9);
        int[][] b = m.getMap();
        assertNotNull(b);
        assertEquals(3, b.length);
        assertEquals(2, b[0].length);

        b[0][0] = 12345;
        assertEquals(9, m.getPixel(0,0));
    }

    @Test
    void getWidth() {
        Map m = new Map(5, 1, 0);
        assertEquals(5, m.getWidth());

}

    @Test
    void getHeight() {
        Map m = new Map(1, 6, 0);
        assertEquals(6, m.getHeight());
    }

    @Test
    void getPixel() {
        Map m = new Map(3, 3, 0);
        m.setPixel(2, 1, 8);
        assertEquals(8, m.getPixel(2, 1));
        assertEquals(0, m.getPixel(0, 0));
    }

    @Test
    void testGetPixel() {
        Map m = new Map(3, 3, 0);
        m.setPixel(1, 2, 11);
        Pixel2D p = new Index2D(1, 2);
        assertEquals(11, m.getPixel(p));
    }

    @Test
    void setPixel() {
        Map m = new Map(2, 2, 5);
        m.setPixel(0, 1, 7);
        assertEquals(7, m.getPixel(0, 1));
        assertEquals(5, m.getPixel(1, 1));
    }

    @Test
    void testSetPixel() {
        Map m = new Map(2, 2, 0);
        Pixel2D p = new Index2D(1, 0);
        m.setPixel(p, 6);
        assertEquals(6, m.getPixel(1, 0));
    }

    @Test
    void isInside() {
        Map m = new Map(4, 3, 0);
        assertTrue(m.isInside(new Index2D(0,0)));
        assertTrue(m.isInside(new Index2D(3,2)));
        assertFalse(m.isInside(new Index2D(-1,0)));
        assertFalse(m.isInside(new Index2D(4,0)));
        assertFalse(m.isInside(new Index2D(0,3)));
    }

    @Test
    void sameDimensions() {
        Map a = new Map(4, 3, 0);
        Map b = new Map(4, 3, 1);
        Map c = new Map(3, 4, 0);
        assertTrue(a.sameDimensions(b));
        assertFalse(a.sameDimensions(c));
    }

    @Test
    void addMap2D() {
        Map a = new Map(2, 2, 0);
        Map b = new Map(2, 2, 0);

        a.setPixel(0,0,1); a.setPixel(1,0,2);
        a.setPixel(0,1,3); a.setPixel(1,1,4);

        b.setPixel(0,0,10); b.setPixel(1,0,20);
        b.setPixel(0,1,30); b.setPixel(1,1,40);

        a.addMap2D(b);

        assertEquals(11, a.getPixel(0,0));
        assertEquals(22, a.getPixel(1,0));
        assertEquals(33, a.getPixel(0,1));
        assertEquals(44, a.getPixel(1,1));

        assertEquals(10, b.getPixel(0,0));
    }

    @Test
    void mul() {
        Map m = new Map(2, 2, 0);
        m.setPixel(0,0,2);
        m.setPixel(1,0,4);
        m.setPixel(0,1,6);
        m.setPixel(1,1,8);

        m.mul(0.5);
        assertEquals(1, m.getPixel(0,0));
        assertEquals(2, m.getPixel(1,0));
        assertEquals(3, m.getPixel(0,1));
        assertEquals(4, m.getPixel(1,1));

        m.mul(2);
        assertEquals(2, m.getPixel(0,0));
        assertEquals(4, m.getPixel(1,0));
        assertEquals(6, m.getPixel(0,1));
        assertEquals(8, m.getPixel(1,1));
    }

    @Test
    void rescale() {
        int[][] a = {
                {1, 3},  // x=0
                {2, 4}   // x=1
        };
        Map m = new Map(a);

        m.rescale(2.0, 2.0);
        assertEquals(4, m.getWidth());
        assertEquals(4, m.getHeight());

        assertEquals(1, m.getPixel(0,0));
        assertEquals(1, m.getPixel(1,0));
        assertEquals(2, m.getPixel(2,0));
        assertEquals(2, m.getPixel(3,0));

        assertEquals(3, m.getPixel(0,2));
        assertEquals(4, m.getPixel(3,3));
    }

    @Test
    void drawCircle() {
        Map m = new Map(11, 11, 0);
        Pixel2D c = new Index2D(5,5);
        m.drawCircle(c, 2.0, 7);

        assertEquals(7, m.getPixel(5,5));

        assertEquals(7, m.getPixel(7,5));
        assertEquals(7, m.getPixel(3,5));
        assertEquals(7, m.getPixel(5,7));
        assertEquals(7, m.getPixel(5,3));

        assertEquals(0, m.getPixel(8,5));
    }

    @Test
    void drawLine() {
        Map m = new Map(6, 6, 0);
        m.drawLine(new Index2D(0,0), new Index2D(5,5), 9);

        for (int i = 0; i <= 5; i++) {
            assertEquals(9, m.getPixel(i,i));
        }

        assertEquals(0, m.getPixel(0,1));
    }

    @Test
    void drawRect() {
        Map m = new Map(5, 5, 0);
        m.drawRect(new Index2D(1,1), new Index2D(3,3), 4);

        for (int x = 1; x <= 3; x++) {
            for (int y = 1; y <= 3; y++) {
                assertEquals(4, m.getPixel(x,y));
            }
        }

        assertEquals(0, m.getPixel(0,0));
        assertEquals(0, m.getPixel(4,4));
    }

    @Test
    void testEquals1() {
        Map a = new Map(3, 2, 0);
        Map b = new Map(3, 2, 0);
        assertEquals(a, b);

        b.setPixel(1, 1, 7);
        assertNotEquals(a, b);

        a.setPixel(1, 1, 7);
        assertEquals(a, b);

    }

    @Test
    void fill() {
        int[][] data = {
                {0,0,0},  // x=0
                {0,1,0},  // x=1
                {1,1,0},  // x=2
                {0,0,0}   // x=3
        };
        Map m = new Map(data);

        int changed = m.fill(new Index2D(0,0), 5, false);
        assertTrue(changed > 0);

        assertEquals(5, m.getPixel(0,0));
        assertEquals(5, m.getPixel(0,1));
        assertEquals(5, m.getPixel(0,2));

        assertEquals(1, m.getPixel(2,0));
        assertEquals(1, m.getPixel(1,1));

        assertEquals(0, m.getPixel(3,0));
        assertEquals(0, m.getPixel(3,1));
    }

    @Test
    void shortestPath() {
        int[][] data = {
                {0,1,0},  // x=0
                {0,1,0},  // x=1
                {0,0,0},  // x=2
                {0,1,0}   // x=3
        };
        Map m = new Map(data);

        Pixel2D s = new Index2D(0,0);
        Pixel2D t = new Index2D(3,2);

        Pixel2D[] path = m.shortestPath(s, t, 1, false);
        assertNotNull(path);
        assertTrue(path.length > 0);

        assertEquals(s.getX(), path[0].getX());
        assertEquals(s.getY(), path[0].getY());
        assertEquals(t.getX(), path[path.length-1].getX());
        assertEquals(t.getY(), path[path.length-1].getY());

        for (int i = 1; i < path.length; i++) {
            int dx = Math.abs(path[i].getX() - path[i-1].getX());
            int dy = Math.abs(path[i].getY() - path[i-1].getY());
            assertEquals(1, dx + dy);
        }

        for (Pixel2D p : path) {
            assertNotEquals(1, m.getPixel(p));
        }
    }

    @Test
    void allDistance() {
        int[][] data = {
                {0,0,0},
                {1,1,0},
                {0,0,0}
        };
        Map m = new Map(data);

        Pixel2D start = new Index2D(0,0);
        Map2D dist = m.allDistance(start, 1, false);
        assertNotNull(dist);

        assertEquals(0, dist.getPixel(0,0));

        assertTrue(dist.getPixel(2,0) >= 0);

        assertTrue(dist.getPixel(0,1) == -1 || dist.getPixel(0,1) == 1);
    }
}