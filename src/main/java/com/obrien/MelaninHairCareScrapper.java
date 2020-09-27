package com.obrien;


import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MelaninHairCareScrapper implements Scrapper {
    String webUrl = "https://melaninhaircare.com/collections/frontpage";
    String baseURL = "https://melaninhaircare.com";
    List<Element> productLinks = new CopyOnWriteArrayList<Element>();
    List<Product> products = new CopyOnWriteArrayList<Product>();
    Pattern pattern  = Pattern.compile(".*(shampoo|cream|oil|conditioner).*", Pattern.CASE_INSENSITIVE);


    public void scrape(){
        System.out.println("Scrapping MelaninHairCare");
        getProductLinks();
    }
    public void getProductLinks() {
        try {

            Document doc = Jsoup.connect(webUrl).get();
            Elements elements = doc.select("div.product");
            for (Element element : elements) {
                if(element.select("div.ci > div.so.icn").isEmpty() && filterNonHairProduct(element)){
                    productLinks.add(element.select("div.ci > a").first());
                }
            }
            for (Element link : productLinks) {
               getProduct(link);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean filterNonHairProduct(Element element){
        String productName = element.select(".product-details a > h3").text();
        Matcher matcher = pattern.matcher(productName);
        return matcher.matches();
    }



    public void getProduct(Element link) throws Exception {
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
            productName = doc.getElementById("product-description").child(0).text();
            price = doc.getElementById("product-price").child(0).text();
            description = doc.select("div.rte").first().child(2).text();
            image = ("https:" + doc.getElementById("product-main-image").select("img").attr("src")).split("\\?")[0];
            ingredients = getIngredient(doc);

            if(ingredients != null && !ingredients.isEmpty() && !price.isBlank() && price != null && !isProductCollections(productName)){
                double priceNum = Double.parseDouble(price.replaceAll("[\\$a-zA-Z ]", ""));
                Scrapper.products.add(new Product(productLink, productName, priceNum, ingredients, description, image, size));
            }
            } catch(Exception e){
            e.printStackTrace();
        }

    }

    public String getIngredient(Document doc) throws Exception {
        return doc.select("table > tbody > tr > td").text();
    }
}
