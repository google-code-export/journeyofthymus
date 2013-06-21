package items;

/**
 *
 * @author MIKUiqnw0
 * @since 29/03/2013
 * @version 0.00.01
 */
public class Boots extends Item {
    private static final float speedModifier = 0.08f;
    private static final float speedTime = 15;
    private static final float cooldown = 60;
    
    /**
     *
     * @param type
     */
    public Boots(ItemType type) {
        super(type);
    }
    
    /**
     *
     * @return
     */
    public static float getModifier() {
        return speedModifier;
    }
    /**
     *
     * @return
     */
    public static float getSpeedTime() {
        return speedTime;
    }
    /**
     *
     * @return
     */
    public static float getCooldown() {
        return cooldown;
    }
}
