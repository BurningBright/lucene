package org.apache.lucene.chapter2;

import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChenGuang.Lin on 2019-02-20.
 */
public class FilePreProcess {

    public static void preprocess(File file, String destDir, String tmpFileName) {
        try {
            splitFile(charactorProcess(file, destDir + tmpFileName), destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File charactorProcess(File file, String destFile) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(destFile));
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        // 替换全角字符
        while ((line = reader.readLine()) != null) {
            if (!"\r\n".equals(line)) {
                writer.write(replace(line));
                writer.newLine();
            }
        }
        reader.close();
        writer.close();
        return new File(destFile);
    }

    public static void splitFile(File file, String destDir) throws IOException {
        BufferedWriter writer = null;
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        String title = null;
        // 拆分章节
        while ((line = reader.readLine()) != null) {

            if (line.matches(".*第[\\u4e00-\\u9fa5]+回.*")) {
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


    private static String replace(String line) {
        Map<String, String> map = new HashMap();
        map.put("，", ",");
        map.put("。", ".");
        map.put("．", ".");
        map.put("＜", "<");
        map.put("＞", ">");
        map.put("《", "<");
        map.put("》", ">");
        map.put("？", "?");
        map.put("“", "\"");
        map.put("”", "\"");
        map.put("：", ":");
        map.put("、", ",");
        map.put("（", "(");
        map.put("）", ")");
        map.put("【", "[");
        map.put("】", "]");
        map.put("－", "-");
        map.put("～", "~");
        map.put("！", "!");
        map.put("＇", "'");
        map.put("＂", "\"");

        int length = line.length();
        for (int i=0; i<length; i++) {
            String c = line.substring(i, i+1);
            if (map.get(c) != null)
                line = line.replace(c, map.get(c));
        }
        return line;
    }

    @Test
    public void test() {
        // 三国演义原文件
        String file = "resource/chapter2/Romance of the Three Kingdoms.txt";
        // 三国演义拆分文件
        String destDir = "resource/chapter2/3Kingdoms/";
        // 三国演义临时文件
        String tmpFile = "Romance of the Three Kingdoms.tmp";

        if (!new File(destDir).exists()) new File(destDir).mkdirs();

        preprocess(new File(file), destDir, tmpFile);
    }

}
