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
import java.util.Scanner;

public class MapFileReader {

    public static Tile[][] loadMap(String name) {

        Scanner s = null;
        String line = null, nextLine = null;
        int dimX = 0, dimY = 1;
        boolean isSane = true;

        try {
            s = new Scanner(new BufferedReader(new FileReader(name)));

            line = s.nextLine();
            dimX = line.length();

            while (s.hasNextLine() && isSane) {
                nextLine = s.nextLine();
                if (line.length() == nextLine.length()) {
                    dimY++;

                } else {
                    return null;
                }
                line = nextLine;
                //get dimensions/sanity check
            }
        } catch (FileNotFoundException ex) {
        } finally {
            if (s != null) {
                s.close();
            }
        }

        // allocate array based on map dimensions
        Tile[][] tiles = null;

        if (dimX == dimY) {
            tiles = new Tile[dimX][dimY];
            // read data from file into array
            readMap(tiles, name, dimX);
            // redundancy parse
            clean(tiles, dimX, dimY);
            // add decorations to array
            decorate(tiles);
            // result = clean map ready for build
        }

        return tiles;
    }

    private static void readMap(Tile[][] tiles, String name, int dimX) {

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

    private static void clean(Tile[][] tiles, int dimX, int dimY) {

        int x, y;

        for (y = 0; y < dimY; y++) {
            for (x = 0; x < dimX; x++) {
                if (tiles[x][y].code == 'B' && isRedundant(tiles, x, y, dimX, dimY)) {
                    tiles[x][y].code = 'V';
                }
            }
        }
    }

    private static boolean isRedundant(Tile[][] tiles, int x, int y, int dimX, int dimY) {

        boolean isRedundant = false;

        if (x > 0 && x < dimX - 1) {            // Not on x dimension edges
            if (y > 0 && y < dimY - 1) {            // Not on y dimension edges
                if ((tiles[x + 1][y].code == 'B' || tiles[x + 1][y].code == 'V')
                        && (tiles[x + 1][y + 1].code == 'B' || tiles[x + 1][y + 1].code == 'V')
                        && (tiles[x][y + 1].code == 'B' || tiles[x][y + 1].code == 'V')
                        && (tiles[x - 1][y + 1].code == 'B' || tiles[x - 1][y + 1].code == 'V')
                        && (tiles[x - 1][y].code == 'B' || tiles[x - 1][y].code == 'V')
                        && (tiles[x - 1][y - 1].code == 'B' || tiles[x - 1][y - 1].code == 'V')
                        && (tiles[x][y - 1].code == 'B' || tiles[x][y - 1].code == 'V')
                        && (tiles[x + 1][y - 1].code == 'B' || tiles[x + 1][y - 1].code == 'V')) {
                    isRedundant = true;
                }
            } else if (y > 0 && y == dimY - 1) {    // On bottom y dimension edge
                if ((tiles[x + 1][y].code == 'B' || tiles[x + 1][y].code == 'V')
                        && (tiles[x - 1][y].code == 'B' || tiles[x - 1][y].code == 'V')
                        && (tiles[x - 1][y - 1].code == 'B' || tiles[x - 1][y - 1].code == 'V')
                        && (tiles[x][y - 1].code == 'B' || tiles[x][y - 1].code == 'V')
                        && (tiles[x + 1][y - 1].code == 'B' || tiles[x + 1][y - 1].code == 'V')) {
                    isRedundant = true;
                }
            } else if (y == 0 && y < dimY - 1) {    // On top y dimension edge
                if ((tiles[x + 1][y].code == 'B' || tiles[x + 1][y].code == 'V')
                        && (tiles[x + 1][y + 1].code == 'B' || tiles[x + 1][y + 1].code == 'V')
                        && (tiles[x][y + 1].code == 'B' || tiles[x][y + 1].code == 'V')
                        && (tiles[x - 1][y + 1].code == 'B' || tiles[x - 1][y + 1].code == 'V')
                        && (tiles[x - 1][y].code == 'B' || tiles[x - 1][y].code == 'V')) {
                    isRedundant = true;
                }
            }
        } else if (x > 0 && x == dimX - 1) {    // On right x dimension edge
            if (y > 0 && y < dimY - 1) {            // Not on y dimension edges
                if ((tiles[x][y + 1].code == 'B' || tiles[x][y + 1].code == 'V')
                        && (tiles[x - 1][y + 1].code == 'B' || tiles[x - 1][y + 1].code == 'V')
                        && (tiles[x - 1][y].code == 'B' || tiles[x - 1][y].code == 'V')
                        && (tiles[x - 1][y - 1].code == 'B' || tiles[x - 1][y - 1].code == 'V')
                        && (tiles[x][y - 1].code == 'B' || tiles[x][y - 1].code == 'V')) {
                    isRedundant = true;
                }
            } else if (y > 0 && y == dimY - 1) {    // On bottom y dimension edge
                if ((tiles[x - 1][y].code == 'B' || tiles[x - 1][y].code == 'V')
                        && (tiles[x - 1][y - 1].code == 'B' || tiles[x - 1][y - 1].code == 'V')
                        && (tiles[x][y - 1].code == 'B' || tiles[x][y - 1].code == 'V')) {
                    isRedundant = true;
                }
            } else if (y == 0 && y < dimY - 1) {    // On top y dimension edge
                if ((tiles[x][y + 1].code == 'B' || tiles[x][y + 1].code == 'V')
                        && (tiles[x - 1][y + 1].code == 'B' || tiles[x - 1][y + 1].code == 'V')
                        && (tiles[x - 1][y].code == 'B' || tiles[x - 1][y].code == 'V')) {
                    isRedundant = true;
                }
            }
        } else if (x == 0 && x < dimX - 1) {    // On left x dimension edge
            if (y > 0 && y < dimY - 1) {            // Not on y dimension edges
                if ((tiles[x + 1][y].code == 'B' || tiles[x + 1][y].code == 'V')
                        && (tiles[x + 1][y + 1].code == 'B' || tiles[x + 1][y + 1].code == 'V')
                        && (tiles[x][y + 1].code == 'B' || tiles[x][y + 1].code == 'V')
                        && (tiles[x][y - 1].code == 'B' || tiles[x][y - 1].code == 'V')
                        && (tiles[x + 1][y - 1].code == 'B' || tiles[x + 1][y - 1].code == 'V')) {
                    isRedundant = true;
                }
            } else if (y > 0 && y == dimY - 1) {    // On bottom y dimension edge
                if ((tiles[x + 1][y].code == 'B' || tiles[x + 1][y].code == 'V')
                        && (tiles[x][y - 1].code == 'B' || tiles[x][y - 1].code == 'V')
                        && (tiles[x + 1][y - 1].code == 'B' || tiles[x + 1][y - 1].code == 'V')) {
                    isRedundant = true;
                }
            } else if (y == 0 && y < dimY - 1) {    // On top y dimension edge
                if ((tiles[x + 1][y].code == 'B' || tiles[x + 1][y].code == 'V')
                        && (tiles[x + 1][y + 1].code == 'B' || tiles[x + 1][y + 1].code == 'V')
                        && (tiles[x][y + 1].code == 'B' || tiles[x][y + 1].code == 'V')) {
                    isRedundant = true;
                }
            }
        }

        return isRedundant;
    }

    private static void decorate(Tile[][] tiles) {
        // random decorator - where there are blank spaces, randomly choose a decoration
    }
}
