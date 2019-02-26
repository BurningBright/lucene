package org.apache.lucene.chapter3;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by ChenGuang.Lin on 2019-02-26.
 */
public class IndexOptTest {

    public static final String PATH = "resource/chapter3/merge-index";

    @Test
    public void testMerge() throws IOException, InterruptedException {
        if (!new File(PATH).exists())
            new File(PATH).mkdirs();

        IndexWriter writer = new IndexWriter(PATH, new StandardAnalyzer(), true);
        writer.setUseCompoundFile(false);

        // batch mode set bigger [file num]
        writer.setMergeFactor(3);
        // batch mode set bigger [doc size]
        writer.setMaxMergeDocs(100);
        // cache memory size [performance]
        writer.setMaxBufferedDocs(10);

        Document doc;
        Field field;
        for (int i=1; i<=100; i++) {
            doc = new Document();
            field = new Field("k", "v" + i,
                    Field.Store.YES, Field.Index.TOKENIZED);
            doc.add(field);
            writer.addDocument(doc);
            Thread.sleep(100);
            System.out.println(i);
        }
        writer.close();
    }

    @Test
    public void testFsRamMerge() throws IOException {

        FSDirectory fsDir = FSDirectory.getDirectory(PATH, false);
        IndexWriter fsWriter = new IndexWriter(fsDir, new StandardAnalyzer(), false);
        fsWriter.setUseCompoundFile(false);

        RAMDirectory ramDir = new RAMDirectory();
        IndexWriter ramWriter = new IndexWriter(ramDir, new StandardAnalyzer(), true);

        Document doc;
        Field field;
        for (int i= 101; i<=200; i++) {
            doc = new Document();
            field = new Field("k", "v" + i,
                    Field.Store.YES, Field.Index.TOKENIZED);
            doc.add(field);
            ramWriter.addDocument(doc);
            System.out.println(i);
        }
        ramWriter.close();

        fsWriter.addIndexes(new Directory[]{ramDir});
        fsWriter.close();
    }

    @Test
    public void testTraversing() throws IOException {
        IndexReader reader = IndexReader.open(PATH);
        for (int i=0; i<reader.numDocs(); i++)
            System.out.println(reader.document(i));

        System.out.println(reader.getVersion());
        System.out.println(reader.numDocs());

        Term term = new Term("k", "v77");
        TermDocs docs = reader.termDocs(term);
        while (docs.next()) {
            System.out.println(docs.doc());
            System.out.println(docs.freq());
            System.out.println();
        }
        reader.close();
    }

    @Test
    public void testDelete() throws IOException {
        IndexReader reader = IndexReader.open(PATH);
        reader.deleteDocument(0);
        Term term = new Term("k", "v77");
        reader.deleteDocuments(term);
        reader.close();

        reader = IndexReader.open(PATH);
        output(reader);

        reader = IndexReader.open(PATH);
        reader.undeleteAll();
        output(reader);
    }

    private void output(IndexReader reader) throws IOException {
        for (int i=0; i<100; i++) {
            try {
                System.out.println(reader.document(i));
            } catch (Exception e) {
                System.out.println(e.getMessage() + " miss " + i);
            }
        }
        reader.close();
    }

}
