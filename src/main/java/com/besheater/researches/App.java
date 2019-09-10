package com.besheater.researches;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class App {

    private static int partSize = 3400; // characters
    private static File originalTextFile;
    private static File wordTextFile;
    private static File pdfTextFile;
    private static File originalTextPartsFolder;
    private static File printedTextPartsForRecognitionFolder;
    private static File imagesFolder;
    private static File pdfsFolder;
    private static File recognisedTextPartsFolder;
    private static File aggregateTextFilesFolder;

    public static void main( String[] args ) throws IOException, URISyntaxException {
        createOutputFolders("/home/beinzone/testocr/kek/abay_cyr_raw.txt");
        splitOriginalTextToParts();
        generateWordFile();
    }

    public static void createOutputFolders(String origTextFilePath) throws IOException {
        originalTextFile = new File(origTextFilePath);
        // Create folders
        originalTextPartsFolder =
                originalTextFile.toPath().resolveSibling("original_text_parts").toFile();
        printedTextPartsForRecognitionFolder =
                originalTextFile.toPath().resolveSibling("printed_text_parts_for_recogniton").toFile();
        imagesFolder =
                printedTextPartsForRecognitionFolder.toPath().resolve("images").toFile();
        pdfsFolder =
                printedTextPartsForRecognitionFolder.toPath().resolve("pdfs").toFile();
        recognisedTextPartsFolder =
                originalTextFile.toPath().resolveSibling("recognised_text_parts").toFile();
        aggregateTextFilesFolder =
                originalTextFile.toPath().resolveSibling("aggregate_text_files").toFile();
        wordTextFile = createBlankWordFile(aggregateTextFilesFolder);
        pdfTextFile = aggregateTextFilesFolder.toPath().resolve("output.pdf").toFile();
        FileUtils.forceMkdir(originalTextPartsFolder);
        FileUtils.forceMkdir(printedTextPartsForRecognitionFolder);
        FileUtils.forceMkdir(imagesFolder);
        FileUtils.forceMkdir(pdfsFolder);
        FileUtils.forceMkdir(recognisedTextPartsFolder);
        FileUtils.forceMkdir(aggregateTextFilesFolder);
    }

    public static File createBlankWordFile(File aggregateTextFilesFolder) throws IOException {
        URL inputUrl = App.class.getResource("/blank.docx");
        File dest = aggregateTextFilesFolder.toPath().resolve("output.docx").toFile();
        FileUtils.copyURLToFile(inputUrl, dest);
        return dest;
    }

    public static void splitOriginalTextToParts() throws IOException, URISyntaxException {
        System.out.println("Splitting original text file in to parts...");
        FileSplitter.divideFileToParts(originalTextFile, originalTextPartsFolder, partSize);
        System.out.println("Original text file splitting finished");
    }

    public static void generateWordFile() throws IOException {
        System.out.println("Generating word file...");
        WordFileHelper.fillWordFileFromTextParts(wordTextFile, originalTextPartsFolder);
        System.out.println("Word file generation completed");
    }

    public static void generatePdfFile() throws IOException {
        // Not working properly some symbols missing
        /*
        System.out.println("Generating pdf files...");
        PdfFileHelper.printPdfFile(wordTextFile, pdfTextFile);
        System.out.println("PDF files generation completed");
         */
    }
}
