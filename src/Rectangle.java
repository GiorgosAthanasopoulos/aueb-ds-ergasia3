public class Rectangle {
    private final int xmin, xmax, ymin, ymax;

    public Rectangle(int xmin, int xmax, int ymin, int ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }

    public int xmin() {
        return xmin;
    }
    public int xmax() {
        return xmax;
    }
    public int ymin() {
        return ymin;
    }
    public int ymax() {
        return ymax;
    }

    public boolean contains(Point p) {
        return p.x() >= xmin && p.x() <= xmax && p.y() <= ymax && p.y() >= ymin;
    }
    public boolean intersects(Rectangle that) {
        return xmin < that.xmax && xmax > that.xmin && ymax > that.ymin && ymin < that.ymax;
    }

    public double distanceTo(Point p) {
        int dx = Math.max(Math.max(xmin - p.x(), 0), p.x() - xmax);
        int dy = Math.max(Math.max(ymin - p.y(), 0), p.y() - ymax);
        return Math.sqrt(dx*dx + dy*dy);
    }
    public int squareDistanceTo(Point p) {
        return (int) Math.pow(distanceTo(p), 2);
    }

    public String toString() {
        return "[" + xmin +", " + xmax + "] x [" + ymin + ", " + ymax + "]";
    }
}
