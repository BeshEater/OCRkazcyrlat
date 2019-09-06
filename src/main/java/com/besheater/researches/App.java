package com.besheater.researches;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class App {

    private static int partSize = 3500; // characters
    private static String origTextFilePath = "/home/beinzone/testocr/file_split/abay_cyr_raw.txt";
    private static String wordFilePath = "/home/beinzone/testocr/test/output.docx";
    private static String textPartsFolderPath = "/home/beinzone/testocr/file_split/output/";
    public static String recognTextPartsFolderPath = "/home/beinzone/testocr/test/recogn_parts/Tesseract/";

    public static void main( String[] args ) throws IOException, URISyntaxException {
        // splitFile();
        // generateWordFile();
        RecognitionAccuracyCalculator.printLevenshteinDistances(
                new File(recognTextPartsFolderPath),
                new File(textPartsFolderPath)
        );
    }

    public static void splitFile() throws IOException, URISyntaxException {
        FileSplitter.divideFileToParts(new File(origTextFilePath), new File(textPartsFolderPath), partSize);
    }

    public static void generateWordFile() throws IOException {
        WordFileHelper.fillWordFileFromTextParts(new File(wordFilePath), new File(textPartsFolderPath));
    }
}
