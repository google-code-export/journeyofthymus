package generators;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * Simple factory class to load models and generate objects in the game world
 * @author James
 * @param
 * @since 8/03/2013
 * @version 0.00.01
 */
abstract public class ObjectFactory {

    private static AssetManager assetManager;
    public static final float CRATE_SIZE = 0.5f;

    public static Spatial makeKey() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static Spatial makePotion() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static Spatial makeBoots() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static Spatial makeFloor(float width) {
        Geometry floor = new Geometry("Floor", new Box(width / 2, 0.25f, width / 2));
        Material floorMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        floorMat.setBoolean("UseMaterialColors", true);
        ColorRGBA color = ColorRGBA.randomColor();
        floorMat.setColor("Diffuse", color);
        floorMat.setColor("Ambient", color);
        floor.setMaterial(floorMat);
        return floor;
    }

    public static Spatial makeCeiling(float width) {
        Geometry ceiling = new Geometry("Ceiling", new Box(width / 2, 0.25f, width / 2));
        Material ceilingMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        ceilingMat.setBoolean("UseMaterialColors", true);
        ColorRGBA color = ColorRGBA.randomColor();
        ceilingMat.setColor("Diffuse", color);
        ceilingMat.setColor("Ambient", color);
        ceiling.setMaterial(ceilingMat);
        return ceiling;
    }

    public static Spatial makeBlock(float width, float height, String id) {
        int rand = (int) (Math.random() * 4) + 1;
        Spatial block = assetManager.loadModel("Models/wall_"+ rand + "/wall_"+ rand + ".obj");
        Material blockMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        blockMat.setTexture("DiffuseMap", assetManager.loadTexture("Models/wall_" + rand + "/wall_" + rand + "_texture.jpg"));
        block.setMaterial(blockMat);

        return block;
    }
    
    public static GhostControl makeGhost(float width, float height) {
        GhostControl soundGhost = new GhostControl(new BoxCollisionShape(new Vector3f(width / 2, height / 2, width / 2)));
        return soundGhost;
    }

    public static Spatial makeDecoration() {
        Geometry decoration = new Geometry("Crate", new Box(CRATE_SIZE, CRATE_SIZE, CRATE_SIZE));
        Material decorationMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        //Spatial decoration = assetManager.loadModel("Models/Crate.j3o");
        decorationMat.setTexture("DiffuseMap", assetManager.loadTexture("Models/crate/Crate Texture.jpg"));
        
        decoration.setMaterial(decorationMat);
        return decoration;

    }

    public static Spatial makeDoor() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static Spatial makeTinderBox() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static Spatial makeOilCanister() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static Spatial makeTorch() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * @param aAssetManager the assetManager to set
     */
    public static void setAssetManager(AssetManager aAssetManager) {
        assetManager = aAssetManager;
    }
}
