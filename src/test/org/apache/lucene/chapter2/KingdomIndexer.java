package org.apache.lucene.chapter2;

import jeasy.analysis.MMAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.junit.Test;

import java.io.*;

/**
 * Created by ChenGuang.Lin on 2019-02-20.
 */
public class KingdomIndexer {

    public void createIndex(String inputDir, String outputDir) {
        try {
            IndexWriter writer = new IndexWriter(outputDir, new MMAnalyzer(), true);
            File fileDir = new File(inputDir);

            File[] files = fileDir.listFiles();

            for (File file: files) {
                String fileName = file.getName();
                if (fileName.matches(".*\\.txt")) {
                    Document doc = new Document();
                    Field field = new Field("fileName", fileName, Field.Store.YES, Field.Index.TOKENIZED);
                    doc.add(field);

                    field = new Field("content", fileName, Field.Store.NO, Field.Index.TOKENIZED);
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
    public void test() {
        String src = "resource/chapter2/3Kingdoms";
        String index = "resource/chapter2/3Kingdoms-index";

        if (!new File(index).exists()) new File(index).mkdirs();
        new KingdomIndexer().createIndex(src, index);
    }

}
