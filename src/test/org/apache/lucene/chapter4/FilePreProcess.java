package org.apache.lucene.chapter4;

import org.apache.lucene.chapter2.KingdomIndexer;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChenGuang.Lin on 2019-02-20.
 */
public class FilePreProcess {

    public static void preprocess(File file, String destDir) {
        try {
            splitFile(file, destDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void splitFile(File file, String destDir) throws IOException, InterruptedException {
        BufferedWriter writer = null;
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        String title = null;
        // 拆分章节
        while ((line = reader.readLine()) != null) {

            if (line.matches("^Chapter B0.*")) {
                title = line.trim();
                if (writer != null) writer.close();
                writer = new BufferedWriter(new FileWriter(destDir + title + ".txt"));
            }

            if (title != null) {
                writer.write(line);
                writer.write("\r\n");
            }
        }
        if (writer != null) writer.close();
    }

    @Test
    public void test() {
        // 双城记原文件
        String file = "resource/chapter4/A Tale of Two Cities.txt";
        // 双城记拆分文件
        String destDir = "resource/chapter4/2Cities/";

        if (!new File(destDir).exists()) new File(destDir).mkdirs();
        preprocess(new File(file), destDir);
    }

}
