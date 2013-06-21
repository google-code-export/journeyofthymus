package items;

/**
 *
 * @author MIKUiqnw0
 * @since 
 * @version 0.00.01
 */
abstract public class Item {
    private static int itemCreateCount;
    private int itemID;
    private ItemType type;
    
    Item (ItemType type) {
        itemID = itemCreateCount;
        ++itemCreateCount;
        this.type = type;
    }
    
    /**
     *
     * @return
     */
    public int getItemID() {
        return itemID;
    }
    /**
     *
     * @return
     */
    public ItemType getItemType() {
        return type;
    }
}
