package ai;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;
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
 * @param 
 * @since 4/06/2013
 * @version 0.00.01
 */
public class Pathfinder {
    private Tile[][] map;
    private int x, y, dimX;
    private static int nodeCount;
    private AssetManager assetManager;
    private GhostControl ghostControl;
    private Node waypointNode;
    private List<PathfinderNode> wpNodes;
    private boolean isDebugActive;

    public Pathfinder(ApplicationInterface app) {
        map = new Map(MapFileReader.loadMap("assets/MapFiles/Labyrinth1.txt")).map;
        dimX = MapFileReader.getDimensions();
        assetManager = app.getAssetManager();
        waypointNode = (Node)((Node) app.getRootNode().getChild("Light Node")).getChild("Waypoint Node");
    }
    
    public void buildGraph() {        
        wpNodes = new LinkedList<>();
        
        for (y = 0; y < dimX; y++) {
            for (x = 0; x < dimX; x++) {
                ++nodeCount;
                switch (map[x][y].code) {
                    case ' ':               
                    case 'S':                      
                        PathfinderNode waypoint = new PathfinderNode("wp_Node" + nodeCount, waypointNode);
                        Spatial helper = createDebugVisuals();
                        helper.setLocalTranslation((4 * x), 0, (4 * y));
                        //setLocalTranslation((4 * x), 0, (4 * y));
                        waypointNode.attachChild(waypoint);
                        wpNodes.add(waypoint);
                        waypoint.attachChild(helper);
                        break;
                }
            }
        }
        
        for(PathfinderNode node : wpNodes) {
            node.locateAdjacentNodes(assetManager);
        }
    }
    
    private Spatial createDebugVisuals() {
        Sphere wpSphere = new Sphere(32, 32, 0.2f);
        Material wpMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        Geometry wpGeom = new Geometry("wp_visual", wpSphere);
        ghostControl = new GhostControl(new SphereCollisionShape(0.2f));
        wpGeom.addControl(ghostControl);
        wpMat.setBoolean("UseMaterialColors", true);
        ColorRGBA color = ColorRGBA.Red;
        wpMat.setColor("Diffuse", color);
        wpSphere.setTextureMode(Sphere.TextureMode.Projected);
        TangentBinormalGenerator.generate(wpSphere);
        wpGeom.setMaterial(wpMat);
        return wpGeom;
    }
    
    public void enableDebugVisuals(boolean value) {
        isDebugActive = value;
    }
}
