import java.io.*;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
public class Lucene{
	public void createIndex(String filePath,ArrayList<String> title,ArrayList<String> content){
		File f=new File(filePath);
		IndexWriter iwr=null;
		try {
			Directory dir=FSDirectory.open(f);
			Analyzer analyzer = new IKAnalyzer();
			
			IndexWriterConfig conf=new IndexWriterConfig(Version.LUCENE_4_10_0,analyzer);
			iwr=new IndexWriter(dir,conf);//����IndexWriter
			for (int i=0;i<title.size();i++ )
			{
				if (title.get(i)!=null)
				{
					Document doc=getDocument(title.get(i),content.get(i));
					iwr.addDocument(doc);//���doc��Lucene�ļ�������documentΪ������λ
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			iwr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Document getDocument(String title, String content){
		//doc��������field���ɣ��ڼ��������У�Lucene�ᰴ��ָ����Field��������ÿ��document�ĸ���field�Ƿ����Ҫ��
		Document doc=new Document();
		Field fTitle = new TextField("title",title,Field.Store.YES);
		Field fContent = new TextField("content",content,Field.Store.YES);
		doc.add(fTitle);
		doc.add(fContent);
		
		return doc;
	}
	
	public void searrh(String filePath,String queryStr){
		File f=new File(filePath);
		ArrayList<String> title = new   ArrayList<String>();
		ArrayList<String> content = new   ArrayList<String>();
		
		try {
			IndexSearcher searcher=new IndexSearcher(DirectoryReader.open(FSDirectory.open(f)));
			Analyzer analyzer = new IKAnalyzer();
			//ָ��fieldΪ��name����Lucene�ᰴ�չؼ�������ÿ��doc�е�name��
			QueryParser parser = new QueryParser(Version.LUCENE_4_10_0, "title", analyzer);
			
			Query query=parser.parse(queryStr); 
			TopDocs hits=searcher.search(query,10);//ǰ�漸�д���Ҳ�ǹ̶���·��ʹ��ʱֱ�Ӹ�field�͹ؼ��ʼ���
			for(ScoreDoc doc:hits.scoreDocs){	//����������ɵĽ���������
				Document d=searcher.doc(doc.doc);		
				String tempTitle=d.get("title");
				if (!title.contains(tempTitle))
				{
					title.add(tempTitle);
					content.add(d.get("content"));
				}
			}
			for (int i=0;i<title.size();i++)
			{
				System.out.println(title.get(i));
				System.out.println(content.get(i)+"\n");
			}
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
