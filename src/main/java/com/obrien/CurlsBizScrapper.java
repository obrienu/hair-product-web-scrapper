package com.obrien;
 
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CurlsBizScrapper implements Scrapper {
    final String webUrl = "https://unclefunkysdaughter.com/hair-care.html";
    List<Element> productLinks = new CopyOnWriteArrayList<Element>();
    List<Product> products = new CopyOnWriteArrayList<Product>();

    public void scrap() {
        System.out.println("Scrapping CurlsBiz");
        getProductLinks();
    }

    public void getProductLinks() {
        try {
            Document doc = Jsoup.connect(webUrl).get();
            // Files.write(Paths.get("./files.txt"), doc.toString().getBytes());
            Elements links = doc.select(".product.photo.product-item-photo > a:first-child");
            for (Element link : links) {
                getProduct(link);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getProduct(Element link) throws IOException {
       
        try{
            // convert page to generated HTML and convert to document
            String productLink = link.attr("href");
            Document doc = Jsoup.connect(productLink).get();
            String image = link.child(0).attr("data-src");
            String productName = doc.select("h1.page-title > span").text();
            String price = doc.select("div.product-info-price div.price-box.price-final_price > span.price-container.price-final_price.tax.weee > span.price-wrapper > span.price").text();
            String ingredients = doc.select("div.ingredient > div > table tbody > tr > td").text();
            String description = doc.select("div.product-description").text();
            
            if(ingredients != null && !ingredients.isEmpty() && !price.isBlank() && price != null && !isProductCollections(productName)){
                double priceNum = Double.parseDouble(price.replaceAll("[\\$a-zA-Z ]", ""));
                Scrapper.products.add(new Product(productLink, productName, priceNum, ingredients, description, image, null));
            }     
            
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    
}
