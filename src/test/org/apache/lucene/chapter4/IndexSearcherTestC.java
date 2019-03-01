package org.apache.lucene.chapter4;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.regex.RegexQuery;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by ChenGuang.Lin on 2019-03-01.
 */
public class IndexSearcherTestC {

    public static final String INDEX_PATH = "resource/chapter4/2Cities-index";

    @Test
    public void testSpanSearch() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
    }

    @Test
    public void testRegexSearch() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        RegexQuery query = new RegexQuery(new Term("content", ".*cruncher.*"));
        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

}
