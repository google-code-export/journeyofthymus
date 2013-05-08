package items;

/**
 *
 * @author MIKUiqnw0
 * @param 
 * @since 
 * @version 0.00.01
 */
public class Key extends Item {

    private int keyID;
    
    public Key(ItemType type) {
        super(type);
    }
    
    public int getKeyID() {
        return keyID;
    }
}
