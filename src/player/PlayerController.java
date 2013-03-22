package player;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

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
    private InputManager inputManager;

    public PlayerController(InputManager inputManager) {
        health = 100;
        runSpeed = 5;
        healthPot = 0;
        speedPot = 0;
        tinder = 0;
        regenRate = 15;
        regenAmount = 1;

        this.inputManager = inputManager;
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

    public void setupKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Use", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("Sprint", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("Crouch", new KeyTrigger(KeyInput.KEY_LCONTROL));

        inputManager.addListener(this, new String[]{
                    "Left", "Right", "Up", "Down", "Use", "Sprint", "Crouch"
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
                case "Up":
                    break;
                case "Down":
                    break;
                case "Use":
                    break;
                case "Sprint":
                    break;
                case "Crouch":
                    break;
            }
        }
    }
}
