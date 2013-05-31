package ai;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import player.PlayerController;

/**
 * AI Controller for the Banshee monster<br> States:<br> IDLE: Performs no
 * movement commands or wanders within waypoint range<br> NEXT_WAYPOINT: Paths
 * to next waypoint<br> TARGET_CHASE: Locks on to and chases target (player)<br>
 * TARGET_LOST: Loses lock on target and wanders slighty from last known
 * location, scanning directions<br> TARGET_ATTACK: Strikes target and deals
 * damage<br> RECYCLE: Performs death animation (after TARGET_ATTACK) and
 * respawns the monster elsewhere into IDLE mode<br>
 *
 * @author MIKUiqnw0
 * @since 31/05/2013
 */
public class BansheeController extends CharacterControl implements AnimEventListener {

    private int damage;
    private float moveSpeed, directionTimer;
    private boolean isWalking;
    private Vector3f playerPosition;
    private PlayerController player;
    private AnimControl animControl;
    private AnimChannel animChannel;
    private AIState aiState;
    

    private static enum AIState {

        IDLE, NEXT_WAYPOINT, TARGET_CHASE, TARGET_LOST, TARGET_ATTACK, RECYCLE
    };

    public BansheeController(PlayerController player, Node attachedObject) {
        super(new CapsuleCollisionShape(0.4f, 1.5f, 1), 0.55f);
        damage = 25;
        moveSpeed = player.getMoveSpeed() - 0.03f;
        aiState = AIState.IDLE;
        this.player = player;
        animControl = attachedObject.getControl(AnimControl.class);
        animControl.addListener(this);
        animChannel = animControl.createChannel();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        stateMachine(tpf);
    }

    private void stateMachine(float tpf) {
        if (aiState != AIState.RECYCLE) {
            switch (aiState) {
                case IDLE:
                    if(!isWalking) {
                        isWalking = true;
                        animChannel.setAnim("Walk", 0.55f);
                        animChannel.setLoopMode(LoopMode.Loop);
                    }
                    
                    if (directionTimer <= 0) {
                        directionTimer = 2;
                        playerPosition = player.getPhysicsLocation().subtract(getPhysicsLocation());
                        walkDirection.addLocal(player.getPhysicsLocation().subtractLocal(getPhysicsLocation())).normalizeLocal();
                        walkDirection.multLocal(moveSpeed);
                    } else {
                        setViewDirection(playerPosition);
                        if (getPhysicsLocation().distance(player.getPhysicsLocation()) > 2.5f) {
                            setWalkDirection(walkDirection);
                        } else if (isWalking) {
                            isWalking = false;
                            animChannel.setAnim("stand");
                            animChannel.setLoopMode(LoopMode.DontLoop);
                            setWalkDirection(Vector3f.ZERO);
                        }
                    }
                    directionTimer -= tpf;
                    break;
            }
        } else {
        }
    }
    
    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }
}
