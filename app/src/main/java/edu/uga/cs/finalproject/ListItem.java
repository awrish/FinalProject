package edu.uga.cs.finalproject;

/**
 * This class represents a single list item.
 */
public class ListItem {

    //should we have a key variable ???
    private String key;
    private String roommateName;  //set separately when you add an item ??? show in list along with info
    private String productName;
    private int price;

    public ListItem() {
        this.key = null;
        this.roommateName = null;
        this.productName = null;
        this.price = 0;
    }

    public ListItem(String productName){
        this.productName = productName;
        this.price = 0;
        this.key = null;
        this.roommateName = null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRoommateName() {
        return roommateName;
    }

    public void setRoommateName(String roommateName) {
        this.roommateName = roommateName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
