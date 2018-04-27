import java.util.Scanner;

import de.l3s.boilerpipe.document.TextDocument;

import java.util.ArrayList;

public class WebCrawler {

	
  public static void main(String[] args) {
	ArrayList<String> title = new   ArrayList<String>();
	ArrayList<String> content = new   ArrayList<String>();
	  
    java.util.Scanner input = new java.util.Scanner(System.in);
    System.out.print("Enter a URL: ");
    String url = input.nextLine(); 
    String queryStr= input.nextLine();
    
    ArrayList<String> listOfTraversedURLs= crawler(url); // Traverse the Web from the a starting url
    System.out.print("Crawl completed!\n");
    for (int i=0;i<listOfTraversedURLs.size();i++)
    {
    	String tempUrl= listOfTraversedURLs.get(i);
    	ContentParse contentParse= new ContentParse();
    	TextDocument doc=null;
    	try {
    		doc =  contentParse.ParseWeb(tempUrl);
    	} catch (Exception e) {
    		System.out.print("Parse error!");
    	};
    	if (doc!=null)
    	{
    		title.add(doc.getTitle());
        	content.add(doc.getContent()); 	 		
    	}
    }
    
    Lucene w=new Lucene();
	String filePath="d:/index";//创建索引的存储目录
	w.createIndex(filePath,title,content);//创建索引
	w.searrh(filePath,queryStr);//搜索	
  }

  public static ArrayList<String> crawler(String startingURL) {
    ArrayList<String> listOfPendingURLs = new ArrayList<>();
    ArrayList<String> listOfTraversedURLs = new ArrayList<>();
    
    listOfPendingURLs.add(startingURL);
    while (!listOfPendingURLs.isEmpty() && 
        listOfTraversedURLs.size() <= 50) {
      String urlString = listOfPendingURLs.remove(0);
      if (!listOfTraversedURLs.contains(urlString)) {
        listOfTraversedURLs.add(urlString);
        System.out.println("Craw " + urlString);

        for (String s: getSubURLs(urlString)) {
          if (!listOfTraversedURLs.contains(s))
            listOfPendingURLs.add(s);
        }
      }
    }
    return listOfTraversedURLs;
  }
  
  public static ArrayList<String> getSubURLs(String urlString) {
    ArrayList<String> list = new ArrayList<>();
    
    try {
      java.net.URL url = new java.net.URL(urlString); 
      Scanner input = new Scanner(url.openStream());
      int current = 0;
      while (input.hasNext()) {
        String line = input.nextLine();
        current = line.indexOf("http:", current);
        while (current > 0) {
          int endIndex = line.indexOf("\"", current);
          if (endIndex > 0) { // Ensure that a correct URL is found
            list.add(line.substring(current, endIndex)); 
            current = line.indexOf("http:", endIndex);
          }
          else 
            current = -1;
        }
      } 
    }
    catch (Exception ex) {
      System.out.println("Error: " + ex.getMessage());
    }
    
    return list;
  }
}