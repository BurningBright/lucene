package org.apache.lucene.chapter5;

import jeasy.analysis.MMAnalyzer;
import org.apache.lucene.chapter2.KingdomIndexer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.util.IndexFileUtil;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by ChenGuang.Lin on 2019-03-25.
 */
public class YinYangIndexer {

    public void createIndex(String inputDir, String outputDir) {
        try {
            IndexWriter writer = new IndexWriter(outputDir, new MMAnalyzer(), true);
            File fileDir = new File(inputDir);
            File[] files = fileDir.listFiles();

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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        String src = "resource/chapter5/Yin Yang Tattooist/";
        String index = "resource/chapter5/Yin Yang Index";

        if (!new File(index).exists()) new File(index).mkdirs();
        new YinYangIndexer().createIndex(src, index);
    }

}
