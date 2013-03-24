package player;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import driver.ApplicationInterface;
import items.Potion.PotionType;

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

    private int health;
    private float runSpeed;
    private int healthPot,
            speedPot,
            tinder;
    private float regenRate;
    private int regenAmount;
    private ApplicationInterface app;

    public PlayerController(ApplicationInterface app) {
        super(new CapsuleCollisionShape(1.5f, 5f, 1), 0.05f);
        health = 100;
        runSpeed = 5;
        healthPot = 0;
        speedPot = 0;
        tinder = 0;
        regenRate = 15;
        regenAmount = 1;

        this.app = app;
    }

    public int getHealth() {
        return health;
    }

    public int getHealthPotCount() {
        return healthPot;
    }

    public int getSpeedPotCount() {
        return speedPot;
    }

    public int getTinderCount() {
        return tinder;
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

    public Vector3f getPlayerLocation() {
        return this.getPhysicsLocation();
    }

    public void setHealth(int newHealth) {
        health = newHealth;
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
        healthPot += modifier;
    }

    public void setSpeedPotionCount(int modifier) {
        speedPot += modifier;
    }

    public void initializeArms() {
        //model loading here
    }

    private void usePotion(PotionType type) {
        if (isPositiveInventory(type)) {
        }
    }

    private boolean isPositiveInventory(PotionType type) {
        if ((type == PotionType.HEALTH && healthPot > 0) || 
            (type == PotionType.SPEED && speedPot > 0))   {
            return true;
        }
        return false;
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
                    break;
                case "Right":
                    break;
                case "Forward":
                    break;
                case "Backward":
                    break;
                case "Use":
                    break;
                case "Use_HP":
                    break;
                case "Use_SP":
                    break;
                case "Use_Oil":
                    break;
                case "Use_Tinder":
                    break;
                case "Sprint":
                    break;
                case "Crouch":
                    break;
            }
        }
    }
}
