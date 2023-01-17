public class Point {
    private final int[] coords;

    public Point(int x, int y) {
        coords = new int[2];
        coords[0] = x;
        coords[1] = y;
    }

    public int x() {
        return coords[0];
    }

    public int y() {
        return coords[1];
    }

    public int[] coords() {
        return coords;
    }

    public double distanceTo(Point z) {
        return Math.sqrt(Math.pow(z.x() - coords[0], 2) + Math.pow(z.y() - coords[1], 2));
    }

    public int squareDistanceTo(Point z) {
        return (int) Math.pow(distanceTo(z), 2);
    }

    public String toString() {
        return "(" + coords[0] + ", " + coords[1] + ")";
    }
}
