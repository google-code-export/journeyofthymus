package player;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import driver.ApplicationInterface;
import items.Boots;
import items.ItemType;
import items.Oil;
import items.Potion;
import items.Torch;

/**
 * Object establishes attributes of the human controlled character, including
 * input controls.
 *
 * @author MIKUiqnw0
 * @param inputManager Requires the inputManager object to implement controls
 * @since 8/03/2013
 * @version 0.00.02
 */
public class PlayerController extends CharacterControl implements ActionListener {

    private int health, invHealthPotion, invSpeedPot, invOil, invTinder, regenAmount;
    private float regenRate, regenTimer, runSpeed;
    private float sprintTimer, speedPotionTimer, bootsCooldownTimer;
    private boolean canSprint, crouching, sprintingStatus, speedPotionStatus, bootsCooldown;
    private boolean left, right, forward, backward;
    private ApplicationInterface app;
    private Torch torchObj;
    private Vector3f eyeHeight;

    public PlayerController(ApplicationInterface app) {
        super(new CapsuleCollisionShape(0.2f, 1.5f, 1), 1f);
        health = 100;
        runSpeed = 5;
        regenRate = 15;
        regenAmount = 1;

        this.app = app;
        torchObj = new Torch(new BoxCollisionShape(new Vector3f(0.3f, 1, 0.3f)));
    }

    public int getHealth() {
        return health;
    }

    public int getHealthPotCount() {
        return invHealthPotion;
    }

    public int getSpeedPotCount() {
        return invSpeedPot;
    }

    public int getOilCount() {
        return invOil;
    }

    public int getTinderCount() {
        return invTinder;
    }

    public float getRunSpeed() {
        return runSpeed;
    }

    public float getRegenRate() {
        return regenRate;
    }

    public int getRegenAmount() {
        return regenAmount;
    }

    public void setHealth(int modifier) {
        health += modifier;
        // Add validation
    }

    public void setRunSpeed(float newRunSpeed) {
        runSpeed = newRunSpeed;
    }

    public void setRegenRate(float newRegenRate) {
        regenRate = newRegenRate;
    }

    public void setRegenAmount(int newRegenAmount) {
        regenAmount = newRegenAmount;
    }

    public void setHealthPotionCount(int modifier) {
        invHealthPotion += modifier;
    }

    public void setSpeedPotionCount(int modifier) {
        invSpeedPot += modifier;
    }

    public void setOilCount(int modifier) {
        invOil += modifier;
    }

    public void setTinderCount(int modifier) {
        invTinder += modifier;
    }

    public void initializeArms() {
        //model loading here
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
                    runSpeed += Potion.getModifier(type);
                    --invSpeedPot;
                    speedPotionStatus = true;
                    break;
            }
        }
    }

    private void useBoots() {
        if (canSprint && !bootsCooldown) {
            runSpeed += Boots.getModifier();
            sprintingStatus = true;
            bootsCooldown = true;
        }
    }

    private void useOil() {
        if (isPositiveInventory(ItemType.OIL)) {
            if (torchObj.getOilTimer() < Torch.getMaxOilTime()) {
                torchObj.setOilTimer(Oil.getExtensionTime());
                --invOil;
            }
        }
    }

    private void useTinder() {
        if (isPositiveInventory(ItemType.TINDER)) {
            //method to tinder use
            --invTinder;
        }
    }

    private void worldInteract() {
        // world interaction method, like doors
    }

    private void doCrouchAnim() {
        // do animation
    }

    public void enableSprint() {
        canSprint = true;
    }

    // Experiment with collection 
    private boolean isPositiveInventory(ItemType type) {
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
            runSpeed -= Boots.getModifier();
            sprintingStatus = false;
            sprintTimer = 0;
        }
    }

    private void speedPotionStatusCheck(float tpf) {
        if (speedPotionTimer < Potion.getSpeedTime()) {
            speedPotionTimer += tpf;
        } else {
            runSpeed -= Potion.getModifier(ItemType.SPEEDPOTION);
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
        Vector3f camDir = app.getCamera().getDirection().clone();
        Vector3f camLeft = app.getCamera().getLeft().clone();
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (forward) {
            walkDirection.addLocal(camDir);                                  // Restrict Y plane movement when moving forward
        }
        if (backward) {
            walkDirection.addLocal(camDir.negate());                                    // and when moving backwards
        }
        walkDirection.setY(0);
        walkDirection.normalizeLocal().multLocal(0.15f);
        setWalkDirection(walkDirection);
        
        eyeHeight = getPhysicsLocation();
        eyeHeight.y += 0.65;
        eyeHeight.add(camDir.negate().multLocal(2f));
        app.getCamera().setLocation(eyeHeight);
    }

    @Override
    public void update(float tpf) {
        if (health <= 0 || health != 100) {
            regenerationCheck(tpf);
        }
        if (sprintingStatus) {
            sprintingStatusCheck(tpf);
        }
        if (speedPotionStatus) {
            speedPotionStatusCheck(tpf);
        }
        if (bootsCooldown) {
            updateCooldownTimer(tpf);
        }

    //    if (torchObj.getO) {}
        updateMovement();
        System.out.println(getPhysicsLocation());
    }

    /*
     * Initializes the default player keyboard setup for Journey of Thymus
     */
    public void setupKeys() {
        app.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        app.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        app.getInputManager().addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        app.getInputManager().addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        app.getInputManager().addMapping("Use", new KeyTrigger(KeyInput.KEY_E));
        app.getInputManager().addMapping("Use_HP", new KeyTrigger(KeyInput.KEY_1));
        app.getInputManager().addMapping("Use_SP", new KeyTrigger(KeyInput.KEY_2));
        app.getInputManager().addMapping("Use_Oil", new KeyTrigger(KeyInput.KEY_3));
        app.getInputManager().addMapping("Use_Tinder", new KeyTrigger(KeyInput.KEY_4));
        app.getInputManager().addMapping("Sprint", new KeyTrigger(KeyInput.KEY_LSHIFT));
        app.getInputManager().addMapping("Crouch", new KeyTrigger(KeyInput.KEY_LCONTROL));

        app.getInputManager().addListener(this, new String[]{
                    "Left", "Right", "Forward", "Backward", "Use", "Use_HP", "Use_SP",
                    "Use_Oil", "Use_Tinder", "Sprint", "Crouch"
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
                case "Use":
                    if (isPressed) {
                        worldInteract();
                    }
                    break;
                case "Use_HP":
                    if (isPressed) {
                        usePotion(ItemType.HEALTHPOTION);
                    }
                    break;
                case "Use_SP":
                    if (isPressed) {
                        usePotion(ItemType.SPEEDPOTION);
                    }
                    break;
                case "Use_Oil":
                    if (isPressed) {
                        useOil();
                    }
                    break;
                case "Use_Tinder":
                    if (isPressed) {
                        useTinder();
                    }
                    break;
                case "Sprint":
                    if (isPressed) {
                        useBoots();
                    }
                    break;
                case "Crouch":
                    crouching = isPressed;
                    if (isPressed) {
                        doCrouchAnim();
                    }
                    break;
            }
        }
    }
}
