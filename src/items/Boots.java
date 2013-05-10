package items;

/**
 *
 * @author MIKUiqnw0
 * @param 
 * @since 29/03/2013
 * @version 0.00.01
 */
public class Boots extends Item {
    private static final float speedModifier = 0.2f;
    private static final float speedTime = 15;
    private static final float cooldown = 60;
    
    public Boots(ItemType type) {
        super(type);
    }
    
    public static float getModifier() {
        return speedModifier;
    }
    public static float getSpeedTime() {
        return speedTime;
    }
    public static float getCooldown() {
        return cooldown;
    }
}
