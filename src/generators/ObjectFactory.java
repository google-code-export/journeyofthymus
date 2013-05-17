package generators;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * 
 * @author MIKUiqnw0(Patricio)
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

    public static Spatial makeFloor(int width) {
        Geometry floor = new Geometry("Floor", new Box(width / 2, 0.25f, width / 2));
        Material floorMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        floorMat.setColor("Color", ColorRGBA.Gray);
        floor.setMaterial(floorMat);
        return floor;
    }
    
    public static Spatial makeCeiling(int width) {
        Geometry ceiling = new Geometry("Ceiling", new Box(width / 2, 0.25f, width / 2));
        Material ceilingMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        ceilingMat.setColor("Color", ColorRGBA.Gray);
        ceiling.setMaterial(ceilingMat);
        return ceiling;
    }
    
    public static Spatial makeBlock(int width, int height, String id) {
        Geometry block = new Geometry("Block" + id, new Box(width / 2, height / 2, width / 2));
        Material blockMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blockMat.setColor("Color", ColorRGBA.randomColor());
        block.setMaterial(blockMat);
        return block;
    }
    
    public static Spatial makeDecoration() {
        Geometry decoration = new Geometry("Crate", new Box(CRATE_SIZE, CRATE_SIZE, CRATE_SIZE));
        //Spatial decoration = assetManager.loadModel("Models/Crate.j3o");
        Material decorationMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        decorationMat.setTexture("ColorMap", assetManager.loadTexture("Models/Crate Texture.jpg"));
        //decorationMat.setColor("Color", ColorRGBA.White);
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
