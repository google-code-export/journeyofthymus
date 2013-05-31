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
import java.util.ArrayList;
import java.util.Random;

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
    private final float BLOCK_WIDTH = 4,
            BLOCK_HEIGHT = 4,
            SEGMENTS = 32;
    private float DEC_CHANCE = 0.2f;
    private Vector3f offset;
    private Random rand;
    private ArrayList<MapFileReader.Direction> decPlace;
    private Tile[][] map;

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
                ceiling,
                block,
                decoration;

        map = new Map(MapFileReader.loadMap("assets/MapFiles/Labyrinth1.txt")).map;
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
                        spawnPoint.setLocalTranslation((BLOCK_WIDTH * iX), 1, (BLOCK_WIDTH * iY));
                        mapNode.attachChild(spawnPoint);
                        g.setColor(Color.BLACK);
                        break;
                    case 'V':
                        g.setColor(Color.GRAY);
                        break;
                    case ' ':
                        rand = new Random();
                        if (rand.nextFloat() < DEC_CHANCE) {
                            decoration = ObjectFactory.makeDecoration();
                            setOffset(decoration.getName(), iX, iY);
                            decoration.setLocalTranslation(offset);
                            decoration.setShadowMode(ShadowMode.CastAndReceive);
                            mapNode.attachChild(decoration);
                        }
                        g.setColor(Color.BLACK);
                        break;
                    default:
                        break;
                }
                g.drawRect(iX, iY, 1, 1);
            }
        }
        
        for (float i = 1; i < SEGMENTS; i += 2) {
            for (float j = 1; j < SEGMENTS; j += 2) {
                floor = ObjectFactory.makeFloor(dimX * BLOCK_WIDTH / (SEGMENTS / 2));
                floor.setLocalTranslation(dimX * BLOCK_WIDTH * i / SEGMENTS - (BLOCK_WIDTH / 2),
                        -((BLOCK_HEIGHT / 2) + 0.25f),
                        dimX * BLOCK_WIDTH * j / SEGMENTS - (BLOCK_WIDTH / 2));
                floor.setShadowMode(ShadowMode.Receive);
                mapNode.attachChild(floor);

            }
        }
        
        for (float i = 1; i < SEGMENTS; i += 2) {
            for (float j = 1; j < SEGMENTS; j += 2) {
                ceiling = ObjectFactory.makeCeiling(dimX * BLOCK_WIDTH / (SEGMENTS / 2));
                ceiling.setLocalTranslation(dimX * BLOCK_WIDTH * i / SEGMENTS - (BLOCK_WIDTH / 2),
                        ((BLOCK_HEIGHT / 2) + 0.25f),
                        dimX * BLOCK_WIDTH * j / SEGMENTS - (BLOCK_WIDTH / 2));
                ceiling.setShadowMode(ShadowMode.Receive);
                mapNode.attachChild(ceiling);
            }
        }
        
        CollisionShape labyrinthShape = CollisionShapeFactory.createMeshShape(mapNode);
        RigidBodyControl labyrinth = new RigidBodyControl(labyrinthShape, 0);
        mapNode.addControl(labyrinth);

        physicsSpace.add(labyrinth);
        lightNode.attachChild(mapNode);
    }

    private void setOffset(String decType, int x, int z) {
        Vector3f decSize = new Vector3f();
        decPlace = new ArrayList<>();
        if (map[x - 1][z].code == 'B') {
            decPlace.add(MapFileReader.Direction.Left);
        }
        if (map[x + 1][z].code == 'B') {
            decPlace.add(MapFileReader.Direction.Right);
        }
        if (map[x][z - 1].code == 'B') {
            decPlace.add(MapFileReader.Direction.Up);
        }
        if (map[x][z + 1].code == 'B') {
            decPlace.add(MapFileReader.Direction.Down);
        }

        switch (decType) {
            case "Crate":
                decSize.x = ObjectFactory.CRATE_SIZE;
                decSize.y = ObjectFactory.CRATE_SIZE / 2 + 0.25f;
                decSize.z = ObjectFactory.CRATE_SIZE;
                break;
            case "Jug":
                break;
            default:
                break;
        }

        getOffset(decSize, x, z);
    }

    private void getOffset(Vector3f size, int x, int z) {
        float xOffset = 0, zOffset = 0;
        Random r = new Random();
        int side;

        if (decPlace.contains(MapFileReader.Direction.Left) && decPlace.contains(MapFileReader.Direction.Down)) {
            xOffset = -(BLOCK_WIDTH / 2) + (size.x / 2);
            zOffset = -(BLOCK_WIDTH / 2) + (size.z / 2);
        } else if (decPlace.contains(MapFileReader.Direction.Left) && decPlace.contains(MapFileReader.Direction.Up)) {
            xOffset = -(BLOCK_WIDTH / 2) + (size.x / 2);
            zOffset = (BLOCK_WIDTH / 2) - (size.z / 2);
        } else if (decPlace.contains(MapFileReader.Direction.Left) && decPlace.contains(MapFileReader.Direction.Right)) {
            side = r.nextInt(2);
            switch (side) {
                case 0:
                    xOffset = -(BLOCK_WIDTH / 2) + (size.x / 2);
                    break;
                case 1:
                    xOffset = (BLOCK_WIDTH / 2) + (size.x / 2);
                    break;
            }
            zOffset = 0;
        } else if (decPlace.contains(MapFileReader.Direction.Right) && decPlace.contains(MapFileReader.Direction.Down)) {
            xOffset = (BLOCK_WIDTH / 2) + (size.x / 2);
            zOffset = -(BLOCK_WIDTH / 2) + (size.z / 2);
        } else if (decPlace.contains(MapFileReader.Direction.Right) && decPlace.contains(MapFileReader.Direction.Up)) {
            xOffset = (BLOCK_WIDTH / 2) + (size.x / 2);
            zOffset = (BLOCK_WIDTH / 2) + (size.z / 2);
        } else if (decPlace.contains(MapFileReader.Direction.Down) && decPlace.contains(MapFileReader.Direction.Up)) {
            side = r.nextInt(2);
            switch (side) {
                case 0:
                    zOffset = -(BLOCK_WIDTH / 2) + (size.z / 2);
                    break;
                case 1:
                    zOffset = (BLOCK_WIDTH / 2) + (size.z / 2);
                    break;
            }
            xOffset = 0;
        }

        offset = new Vector3f((BLOCK_WIDTH * x) + xOffset,
                -(BLOCK_WIDTH / 2) + size.y,
                (BLOCK_WIDTH * z) + zOffset);
    }

    public Vector3f getSpawnPoint() {
        return spawnPoint.getLocalTranslation();
    }
}