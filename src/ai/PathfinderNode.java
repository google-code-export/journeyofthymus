package ai;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import java.util.List;

/**
 * @author MIKUiqnw0
 * @since 
 * @version 0.00.01
 */
public class PathfinderNode extends Node {

    private int h_heuristicValue, g_movementCost, f_totalCost;
    private PathfinderNode parentNode, north, south, east, west;
    private Geometry mark, line;
    private List<PathfinderNode> pfNodeList;
        
    /**
     *
     * @param name
     * @param pfNodeList
     */
    public PathfinderNode(String name, List<PathfinderNode> pfNodeList) {
        super(name);
        h_heuristicValue = 0;
        g_movementCost = 0;
        f_totalCost = 0; 
        this.pfNodeList = pfNodeList;
    }
    
    /**
     *
     * @return
     */
    public int getHeuristic() {
        return h_heuristicValue;
    }
    /**
     *
     * @return
     */
    public int getMoveCost() {
        return g_movementCost;
    }
    /**
     *
     * @return
     */
    public int getTotalCost() {
        return f_totalCost = 0;
    }
    /**
     *
     * @param value
     */
    public void setHeuristic(int value) {
        h_heuristicValue = value;
    }
    /**
     *
     * @param value
     */
    public void setMoveCost(int value) {
        g_movementCost = value;
    }
    
    /**
     *
     */
    public void calculateTotalCost() {
        f_totalCost = g_movementCost + h_heuristicValue;
    }
    
    /**
     *
     * @param assetManager
     */
    protected void initVisuals(AssetManager assetManager) {
        Box box = new Box(.05f, .05f, .05f);
        mark = new Geometry("box", box);
        line = new Geometry("line", new Line(Vector3f.ZERO, Vector3f.ZERO));
        Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mark_mat.setColor("Color", ColorRGBA.Green);
        mark.setMaterial(mark_mat);
        line.setMaterial(mark_mat);
    }
    
    /*
     * Each PathfinderNode requires knowledge of neighbouring PathfinderNodes
     */
    /**
     *
     * @param assetManager
     */
    public void locateAdjacentNodes(AssetManager assetManager) {
        CollisionResults[] results = new CollisionResults[4]; // CollisionResults collects collision entries caught by line 132.
        Ray[] ray = new Ray[4]; // Ray is used to produce a collidable object that fires from a position to a target point
        initVisuals(assetManager);
        
        for(int i = 0; i < 4; ++i) {
            results[i] = new CollisionResults();
        }

        /*
         * Fire rays in positive and negative directions along the X and Z axes
         * The ray translation is offset by .3 to avoid casting rays that collide
         * with the visual geometry we're firing from. We offset the y axis by .05
         * in order to draw clear visual representations of their relationship during
         * debugging
         */
        ray[0] = new Ray(getLocalTranslation().add(0, -.05f, .3f), Vector3f.UNIT_Z);        // Z Positive
        ray[1] = new Ray(getLocalTranslation().add(0, .05f, -.3f), new Vector3f(0, 0, -1)); // Z Negative
        ray[2] = new Ray(getLocalTranslation().add(.3f, -.05f, 0), Vector3f.UNIT_X);        // X Positive
        ray[3] = new Ray(getLocalTranslation().add(-.3f, .05f, 0), new Vector3f(-1, 0, 0)); // X Negative
        
        /*
         * Loop through the list of PathfinderNodes and setup the grid
         */
        for(int i = 0; i < 4; ++i) {
            for(PathfinderNode node : pfNodeList) {
                node.getChild("pf_visual").collideWith(ray[i], results[i]);
                if(results[i].size() > 0) {
                    CollisionResult closest = results[i].getClosestCollision();
                    
                    if(closest.getDistance() < 4) {
                        switch(i) {
                            case 0:
                                south = (PathfinderNode) closest.getGeometry().getParent();
                                break;
                            case 1:
                                north = (PathfinderNode) closest.getGeometry().getParent();
                                break;
                            case 2:
                                east = (PathfinderNode) closest.getGeometry().getParent();
                                break;
                            case 3:
                                west = (PathfinderNode) closest.getGeometry().getParent();
                                break;
                        }
                        
                        if(Pathfinder.isDebugActive()) {
                            Spatial markclone = mark.clone();
                            Geometry lineclone = line.clone();
                            markclone.setLocalTranslation(closest.getContactPoint());
                            getParent().getParent().attachChild(markclone);
                            getParent().getParent().attachChild(lineclone);
                            ((Line)lineclone.getMesh()).updatePoints(ray[i].getOrigin(), closest.getContactPoint());
                        }
                    }
                }
            }
        }
    }
}
