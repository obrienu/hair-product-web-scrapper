package com.obrien;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Scrapper {
    static final List<Product> products = new CopyOnWriteArrayList<Product>();
    /**
     * The method scrapes the web for products and saves the products 
     * to a products list
     */
    public void scrap();

    default boolean isProductCollections(String name){
        return name.toLowerCase().matches(".*(ki(t|ts)|collectio(n|ns)|pack).*");
    }
}
