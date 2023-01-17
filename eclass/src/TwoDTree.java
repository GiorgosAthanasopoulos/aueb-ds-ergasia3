import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class TwoDTree {
    private static class TreeNode {
        Point item;
        TreeNode l;
        TreeNode r;

        public TreeNode(Point p) {
            this.item = p;
        }
    }

    private TreeNode head;
    private int size;

    public TwoDTree() {
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private TreeNode insertR(TreeNode h, Point p, int depth) {
        if (h == null) {
            return new TreeNode(p);
        }

        int cd = depth % 2;

        if (p.coords()[cd] < h.item.coords()[cd]) {
            h.l = insertR(h.l, p, depth + 1);
        } else {
            h.r = insertR(h.r, p, depth + 1);
        }

        return h;
    }

    public void insert(Point p) {
        head = insertR(head, p, 0);
        size++;
    }

    private boolean searchR(TreeNode h, Point p, int depth) {
        if (h == null) {
            return false;
        }
        if (h.item.x() == p.x() && h.item.y() == p.y()) {
            return true;
        }

        int cd = depth % 2;

        if (p.coords()[cd] < h.item.coords()[cd]) {
            return searchR(h.l, p, depth + 1);
        }

        return searchR(h.r, p, depth + 1);
    }

    public boolean search(Point p) {
        return searchR(head, p, 0);
    }

    private TreeNode closest(TreeNode n0, TreeNode n1, Point target) {
        if (n0 == null) {
            return n1;
        }
        if (n1 == null) {
            return n0;
        }

        int d1 = n0.item.squareDistanceTo(target);
        int d2 = n1.item.squareDistanceTo(target);

        if (d1 < d2) {
            return n0;
        } else {
            return n1;
        }
    }

    private TreeNode findNN(TreeNode root, Point target, int depth) {
        if (root == null) return null;

        int cd = depth % 2;
        TreeNode nextBranch, otherBranch;

        if (target.coords()[cd] < root.item.coords()[cd])  {
            nextBranch = root.l;
            otherBranch = root.r;
        } else {
            nextBranch = root.r;
            otherBranch = root.l;
        }

        TreeNode temp = findNN(nextBranch, target, depth + 1);
        TreeNode best = closest(temp, root, target);

        int radiusSquared = target.squareDistanceTo(best.item);
        int dist = target.coords()[cd] - root.item.coords()[cd];

        if (radiusSquared >= dist * dist) {
            temp = findNN(otherBranch, target, depth + 1);
            best = closest(temp, best, target);
        }

        return best;
    }

    public TreeNode nearestNeighbour(Point p) {
        if (isEmpty()) {
            return null;
        }
        return findNN(head, p, 0);
    }

    private void rangeSearchHelper(TreeNode node, Rectangle range, List<Point> result) {
        if (node == null) {
            return;
        }
        if (range.contains(node.item)) {
            result.add(node.item);
        }
        if (node.l != null) {
            rangeSearchHelper(node.l, range, result);
        }
        if (node.r != null) {
            rangeSearchHelper(node.r, range, result);
        }
    }

    public List<Point> rangeSearch(Rectangle rect) {
        List<Point> result = new ArrayList<>();
        rangeSearchHelper(head, rect, result);
        return result;
    }

    private static class FileData {
        int n;
        List<Point> points;
    }

    private static List<Point> readFile(String filename) throws IOException {
        File file = new File(filename);

        try {
            Paths.get(filename);
        } catch (Exception __) {
            System.out.println("Input path is not valid!");
            System.exit(1);
        }

        if (file.isDirectory()) {
            System.out.println("Input path must be a file!");
            System.exit(1);
        }

        if (!file.exists()) {
            System.out.println("Input file does not exist!");
            System.exit(1);
        }

        if (file.length() == 0) {
            System.out.println("Input file is empty!");
            System.exit(1);
        }

        if (Files.lines(Path.of(filename)).filter((line) -> !(line.trim().strip().equals("") || line.trim().strip().equals(" "))).count() < 2) {
            System.out.println("Input file format is invalid: line count < 2 (required)");
            System.exit(1);
        }

        Pattern regex = Pattern.compile("^[0-9]*$");
        int lineCounter = 1, infoLineCounter = 0;
        FileData fileData = new FileData();
        fileData.points = new ArrayList<>();

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] lineInput = sc.nextLine().trim().strip().split(" ");

                if (lineCounter == 1 && !(lineInput.length == 1)) {
                    System.out.println("First line must only contain 1 number!");
                    System.exit(1);
                }

                if (lineInput.length == 1) {
                    infoLineCounter++;
                }

                for (String s : lineInput) {
                    if (lineCounter < 2) {
                        if (!regex.matcher(s).find()) {
                            System.out.println("Input file format is invalid: info line: " + lineCounter + " contains invalid literal: " + s);
                            System.exit(1);
                        }
                    } else {
                        // following if is to allow for comments in the input file
                        if (lineInput[0].startsWith("!")) {
                            break;
                        }
                        if (!regex.matcher(s).find()) {
                            System.out.println("Input file format is invalid: point line: " + lineCounter + " contains invalid literal: " + s);
                            System.exit(1);
                        }
                    }
                }

                switch (lineCounter) {
                    case 1 -> {
                        fileData.n = Integer.parseInt(lineInput[0]);
                    }
                    default -> {
                        if (lineInput[0].startsWith("!")) {
                            continue;
                        }
                        if (infoLineCounter < 1) {
                            System.out.println("Input file format is invalid: info line 1 is missing!");
                            System.exit(1);
                        }
                        if (lineInput.length != 2) {
                            System.out.println("Line: " + lineCounter + " does not contain 2 columns");
                            System.exit(1);
                        }

                        fileData.points.add(new Point(Integer.parseInt(lineInput[0]), Integer.parseInt(lineInput[1])));
                    }
                }

                lineCounter++;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading input file!");
            System.exit(1);
        }

        if (fileData.n != fileData.points.size()) {
            System.out.println("Input file format for point count is incorrect.Check that the number provided in the first line matches the amount of points provided!");
            System.exit(1);
        }

        return fileData.points;
    }

    private static void clearConsole() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Missing argument: filename");
            System.exit(1);
        }

        String filename = args[0];
        TwoDTree twoDTree = new TwoDTree();
        for (Point p : readFile(filename)) {
            twoDTree.insert(p);
        }

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("---|---|---|---|---|--------|---|---|---|---|---\n|                                              |\n| 1.Compute the size of the tree               |\n| 2.Insert a new point                         |\n| 3.Search if a given point exists in the tree |\n| 4.Provide a query rectangle                  |\n| 5.Provide a query point                      |\n| 6.Clear the console                          |\n| 7.Exit                                       |\n|                                              |\n---|---|---|---|---|--------|---|---|---|---|---\n>> ");
            int selection = sc.nextInt();

            switch (selection) {
                case 1:
                    System.out.println(twoDTree.size());
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                    System.out.print("Enter x1(for option 4: x1=xmin): ");
                    int x = sc.nextInt();
                    System.out.print("Enter y1(for option 4: y1=ymin): ");
                    int y = sc.nextInt();
                    Point p = new Point(x, y);
                    if (selection == 2) {
                        twoDTree.insert(p);
                    } else if (selection == 3) {
                        System.out.println(twoDTree.search(p));
                    } else if (selection == 4) {
                        System.out.print("Enter x2(max): ");
                        int x1 = sc.nextInt();
                        System.out.print("Enter y2(max): ");
                        int y1 = sc.nextInt();
                        System.out.println(Arrays.toString(twoDTree.rangeSearch(new Rectangle(x, x1, y, y1)).toArray()));
                    } else {
                        Point res = twoDTree.nearestNeighbour(p).item;
                        System.out.println(res);
                        System.out.println(res.distanceTo(p));
                    }
                    break;
                case 6:
                    clearConsole();
                    break;
                case 7:
                    System.exit(0);
            }
        }
    }
}
