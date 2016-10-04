package info.stuber.fhnw.thesis;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

import info.stuber.fhnw.thesis.collector.SourceLoader;
import info.stuber.fhnw.thesis.collector.WebCrawler;

/**
 * Hello world!
 *
 */
public class App 
{
	SourceLoader ls = null;
	
	public App()
	{
		this.ls = new SourceLoader();
	}
	
	
    public static void main( String[] args ) throws IllegalAccessException
    {
        App app = new App();
    	        
        while(true)
        {
            // create a scanner so we can read the command-line input
            Scanner scanner = new Scanner(System.in);

            System.out.println();
            System.out.println("[1] load source urls");
            System.out.println("[2] Start crawler");
            System.out.println("[9] Quit");
            System.out.println();
            System.out.print("Command: ");

            // get the age as an int
            int age = scanner.nextInt();

            if(age == 1 )
            	app.readSources();
            else if (age == 2)
            	app.startCrawler();
            else if(age == 9)
            {
            	System.exit(0);
            	System.out.println("Good bye");
            	
            }
            else 
            	System.out.println("Invalid input! Repeat. ");
        }

    }
    
    public void readSources()
    {
    	System.out.println("Source URLs loading...");
    	ls.readSourceFile();
    	System.out.println("Total unique webpages to download: " + ls.getSourceCount());
    }
      
    public void startCrawler()  {
    	
        System.out.println("WebCrawler starting...");
        
        if(ls.getSourceCount() == 0) {
        	System.out.println("Load Sources not executed yet! Abort. ");
        	return;
        }
        
        WebCrawler crawler = new WebCrawler(ls.getSources());
		crawler.crawlSites();
		
		System.out.println("WebCrawler finished.");
		
    }
}
