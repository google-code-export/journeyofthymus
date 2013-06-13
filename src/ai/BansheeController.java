package ai;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.List;
import java.util.Random;
import player.PlayerController;

/**
 * AI Controller for the Banshee monster<br/> States:<br/> IDLE: Performs no
 * movement commands or wanders within waypoint range<br/> NEXT_WAYPOINT: Paths
 * to next waypoint<br/> TARGET_CHASE: Locks on to and chases target
 * (player)<br/> TARGET_LOST: Loses lock on target and wanders slighty from last
 * known location, scanning directions<br/> TARGET_ATTACK: Strikes target and
 * deals damage<br/> RECYCLE: Performs death animation (after TARGET_ATTACK) and
 * respawns the monster elsewhere into IDLE mode<br/>
 *
 * @author MIKUiqnw0
 * @since 31/05/2013
 */
public class BansheeController extends CharacterControl implements AnimEventListener {

    private final int damage = -25;
    private float moveSpeed, deathTimer = 30;
    private AIState aiState;
    private PlayerController player;
    private Pathfinder pathfinder;
    private AnimControl animControl;
    private AnimChannel animChannel;
    private List<PathfinderNode> pfNodeList;
    private PathfinderNode currentWaypoint;   
    private boolean isWalking;
    private Node attachedObject;

    private enum AIState {

        IDLE, TRAVERSE_WAYPOINT, TARGET_CHASE, TARGET_LOST, TARGET_ATTACK, RECYCLE
    };
    
    public BansheeController(PlayerController player, Node attachedObject, Pathfinder pathfinder) {
        super(new CapsuleCollisionShape(0.6f, 2f, 1), 0.55f);
        moveSpeed = player.getMoveSpeed() - 0.03f;
        aiState = AIState.TARGET_CHASE;
        this.player = player;
        this.pathfinder = pathfinder;
        animControl = attachedObject.getControl(AnimControl.class);
        animControl.addListener(this);
        animChannel = animControl.createChannel();
        this.attachedObject = attachedObject;
        pfNodeList = pathfinder.getNodeList();
        currentWaypoint = pfNodeList.get(new Random().nextInt(pfNodeList.size()));
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        stateMachine(tpf);
    }

    private void stateMachine(float tpf) {
        switch (aiState) {
            case IDLE:
                aiState = AIState.TARGET_CHASE;
                break;
            case TRAVERSE_WAYPOINT:
                // Select a random node from the graph list
                PathfinderNode target = pfNodeList.get(new Random().nextInt(pfNodeList.size()));
                break;
            case TARGET_CHASE:
                Vector3f playerPosition = player.getPhysicsLocation().subtract(getPhysicsLocation());
                walkDirection.addLocal(player.getPhysicsLocation().subtractLocal(getPhysicsLocation())).normalizeLocal();
                walkDirection.multLocal(moveSpeed);
                setViewDirection(playerPosition);
                if (getPhysicsLocation().distance(player.getPhysicsLocation()) > 2.5f) {
                    setWalkDirection(walkDirection);
                    if (!isWalking) {
                        isWalking = true;
                        animChannel.setAnim("Walk", 0.55f);
                        animChannel.setLoopMode(LoopMode.Loop);
                    }
                } else {
                    aiState = AIState.TARGET_ATTACK;
                }
                break;

            case TARGET_LOST:
                break;
            case TARGET_ATTACK:
                isWalking = false;
                animChannel.setAnim("stand");
                //animChannel.setAnim("attack");
                animChannel.setLoopMode(LoopMode.DontLoop);
                setWalkDirection(Vector3f.ZERO);

                //animChannel.setAnim("scream");
                Geometry objGeom = (Geometry)attachedObject.getChild(0);
                objGeom.getMaterial().setBoolean("UseMaterialColors", true);
                objGeom.getMaterial().setColor("GlowColor", ColorRGBA.Red);
                
                player.setHealth(damage);
                aiState = AIState.RECYCLE;
                break;
            case RECYCLE:
                if (deathTimer <= 0) {
                    aiState = AIState.IDLE;
                    deathTimer = 30;
                } else {
                    deathTimer -= tpf;
                }
                break;
        }
    }

    public PathfinderNode getCurrentWaypoint() {
        return currentWaypoint;
    }
    
    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }
}
