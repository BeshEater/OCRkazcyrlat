package com.besheater.researches;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileHelper {

    public static void saveTextFile(File file, String text) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        out.write(text.getBytes());
        System.out.println("File " + file.getPath() + " created");
        out.close();
    }

    public static String readFile(File file, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded, encoding);
    }

    public static String readFile(File file) throws IOException
    {
        return readFile(file, StandardCharsets.UTF_8);
    }

    public static int numericFileNamesCompareAsc(File f1, File f2) {
        int f1num = Integer.parseInt(getNameWithoutExtension(f1));
        int f2num = Integer.parseInt(getNameWithoutExtension(f2));
        return f1num - f2num;
    }

    public static String getNameWithoutExtension(File file) {
        String name = file.getName();
        return name.substring(0, name.lastIndexOf("."));
    }

    public static void printFilesName(File[] files) {
        for (File file : files) {
            System.out.println(file.getName());
        }
    }
}
