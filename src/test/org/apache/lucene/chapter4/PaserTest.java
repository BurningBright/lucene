package org.apache.lucene.chapter4;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by ChenGuang.Lin on 2019-03-07.
 */
public class PaserTest {

    public static final String INDEX_PATH = "resource/chapter4/2Cities-index";

    @Test
    public void simpleTest() throws IOException, ParseException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        QueryParser parser = new QueryParser("title", new StandardAnalyzer());
        Query query = parser.parse("Fellow Delicacy");
        System.out.println(query);
        searcher.search(query);

        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

    @Test
    public void changeDefaultTest() throws IOException, ParseException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        QueryParser parser = new QueryParser("content", new StandardAnalyzer());
        parser.setDefaultOperator(QueryParser.OR_OPERATOR);
        Query query = parser.parse("one nine -two");
        System.out.println(query);
        searcher.search(query);

        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

    @Test
    public void parseTest() throws IOException, ParseException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        QueryParser parser = new QueryParser("content", new StandardAnalyzer());
        Query query = parser.parse("\"one two three\"~5");
        System.out.println(query);
        searcher.search(query);

        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

    @Test
    public void fuzzyTest() throws IOException, ParseException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        FuzzyQuery query = new FuzzyQuery(new Term("title", "town"), .2f);
        System.out.println(query);
        searcher.search(query);

        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

    @Test
    public void wildcardTest() throws IOException, ParseException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        WildcardQuery query = new WildcardQuery(new Term("title", "t*"));
        System.out.println(query);
        searcher.search(query);

        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

    @Test
    public void fieldTest() throws IOException, ParseException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        QueryParser parser = new QueryParser("title", new StandardAnalyzer());
        Query query = parser.parse("fellow content:one");
        System.out.println(query);
        searcher.search(query);

        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.println(hits.doc(i).getField("title"));
        }
    }

}
