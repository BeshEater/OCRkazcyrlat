package com.besheater.researches;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;

public class FileHelper {

    public static void saveFile(File file, String text) throws IOException {
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

    public static String getFileExtension(File file) {
        String name = file.getName();
        return name.substring(name.lastIndexOf(".") + 1, name.length());
    }

    public static void cropFilesNames(File folder, String wordToErase) throws IOException {
        File[] files = folder.listFiles();
        for (File file : files) {
            String newName = file.getName().replaceAll(wordToErase, "");
            if (!file.getName().equals(newName)) {
                File destFile = file.toPath().resolveSibling(newName).toFile();
                FileUtils.moveFile(file, destFile);
            }
        }
    }

    public static void removeLeadingZeros(File folder) throws IOException {
        File[] files = folder.listFiles();
        for (File file : files) {
            String newName = file.getName().replaceFirst("^0+(?!$)", "");
            if (!file.getName().equals(newName)) {
                File destFile = file.toPath().resolveSibling(newName).toFile();
                FileUtils.moveFile(file, destFile);
            }
        }
    }

    public static void printFilesNames(File[] files) {
        for (File file : files) {
            System.out.println(file.getName());
        }
    }

    public static void printFilesNames(Collection<File> files) {
        for (File file : files) {
            System.out.println(file.getName());
        }
    }
}
