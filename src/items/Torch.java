package items;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;

/**
 *
 * @author MIKUiqnw0
 */
public class Torch extends RigidBodyControl {
    
    private float oilTimer, tinderTimer;
    private static final float tinderTimerMax = 480;
    private static final float oilTimerMax = 600;
            
    public Torch(CollisionShape shape) {
        super(shape);
        oilTimer = 600;
        tinderTimer = 480;
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
}
