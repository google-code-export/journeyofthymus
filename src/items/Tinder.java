package items;

/**
 *
 * @author MIKUiqnw0
 * @param 
 * @since 
 * @version 0.00.01
 */
public class Tinder extends Item {
    
    private static final float extensionTime = 300;
    
    public Tinder(ItemType type) {
        super(type);
    }

    public static float getExtensionTime() {
        return extensionTime;
    }
}
