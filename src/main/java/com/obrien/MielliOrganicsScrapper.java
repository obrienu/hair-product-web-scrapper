package com.obrien;

 

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MielliOrganicsScrapper implements Scrapper {
    String webUrl = "https://mielleorganics.com/collections/all";
    String baseURL = "https://mielleorganics.com";
    List<Element> productLinks = new CopyOnWriteArrayList<Element>();
    List<Product> products = new CopyOnWriteArrayList<Product>();

    public void scrap() {
        System.out.println("Scrapping MielliOrganics");
        getProductLinks();
    }

    public void getProductLinks() {
        try {

            while (true) {
                Document doc = Jsoup.connect(webUrl).get();

                Elements elements = doc.select("div.four.columns > div.product-wrap > div.relative.product_image > a");
                for (Element element : elements) {
                    productLinks.add(element);
                }
                Element linkToNextPage = doc.select("div.js-load-more.load-more > a").first();
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

    public void getProduct(Element link) throws Exception {
        String productLink = null;
        String ingredients = null;
        String price = null;
        String description = null;
        String image = null;
        String productName = null;
        String size = null;

        
            // convert page to generated HTML and convert to document
            productLink = baseURL + link.attr("href");
            Document doc = Jsoup.connect(productLink).get();
           
            productName = doc.select("h2.product_name").text();
            price = doc.select("span.current_price > span > span > span.money").text();
            description = getDescription(doc);
            image = ("https:" + doc.select("div.image__container > img").attr("src")).split("\\?")[0];
            ingredients = getIngredient(doc);
            
            if(ingredients != null && !ingredients.isEmpty() && !price.isBlank() && price != null && !isProductCollections(productName)){
                double priceNum = Double.parseDouble(price.replaceAll("[\\$a-zA-Z ]", ""));
                Scrapper.products.add(new Product(productLink, productName, priceNum, ingredients,description, image, size));
           }
        
    }

    public String getDescription(Document doc) throws Exception{
        String  description = "";
        if(doc.select("div.description.bottom > h3").first() != null){
            description = doc.select("div.description.bottom > h3").first().text();
        }

        if( description.isBlank() && doc.select("div.description.bottom > h3").first() != null){
            description = doc.select("div.description.bottom h3").first().nextElementSibling().text();
        }

        if(description.isBlank()){
            Elements links = doc.select("div.description.bottom > ul.tabs > li > a");
            for(Element a: links ){
                if("product".equals(a.text().toLowerCase()) || "product".equals(a.text().toLowerCase())){
                    String id = a.attr("href").replaceAll("#", "");
                    description = doc.getElementById(id).text();
                }
            }        
        }
        return description;
    }


    public String getIngredient(Document doc) throws  Exception  {
        
            Elements elements = doc.select("div.description.bottom > h3");
            for(Element h3: elements ){
                if("ingredients".equals(h3.text().toLowerCase()) || "ingredient".equals(h3.text().toLowerCase())){
                    return h3.nextElementSibling().text();
                }
            }
            Elements links = doc.select("div.description.bottom > ul.tabs > li > a");
            for(Element a: links ){
                if("ingredients".equals(a.text().toLowerCase()) || "ingredient".equals(a.text().toLowerCase())){
                    String id = a.attr("href").replaceAll("#", "");
                    return doc.getElementById(id).text();
                }
            }
            return "";
        
    }
    
}




