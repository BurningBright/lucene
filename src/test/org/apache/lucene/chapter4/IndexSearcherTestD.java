package org.apache.lucene.chapter4;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.*;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by ChenGuang.Lin on 2019-03-23.
 */
public class IndexSearcherTestD {

    private final String INDEX_PATH_KINGDOM = "resource/chapter2/3Kingdoms-index";

    private final String INDEX_PATH_CITIES = "resource/chapter4/2Cities-index";

    @Test
    public void testMultiSearch() throws IOException, ParseException {

        String[] queries = {"[005 TO 007]", "fellow"};
        String[] fileds = {"id", "title"};

        BooleanClause.Occur [] clauses = {BooleanClause.Occur.MUST, BooleanClause.Occur.MUST};
        Query query  = MultiFieldQueryParser.parse(queries, fileds, clauses, new StandardAnalyzer());

        IndexSearcher kSearcher = new IndexSearcher(INDEX_PATH_KINGDOM);
        IndexSearcher cSearcher = new IndexSearcher(INDEX_PATH_CITIES);
        IndexSearcher[] searchers = {kSearcher, cSearcher};

        MultiSearcher searcher = new MultiSearcher(searchers);

        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }
}
