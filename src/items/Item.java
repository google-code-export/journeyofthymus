package items;

/**
 *
 * @author MIKUiqnw0
 * @param 
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
    
    public int getItemID() {
        return itemID;
    }
    public ItemType getItemType() {
        return type;
    }
}
