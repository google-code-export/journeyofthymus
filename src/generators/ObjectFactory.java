package generators;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.effect.ParticleEmitter;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import items.ItemType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple factory class to load models and generate objects in the game world
 * @author James
 * @since 8/03/2013
 * @version 0.00.01
 */
abstract public class ObjectFactory {

    private static AssetManager assetManager;
    public static final float CRATE_SIZE = 0.5f;

    /**
     *
     * @return
     */
    public static ParticleEmitter makeFog() {
        ParticleEmitter fog = // Emits particles based on settings defined below
                new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 10000);
        Material mat_red = new Material(assetManager, 
                "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture(
                "Effects/Smoke/Smoke.png"));
        fog.setMaterial(mat_red);
        fog.setShape(new EmitterBoxShape(new Vector3f(0, -1.5f, 0), new Vector3f(MapFileReader.getDimensions() * 4, -1f, MapFileReader.getDimensions() * 4)));
        fog.setParticlesPerSec(1000);
        fog.setImagesX(15); 
        fog.setImagesY(1); // 2x2 texture animation
        fog.setEndColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.01f));   // red
        fog.setStartColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.5f)); // yellow
        fog.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0.02f, 0));
        fog.setStartSize(.01f);
        fog.setEndSize(5f);
        fog.setGravity(0, 0.01f, 0);
        fog.setRotateSpeed(3);
        fog.setLowLife(8f);
        fog.setHighLife(36f);
        fog.getParticleInfluencer().setVelocityVariation(5f);
        return fog;
    }
    
    /**
     *
     * @return
     */
    public static Spatial makeKey() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     *
     * @param potionType
     * @return
     */
    public static Spatial makePotion(ItemType potionType) {
        Spatial potion = null;
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        switch(potionType) {
            case HEALTHPOTION:
                try {
                    potion = assetManager.loadModel("Models/health_flask/health_flask.obj");
                } catch (AssetNotFoundException e) {
                    Logger.getLogger("ObjectFactory").log(Level.WARNING, "Health potion model missing! Drawing block");
                    potion = new Geometry("tmphealthpotion", new Box(0.2f, 0.2f, 0.2f));
                }
                
                try {
                    mat.setTexture("DiffuseMap", assetManager.loadTexture("Models/health_flask/health.jpg"));
                } catch (AssetNotFoundException e) {
                    Logger.getLogger("ObjectFactory").log(Level.WARNING, "Health potion texture missing! Applying flat color (ColorRGBA.Red)");
                    mat.setBoolean("UseMaterialColors", true);
                    mat.setColor("Diffuse", ColorRGBA.Red);
                }
                break;
            case SPEEDPOTION:
                try {
                    potion = assetManager.loadModel("Models/stamina_flask/stamina_flask.obj");
                } catch (AssetNotFoundException e) {
                    Logger.getLogger("ObjectFactory").log(Level.WARNING, "Speed potion model missing! Drawing block");
                    potion = new Geometry("tmpspeedpotion", new Box(0.2f, 0.2f, 0.2f));
                }
                
                try {
                    mat.setTexture("DiffuseMap", assetManager.loadTexture("Models/stamina_flask/stamina.jpg"));
                } catch (AssetNotFoundException e) {
                    Logger.getLogger("ObjectFactory").log(Level.WARNING, "Speed potion texture missing! Applying flat color (ColorRGBA.Yellow)");
                    mat.setBoolean("UseMaterialColors", true);
                    mat.setColor("Diffuse", ColorRGBA.Yellow);
                }
                break;
        }
        potion.setMaterial(mat);
        return potion;
    }

    /**
     *
     * @return
     */
    public static Spatial makeBoots() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     *
     * @param width
     * @return
     */
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

    /**
     *
     * @param width
     * @return
     */
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

    /**
     *
     * @param width
     * @param height
     * @param id
     * @return
     */
    public static Spatial makeBlock(float width, float height, String id) {
        Spatial block;
        Material blockMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        int rand = (int) (Math.random() * 4) + 1;
        
        try {
            block = assetManager.loadModel("Models/wall_"+ rand + "/wall_"+ rand + ".obj");
        } catch (AssetNotFoundException e) {
            Logger.getLogger("ObjectFactory").log(Level.WARNING, "Missing wall models! Reverting to geometry");
            block = new Geometry("tempwall", new Box(width, height, width));
        }
        
        try {
            blockMat.setTexture("DiffuseMap", assetManager.loadTexture("Models/wall_" + rand + "/wall_" + rand + "_texture.jpg"));
        } catch (AssetNotFoundException e) {
            Logger.getLogger("ObjectFactory").log(Level.WARNING, "Missing wall textures! Using flat color");
            blockMat.setBoolean("UseMaterialColors", true);
            blockMat.setColor("Diffuse", ColorRGBA.randomColor());
        }
        
        block.setMaterial(blockMat);

        return block;
    }
    
    public static GhostControl makeGhost(float width, float height) {
        GhostControl soundGhost = new GhostControl(new BoxCollisionShape(new Vector3f(width / 2, height / 2, width / 2)));
        return soundGhost;
    }

    /**
     *
     * @return
     */
    public static Spatial makeDecoration() {
        Geometry decoration = new Geometry("Crate", new Box(CRATE_SIZE, CRATE_SIZE, CRATE_SIZE));
        Material decorationMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        //Spatial decoration = assetManager.loadModel("Models/Crate.j3o");
        try {
            decorationMat.setTexture("DiffuseMap", assetManager.loadTexture("Models/crate/Crate Texture.jpg"));
        } catch (AssetNotFoundException e) {
            Logger.getLogger("ObjectFactory").log(Level.WARNING, "Missing decoration texture! Using flat color");
            decorationMat.setBoolean("UseMaterialColors", true);
            decorationMat.setColor("Diffuse", ColorRGBA.randomColor());
        }
        
        decoration.setMaterial(decorationMat);
        return decoration;

    }

    /**
     *
     * @return
     */
    public static Spatial makeDoor() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     *
     * @return
     */
    public static Spatial makeTinderBox() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     *
     * @return
     */
    public static Spatial makeOilCanister() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     *
     * @return
     */
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
