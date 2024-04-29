// This package declaration states that the Cart class belongs to the com.example.folium_nav.Domain package.
package com.example.folium_nav.Domain;

// Cart class representing a cart item.
public class Cart {
    // Fields for cart item attributes.
    String img_url;       // URL of the item's image.
    String name;          // Name of the item.
    String price;         // Price of the item.
    String currentDate;   // Current date when the item was added to the cart.
    String currentTime;   // Current time when the item was added to the cart.
    String totalPrice;    // Total price of the item.
    String quantity;      // Quantity of the item.
    String p_id;          // Product ID of the item.

    // Default constructor for the Cart class.
    public Cart() {

    }

    // Getter method for the img_url field.
    public String getImg_url() {
        return img_url;
    }

    // Setter method for the img_url field.
    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    // Getter method for the name field.
    public String getName() {
        return name;
    }

    // Setter method for the name field.
    public void setName(String name) {
        this.name = name;
    }

    // Getter method for the price field.
    public String getPrice() {
        return price;
    }

    // Setter method for the price field.
    public void setPrice(String price) {
        this.price = price;
    }

    // Getter method for the currentDate field.
    public String getCurrentDate() {
        return currentDate;
    }

    // Setter method for the currentDate field.
    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    // Getter method for the currentTime field.
    public String getCurrentTime() {
        return currentTime;
    }

    // Setter method for the currentTime field.
    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    // Getter method for the totalPrice field.
    public String getTotalPrice() {
        return totalPrice;
    }

    // Setter method for the totalPrice field.
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Getter method for the quantity field.
    public String getQuantity() {
        return quantity;
    }

    // Setter method for the quantity field.
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    // Getter method for the p_id field.
    public String getP_id() {
        return p_id;
    }

    // Setter method for the p_id field.
    public void setP_id(String p_id) {
        this.p_id = p_id;
    }
}
