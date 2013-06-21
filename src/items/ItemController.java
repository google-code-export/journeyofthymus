package items;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import driver.ApplicationInterface;
import player.PlayerController;

/**
 * Establishes collision of all in game collectable items and deals with their
 * removal upon collection.
 *
 * @author MIKUiqnw0
 * @since 21/03/2013
 * @version 0.00.01
 */
public class ItemController extends RigidBodyControl implements PhysicsCollisionListener {

    private boolean taken;
    private Item itemReference;

    /**
     *
     * @param shape
     * @param mass
     * @param app
     * @param type
     */
    public ItemController(CollisionShape shape, float mass, ApplicationInterface app, ItemType type) {
        super(shape, mass);
        app.getStateManager().getState(BulletAppState.class).getPhysicsSpace().addCollisionListener(this);
        taken = false;

        switch (type) {
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

    /**
     *
     * @param event
     */
    @Override
    public void collision(final PhysicsCollisionEvent event) {
        PlayerController player;
        ItemController item;

        if(event.getNodeA() != null && event.getNodeB() != null) {
            if (!this.taken && (player = event.getNodeA().getControl(PlayerController.class)) != null) {
                if ((item = event.getNodeB().getControl(ItemController.class)) != null) {
                    takeItem(player, item.getItemReference().getItemType(), (Node) event.getNodeB(), item);
                }
            }
        }
    }

    private void takeItem(PlayerController player, ItemType type, Node itemNode, ItemController item) {
        switch (type) {
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
        
        if (itemNode != null && this.taken == true) {
            itemNode.removeControl(item);
            itemNode.removeFromParent();
            getPhysicsSpace().remove(item);
        }
    }

    /**
     *
     * @return
     */
    public Item getItemReference() {
        return itemReference;
    }
}
