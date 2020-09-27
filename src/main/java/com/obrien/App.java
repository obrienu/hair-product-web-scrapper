package com.obrien;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        List<Scrapper> scrappers = new ArrayList<>();
        scrappers.add(new BriogeohairScrapper());
        scrappers.add(new CurlsBizScrapper());
        scrappers.add(new CurlSmithScrapper());
        scrappers.add(new MelaninHairCareScrapper());
        scrappers.add(new MielliOrganicsScrapper());
        scrappers.add(new AlikayNaturalsScrapper());

        for (Scrapper scrapper : scrappers) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    scrapper.scrap();
                };
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(Product product : Scrapper.products){
            Files.write(Paths.get("./files.txt"), product.toString().getBytes(), StandardOpenOption.APPEND);
   
        }



    }

    
}
