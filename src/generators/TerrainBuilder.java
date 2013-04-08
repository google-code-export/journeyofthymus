package generators;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

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
    private Node rootNode, mapNode;
    private final int BLOCK_WIDTH = 2,
            BLOCK_HEIGHT = 10;

    public TerrainBuilder(AssetManager assetManager, Node rootNode) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
    }

    public void buildMap() {

        int dimX = MapFileReader.getDimensions(),
                iX,
                iY;
        Spatial floor,
                block;

        Tile[][] map = new Map(MapFileReader.loadMap("assets/MapFiles/Labyrinth1.txt")).map;

        ObjectFactory.setAssetManager(assetManager);
        mapNode = new Node("Map");

        floor = ObjectFactory.makeFloor(dimX);
        mapNode.attachChild(floor);
        floor.setLocalTranslation(dimX * 0.5f, -0.1f, dimX * 0.5f);

        for (iY = 0; iY < dimX; iY++) {
            for (iX = 0; iX < dimX; iX++) {
                switch (map[iX][iY].code) {
                    case 'B':
                        block = ObjectFactory.makeBlock(BLOCK_WIDTH, BLOCK_HEIGHT);
                        mapNode.attachChild(block);
                        block.setLocalTranslation((BLOCK_WIDTH * iY) + (BLOCK_WIDTH * 0.5f),
                                BLOCK_HEIGHT * 0.5f,
                                (BLOCK_WIDTH * iY) + (BLOCK_WIDTH * 0.5f));
                        break;
                    default:
                        break;
                }
            }
        }
        rootNode.attachChild(mapNode);
    }
}