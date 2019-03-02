package org.apache.lucene.chapter4;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.regex.RegexQuery;
import org.apache.lucene.search.spans.SpanFirstQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by ChenGuang.Lin on 2019-03-01.
 */
public class IndexSearcherTestC {

    public static final String INDEX_PATH = "resource/chapter4/2Cities-index";

    @Test
    public void testSpanTermSearch() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        SpanTermQuery query = new SpanTermQuery(new Term("content", "message"));
        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

    @Test
    public void testSpanFirstSearch() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        SpanTermQuery q = new SpanTermQuery(new Term("content", "message"));
        SpanFirstQuery query = new SpanFirstQuery(q, 500);
        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

    @Test
    public void testSpanNearSearch() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        SpanTermQuery a = new SpanTermQuery(new Term("content", "message"));
        SpanTermQuery b = new SpanTermQuery(new Term("content", "mind"));
        SpanNearQuery query = new SpanNearQuery(new SpanQuery[]{a, b}, 100, false);
        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

    @Test
    public void testRegexSearch() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        RegexQuery query = new RegexQuery(new Term("content", "scratch.*"));
        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

}
