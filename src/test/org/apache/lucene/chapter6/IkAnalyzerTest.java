package org.apache.lucene.chapter6;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.util.IndexFileUtil;
import org.junit.Test;
import org.mira.lucene.analysis.IK_CAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * Created by ChenGuang.Lin on 2019-03-26.
 */
public class IkAnalyzerTest {


    @Test
    public void testIk() throws IOException {
        String inputPath = "resource/chapter5/Yin Yang Tattooist/";
        String outputPath = "resource/chapter6/Yin-Yang-Index-IK";

        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        IndexWriter writer = new IndexWriter(outputFile, new IK_CAnalyzer(), true);
        createIndex(inputFile.listFiles(), writer);
    }

    @Test
    public void testSearch() throws IOException {
        IndexSearcher searcher = new IndexSearcher("resource/chapter6/Yin-Yang-Index-IK");
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

    private void createIndex(File[] files, IndexWriter writer) throws IOException {
        int i = 0;
        for (File file: files) {
            String fileName = file.getName();
            if (fileName.matches(".*\\.txt")) {
                Document doc = new Document();

                i++;
                String id = i> 999? ""+ i :(i > 99 ? "0" + i : (i > 9 ? "00" + i : "000" + i));
                Field field = new Field("id", id, Field.Store.YES, Field.Index.TOKENIZED);
                doc.add(field);

                field = new Field("title", fileName, Field.Store.YES, Field.Index.TOKENIZED);
                doc.add(field);

                field = new Field("content", IndexFileUtil.loadFile2Str(file), Field.Store.NO, Field.Index.TOKENIZED);
                doc.add(field);

                field = new Field("price", "" + (int)(Math.random() * 10), Field.Store.YES, Field.Index.TOKENIZED);
                doc.add(field);

                writer.addDocument(doc);
            }
        }
        writer.close();
    }

}
