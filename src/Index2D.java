public class Index2D implements Pixel2D {

    private int _x, _y;

    // Creates a pixel with given x and y values
    public Index2D(int w, int h) {
        this._x = w;
        this._y = h;
    }

    // Creates a pixel from another Pixel2D object
    public Index2D(Pixel2D other) {
        this._x = other.getX();
        this._y = other.getY();
    }

    // Returns the x value of this pixel
    @Override
    public int getX() {
        return this._x;
    }

    // Returns the y value of this pixel
    @Override
    public int getY() {
        return this._y;
    }

    // Computes the distance between this pixel and another pixel
    @Override
    public double distance2D(Pixel2D p2) {
        int dx = this._x - p2.getX();
        int dy = this._y - p2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Returns the pixel as a string in the form x,y
    @Override
    public String toString() {
        return this._x + "," + this._y;
    }

    // Checks if two pixels have the same x and y values
    @Override
    public boolean equals(Object p) {
        if (p == this) return true;
        if (p == null) return false;
        if (!(p instanceof Index2D)) return false;

        Pixel2D other = (Pixel2D) p;
        return this._x == other.getX() && this._y == other.getY();
    }
}
