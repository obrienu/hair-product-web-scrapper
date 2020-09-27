package com.obrien;

import java.util.Objects;

public class Product {
    private String productURL;
    private String name;
    private double price;
    private String ingredients;
    private String description;
    private String productImage;
    private String size;



    public Product() {
    }

    public Product(String productURL, String name, double price, String ingredients, String description, String productImage, String size) {
        this.productURL = productURL;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.description = description;
        this.productImage = productImage;
        this.size = size;
    }

    public String getProductURL() {
        return this.productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductImage() {
        return this.productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Product productURL(String productURL) {
        this.productURL = productURL;
        return this;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public Product price(double price) {
        this.price = price;
        return this;
    }

    public Product ingredients(String ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public Product description(String description) {
        this.description = description;
        return this;
    }

    public Product productImage(String productImage) {
        this.productImage = productImage;
        return this;
    }

    public Product size(String size) {
        this.size = size;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Product)) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(productURL, product.productURL) && Objects.equals(name, product.name) && price == product.price && Objects.equals(ingredients, product.ingredients) && Objects.equals(description, product.description) && Objects.equals(productImage, product.productImage) && Objects.equals(size, product.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productURL, name, price, ingredients, description, productImage, size);
    }

    @Override
    public String toString() {
        return " productURL= '" + getProductURL() + "'\n" +
            "name= '" + getName() + "'\n" +
            "price= '" + getPrice() + "'\n" +
            "ingredients= '" + getIngredients() + "'\n" +
            "description= '" + getDescription() + "'\n" +
            "productImage= '" + getProductImage() + "'\n" +
            "size='" + getSize() + "'\n\n"  ;
    }
 

}
