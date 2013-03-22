package items;

import player.PlayerController;

/**
 * Skeleton structure of a potion type and its effects.
 *
 * @author MIKUiqnw0
 * @param type Specifies what type of Potion were dealing with
 * @param player PlayerController reference for checking inventory validity
 * @since 22/03/2013
 * @version 0.00.01
 */
public class Potion {
    
    private enum PotionType {
        HEALTH, SPEED
    }
    private PotionType type;
    private PlayerController player;
    private float healthModifier;
    private float speedModifier;

    
    public Potion(PotionType type, PlayerController player) {
        this.type = type;
        this.player = player;
    }
    
    public void usePotion() {
        if(isPositiveInventory()) {
            
            player.setHealthPotionCount(-1);
        }
    }
    // Potion must not know about the player, try to derefer.
    private boolean isPositiveInventory() {
        if((type == PotionType.HEALTH && player.getHealthPotCount() > 0) ||
           (type == PotionType.SPEED && player.getSpeedPotCount() > 0)) {
            return true;
        } else if(type == null) {
            //throw
        }
        return false;
    }
}
