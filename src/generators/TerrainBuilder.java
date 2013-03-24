package generators;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

/**
 *
 * @author @param @since @version 0.00.01
 */
public class TerrainBuilder {

    // Pass in reference to assetmanager so that you can:
    // Load models
    // pass in Map object, consisting of a 2d tile array
    // for each tile,use a current position vector to place the correct model
    // move on to next tile and increment vector by fixed amount on X or Z
    private AssetManager assetManager;
    private Node rootNode;

    public TerrainBuilder(AssetManager assetManager, Node rootNode) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
    }

    public Tile[][] getMap(String name) {
        return new Map(MapFileReader.loadMap(name)).map;
    }
}