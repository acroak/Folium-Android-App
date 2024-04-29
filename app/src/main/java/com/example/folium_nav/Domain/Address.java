// This package declaration states that the Address class belongs to the com.example.folium_nav.Domain package.
package com.example.folium_nav.Domain;

// Serializable is a marker interface used to indicate that a class can be serialized.
import java.io.Serializable;

// Address class representing an address object.
public class Address implements Serializable {
    // Private fields for the address attributes.
    private String completeAddress;
    private String street;
    private String city;
    private String name;
    private String phone;
    private String postcode;

    // Boolean field to indicate if the address is selected.
    private boolean isSelected;

    // Default constructor for the Address class.
    public Address() {
    }

    // Getter method for the complete address.
    public String getCompleteAddress() {
        return completeAddress;
    }

    // Setter method for the complete address.
    public void setCompleteAddress(String completeAddress) {
        this.completeAddress = completeAddress;
    }

    // Getter method for the street.
    public String getStreet() {
        return street;
    }

    // Setter method for the street.
    public void setStreet(String street) {
        this.street = street;
    }

    // Getter method for the city.
    public String getCity() {
        return city;
    }

    // Setter method for the city.
    public void setCity(String city) {
        this.city = city;
    }

    // Getter method for the name.
    public String getName() {
        return name;
    }

    // Setter method for the name.
    public void setName(String name) {
        this.name = name;
    }

    // Getter method for the phone number.
    public String getPhone() {
        return phone;
    }

    // Setter method for the phone number.
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter method for the postcode.
    public String getPostcode() {
        return postcode;
    }

    // Setter method for the postcode.
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    // Getter method for the isSelected field.
    public boolean isSelected() {
        return isSelected;
    }

    // Setter method for the isSelected field.
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
