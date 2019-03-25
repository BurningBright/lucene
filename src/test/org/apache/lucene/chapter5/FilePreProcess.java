package org.apache.lucene.chapter5;

import org.junit.Test;

import java.io.*;

/**
 * Created by ChenGuang.Lin on 2019-02-20.
 */
public class FilePreProcess {

    public static void preprocess(File file, String destDir, String tmpFileName) {
        try {
//            org.apache.lucene.chapter2.FilePreProcess.charactorProcess(file, tmpFileName);

            splitFile(new File(tmpFileName), destDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void splitFile(File file, String destDir) throws IOException, InterruptedException {
        BufferedWriter writer = null;
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        String title = null;
        int i = 1;
        // 拆分章节
        while ((line = reader.readLine()) != null) {

            if (line.matches("第[\\u4e00-\\u9fa5]+章.*")) {

                title = line.trim().replace('?', '？').replace(".", "");
                String id = i> 999? ""+ i :(i > 99 ? "0" + i : (i > 9 ? "00" + i : "000" + i));

                if (writer != null) writer.close();
                writer = new BufferedWriter(new FileWriter(destDir + id+ ' ' + title + ".txt"));

                i++;
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
        // 原文件
        String file = "resource/chapter5/Yin Yang Tattooist.txt";
        // 拆分文件
        String destDir = "resource/chapter5/Yin Yang Tattooist/";
        // 临时文件
        String tmpFile = "resource/chapter5/Yin Yang Tattooist.tmp";

        if (!new File(destDir).exists()) new File(destDir).mkdirs();

        preprocess(new File(file), destDir, tmpFile);
    }

}
