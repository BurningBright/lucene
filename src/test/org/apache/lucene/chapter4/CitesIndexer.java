package org.apache.lucene.chapter4;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.chapter2.KingdomIndexer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by ChenGuang.Lin on 2019-03-01.
 */
public class CitesIndexer {

    public void createIndex(String inputDir, String outputDir) {
        try {
            IndexWriter writer = new IndexWriter(outputDir, new StandardAnalyzer(), true);
            File fileDir = new File(inputDir);

            for (File file: fileDir.listFiles()) {
                String fileName = file.getName();
                if (fileName.matches(".*\\.txt")) {
                    Document doc = new Document();

                    Field field = new Field("title", fileName, Field.Store.YES, Field.Index.TOKENIZED);
                    doc.add(field);

                    field = new Field("content", loadFile2Str(file), Field.Store.NO, Field.Index.TOKENIZED);
                    doc.add(field);

                    writer.addDocument(doc);
                }
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadFile2Str(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuffer buffer = new StringBuffer();

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testIndex() {
        String src = "resource/chapter4/2Cities";
        String index = "resource/chapter4/2Cities-index";

        if (!new File(index).exists()) new File(index).mkdirs();
        new KingdomIndexer().createIndex(src, index);
    }

}
