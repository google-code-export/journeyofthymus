package items;

/**
 *
 * @author MIKUiqnw0
 * @since 
 * @version 0.00.01
 */
public class Key extends Item {

    private int keyID;
    
    /**
     *
     * @param type
     */
    public Key(ItemType type) {
        super(type);
    }
    
    /**
     *
     * @return
     */
    public int getKeyID() {
        return keyID;
    }
}
