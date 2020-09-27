package com.obrien;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BriogeohairScrapper implements Scrapper {
    String webUrl = "https://briogeohair.com/collections/all-products";
    String baseURL = "https://briogeohair.com";
    List<Element> productLinks = new CopyOnWriteArrayList<Element>();
    List<Product> products = new CopyOnWriteArrayList<Product>();

    public void scrap() {
        System.out.println("Scrapping Briogeohair");
        getProductLinks();
    }

    public void getProductLinks() {
        try {
            Document doc = Jsoup.connect(webUrl).get();
            Elements elements = doc.select("a.newcol-product");
            for (Element element : elements) {
                productLinks.add(element);
            }
            for (Element link : productLinks) {
                getProduct(link);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getProduct(Element link) throws IOException {
        String productLink = null;
        String ingredients = null;
        String price = null;
        String description = null;
        String image = null;
        String productName = null;
        String size = null;
        try{
            // convert page to generated HTML and convert to document
            productLink = baseURL + link.attr("href");
            Document doc = Jsoup.connect(productLink).get();
            productName = doc.select("h1.pdp-details-title").first().text();
            price = doc.select("h5.pdp-details-price > span").attr("flow-default");
            if(doc.select("div.product-info-content.product-info-rte").size() > 0){
                ingredients = doc.select("div.product-info-content.product-info-rte").get(1).text();
            }
            
            description = doc.select("div.product-info-content.product-info-rte").first().select("div.product-info-content-column:nth-child(2)").text();
            image = ("https:" + link.select("img.newcol-product-img-first").attr("src")).split("\\?")[0];
            
            if(ingredients != null && !ingredients.isEmpty() && !price.isBlank() && price != null && !isProductCollections(productName)){
                double priceNum = Double.parseDouble(price.replaceAll("[\\$a-zA-Z ]", ""));
                Scrapper.products.add(new Product(productLink, productName, priceNum, ingredients, description, image, size));
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        
     
    }
}
