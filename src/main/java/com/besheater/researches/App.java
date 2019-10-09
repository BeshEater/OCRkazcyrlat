package com.besheater.researches;

import com.besheater.researches.examples.QuickStartGoogleOCR;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

public class App {

    private static int partSize = 3360; // characters
    private static File originalTextFile;
    private static File wordTextFile;
    private static File pdfTextFile;
    private static File originalTextPartsFolder;
    private static File printedTextPartsForRecognitionFolder;
    private static File imagesFolder;
    private static File pdfsFolder;
    private static File recognisedTextPartsFolder;
    private static File aggregateTextFilesFolder;

    public static void main( String[] args ) throws Exception {
        UppercaseMaker.makeSomeWordsUppercase(new File("/home/beinzone/Projects/kazakh_texts_cyr_lat_OCR_test_data/cyrillic/Civil_Code_RK_special_part/cyrillic_alphabet.txt"),
                Locale.ROOT, 1.0);
        // initialStep();
        // cropPdfPartsFilesNames(new File("C:\\Users\\Mustang\\Desktop\\temo\\latin\\The_Abay_way\\printed_text_parts_for_recogniton\\pdfs"));
        // ScannedLookGenerator.makeBatFileForScannedPdfsGeneration(new File("C:\\Users\\Mustang\\Desktop\\temo\\latin\\The_Abay_way\\printed_text_parts_for_recogniton\\pdfs"));
        // cropImagePartsFilesNames(new File("C:\\Users\\Mustang\\Desktop\\temo\\latin\\The_Abay_way\\printed_text_parts_for_recogniton\\images"));
        Recogniser.makeTesseractBashScriptForRecogn(new File("/home/beinzone/Projects/OCR_cyr_lat_research/cyrillic/Civil_Code_RK_special_part/printed_text_parts_for_recogniton/images/"), "kaz");
        // Recogniser.makeGoogleRecognisedParts(new File("/home/beinzone/Desktop/test/"));
        // RecognitionAccuracyCalculator.refineRecognisedTextParts(new File("C:\\Users\\Mustang\\Desktop\\OCR_cyr_lat_research\\latin\\The_Abay_way\\recognised_text_parts\\tesseract"));
        // RecognitionAccuracyCalculator.makeAccuracyReportScript(new File("/home/beinzone/Projects/OCR_cyr_lat_research/cyrillic/Civil_Code_RK_special_part/reports/"), 100);
    }

    public static void initialStep() throws IOException, URISyntaxException {
        String origTextFilePath = getUserInput("Enter original text file path:");
        createOutputFolders(origTextFilePath);
        splitOriginalTextToParts();
        generateWordFile();
    }

    public static void cropPdfPartsFilesNames(File pdfsFolder) throws IOException {
        FileHelper.cropFilesNames(pdfsFolder, "output ");
    }

    public static void cropImagePartsFilesNames(File imagesFolder) throws IOException {
        FileHelper.cropFilesNames(imagesFolder, "Binder1_Страница_");
        FileHelper.removeLeadingZeros(imagesFolder);
    }

    public static String getUserInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        return scanner.next();
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
}
