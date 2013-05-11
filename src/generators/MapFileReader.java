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
import java.util.Scanner;

public class MapFileReader {

    public enum Direction {

        Up, Down, Left, Right
    };
    private static int dimX = 0, dimY = 0;
    private static Tile[][] tiles = null;
    private static Tile[][] cleanMap = null;

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
        tiles = createMapArray();
        cleanMap = createMapArray();
        // read data from file into array
        readMap(name);
        // redundancy parse
        tiles = clean();
        return tiles;
    }

    private static Tile[][] createMapArray() {
        Tile[][] newMapArray = new Tile[dimX][dimY];
        for (int i = 0; i < dimY; i++) {
            for (int j = 0; j < dimX; j++) {
                newMapArray[j][i] = new Tile('V');
            }
        }

        return newMapArray;
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

    private static Tile[][] clean() {

        int x, y;

        for (y = 0; y < dimY; y++) {
            for (x = 0; x < dimX; x++) {
                ArrayList<Direction> edges = edge(x, y);
                cleanMap[x][y].code = tiles[x][y].code;
                if (edges.contains(Direction.Up)) {
                    if (!checkRedundancy(x, y - 1)) {
                        continue;
                    }
                }
                if (edges.contains(Direction.Down)) {
                    if (!checkRedundancy(x, y + 1)) {
                        continue;
                    }
                }
                if (edges.contains(Direction.Left)) {
                    if (!checkRedundancy(x - 1, y)) {
                        continue;
                    }
                }
                if (edges.contains(Direction.Right)) {
                    if (!checkRedundancy(x + 1, y)) {
                        continue;
                    }
                }
                if (edges.contains(Direction.Up) && edges.contains(Direction.Left)) {
                    if (!checkRedundancy(x - 1, y - 1)) {
                        continue;
                    }
                }
                if (edges.contains(Direction.Up) && edges.contains(Direction.Right)) {
                    if (!checkRedundancy(x + 1, y - 1)) {
                        continue;
                    }
                }
                if (edges.contains(Direction.Down) && edges.contains(Direction.Left)) {
                    if (!checkRedundancy(x - 1, y + 1)) {
                        continue;
                    }
                }
                if (edges.contains(Direction.Down) && edges.contains(Direction.Right)) {
                    if (!checkRedundancy(x + 1, y + 1)) {
                        continue;
                    }
                }
                cleanMap[x][y].code = 'V';
            }
        }
        return cleanMap;
    }

    private static boolean checkRedundancy(int x, int y) {
        switch (tiles[x][y].code) {
            case 'B':
                return true;
            default:
                return false;
        }
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