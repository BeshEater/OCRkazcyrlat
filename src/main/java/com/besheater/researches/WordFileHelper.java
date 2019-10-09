package com.besheater.researches;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class WordFileHelper {

    private static String outputWordFileName = "output.docx";
    private static double boldTextAmount = 0.15;
    private static double italicTextAmount = 0.15;

    public static void fillWordFileFromTextParts(File outputWordFile, File textPartsFolder) throws IOException {
        File[] textPartFiles = textPartsFolder.listFiles();
        Arrays.sort(textPartFiles, FileHelper::numericFileNamesCompareAsc);
        // FileHelper.printFilesName(textPartFiles);
        if (!outputWordFile.exists()) {
            throw new FileNotFoundException("The output Word document " + outputWordFile.getAbsolutePath() + " does not exist.");
        }
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(outputWordFile))) {

            int pageNum = 1;
            for (File file : textPartFiles) {
                String text = FileHelper.readFile(file);
                writePage(pageNum, text, doc);
                pageNum++;
            }
            try (FileOutputStream out = new FileOutputStream(outputWordFile)) {
                doc.write(out);
            }
        }
    }

    private static void writePage(int pageNum, String text, XWPFDocument doc) {
        String[] words = text.split(" ");
        XWPFParagraph p1 = doc.createParagraph();
        for (String word : words) {
            writeWord(word, p1.createRun(), pageNum);
        }
        XWPFRun newPageSeparator = p1.createRun();
        newPageSeparator.addBreak(BreakType.PAGE);
        System.out.println("Page " + pageNum + " created. Text length = " + text.length());
    }

    private static void writeWord(String word, XWPFRun run, int pageNum) {
        run.setFontFamily(getFontName(pageNum));
        run.setFontSize(12);
        if (isBold()) {
            run.setBold(true);
        }
        if (isItalic()) {
            run.setItalic(true);
        }
        run.setText(word + " ");
    }

    private static String getFontName(int pageNum) {
        if (isEven(pageNum)) {
            return "Arial";
        } else {
            return "Times New Roman";
        }
    }

    private static boolean isBold() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        if (random.nextDouble() < boldTextAmount) {
            return true;
        }
        return false;
    }

    private static boolean isItalic() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        if (random.nextDouble() < italicTextAmount) {
            return true;
        }
        return false;
    }

    private static boolean isEven(int num) {
        return  num % 2 == 0;
    }

    private static File makeBlankOutputFile(File templateFile) throws IOException {
        File outputFile = templateFile.toPath().resolveSibling(outputWordFileName).toFile();
        FileUtils.copyFile(templateFile, outputFile);
        System.out.println("Output file " + outputFile.getAbsolutePath());
        return outputFile;
    }
}
