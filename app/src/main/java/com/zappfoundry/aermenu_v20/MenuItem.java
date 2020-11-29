package com.zappfoundry.aermenu_v20;

import android.graphics.Bitmap;

public class MenuItem<string> implements Comparable<MenuItem> {
    public boolean isExpanded=false;//for expandable layout

    public Boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isVeg() {
        return isVeg;
    }

    public void setVeg(boolean veg) {
        isVeg = veg;
    }

    // public MenuItem (String itemName, String itemPrice, Boolean itemIsAvailable, Boolean isExpanded){
      //  this.isExpanded=false;
  //  }
private boolean isVeg;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Boolean getItemIsAvailable() {
        return itemIsAvailable;
    }

    public void setItemIsAvailable(Boolean itemIsAvailable) {
        this.itemIsAvailable = itemIsAvailable;
    }

    String itemName;
    String itemPrice;

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Bitmap getItemImage() {
        return itemImage;
    }

    public void setItemImage(Bitmap itemImage) {
        this.itemImage = itemImage;
    }

    Bitmap itemImage;

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    String itemID;
    String itemDescription;
    Boolean itemIsAvailable;

    @Override
    public int compareTo(MenuItem menuItem) {
        return this.getItemName().compareTo(menuItem.getItemName());
    }

    public string getImageURL() {
        return imageURL;
    }

    public void setImageURL(string imageURL) {
        this.imageURL = imageURL;
    }

    public string imageURL;

    public string getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(string itemCategory) {
        this.itemCategory = itemCategory;
    }

    public string itemCategory;
}
