package generators;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import driver.ApplicationInterface;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

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
    private Node lightNode, mapNode, spawnPoint;
    private PhysicsSpace physicsSpace;
    private final int BLOCK_WIDTH = 4,
            BLOCK_HEIGHT = 3;

    public TerrainBuilder(ApplicationInterface app, Node mapNode) {
        this.assetManager = app.getAssetManager();
        this.mapNode = mapNode;
        lightNode = (Node) app.getRootNode().getChild("Light Node");
        physicsSpace = app.getStateManager().getState(BulletAppState.class).getPhysicsSpace();
    }

    public void buildMap() {

        int dimX,
                iX,
                iY;
        Spatial floor,
                block;

        Tile[][] map = new Map(MapFileReader.loadMap("assets/MapFiles/Labyrinth1.txt")).map;
        dimX = MapFileReader.getDimensions();
        ObjectFactory.setAssetManager(assetManager);

        BufferedImage bitmap = new BufferedImage(dimX, dimX, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bitmap.getGraphics();

        for (iY = 0; iY < dimX; iY++) {
            for (iX = 0; iX < dimX; iX++) {
                switch (map[iX][iY].code) {
                    case 'B':
                        block = ObjectFactory.makeBlock(BLOCK_WIDTH, BLOCK_HEIGHT, String.valueOf(iX) + String.valueOf(iY));
                        block.setLocalTranslation((BLOCK_WIDTH * iX), 0, (BLOCK_WIDTH * iY));
                        block.setShadowMode(ShadowMode.CastAndReceive);
                        mapNode.attachChild(block);
                        g.setColor(Color.GRAY);
                        break;
                    case 'S':
                        spawnPoint = new Node("SpawnPoint");
                        spawnPoint.setLocalTranslation((BLOCK_WIDTH * iX), 2, (BLOCK_WIDTH * iY));
                        mapNode.attachChild(spawnPoint);
                        g.setColor(Color.BLACK);
                        break;
                    case 'V':
                        g.setColor(Color.GRAY);
                        break;
                    case ' ':
                        g.setColor(Color.BLACK);
                        break;
                    default:
                        break;
                }
                g.drawRect(iX, iY, 1, 1);
            }
        }
        floor = ObjectFactory.makeFloor(dimX * BLOCK_WIDTH);
        floor.setLocalTranslation(dimX * BLOCK_WIDTH / 2 - (BLOCK_WIDTH / 2), -((BLOCK_HEIGHT / 2) + 0.25f), dimX * BLOCK_WIDTH / 2 - (BLOCK_WIDTH / 2));
        mapNode.attachChild(floor);
        floor.setShadowMode(ShadowMode.Receive);

        CollisionShape labyrinthShape = CollisionShapeFactory.createMeshShape(mapNode);
        RigidBodyControl labyrinth = new RigidBodyControl(labyrinthShape, 0);
        mapNode.addControl(labyrinth);

        physicsSpace.add(labyrinth);

        lightNode.attachChild(mapNode);
    }

    public Vector3f getSpawnPoint() {
        return spawnPoint.getLocalTranslation();
    }
}