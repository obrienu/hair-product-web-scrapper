package com.obrien;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AlikayNaturalsScrapper implements Scrapper {

    String webUrl = "https://alikaynaturals.com/collections/hair?page=1&view=all";
    String baseURL = "https://alikaynaturals.com";
    List<Element> productLinks = new CopyOnWriteArrayList<Element>();
    List<Product> products = new CopyOnWriteArrayList<Product>();

    public void scrape() {
        System.out.println("Scrapping AlikayNaturals");
        getProductLinks();
    }

    public void getProductLinks() {
        try {

            while (true) {
                Document doc = Jsoup.connect(webUrl).get();

                Elements elements = doc.select("div.product-top > div.product-image > a");
                for (Element element : elements) {
                    productLinks.add(element);
                }
                Element linkToNextPage = doc.select("div.infinite-scrolling > a").first();
                if (linkToNextPage == null)
                    break;
                webUrl = baseURL + linkToNextPage.attr("href");
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
            productName = doc.select("header.product-title > h1").first().text();
            price = doc.select("div.total-price > span > span.money").text();

            if(doc.select("#tabs > ul > li:nth-child(2)") != null 
                && doc.select("#tabs > ul > li:nth-child(2) > a").text().toLowerCase().equals("ingredients") ){
                ingredients = doc.getElementById("tabs-2").select("p:first-child").text();
            }else if(doc.select("#tabs > ul > li:nth-child(3)") != null 
                && doc.select("#tabs > ul > li:nth-child(3) > a").text().toLowerCase().equals("ingredients") ){
                ingredients = doc.getElementById("tabs-3").select("p:first-child").text();
            }
            description = doc.getElementById("tabs-1").text();
            image = ("https:" + doc.getElementById("product-featured-image").attr("src")).split("\\?")[0];
            if(doc.select("div.swatch-element.available > label") != null){
                size = doc.select("div.swatch-element.available > label").text().split(" ")[0];  
            }

            if(ingredients != null && !ingredients.isEmpty() && !price.isBlank() && price != null && !isProductCollections(productName)){
                double priceNum = Double.parseDouble(price.replaceAll("[\\$a-zA-Z ]", ""));
                Scrapper.products.add(new Product(productLink, productName, priceNum, ingredients, description, image, size));
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
