package com.zappfoundry.aermenu_v20;

import java.io.Serializable;

public class Order implements Serializable
{
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    private String itemName;

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int  itemCount) {
        this.itemCount=itemCount;
    }

    private int itemCount;

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    private  int itemPrice;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    private String timeStamp;

    public int getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(int itemTotal) {
        this.itemTotal = itemTotal;
    }

    public int itemTotal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public boolean isFromDB() {
        return isFromDB;
    }

    public void setFromDB(boolean fromDB) {
        isFromDB = fromDB;
    }

    private boolean isFromDB;

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    private int itemID;
    private String tableNumber;
    private String  instructions;
    private boolean isDelivered;
    //orderID,user detail,timestamp,total bill amt,iscomplete
    //itemname,cnt,price,itmtotal,bill total.
    //notif-timestamp,string,tblnmbr

    //updatecount.
}
