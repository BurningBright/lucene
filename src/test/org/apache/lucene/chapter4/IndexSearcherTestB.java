package org.apache.lucene.chapter4;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by ChenGuang.Lin on 2019-03-01.
 */
public class IndexSearcherTestB {

    public static final String INDEX_PATH = "resource/chapter2/3Kingdoms-index";

    @Test
    public void testPrefixSearch() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        PrefixQuery query = new PrefixQuery(new Term("title", "诸葛"));
        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

    @Test
    public void testPhaseSearch() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        PhraseQuery query = new PhraseQuery();
        query.add(new Term("title", "三"));
        query.add(new Term("title", "擒"));

        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

    @Test
    public void testMultiPhaseSearch() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        MultiPhraseQuery query = new MultiPhraseQuery();
        query.add(new Term("title", "三"));
        Term a = new Term("title", "将");
        Term b = new Term("title", "事");
        Term c = new Term("title", "气");
        query.add(new Term[]{a, b, c});

        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

    @Test
    public void testFuzzySearch() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        FuzzyQuery query = new FuzzyQuery(new Term("title", "大战"), .1f);
        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

    @Test
    public void testWildCardSearch() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        WildcardQuery query = new WildcardQuery(new Term("title", "?江*"));
        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

}
