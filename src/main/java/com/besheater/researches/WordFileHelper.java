package com.besheater.researches;

import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

public class WordFileHelper {

    public static void fillWordFileFromTextParts(File wordFile, File textPartsFolder) throws IOException {
        File[] textPartFiles = textPartsFolder.listFiles();
        Arrays.sort(textPartFiles, FileHelper::numericFileNamesCompareAsc);
        for (File file : textPartFiles) {
            System.out.println(file.getName());
        }

        if (!wordFile.exists()) {
            throw new FileNotFoundException("The output Word document " + wordFile.getAbsolutePath() + " does not exist.");
        }
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(wordFile))) {

            int pageNum = 1;
            for (File file : textPartFiles) {
                String text = FileHelper.readFile(file);
                System.out.println("Page " + pageNum + " created. Text length = " + text.length());
                pageNum++;
                XWPFParagraph p1 = doc.createParagraph();
                XWPFRun r1 = p1.createRun();
                r1.setFontFamily("Times New Roman");
                r1.setFontSize(12);
                r1.setText(text);
                r1.addBreak(BreakType.PAGE);
            }
            try (FileOutputStream out = new FileOutputStream(wordFile)) {
                doc.write(out);
            }
        }
    }
}
