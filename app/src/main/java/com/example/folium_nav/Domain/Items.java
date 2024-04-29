// This package declaration states that the Items class belongs to the com.example.folium_nav.Domain package.
package com.example.folium_nav.Domain;

// Serializable is a marker interface used to indicate that a class can be serialized.
import java.io.Serializable;

// Items class representing an item.
public class Items implements Serializable {
    // Fields for item attributes.
    String description;     // Description of the item.
    String img_url;         // URL of the item's image.
    String name;            // Name of the item.
    String type;            // Type of the item.
    double rating;          // Rating of the item.
    double price;           // Price of the item.
    String p_id;            // Product ID of the item.
    String drought_tol;     // Drought tolerance of the item.
    String fert_Str;        // Fertilizer strength of the item.
    String fert_type;       // Type of fertilizer for the item.
    String harmful_pets;    // Harmful to pets indicator for the item.
    String harmful_ppl;     // Harmful to people indicator for the item.
    String humidity;        // Humidity requirements of the item.
    String light_level;     // Light level requirements of the item.
    String sci_name;        // Scientific name of the item.
    String soil_drain;      // Soil drainage requirements of the item.
    String soil_pH;         // Soil pH requirements of the item.
    String water_freq;      // Watering frequency of the item.
    String lux_range;       //Lux range for associated brightness label

    // Default constructor for the Items class.
    public Items() {
    }

    // Getter method for the description field.
    public String getDescription() {
        return description;
    }

    // Setter method for the description field.
    public void setDescription(String description) {
        this.description = description;
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

    // Getter method for the type field.
    public String getType() {
        return type;
    }

    // Setter method for the type field.
    public void setType(String type) {
        this.type = type;
    }

    // Getter method for the rating field.
    public double getRating() {
        return rating;
    }

    // Setter method for the rating field.
    public void setRating(double rating) {
        this.rating = rating;
    }

    // Getter method for the price field.
    public double getPrice() {
        return price;
    }

    // Setter method for the price field.
    public void setPrice(double price) {
        this.price = price;
    }

    // Getter method for the p_id field.
    public String getP_id() {
        return p_id;
    }

    // Setter method for the p_id field.
    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    // Getter method for the drought_tol field.
    public String getDrought_tol() {
        return drought_tol;
    }

    // Setter method for the drought_tol field.
    public void setDrought_tol(String drought_tol) {
        this.drought_tol = drought_tol;
    }

    // Getter method for the fert_Str field.
    public String getFert_Str() {
        return fert_Str;
    }

    // Setter method for the fert_Str field.
    public void setFert_Str(String fert_Str) {
        this.fert_Str = fert_Str;
    }

    // Getter method for the fert_type field.
    public String getFert_type() {
        return fert_type;
    }

    // Setter method for the fert_type field.
    public void setFert_type(String fert_type) {
        this.fert_type = fert_type;
    }

    // Getter method for the harmful_pets field.
    public String getHarmful_pets() {
        return harmful_pets;
    }

    // Setter method for the harmful_pets field.
    public void setHarmful_pets(String harmful_pets) {
        this.harmful_pets = harmful_pets;
    }

    // Getter method for the harmful_ppl field.
    public String getHarmful_ppl() {
        return harmful_ppl;
    }

    // Setter method for the harmful_ppl field.
    public void setHarmful_ppl(String harmful_ppl) {
        this.harmful_ppl = harmful_ppl;
    }

    // Getter method for the humidity field.
    public String getHumidity() {
        return humidity;
    }

    // Setter method for the humidity field.
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    // Getter method for the light_level field.
    public String getLight_level() {
        return light_level;
    }

    // Setter method for the light_level field.
    public void setLight_level(String light_level) {
        this.light_level = light_level;
    }

    // Getter method for the sci_name field.
    public String getSci_name() {
        return sci_name;
    }

    // Setter method for the sci_name field.
    public void setSci_name(String sci_name) {
        this.sci_name = sci_name;
    }

    // Getter method for the soil_drain field.
    public String getSoil_drain() {
        return soil_drain;
    }

    // Setter method for the soil_drain field.
    public void setSoil_drain(String soil_drain) {
        this.soil_drain = soil_drain;
    }

    // Getter method for the soil_pH field.
    public String getSoil_pH() {
        return soil_pH;
    }

    // Setter method for the soil_pH field.
    public void setSoil_pH(String soil_pH) {
        this.soil_pH = soil_pH;
    }

    // Getter method for the water_freq field.
    public String getWater_freq() {
        return water_freq;
    }

    // Setter method for the water_freq field.
    public void setWater_freq(String water_freq) {
        this.water_freq = water_freq;
    }

    // Getter method for the lux_range field.
    public String getLux_range() {
        return lux_range;
    }

    // Setter method for the lux_range field.
    public void setLux_range(String lux_range) {
        this.lux_range = lux_range;
    }
}
