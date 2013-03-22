package generators;

import java.io. 
        
        /**
         * Reads map file formats so that it can be processed by the
         * TerrainBuilder object
         *
         * @author
         * @param
         * @since
         * @version 0.00.01
         */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.Scanner;

public class MapFileReader {

    public static Tile[][] LoadMap(String name) {


        Scanner s = null;

        try {
            s = new Scanner(new BufferedReader(new FileReader(name)));

            while (s.hasNext()) {
                System.out.println(s.next());
                //get dimensions/sanity check
            }
        } catch (FileNotFoundException ex) {
        } finally {
            if (s != null) {
                s.close();
            }
        }
        
        // allocate array based on map dimensions
        Tile[][] tiles = new Array();
        // read data from file into array
        
       // redundancy parse
        Clean(tiles);
        Decorate(tiles);
        // result = clean map ready for build
        return tiles;
        
    }

    
    private void Clean(Array[][] tiles)
    {
        // delete redundant blocks - replace with a void space character meaning not to place ANYTHING here
    }
    
    private void Decorate(Array[][] tiles)
    {
        // random decorator - where there are blank spaces, randomly choose a decoration

    }



}
