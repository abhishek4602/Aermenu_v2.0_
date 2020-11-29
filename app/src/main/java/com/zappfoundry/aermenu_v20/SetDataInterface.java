package com.zappfoundry.aermenu_v20;

public interface SetDataInterface {
    public void addItemToCart(Order order, int flag, int id);
    void removeFromCart(Order order, int id);
    public void DeletefromCart(Order order, int id);
}
