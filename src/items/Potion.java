package items;

/**
 * Skeleton structure of a potion type and its effects.
 *
 * @author MIKUiqnw0
 * @param type Specifies what type of Potion were dealing with
 * @since 22/03/2013
 * @version 0.00.01
 */
public class Potion extends Item {

    private static final float healthModifier = 20;
    private static final float speedModifier = 0.07f;
    private static final float speedTime = 10;
    
    public Potion(ItemType type) {
        super(type);
    }

    public static float getModifier(ItemType type) {
        switch(type) {
            case HEALTHPOTION:
                return healthModifier;
            case SPEEDPOTION:
                return speedModifier;
            default:
                return 0;
        }
    }
    
    public static float getSpeedTime() {
        return speedTime;
    }
}
