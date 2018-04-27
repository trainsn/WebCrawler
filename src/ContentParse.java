import java.net.URL;

import org.xml.sax.InputSource;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;
import de.l3s.boilerpipe.sax.HTMLDocument;
import de.l3s.boilerpipe.sax.HTMLFetcher;

public class ContentParse {
	public TextDocument ParseWeb(String url) throws Exception {  
	    //String url = "http://www.zju.edu.cn/"; 
	    final HTMLDocument htmlDoc = HTMLFetcher.fetch(new URL(url));
        final TextDocument doc = new BoilerpipeSAXInput(htmlDoc.toInputSource()).getTextDocument();
        String title = doc.getTitle();
        String content = ArticleExtractor.INSTANCE.getText(doc);
	   //前面几行代码就是一些固定方式，修改不同的url，获取不同的值。使用时用doc.getTitle(),doc.getContent()即可。
	    //System.out.println("title:" + doc.getTitle());  
	    //System.out.println("content:" + doc.getContent());  
	    return doc;
	}  
}