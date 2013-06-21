package items;

/**
 *
 * @author MIKUiqnw0
 * @since 
 * @version 0.00.01
 */
public class Oil extends Item {
    
    private static final float extensionTime = 300;
    
    /**
     *
     * @param type
     */
    public Oil(ItemType type) {
        super(type);
    }

    /**
     *
     * @return
     */
    public static float getExtensionTime() {
        return extensionTime;
    }
}
