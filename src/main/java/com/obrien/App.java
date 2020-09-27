package com.obrien;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
       runApp();
    }


    public static void runApp() {
        int MYTHREADS = 10;
        ExecutorService executor = null;
        try{
        executor = Executors.newFixedThreadPool(MYTHREADS);
        List<Scrapper> scrappers = new ArrayList<>();
        scrappers.add(new BriogeohairScrapper());
        scrappers.add(new CurlsBizScrapper());
        scrappers.add(new CurlSmithScrapper());
        scrappers.add(new MelaninHairCareScrapper());
        scrappers.add(new MielliOrganicsScrapper());
        scrappers.add(new AlikayNaturalsScrapper());

        for (Scrapper scrapper : scrappers) {
             executor.execute(new Runnable() {
                @Override
                public void run() {
                    scrapper.scrape();
                };
            });
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
 
		}
		
        
        for(Product product : Scrapper.products){
            Files.write(Paths.get("./files.txt"), product.toString().getBytes(), StandardOpenOption.APPEND);

        }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(executor != null){
                executor.shutdown();
                System.out.println("\nFinished all threads");
            }

        }
    }

    
}
