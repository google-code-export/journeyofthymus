/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

/** Description
 *  
 * @author MIKUiqnw0 (Patricio)
 * @param inputManager Requires the inputManager object to implement controls
 * @since 8/03/2013
 * @version 0.00.01
 */
public class PlayerController extends CharacterControl implements ActionListener {
    private int     health;
    private float   runspeed;
    private int     healthPot,
                    speedPot,
                    tinder;
    private float   regenRate;
    private int     regenAmount;
    private InputManager inputManager;
            
    public PlayerController(InputManager inputManager) {
        health = 100;
        runspeed = 5;
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
    public void setHealth(int newHealth) {
        health = newHealth;
        // Add validation
    }
    
    public void setupKeys(InputManager inputManager) {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Use", new KeyTrigger(KeyInput.KEY_E));
        inputManager.addMapping("Activate Sprint", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("", triggers)
    }
    
    public void onAction(String name, boolean isPressed, float tpf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
