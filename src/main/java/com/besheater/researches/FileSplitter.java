package com.besheater.researches;

import java.io.*;
import java.net.URISyntaxException;

public class FileSplitter {

    public static void divideFileToParts(File inputFile, File outputFolder, int partSize) throws IOException, URISyntaxException {
        BufferedReader reader = new BufferedReader
                (new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));

        // Read file one character at time and save to files
        int currentPartFileNum = 1;
        StringBuilder builder = new StringBuilder(partSize + 100);
        int currentCharVal = reader.read();
        while (currentCharVal >= 0) {
            builder.append((char) currentCharVal);
            if(builder.length() == partSize) {
                String newFileName = currentPartFileNum + ".txt";
                File newFile = new File(outputFolder, newFileName);
                FileHelper.saveFile(newFile, builder.toString());
                builder = new StringBuilder(partSize + 100);
                currentPartFileNum++;
            }
            currentCharVal = reader.read();
        }
        reader.close();
    }
}
