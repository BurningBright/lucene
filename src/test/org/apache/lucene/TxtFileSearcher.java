package org.apache.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * This class is used to demonstrate the
 * process of searching on an existing
 * Lucene index
 *
 * Created by ChenGuang.Lin on 2019-01-21.
 */
public class TxtFileSearcher {

    //indexDir is the directory that hosts Lucene's index files
    private static File indexDir = new File("E:\\laboratory\\lucene-2.0.0\\index");
    private static FSDirectory directory;
    private static IndexSearcher searcher;

    @BeforeClass
    public static void index() throws IOException {
        directory = FSDirectory.getDirectory(indexDir,false);
        searcher = new IndexSearcher(directory);
        //This is the directory that hosts the Lucene index
        if(!indexDir.exists()){
            System.out.println("The Lucene index is not exist");
            return;
        }
    }

    @Test
    public void testTermQuery() throws Exception {
        String queryStr = "Lecturer";
        Term term = new Term("contents", queryStr.toLowerCase());
        TermQuery luceneQuery = new TermQuery(term);

        Hits hits = searcher.search(luceneQuery);
        for (int i = 0; i < hits.length(); i++) {
            Document document = hits.doc(i);
            System.out.println("File: " + new String(document.getField("path").binaryValue()));
        }
    }

    @Test
    public void testMultiQuery() throws Exception {
        MultiPhraseQuery mQuery = new MultiPhraseQuery();
        mQuery.add(new Term[]{
                new Term("contents", "felten"),
                new Term("contents", "scott")
        });

        Hits hits = searcher.search(mQuery);
        for (int i = 0; i < hits.length(); i++) {
            Document document = hits.doc(i);
            System.out.println("File: " + new String(document.getField("path").binaryValue()));
        }
    }

    @Test
    public void testBooleanQuery() throws Exception {
        BooleanQuery bQuery = new BooleanQuery();
        bQuery.add(new TermQuery(new Term("contents", "research")), BooleanClause.Occur.MUST);
        bQuery.add(new TermQuery(new Term("contents", "professor")), BooleanClause.Occur.MUST);

        Hits hits = searcher.search(bQuery);
        for (int i = 0; i < hits.length(); i++) {
            Document document = hits.doc(i);
            System.out.println("File: " + new String(document.getField("path").binaryValue()));
        }
    }

}
