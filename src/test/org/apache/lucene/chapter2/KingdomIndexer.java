package org.apache.lucene.chapter2;

import jeasy.analysis.MMAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.util.IndexFileUtil;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by ChenGuang.Lin on 2019-02-20.
 */
public class KingdomIndexer {

    public void createIndex(String inputDir, String outputDir) {
        try {
            IndexWriter writer = new IndexWriter(outputDir, new MMAnalyzer(), true);
            File fileDir = new File(inputDir);
            File[] files = sortFiles(fileDir);

            int i = 0;
            for (File file: files) {
                String fileName = file.getName();
                if (fileName.matches(".*\\.txt")) {
                    Document doc = new Document();

                    i++;
                    String id = i > 99 ? "" + i : (i > 9 ? "0" + i : "00" + i);
                    Field field = new Field("id", id, Field.Store.YES, Field.Index.TOKENIZED);
                    doc.add(field);

                    field = new Field("title", fileName, Field.Store.YES, Field.Index.TOKENIZED);
                    doc.add(field);

                    field = new Field("content", IndexFileUtil.loadFile2Str(file), Field.Store.NO, Field.Index.TOKENIZED);
                    doc.add(field);

                    writer.addDocument(doc);
                }
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private File[] sortFiles(File directory) {
        File[] files = directory.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0)
                    return 1;
                else if (diff == 0)
                    return 0;
                else
                    return -1;
            }

            public boolean equals(Object obj) {
                return true;
            }
        });
        return files;
    }

    @Test
    public void test() {
        String src = "resource/chapter2/3Kingdoms";
        String index = "resource/chapter2/3Kingdoms-index";

        if (!new File(index).exists()) new File(index).mkdirs();
        new KingdomIndexer().createIndex(src, index);
    }

}
