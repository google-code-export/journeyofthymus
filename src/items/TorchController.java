package items;

import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import player.PlayerController;

/**
 *
 * @author MIKUiqnw0
 */
public class TorchController extends AbstractControl {
    
    private float oilTimer, tinderTimer;
    private static final float tinderTimerMax = 480;
    private static final float oilTimerMax = 600;
    private PlayerController player;
    private PointLight light;
    private Node lightNode;
    private Camera cam;
            
    public TorchController(PlayerController player, Node lightNode, Camera cam) {
        oilTimer = 5;
        tinderTimer = 480;
        this.player = player;
        this.lightNode = lightNode;
        this.cam = cam;
        createLight();
    }

    public float getOilTimer() {
        return oilTimer;
    }
    
    public float getTinderTimer() {
        return tinderTimer;
    }
    
    public static float getMaxOilTime() {
        return oilTimerMax;
    }
    
    public static float getMaxTinderTime() {
        return tinderTimerMax;
    }
    
    public void setOilTimer(float modifier) {
        oilTimer += (oilTimer + modifier > oilTimerMax) ? oilTimerMax - oilTimer : modifier;
    }
    
    public void setTinderTimer(float modifier) {
        tinderTimer += (tinderTimer + modifier > tinderTimerMax) ? tinderTimerMax - tinderTimer : modifier;
    }
    
    public void useOil() {
        if (player.isPositiveInventory(ItemType.OIL)) {
            if (oilTimer < oilTimerMax) {
                setOilTimer(Oil.getExtensionTime());
                player.setOilCount(-1);
            }
        }
    }
        
    public void useTinder() {
        if (player.isPositiveInventory(ItemType.TINDER)) {
            if (tinderTimer < tinderTimerMax) {
                setTinderTimer(Tinder.getExtensionTime());
                player.setTinderCount(-1);
            }
        }
    }

    private void createLight() {
        light = new PointLight();
        light.setColor(ColorRGBA.White.mult(1.5f));
        light.setRadius(10f);
        lightNode.addLight(light);
    }
    
    private void torchFlicker() {
    
}
    @Override
    protected void controlUpdate(float tpf) {
        light.setPosition(cam.getLocation());
        if(oilTimer > 0) {
            oilTimer -= tpf;
        }
        if(tinderTimer > 0) {
            tinderTimer -= tpf;
        }
        if(oilTimer <= 0 || tinderTimer <= 0) {
            light.setRadius(0.001f);
        } else {
            if(light.getRadius() != 10) {
                light.setRadius(10);
            }
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return null;
    }  
}
