package player;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import driver.ApplicationInterface;
import items.Boots;
import items.ItemType;
import items.Potion;
import items.TorchController;

/**
 * Object establishes attributes of the human controlled character, including
 * input controls.
 *
 * @author MIKUiqnw0
 * @since 8/03/2013
 * @version 0.00.02
 */
public class PlayerController extends CharacterControl implements ActionListener {

    private int health, invHealthPotion, invSpeedPot, invOil, invTinder, regenAmount;
    private float regenRate, regenTimer, moveSpeed;
    private float sprintTimer, speedPotionTimer, bootsCooldownTimer;
    private boolean canSprint, sprinting, speedPotionStatus, bootsCooldown;
    private boolean left, right, forward, backward;
    private Camera cam;
    private InputManager inputManager;
    private Node camNode;

    /**
     *
     * @param app
     * @param camNode
     */
    public PlayerController(ApplicationInterface app, Node camNode) {
        super(new CapsuleCollisionShape(0.6f, 2f, 1), 0.55f);
        health = 100;
        moveSpeed = 0.1f;
        regenRate = 15;
        regenAmount = 1;
        
        this.camNode = camNode;
        cam = app.getCamera();
        inputManager = app.getInputManager();
        setupKeys();
    }

    /**
     *
     * @return
     */
    public int getHealth() {
        return health;
    }

    /**
     *
     * @return
     */
    public int getHealthPotCount() {
        return invHealthPotion;
    }

    /**
     *
     * @return
     */
    public int getSpeedPotCount() {
        return invSpeedPot;
    }

    /**
     *
     * @return
     */
    public int getOilCount() {
        return invOil;
    }

    /**
     *
     * @return
     */
    public int getTinderCount() {
        return invTinder;
    }

    /**
     *
     * @return
     */
    public float getMoveSpeed() {
        return moveSpeed;
    }

    /**
     *
     * @return
     */
    public float getRegenRate() {
        return regenRate;
    }

    /**
     *
     * @return
     */
    public int getRegenAmount() {
        return regenAmount;
    }

    /**
     *
     * @param modifier
     */
    public void setHealth(int modifier) {
        health += modifier;
        // Add validation
    }

    /**
     *
     * @param newMoveSpeed
     */
    public void setMoveSpeed(float newMoveSpeed) {
        moveSpeed = newMoveSpeed;
    }

    /**
     *
     * @param newRegenRate
     */
    public void setRegenRate(float newRegenRate) {
        regenRate = newRegenRate;
    }

    /**
     *
     * @param newRegenAmount
     */
    public void setRegenAmount(int newRegenAmount) {
        regenAmount = newRegenAmount;
    }

    /**
     *
     * @param modifier
     */
    public void setHealthPotionCount(int modifier) {
        invHealthPotion += modifier;
    }

    /**
     *
     * @param modifier
     */
    public void setSpeedPotionCount(int modifier) {
        invSpeedPot += modifier;
    }

    /**
     *
     * @param modifier
     */
    public void setOilCount(int modifier) {
        invOil += modifier;
    }

    /**
     *
     * @param modifier
     */
    public void setTinderCount(int modifier) {
        invTinder += modifier;
    }

    private void usePotion(ItemType type) {
        if (isPositiveInventory(type)) {
            switch (type) {
                case HEALTHPOTION:
                    if (health < 100 && health > 0) {
                        health += Potion.getModifier(type);
                        if (health > 100) {
                            health = 100;
                        }
                        --invHealthPotion;
                    }
                    break;
                case SPEEDPOTION:
                    moveSpeed += Potion.getModifier(type);
                    --invSpeedPot;
                    speedPotionStatus = true;
                    break;
            }
        }
    }

    private void useBoots() {
        if (canSprint && !bootsCooldown) {
            moveSpeed += Boots.getModifier();
            sprinting = true;
            bootsCooldown = true;
        }
    }

    private void worldInteract() {
        // world interaction method, like doors
    }

    /**
     *
     */
    public void enableSprint() {
        canSprint = true;
    }

