package ai;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import driver.ApplicationInterface;
import generators.Map;
import generators.MapFileReader;
import generators.Tile;
import java.util.LinkedList;
import java.util.List;

/**
 *
 *
 * @author MIKUiqnw0
 * @param app
 * @param debug
 * @since 4/06/2013
 * @version 0.00.01
 */
public class Pathfinder {

    private Tile[][] map;
    private AssetManager assetManager;
    private Node waypointGraph;
    private int dimX, baseMovementCost = 10;

    private static boolean isDebugActive;
    private boolean foundTarget;
 
    private List<PathfinderNode> pfNodeList;
    private List<PathfinderNode> openList = new LinkedList<>();
    private List<PathfinderNode> closedList = new LinkedList<>();
    private PathfinderNode checkingNode = null;
    private PathfinderNode firstNodeInGrid = null;
    private PathfinderNode startNode = null;
    private PathfinderNode targetNode = null;

    /**
     * @params app
     */
    public Pathfinder(ApplicationInterface app, boolean debug) {
        dimX = MapFileReader.getDimensions();
        assetManager = app.getAssetManager();
        waypointGraph = (Node) ((Node) app.getRootNode().getChild("Light Node")).getChild("Waypoint Graph");
        map = new Map(MapFileReader.loadMap("assets/MapFiles/Labyrinth1.txt")).map;     
        
        if(debug) {
            isDebugActive = true;
        }
        
        buildGraph();
    }

    public List<PathfinderNode> getNodeList() throws NullPointerException {
        if(pfNodeList == null) {
            throw new NullPointerException("The waypoint graph was not created prior this method.");
        } else {
            return pfNodeList;
        }
    }
    
    /**
     * Generates a waypoint graph when pathfinder is first constructed
     */
    private void buildGraph() {
        pfNodeList = new LinkedList<>();

        for (int y = 0; y < dimX; y++) {
            for (int x = 0; x < dimX; x++) {
                switch (map[x][y].code) {
                    case ' ':
                    case 'S':
                        PathfinderNode pfNode = new PathfinderNode("pf_Node", pfNodeList);
                        pfNode.setLocalTranslation((4 * x), 0, (4 * y));
                        Spatial helper = createDebugVisuals();
                        waypointGraph.attachChild(pfNode);
                        pfNodeList.add(pfNode);
                        pfNode.attachChild(helper);
                        break;
                }
            }
        }

        for (PathfinderNode node : pfNodeList) {
            node.locateAdjacentNodes(assetManager);
        }
        if (!isDebugActive) {
            for (PathfinderNode node : pfNodeList) {
                node.getChild("pf_visual").removeFromParent();
            }
        }
    }
    
    private void calculateAllHeuristics() {
        
    }

    private Spatial createDebugVisuals() {
        Box phBox = new Box(.2f, .2f, .2f);
        Material pfMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Geometry pfGeom = new Geometry("pf_visual", phBox);
        ColorRGBA color = ColorRGBA.Red;
        pfMat.setColor("Color", color);
        pfGeom.setMaterial(pfMat);
        return pfGeom;
    }

    public void enableDebugVisuals(boolean value) {
        isDebugActive = value;
    }

    public static boolean isDebugActive() {
        return isDebugActive;
    }
}
