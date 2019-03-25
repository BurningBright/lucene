package org.apache.lucene.chapter5;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by ChenGuang.Lin on 2019-03-25.
 */
public class YinYangSearcher {

    public static final String INDEX_PATH = "resource/chapter5/Yin Yang Index";

    @Test
    public void testYinYang() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("content", "于水")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "陈词")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "沙发")), BooleanClause.Occur.MUST);
        Hits hits = searcher.search(query);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.print(hits.doc(i).getField("title") + "\t\t");
            System.out.println(hits.score(i));
//            System.out.println(searcher.explain(query, hits.id(i)).toString());
        }
    }

    @Test
    public void testYinYangSort() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("content", "于水")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "陈词")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "茧")), BooleanClause.Occur.MUST);

//        Sort sort = new Sort("price", false);
//        Hits hits = searcher.search(query, sort);
        Hits hits = searcher.search(query, Sort.INDEXORDER);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.print(hits.doc(i).getField("title") + "\t");
            System.out.println(hits.doc(i).getField("id"));
        }

    }

    @Test
    public void testYinYangMultiSort() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("content", "于水")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "陈词")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "沙发")), BooleanClause.Occur.MUST);

        SortField price = new SortField("price", SortField.INT, true);
        SortField id = new SortField("id", SortField.STRING, true);

        Sort sort = new Sort(new SortField[]{price, id});

        Hits hits = searcher.search(query, sort);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.print(hits.doc(i).getField("title") + "\t");
            System.out.print(hits.doc(i).getField("price") + "\t");
            System.out.println(hits.doc(i).getField("id"));
        }

    }

}
