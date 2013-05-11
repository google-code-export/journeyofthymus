package items;

/**
 *
 * @author MIKUiqnw0
 * @param 
 * @since 
 * @version 0.00.01
 */
public class Oil extends Item {
    
    private static final float extensionTime = 300;
    
    public Oil(ItemType type) {
        super(type);
    }

    public static float getExtensionTime() {
        return extensionTime;
    }
}
