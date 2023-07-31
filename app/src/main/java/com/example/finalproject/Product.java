package com.example.finalproject;

public class Product { //product class
    String image;
    String name;
    String brand;
    String details;
    Double price;

    public Product(String name, String brand, String details, Double price, String image){
        this.name = name;
        this.brand = brand;
        this.details = details;
        this.price = price;
        this.image = image;
    }
    public Product() {
        // TODO Auto-generated constructor stub
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDetails(){
        return this.details;
    }

    public void setDetails(String details){
        this.details=details;
    }

    public Double getPrice() { return this.price; }

    public void setPrice(Double price) { this.price = price; }


    public int compare(Product other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
