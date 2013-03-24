package items;

import player.PlayerController;

/**
 * Skeleton structure of a potion type and its effects.
 *
 * @author MIKUiqnw0
 * @param type Specifies what type of Potion were dealing with
 * @since 22/03/2013
 * @version 0.00.01
 */
public class Potion {
    
    public enum PotionType {
        HEALTH, SPEED
    }
    private PotionType type;
    private static final float healthModifier = 10;
    private static final float speedModifier = 1;

    
    public Potion(PotionType type) {
        this.type = type;
    }
}
