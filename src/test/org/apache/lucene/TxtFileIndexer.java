package org.apache.lucene;

import junit.framework.TestCase;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;

/**
 * This class demonstrate the process of creating index with Lucene
 * for text files
 *
 * Created by ChenGuang.Lin on 2019-01-21.
 */
public class TxtFileIndexer  extends TestCase {

    public void test() throws Exception{
        //indexDir is the directory that hosts Lucene's index files
        File   indexDir = new File("E:\\laboratory\\lucene-2.0.0\\index");
        //dataDir is the directory that hosts the text files that to be indexed
        File   dataDir  = new File("E:\\laboratory\\lucene-2.0.0\\resource\\data");

        Analyzer luceneAnalyzer = new StandardAnalyzer();
        File[] dataFiles  = dataDir.listFiles();
        IndexWriter indexWriter = new IndexWriter(indexDir,luceneAnalyzer,true);
        long startTime = new Date().getTime();
        for(int i = 0; i < dataFiles.length; i++){
            if(dataFiles[i].isFile() && dataFiles[i].getName().endsWith(".txt")){
                System.out.println("Indexing file " + dataFiles[i].getCanonicalPath());
                Document document = new Document();
                Reader txtReader = new FileReader(dataFiles[i]);
//                document.add(Field.Text("path",dataFiles[i].getCanonicalPath()));
//                document.add(Field.Text("contents",txtReader));
                document.add(new Field("path", dataFiles[i].getCanonicalPath().getBytes(), Field.Store.YES));
                document.add(new Field("contents", txtReader));
                indexWriter.addDocument(document);
            }
        }
        indexWriter.optimize();
        indexWriter.close();
        long endTime = new Date().getTime();

        System.out.println("It takes " + (endTime - startTime)
                + " milliseconds to create index for the files in directory "
                + dataDir.getPath());
    }

}
