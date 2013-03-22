package items;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;

/**
 * Establishes collision of all in game collectable items and deals 
 * with their removal upon collection.
 * 
 * @author MIKUiqnw0
 * @param shape
 * @param mass
 * @param bulletAppState
 * @since 21/03/2013
 * @version 0.00.01
 */
public class ItemController extends RigidBodyControl implements PhysicsCollisionListener {
    
    public ItemController(CollisionShape shape, float mass, BulletAppState bulletAppState) {
        super(shape, mass);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        
    }
}