    /**
     *
     * @param type
     * @return
     */
    public boolean isPositiveInventory(ItemType type) {
        if ((type == ItemType.HEALTHPOTION && invHealthPotion > 0)
                || (type == ItemType.SPEEDPOTION && invSpeedPot > 0)
                || (type == ItemType.OIL && invOil > 0)
                || (type == ItemType.TINDER && invTinder > 0)) {
            return true;
        }
        return false;
    }

    private void regenerationCheck(float tpf) {
        if (regenTimer < regenRate) {
            regenTimer += tpf;
        } else {
            health += regenAmount;
            regenTimer = 0;
        }
    }

    private void sprintingStatusCheck(float tpf) {
        if (sprintTimer < Boots.getSpeedTime()) {
            sprintTimer += tpf;
        } else {
            moveSpeed -= Boots.getModifier();
            sprinting = false;
            sprintTimer = 0;
        }
    }

    private void speedPotionStatusCheck(float tpf) {
        if (speedPotionTimer < Potion.getSpeedTime()) {
            speedPotionTimer += tpf;
        } else {
            moveSpeed -= Potion.getModifier(ItemType.SPEEDPOTION);
            speedPotionStatus = false;
            speedPotionTimer = 0;
        }
    }

    private void updateCooldownTimer(float tpf) {
        if (bootsCooldownTimer < Boots.getCooldown()) {
            bootsCooldownTimer += tpf;
        } else {
            bootsCooldown = false;
            bootsCooldownTimer = 0;
        }
    }

    private void updateMovement() {
        Vector3f camDir = cam.getDirection().clone();
        Vector3f camLeft = cam.getLeft().clone();
        walkDirection.set(0, 0, 0);

        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (forward) {
            walkDirection.addLocal(camDir);
        }
        if (backward) {
            walkDirection.addLocal(camDir.negate());
        }

        walkDirection.normalizeLocal().multLocal(moveSpeed);
        walkDirection.y = 0;
        setWalkDirection(walkDirection);

    }

    private void updateCamera() {
        Vector3f eyeHeight = getPhysicsLocation();
        eyeHeight.y = 0.6f;

        cam.setLocation(eyeHeight);
    }

    @Override
    public void update(float tpf) {
        if (health <= 0 || health != 100) {
            regenerationCheck(tpf);
        }
        if (sprinting) {
            sprintingStatusCheck(tpf);
        }
        if (speedPotionStatus) {
            speedPotionStatusCheck(tpf);
        }
        if (bootsCooldown) {
            updateCooldownTimer(tpf);
        }

        updateMovement();
        updateCamera();
    }

    /*
     * Initializes the default player keyboard setup for Journey of Thymus
     */
    /**
     *
     */
    public final void setupKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Use", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("Use_HP", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("Use_SP", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("Use_Oil", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("Use_Tinder", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("Sprint", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("getcord", new KeyTrigger(KeyInput.KEY_F4));

        inputManager.addListener(this, new String[]{
                    "Left", "Right", "Forward", "Backward", "Use", "Use_HP", "Use_SP",
                    "Use_Oil", "Use_Tinder", "Sprint", "getcord"
        });
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (this.isEnabled()) {
            switch (name) {
                case "Left":
                    left = isPressed;
                    break;
                case "Right":
                    right = isPressed;
                    break;
                case "Forward":
                    forward = isPressed;
                    break;
                case "Backward":
                    backward = isPressed;
                    break;
            }

            if (isPressed) {
                switch (name) {
                    case "Use":
                        worldInteract();
                        break;
                    case "Use_HP":
                        usePotion(ItemType.HEALTHPOTION);
                        break;
                    case "Use_SP":
                        usePotion(ItemType.SPEEDPOTION);
                        break;
                    case "Use_Oil":
                        camNode.getChild("Torch").getControl(TorchController.class).useOil();
                        break;
                    case "Use_Tinder":
                        camNode.getChild("Torch").getControl(TorchController.class).useTinder();
                        break;
                    case "Sprint":
                        useBoots();
                        break;
                    case "getcord":
                        System.out.println(getPhysicsLocation().toString());
                        break;
                }
            }
        }
    }
}
