package org.apache.lucene.chapter5;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.*;
import org.apache.lucene.util.Stopwatch;
import org.junit.Test;

import java.io.IOException;
import java.util.BitSet;

/**
 * Created by ChenGuang.Lin on 2019-03-25.
 */
public class YinYangFilter extends Filter {

    public static final String INDEX_PATH = "resource/chapter5/Yin Yang Index";

    @Override
    public BitSet bits(IndexReader reader) throws IOException {

        final BitSet bits = new BitSet(reader.maxDoc());
        bits.set(0, bits.size() - 1);

        // hide high price chapter
        Term term = new Term("price", "9");
        TermDocs termDocs = reader.termDocs(term);
        while (termDocs.next())
            bits.set(termDocs.doc(), false);

        term = new Term("price", "8");
        termDocs = reader.termDocs(term);
        while (termDocs.next())
            bits.set(termDocs.doc(), false);

        return bits;
    }

    @Test
    public void testYinYangFilter() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("content", "于水")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "陈词")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "沙发")), BooleanClause.Occur.MUST);

        Hits hits = searcher.search(query, new YinYangFilter());
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.print(hits.doc(i).getField("title") + "\t");
            System.out.println(hits.doc(i).getField("price"));
        }

    }

    @Test
    public void testYinYangRangeFilter() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("content", "于水")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "陈词")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "沙发")), BooleanClause.Occur.MUST);

        Hits hits = searcher.search(query,
                new RangeFilter("price", "0", "5", true, false));
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.print(hits.doc(i).getField("title") + "\t");
            System.out.println(hits.doc(i).getField("price"));
        }
    }

    @Test
    public void testYinYangRangeFilterInQuery() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("content", "于水")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "陈词")), BooleanClause.Occur.MUST);

        QueryFilter filter = new QueryFilter(new RangeQuery(
                new Term("price", "0"),
                new Term("price", "2"), true));

        Hits hits = searcher.search(query, filter);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.print(hits.doc(i).getField("title") + "\t");
            System.out.println(hits.doc(i).getField("price"));
        }
    }

    @Test
    public void testYinYangRangeFilterInQueryB() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("content", "于水")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "陈词")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "关系")), BooleanClause.Occur.MUST);

        QueryFilter filter = new QueryFilter(new RangeQuery(
                new Term("id", "1020"),
                new Term("id", "1999"), true));

        Hits hits = searcher.search(query, filter, Sort.INDEXORDER);
        System.out.println(hits.length());
        for (int i=0; i<hits.length(); i++) {
            System.out.print(hits.doc(i).getField("title") + "\t");
            System.out.println(hits.doc(i).getField("price"));
        }
    }

    @Test
    public void testYinYangCacheFilter() throws IOException {
        IndexSearcher searcher = new IndexSearcher(INDEX_PATH);
        BooleanQuery query = new BooleanQuery();
        query.add(new TermQuery(new Term("content", "于水")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "陈词")), BooleanClause.Occur.MUST);
        query.add(new TermQuery(new Term("content", "沙发")), BooleanClause.Occur.MUST);

        // tradition
        Filter filter = new YinYangFilter();
        Stopwatch watch = new Stopwatch();
        for (int j = 0; j < 10; j++) {
            searcher.search(query, filter);
        }
        System.out.println(watch.elapsedTime());

        // caching
        filter = new CachingWrapperFilter(new YinYangFilter());
        watch = new Stopwatch();
        for (int j = 0; j < 10; j++) {
            searcher.search(query, filter);
        }
        System.out.println(watch.elapsedTime());

    }

}
