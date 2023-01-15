public class Point {
    private final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }
    public int y() {
        return y;
    }

    public double distanceTo(Point z) {
        return Math.sqrt(Math.pow(z.x - x, 2) + Math.pow(z.y - y, 2));
    }
    public int squareDistanceTo(Point z) {
        return (int) Math.pow(distanceTo(z), 2);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
