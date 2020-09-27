package com.obrien;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CurlSmithScrapper implements Scrapper {

    final String webUrl = "https://curls.biz/";
    List<Element> productLinks = new CopyOnWriteArrayList<Element>();
    List<Product> products = new CopyOnWriteArrayList<Product>();
    List<String> collections = new CopyOnWriteArrayList<String>();

    public void scrap() {
        System.out.println("Scrapping CurlSmith");
        getProductLinks();
    }

    public void getProductLinks() {
        try {
            Document doc = Jsoup.connect(webUrl).get();

            Elements links = doc.select("#menu-collections > li.menu-item.menu-item-type-post_type > a");
            for (Element link : links) {
                collections.add(link.attr("href"));
            }

            for (String link : collections) {
                doc = Jsoup.connect(link).get();
                links = doc.select("h3.product-title > a");
                for (Element element : links) {
                    productLinks.add(element);
                    getProduct(element);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getProduct(Element link) throws IOException {
        try{
            // convert page to generated HTML and convert to document
            Document doc = Jsoup.connect(link.attr("href")).get();
            String productLink = link.attr("href");
            String image = doc.select("figure.woocommerce-product-gallery__wrapper > div > a > img").attr("src");
            String productName = doc.select("h1.product_title.entry-title").first().text();
            String price = doc.select("span.woocommerce-Price-amount.amount > bdi").first().text();
            String ingredients = doc.select("div#tab-ingredient_tab").text();
            String description = doc.select("div.post-content.woocommerce-product-details__short-description > p:first-child").text();
            String size = doc.select("div.post-content.woocommerce-product-details__short-description > p > strong").text();

            if(ingredients != null && !ingredients.isEmpty() && !price.isBlank() && price != null && !isProductCollections(productName)){
                double priceNum = Double.parseDouble(price.replaceAll("[\\$a-zA-Z ]", ""));
                Scrapper.products.add(new Product(productLink, productName, priceNum, ingredients, description, image, size));
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}

