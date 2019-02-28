package org.apache.lucene.chapter4;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by ChenGuang.Lin on 2019-02-27.
 */
public class IndexSearcherTest {

    public static final String INDEX_PATH = "resource/chapter2/3Kingdoms-index";

    @Test
    public void testTermSearch() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        Term term = new Term("title", "曹植");
        Query query = new TermQuery(term);
        Hits hits = searcher.search(query);
        for (int i=0; i<hits.length(); i++)
            System.out.println(hits.doc(i).getField("title"));

        System.out.println("---------------------");

        term = new Term("content", "曹植");
        query = new TermQuery(term);
        hits = searcher.search(query);
        for (int i=0; i<hits.length(); i++)
            System.out.println(hits.doc(i).getField("title"));
    }

    @Test
    public void testBooleanSearchA() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("content", "曹操")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "刘备")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "周瑜")), BooleanClause.Occur.MUST_NOT);
        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++)
            System.out.println(hits.doc(i).getField("title"));
    }

    @Test
    public void testBooleanSearchB() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("content", "袁绍")), BooleanClause.Occur.SHOULD);
        query.add(new TermQuery(new Term("content", "袁术")), BooleanClause.Occur.SHOULD);
        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++)
            System.out.println(hits.doc(i).getField("title"));
    }

    @Test
    public void testBooleanSearchC() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("content", "袁绍")), BooleanClause.Occur.SHOULD);
        query.add(new TermQuery(new Term("content", "袁术")), BooleanClause.Occur.SHOULD);

        BooleanQuery queryB = new BooleanQuery();
        queryB.add(new TermQuery(new Term("content", "董卓")), BooleanClause.Occur.SHOULD);
        queryB.add(new TermQuery(new Term("content", "吕布")), BooleanClause.Occur.SHOULD);
        query.add(queryB, BooleanClause.Occur.MUST_NOT);

        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++)
            System.out.println(hits.doc(i).getField("title"));
    }

    @Test
    public void testRangeQuery() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);

        Term begin = new Term("id", "007");
        Term end = new Term("id", "077");

        RangeQuery query = new RangeQuery(begin, end, true);
        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++)
            System.out.println(hits.doc(i).getField("title"));
    }
}
