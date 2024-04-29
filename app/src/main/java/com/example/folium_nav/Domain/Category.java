// This package declaration states that the Category class belongs to the com.example.folium_nav.Domain package.
package com.example.folium_nav.Domain;

// Category class representing a category of items.
public class Category {
    // Fields for category attributes.
    String type;    // Type of the category.
    String img_url; // URL of the category's image.

    // Default constructor for the Category class.
    public Category() {
    }

    // Getter method for the type field.
    public String getType() {
        return type;
    }

    // Setter method for the type field.
    public void setType(String type) {
        this.type = type;
    }

    // Getter method for the img_url field.
    public String getImg_url() {
        return img_url;
    }

    // Setter method for the img_url field.
    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
