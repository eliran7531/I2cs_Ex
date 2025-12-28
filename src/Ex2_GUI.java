import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Intro2CS_2026A
 * This class represents a Graphical User Interface (GUI) for Map2D.
 * The class has save and load functions, and a GUI draw function.
 * You should implement this class, it is recommender to use the StdDraw class, as in:
 * https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html
 *
 *
 */
public class Ex2_GUI {

    private static final int EMPTY = 0;
    private static final int OBST  = 1;
    private static final int PATH  = 7;

    private static final String DEFAULT_MAP_FILE = "map.txt";
    private static final String SAVE_MAP_FILE    = "map_saved.txt";

    private static final double HUD = 2.2;

    private static Pixel2D start = null;
    private static Pixel2D target = null;
    private static boolean cyclic = false;

    private enum Mode { TOGGLE_OBSTACLE, ERASE, SET_START, SET_TARGET }
    private static Mode mode = Mode.TOGGLE_OBSTACLE;

    // Draw the map on the screen and let the user interact with it (mouse + keyboard)
    public static void drawMap(Map2D map) {
        if (map == null) return;
        setupStdDraw(map);

        boolean prevMouse = false;

        while (true) {
            handleKeys(map);

            boolean mouse = StdDraw.isMousePressed();
            if (mouse && !prevMouse) {
                onMouseClick(map, StdDraw.mouseX(), StdDraw.mouseY());
            }
            prevMouse = mouse;

            render(map);
            StdDraw.show();
            StdDraw.pause(15);
        }
    }

    // Load a map from a text file (first line: width height, then the grid values)
    public static Map2D loadMap(String mapFileName) {
        if (mapFileName == null) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(mapFileName))) {
            String first = br.readLine();
            if (first == null) return null;

            String[] wh = first.trim().split("\\s+");
            if (wh.length < 2) return null;

            int w = Integer.parseInt(wh[0]);
            int h = Integer.parseInt(wh[1]);

            int[][] data = new int[w][h];

            for (int y = 0; y < h; y++) {
                String line = br.readLine();
                if (line == null) return null;
                String[] parts = line.trim().split("\\s+");
                if (parts.length < w) return null;

                for (int x = 0; x < w; x++) {
                    data[x][y] = Integer.parseInt(parts[x]);
                }
            }
            return new Map(data);
        } catch (Exception e) {
            return null;
        }
    }

    // Save the current map to a text file (first line: width height, then the grid values)
    public static void saveMap(Map2D map, String mapFileName) {
        if (map == null || mapFileName == null) return;

        try (PrintWriter pw = new PrintWriter(new FileWriter(mapFileName))) {
            int w = map.getWidth();
            int h = map.getHeight();

            pw.println(w + " " + h);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    pw.print(map.getPixel(x, y));
                    if (x < w - 1) pw.print(" ");
                }
                pw.println();
            }
        } catch (Exception e) {}
    }

    public static void main(String[] a) {
        Map2D map = loadMap(DEFAULT_MAP_FILE);
        if (map == null) map = new Map(20, 20, EMPTY);
        drawMap(map);
    }

    ////////////////////// Private helper methods //////////////////////

    private static void setupStdDraw(Map2D map) {
        int w = map.getWidth();
        int h = map.getHeight();

        int cellPx = 28;
        int canvasW = Math.max(520, w * cellPx);
        int canvasH = Math.max(520, (int) ((h + HUD) * cellPx));

        StdDraw.setCanvasSize(canvasW, canvasH);
        StdDraw.setXscale(0, w);
        StdDraw.setYscale(0, h + HUD);
        StdDraw.enableDoubleBuffering();
    }

    private static void handleKeys(Map2D map) {
        while (StdDraw.hasNextKeyTyped()) {
            char k = StdDraw.nextKeyTyped();

            if (k == 'q' || k == 'Q') System.exit(0);
            if (k == '1') mode = Mode.TOGGLE_OBSTACLE;
            if (k == '2') mode = Mode.ERASE;
            if (k == 's' || k == 'S') mode = Mode.SET_START;
            if (k == 't' || k == 'T') mode = Mode.SET_TARGET;

            if (k == 'c' || k == 'C') {
                cyclic = !cyclic;
                clearPath(map);
            }

            if (k == 'r' || k == 'R') {
                clearPath(map);
                start = null;
                target = null;
            }

            if (k == 'p' || k == 'P') {
                clearPath(map);
                paintPath(map);
            }

            if (k == 'w' || k == 'W') saveMap(map, SAVE_MAP_FILE);
        }
    }

    private static void onMouseClick(Map2D map, double mx, double my) {
        if (my >= map.getHeight()) return;

        Pixel2D p = toCell(map, mx, my);
        if (p == null) return;

        if (mode == Mode.SET_START) {
            clearPath(map);
            start = p;
            if (start != null && target != null) paintPath(map);
            return;
        }

        if (mode == Mode.SET_TARGET) {
            clearPath(map);
            target = p;
            if (start != null && target != null) paintPath(map);
            return;
        }

        int v = map.getPixel(p);

        if (mode == Mode.ERASE) {
            if (v != OBST) map.setPixel(p, EMPTY);
            clearPath(map);
            return;
        }

        if (mode == Mode.TOGGLE_OBSTACLE) {
            map.setPixel(p, (v == OBST) ? EMPTY : OBST);
            clearPath(map);
        }
    }

    private static Pixel2D toCell(Map2D map, double mx, double my) {
        int x = (int) Math.floor(mx);
        int y = (int) Math.floor(my);
        if (x < 0 || y < 0) return null;
        if (x >= map.getWidth() || y >= map.getHeight()) return null;
        return new Index2D(x, y);
    }

    private static void render(Map2D map) {
        StdDraw.clear(new Color(248, 248, 248));

        int w = map.getWidth();
        int h = map.getHeight();

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int v = map.getPixel(x, y);
                if (v == EMPTY) StdDraw.setPenColor(Color.WHITE);
                else if (v == OBST) StdDraw.setPenColor(Color.BLACK);
                else StdDraw.setPenColor(Color.GREEN);
                StdDraw.filledSquare(x + 0.5, y + 0.5, 0.5);
            }
        }

        StdDraw.setPenColor(new Color(80, 80, 80, 120));
        for (int x = 0; x <= w; x++) StdDraw.line(x, 0, x, h);
        for (int y = 0; y <= h; y++) StdDraw.line(0, y, w, y);

        if (start != null) {
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.filledCircle(start.getX() + 0.5, start.getY() + 0.5, 0.22);
        }

        if (target != null) {
            StdDraw.setPenColor(Color.RED);
            StdDraw.filledCircle(target.getX() + 0.5, target.getY() + 0.5, 0.22);
        }
    }

    private static void paintPath(Map2D map) {
        if (start == null || target == null) return;
        Pixel2D[] path = map.shortestPath(start, target, OBST, cyclic);
        if (path == null) return;

        for (Pixel2D p : path) {
            if (!p.equals(start) && !p.equals(target)) {
                map.setPixel(p, PATH);
            }
        }
    }

    private static void clearPath(Map2D map) {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.getPixel(x, y) == PATH) {
                    map.setPixel(x, y, EMPTY);
                }
            }
        }
    }
}
