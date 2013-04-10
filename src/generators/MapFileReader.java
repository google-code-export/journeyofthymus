package generators;

/**
 * Reads map file formats so that it can be processed by the TerrainBuilder
 * object
 *
 * @author
 * @param
 * @since
 * @version 0.00.01
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapFileReader {

    public enum Direction {

        Up, Down, Left, Right
    };
    private static int dimX = 0, dimY = 0;
    private static Tile[][] tiles = null;

    public static Tile[][] loadMap(String name) {

        Scanner s = null;
        String line = null;
        boolean isSane = true;

        try {
            s = new Scanner(new BufferedReader(new FileReader(name)));
            line = s.nextLine();
            dimX = line.length();
            dimY++;

            while (s.hasNextLine() && isSane) {
                if (line.matches("[^B\\s]")) {
                }
                line = s.nextLine();
                if (line.length() == dimX) {
                    dimY++;
                } else {
                    return null;
                }
                //get dimensions/sanity check
            }
        } catch (FileNotFoundException ex) {
        } finally {
            if (s != null) {
                s.close();
            }
        }

        // allocate array based on map dimensions
        tiles = new Tile[dimX][dimY];
        // read data from file into array
        readMap(name);
        // redundancy parse
        tiles = clean(tiles);
        // add decorations to array
        decorate();
        // result = clean and decorated map ready for build
        return tiles;
    }

    private static void readMap(String name) {

        Scanner s = null;
        String line = null;
        int x, y = 0;

        try {
            s = new Scanner(new BufferedReader(new FileReader(name)));
            line = s.nextLine();

            while (line != null) {
                for (x = 0; x < dimX; x++) {
                    tiles[x][y] = new Tile(line.charAt(x));
                }
                if (s.hasNextLine()) {
                    line = s.nextLine();
                    y++;
                } else {
                    line = null;
                }
            }

        } catch (FileNotFoundException ex) {
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    private static Tile[][] clean(Tile[][] cleanMap) {

        int x, y;
        boolean isRedundant;

        for (y = 0; y < dimY; y++) {
            for (x = 0; x < dimX; x++) {
                ArrayList<Direction> edges = edge(x, y);
                isRedundant = true;
                if (edges.contains(Direction.Up)) {
                    isRedundant = checkRedundancy(x, y - 1, isRedundant);
                }
                if (edges.contains(Direction.Down)) {
                    isRedundant = checkRedundancy(x, y + 1, isRedundant);
                }
                if (edges.contains(Direction.Left)) {
                    isRedundant = checkRedundancy(x - 1, y, isRedundant);
                }
                if (edges.contains(Direction.Right)) {
                    isRedundant = checkRedundancy(x + 1, y, isRedundant);
                }
                if (edges.contains(Direction.Up) && edges.contains(Direction.Left)) {
                    isRedundant = checkRedundancy(x - 1, y - 1, isRedundant);
                }
                if (edges.contains(Direction.Up) && edges.contains(Direction.Right)) {
                    isRedundant = checkRedundancy(x + 1, y - 1, isRedundant);
                }
                if (edges.contains(Direction.Down) && edges.contains(Direction.Left)) {
                    isRedundant = checkRedundancy(x - 1, y + 1, isRedundant);
                }
                if (edges.contains(Direction.Down) && edges.contains(Direction.Right)) {
                    isRedundant = checkRedundancy(x + 1, y + 1, isRedundant);
                }

                if (isRedundant) {
                    cleanMap[x][y].code = 'V';
                } else {
                    cleanMap[x][y].code = tiles[x][y].code;
                }
            }
        }
        return cleanMap;
    }

    private static boolean checkRedundancy(int x, int y, boolean isRedundant) {
        switch (tiles[x][y].code) {
            case 'B':
                return isRedundant;
            default:
                return false;
        }
    }

    private static void decorate() {
        // random decorator - where there are blank spaces, randomly choose a decoration
    }

    public static int getDimensions() {
        return dimX;
    }

    public static ArrayList<Direction> edge(int x, int y) {
        ArrayList<Direction> dir = new ArrayList<>();
        dir.add(Direction.Up);
        dir.add(Direction.Down);
        dir.add(Direction.Left);
        dir.add(Direction.Right);
        if (x == 0) {
            dir.remove(Direction.Left);
        }
        if (x == dimX - 1) {
            dir.remove(Direction.Right);
        }
        if (y == 0) {
            dir.remove(Direction.Up);
        }
        if (y == dimX - 1) {
            dir.remove(Direction.Down);
        }
        return dir;
    }
}