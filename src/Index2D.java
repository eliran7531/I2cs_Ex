public class Index2D implements Pixel2D {
    private int _x, _y;
    public Index2D(int w, int h) {
        ;
    }
    public Index2D(Pixel2D other) {
        ;
    }
    @Override
    public int getX() {

        return this._x;
    }

    @Override
    public int getY() {

        return this._y;
    }

    @Override
    public double distance2D(Pixel2D p2) {
        int dx = this._x -  p2.getX();
        int dy = this._y -  p2.getY();

        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {

        return this._x + "," + this._y;
    }

    @Override
    public boolean equals(Object p) {
     if (p == this) return true;
     if (p == null) return false;

     if(!(p instanceof Index2D)) return false;
        Pixel2D other = (Pixel2D) p;
        return this._x == other.getX() && this._y == other.getY();

    }
}
