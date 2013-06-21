package items;

/**
 * Skeleton structure of a potion type and its effects.
 *
 * @author MIKUiqnw0
 * @since 22/03/2013
 * @version 0.00.01
 */
public class Potion extends Item {

    private static final float healthModifier = 20;
    private static final float speedModifier = 0.07f;
    private static final float speedTime = 10;
    
    /**
     *
     * @param type
     */
    public Potion(ItemType type) {
        super(type);
    }

    /**
     *
     * @param type
     * @return
     */
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
    
    /**
     *
     * @return
     */
    public static float getSpeedTime() {
        return speedTime;
    }
}
