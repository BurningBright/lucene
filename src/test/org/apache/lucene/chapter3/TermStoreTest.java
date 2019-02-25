package org.apache.lucene.chapter3;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by ChenGuang.Lin on 2019-02-22.
 */
public class TermStoreTest {

    @Test
    public void testTIS() throws IOException {

        String path = "resource/chapter3/sea-index";
        if (!new File(path).exists())
            new File(path).mkdirs();

        // term info save
        Document doc = new Document();
        Field f = new Field("name", "she sales sea shells by the sea shore", Field.Store.YES, Field.Index.TOKENIZED);
        doc.add(f);
        IndexWriter writer = new IndexWriter(path, new StandardAnalyzer(), true);
        writer.setUseCompoundFile(false);
        writer.addDocument(doc);
        writer.close();
    }

    @Test
    public void testFDX() throws IOException {

        String path = "resource/chapter3/tea-index";
        if (!new File(path).exists())
            new File(path).mkdirs();

        Document doc1 = new Document();
        Field f1 = new Field("name", "she sales sea shells by the sea shore",
                Field.Store.YES, Field.Index.TOKENIZED);
        doc1.add(f1);
        IndexWriter writer = new IndexWriter(path, new StandardAnalyzer(), true);
        writer.setUseCompoundFile(false);
        writer.addDocument(doc1);

        Document doc2 = new Document();
        Field f2 = new Field("game", "tim tells tea tales to the tea thief",
                Field.Store.YES, Field.Index.TOKENIZED);
        doc2.add(f2);
        writer.setUseCompoundFile(false);
        writer.addDocument(doc2);

        writer.close();
    }



}
