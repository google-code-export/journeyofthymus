package generators;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
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
        Material floorMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        floorMat.setBoolean("UseMaterialColors",true);
        ColorRGBA color = ColorRGBA.randomColor();
        floorMat.setColor("Diffuse", color);
        floorMat.setColor("Ambient", color);
        //floorMat.setColor("Color", ColorRGBA.Gray);
        floor.setMaterial(floorMat);
        return floor;
    }
    
    public static Spatial makeBlock(int width, int height, String id) {
        Geometry block = new Geometry("Block" + id, new Box(width / 2, height / 2, width / 2));
        Material blockMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        blockMat.setBoolean("UseMaterialColors",true);
        ColorRGBA color = ColorRGBA.randomColor();
        blockMat.setColor("Diffuse", color);
        blockMat.setColor("Ambient", color);
        //blockMat.setColor("Color", ColorRGBA.randomColor());
        block.setMaterial(blockMat);

        return block;
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
