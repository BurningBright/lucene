package org.apache.lucene.chapter2;

import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.util.Stopwatch;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by ChenGuang.Lin on 2019-02-20.
 */
public class KingdomSearcher {

    private static final String INDEX = "resource/chapter2/3Kingdoms-index";

    public void indexSearch(String searchType, String searchKey) {
        try {
            IndexSearcher searcher = new IndexSearcher(INDEX);
            Term term = new Term(searchType, searchKey);

            Stopwatch watch = new Stopwatch();
            TermDocs termDocs = searcher.getIndexReader().termDocs(term);

            while (termDocs.next()) {
                System.out.println("find " + termDocs.freq() + " matches in ");
                System.out.println(searcher.getIndexReader().document(termDocs.doc()).
                        getField("title").stringValue());
            }

            System.out.println("time " + watch.elapsedTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stringSearch(String searchDir, String keyword) {
        File[] files = new File(searchDir).listFiles();
        Stopwatch watch = new Stopwatch();

        for (File file: files) {
            int hit = 0;
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    int from = -keyword.length();
                    while ((from = line.indexOf(keyword, from + keyword.length())) != -1)
                        hit++;
                }
                System.out.println("find " + hit + " matches in " + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("time " + watch.elapsedTime());

    }

    @Test
    public void test() {
        KingdomSearcher searcher = new KingdomSearcher();
        searcher.indexSearch("content", "曹操");
        System.out.println("---------------------------------------------------------");
        searcher.stringSearch("resource/chapter2/3Kingdoms", "曹操");

    }

}
