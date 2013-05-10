package items;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import player.PlayerController;

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
    
    private boolean taken;
    private Item itemReference;
    
    public ItemController(CollisionShape shape, float mass, BulletAppState bulletAppState, ItemType type) {
        super(shape, mass);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        taken = false;
        
        switch(type) {
            case HEALTHPOTION:
            case SPEEDPOTION:
                itemReference = new Potion(type);
                break;
            case KEY:
                itemReference = new Key(type);
                break;
            case BOOTS:
                itemReference = new Boots(type);
                break;
            case OIL:
                itemReference = new Oil(type);
                break;
            case TINDER:
                itemReference = new Tinder(type);
                break;
        }
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        PlayerController player;
        ItemController item;
        if(!this.taken && (player = event.getNodeA().getControl(PlayerController.class)) != null) { 
            if((item = event.getNodeB().getControl(ItemController.class)) != null) {
                takeItem(player, item, event.getNodeB(), item.getItemReference().getItemType());
            }
        }
    }
    
    private void takeItem(PlayerController player, Control item, Spatial itemSpatial, ItemType type) {
        switch(type) {
            case HEALTHPOTION:
                player.setHealthPotionCount(1);
                break;
            case SPEEDPOTION:
                player.setSpeedPotionCount(1);
                break;
            case KEY:
                
                break;
            case BOOTS:
                player.enableSprint();
                break;
            case OIL:
                player.setOilCount(1);
                break;
            case TINDER:
                player.setTinderCount(1);
                break;
        }
        
        this.taken = true;
        
        if(itemSpatial != null && this.taken == true) {
            itemSpatial.removeFromParent();
            getPhysicsSpace().remove(item);
        }
        
        
    }
    
    public Item getItemReference() {
        return itemReference;
    }
}
