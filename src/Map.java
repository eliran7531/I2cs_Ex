    import java.io.Serializable;
    /**
     * This class represents a 2D map (int[w][h]) as a "screen" or a raster matrix or maze over integers.
     * This is the main class needed to be implemented.
     *
     * @author boaz.benmoshe
     *
     */
    public class Map implements Map2D, Serializable{

        // edit this class below
        private int[][] _map;

        /**
         * Constructs a w*h 2D raster map with an init value v.
         * @param w
         * @param h
         * @param v
         */
        public Map(int w, int h, int v) {init(w, h, v);}
        /**
         * Constructs a square map (size*size).
         * @param size
         */
        public Map(int size) {this(size,size, 0);}

        /**
         * Constructs a map from a given 2D array.
         * @param data
         */
        public Map(int[][] data) {
            init(data);
        }
        @Override
        public void init(int w, int h, int v) {
            _map = new int[w][h];
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    _map[x][y] = v;
                }
            }
        }
        @Override
        public void init(int[][] arr) {
            if (arr == null || arr.length == 0 || arr[0].length == 0) {
                throw new RuntimeException("Invalid array");
            }
            int h = arr[0].length;
            for (int i = 1; i < arr.length; i++) {
                if (arr[i].length != h) {
                    throw new RuntimeException("Ragged array");
                }
            }

            int w = arr.length;
            _map = new int[w][h];
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    _map[x][y] = arr[x][y];
                }
            }
        }
        @Override
        public int[][] getMap() {
            int w = _map.length;
            int h = _map[0].length;

            int[][] ans = new int[w][h];

            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    ans[x][y] = _map[x][y];
                }
            }
            return ans;
        }
        @Override
        public int getWidth() {
            int ans = _map.length;

            return ans;
        }
        @Override
        public int getHeight() {
            int  ans = _map[0].length;

            return ans;
        }
        @Override
        public int getPixel(int x, int y) {
            int ans = _map[x][y];

            return ans;
        }
        @Override
        public int getPixel(Pixel2D p) {
            int ans = getPixel(p.getX(), p.getY());

            return ans;
        }
        @Override
        public void setPixel(int x, int y, int v) {
          _map[x][y] = v;
        }
        @Override
        public void setPixel(Pixel2D p, int v) {
            setPixel(p.getX(), p.getY(), v);

        }

        @Override
        public boolean isInside(Pixel2D p) {
            if( p.getX() < 0 || p.getX() >= _map.length || p.getY() < 0 || p.getY() >= _map[0].length)
                return false;
            else return true;

        }

        @Override
        public boolean sameDimensions(Map2D p) {
            if( p.getWidth() == _map.length && p.getHeight() == _map[0].length )
                return true;
            else return false;

        }

        @Override
        public void addMap2D(Map2D p) {
            if (!sameDimensions(p)) return;

            int w = getWidth();
            int h = getHeight();
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    setPixel(x, y, getPixel(x, y) + p.getPixel(x, y));
                }
            }
        }
        @Override
        public void mul(double scalar) {
            int w = getWidth();
            int h = getHeight();
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    setPixel(x, y, (int)Math.round(getPixel(x, y) * scalar));
                }
            }
        }

        @Override
        public void rescale(double sx, double sy) {
            int oldW = getWidth();
            int oldH = getHeight();

            int newW = (int) Math.round(oldW * sx);
            int newH = (int) Math.round(oldH * sy);

            int[][] newMap = new int[newW][newH];

            for (int x = 0; x < newW; x++) {
                for (int y = 0; y < newH; y++) {

                    int oldX = (int) (x / sx);
                    int oldY = (int) (y / sy);

                    if (oldX >= oldW) oldX = oldW - 1;
                    if (oldY >= oldH) oldY = oldH - 1;

                    newMap[x][y] = _map[oldX][oldY];
                }
            }

            _map = newMap;

        }

        @Override
        public void drawCircle(Pixel2D center, double rad, int color) {
            int cx = center.getX();
            int cy = center.getY();

            double r2 = rad * rad;  // rad^2

            int xMin = (int) Math.floor(cx - rad);
            int xMax = (int) Math.ceil(cx + rad);
            int yMin = (int) Math.floor(cy - rad);
            int yMax = (int) Math.ceil(cy + rad);

            for (int x = xMin; x <= xMax; x++) {
                for (int y = yMin; y <= yMax; y++) {
                    double dx = x - cx;
                    double dy = y - cy;

                    if (dx * dx + dy * dy <= r2) {
                        Pixel2D p = new Index2D(x, y);
                        if (isInside(p)) {
                            setPixel(x, y, color);
                        }
                    }
                }
            }

        }

        @Override
        public void drawLine(Pixel2D p1, Pixel2D p2, int color) {
            int x1 = p1.getX(), y1 = p1.getY();
            int x2 = p2.getX(), y2 = p2.getY();

            if (x1 == x2 && y1 == y2) {
                if (isInside(p1)) setPixel(x1, y1, color);
                return;
            }

            int dx = Math.abs(x2 - x1);
            int dy = Math.abs(y2 - y1);

            if (dx >= dy) {
                if (x1 > x2) {
                    int tx = x1; x1 = x2; x2 = tx;
                    int ty = y1; y1 = y2; y2 = ty;
                }
                double a = (double)(y2 - y1) / (x2 - x1);
                for (int x = x1; x <= x2; x++) {
                    int y = (int)Math.round(y1 + a * (x - x1));
                    if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
                        setPixel(x, y, color);
                    }
                }
            } else {
                if (y1 > y2) { // swap
                    int tx = x1; x1 = x2; x2 = tx;
                    int ty = y1; y1 = y2; y2 = ty;
                }
                double a = (double)(x2 - x1) / (y2 - y1);
                for (int y = y1; y <= y2; y++) {
                    int x = (int)Math.round(x1 + a * (y - y1));
                    if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
                        setPixel(x, y, color);
                    }
                }
            }
        }

        @Override
        public void drawRect(Pixel2D p1, Pixel2D p2, int color) {
            int x1 = p1.getX();
            int y1 = p1.getY();
            int x2 = p2.getX();
            int y2 = p2.getY();

            int minX = Math.min(x1, x2);
            int maxX = Math.max(x1, x2);
            int minY = Math.min(y1, y2);
            int maxY = Math.max(y1, y2);

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
                        setPixel(x, y, color);
                    }
                }
            }
        }

        @Override
        public boolean equals(Object ob) {
            if (ob == this) return true;
            if (ob == null) return false;
            if (!(ob instanceof Map2D)) return false;

            Map2D other = (Map2D) ob;

            if (!this.sameDimensions(other)) return false;

            int w = this.getWidth();
            int h = this.getHeight();

            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    if (this.getPixel(x, y) != other.getPixel(x, y)) {
                        return false;
                    }
                }
            }
            return true;
        }
        @Override
        /**
         * Fills this map with the new color (new_v) starting from p.
         * https://en.wikipedia.org/wiki/Flood_fill
         */
        public int fill(Pixel2D xy, int new_v,  boolean cyclic) {
            if (!isInside(xy)) return 0;

            int old_v = getPixel(xy);
            if (old_v == new_v) return 0;

            int w = getWidth();
            int h = getHeight();

            boolean[][] visited = new boolean[w][h];
            java.util.Queue<Pixel2D> q = new java.util.LinkedList<>();

            q.add(xy);
            visited[xy.getX()][xy.getY()] = true;

            int count = 0;

            while (!q.isEmpty()) {
                Pixel2D p = q.poll();
                int x = p.getX();
                int y = p.getY();

                if (getPixel(x, y) != old_v) continue;

                setPixel(x, y, new_v);
                count++;

                int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
                for (int[] d : dirs) {
                    int nx = x + d[0];
                    int ny = y + d[1];

                    if (cyclic) {
                        nx = (nx + w) % w;
                        ny = (ny + h) % h;
                    }

                    if (nx >= 0 && nx < w && ny >= 0 && ny < h) {
                        if (!visited[nx][ny] && getPixel(nx, ny) == old_v) {
                            visited[nx][ny] = true;
                            q.add(new Index2D(nx, ny));
                        }
                    }
                }
            }
            return count;
        }

        @Override
        /**
         * BFS like shortest the computation based on iterative raster implementation of BFS, see:
         * https://en.wikipedia.org/wiki/Breadth-first_search
         */
        public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor, boolean cyclic) {
            if (!isInside(p1) || !isInside(p2)) return null;
            if (getPixel(p1) == obsColor || getPixel(p2) == obsColor) return null;

            int w = getWidth();
            int h = getHeight();

            boolean[][] visited = new boolean[w][h];
            Pixel2D[][] parent = new Pixel2D[w][h];

            java.util.Queue<Pixel2D> q = new java.util.LinkedList<>();
            q.add(p1);
            visited[p1.getX()][p1.getY()] = true;
            parent[p1.getX()][p1.getY()] = null;

            int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

            while (!q.isEmpty()) {
                Pixel2D p = q.poll();
                int x = p.getX();
                int y = p.getY();

                if (x == p2.getX() && y == p2.getY()) break;

                for (int[] d : dirs) {
                    int nx = x + d[0];
                    int ny = y + d[1];

                    if (cyclic) {
                        nx = (nx + w) % w;
                        ny = (ny + h) % h;
                    }

                    if (nx >= 0 && nx < w && ny >= 0 && ny < h) {
                        if (!visited[nx][ny] && getPixel(nx, ny) != obsColor) {
                            visited[nx][ny] = true;
                            parent[nx][ny] = p;
                            q.add(new Index2D(nx, ny));
                        }
                    }
                }
            }

            if (!visited[p2.getX()][p2.getY()]) return null;

            java.util.List<Pixel2D> path = new java.util.ArrayList<>();
            Pixel2D cur = p2;

            while (cur != null) {
                path.add(cur);
                cur = parent[cur.getX()][cur.getY()];
            }

            java.util.Collections.reverse(path);
            return path.toArray(new Pixel2D[0]);
        }
        @Override
        public Map2D allDistance(Pixel2D start, int obsColor, boolean cyclic) {
            if (!isInside(start)) return null;
            if (getPixel(start) == obsColor) return null;

            int w = getWidth();
            int h = getHeight();

            Map ans = new Map(w, h, -1);

            boolean[][] visited = new boolean[w][h];
            java.util.Queue<Pixel2D> q = new java.util.LinkedList<>();

            q.add(start);
            visited[start.getX()][start.getY()] = true;
            ans.setPixel(start, 0);

            int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

            while (!q.isEmpty()) {
                Pixel2D p = q.poll();
                int x = p.getX();
                int y = p.getY();

                int distHere = ans.getPixel(x, y);

                for (int[] d : dirs) {
                    int nx = x + d[0];
                    int ny = y + d[1];

                    if (cyclic) {
                        nx = (nx + w) % w;
                        ny = (ny + h) % h;
                    }

                    if (nx >= 0 && nx < w && ny >= 0 && ny < h) {
                        if (!visited[nx][ny] && getPixel(nx, ny) != obsColor) {
                            visited[nx][ny] = true;
                            ans.setPixel(nx, ny, distHere + 1);
                            q.add(new Index2D(nx, ny));
                        }
                    }
                }
            }

            return ans;
        }
        ////////////////////// Private Methods ///////////////////////

    }
