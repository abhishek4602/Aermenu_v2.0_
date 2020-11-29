package com.zappfoundry.aermenu_v20;

public class CategoryItem {
    public String getCategoryItemName() {
        return CategoryItemName;
    }

    public void setCategoryItemName(String categoryItemName) {
        CategoryItemName = categoryItemName;
    }

    String CategoryItemName;

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    Integer itemCount;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    String imageURL;

}
