package ai;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;
import java.util.List;

/**
 * 
 *
 * @author MIKUiqnw0
 * @param 
 * @since 
 * @version 0.00.01
 */
public class PathfinderNode extends Node {

    private int h_heuristicValue, g_movementCost, f_totalCost;
    private PathfinderNode parentNode, north, south, east, west;
    private Node waypointNode;
        
    public PathfinderNode(String name, Node waypointNode) {
        super(name);
        h_heuristicValue = 0;
        g_movementCost = 0;
        f_totalCost = 0; 
        this.waypointNode = waypointNode;
    }
    
    public int getHeuristic() {
        return h_heuristicValue;
    }
    public int getMoveCost() {
        return g_movementCost;
    }
    public int getTotalCost() {
        return f_totalCost = 0;
    }
    public void setHeuristic(int value) {
        h_heuristicValue = value;
    }
    public void setMoveCost(int value) {
        g_movementCost = value;
    }
    
    public void calculateTotalCost() {
        f_totalCost = g_movementCost + h_heuristicValue;
    }
    
    public void locateAdjacentNodes(AssetManager man) {
        CollisionResults results = new CollisionResults();
        Spatial child = getChild("wp_visual");

        Ray ray = new Ray(child.getLocalTranslation(), Vector3f.UNIT_Z);
        Geometry line = new Geometry("line", new Line(ray.getOrigin(), ray.getDirection()));
        Material m = new Material(man, "Common/MatDefs/Misc/Unshaded.j3md");
        line.setMaterial(m);
        attachChild(line);
        
        List<Spatial> objList = getParent().getChildren();
        for(Spatial object : objList) {
            Node node = (Node) object;
            node.getChild("wp_visual").collideWith(ray, results);
            
//            System.out.println("----- Collisions? " + results.size() + "-----");
//            for (int i = 0; i < results.size(); i++) {
//              // For each hit, we know distance, impact point, name of geometry.
//              float dist = results.getCollision(i).getDistance();
//              Vector3f pt = results.getCollision(i).getContactPoint();
//              String hit = results.getCollision(i).getGeometry().getName();
//              System.out.println("* Collision #" + i);
//              System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
//            }
        }
    }
}
