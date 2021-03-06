package com.besheater.researches;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.similarity.LevenshteinDetailedDistance;
import org.apache.commons.text.similarity.LevenshteinResults;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecognitionAccuracyCalculator {

    public static int getLevenshteinDistance(File recognFile, File origFile) throws IOException {
        LevenshteinDetailedDistance distance = LevenshteinDetailedDistance.getDefaultInstance();
        String recognFileText = FileHelper.readFile(recognFile).trim();
        String origFileText = FileHelper.readFile(origFile).trim();
        LevenshteinResults results = distance.apply(recognFileText, origFileText);
        return results.getDistance();
    }

    public static void printLevenshteinDistances(File recognFolder, File origFolder) throws IOException {
        File[] recognFiles = recognFolder.listFiles();
        File[] origFiles = origFolder.listFiles();
        Arrays.sort(recognFiles, FileHelper::numericFileNamesCompareAsc);
        Arrays.sort(origFiles, FileHelper::numericFileNamesCompareAsc);
        for (int i = 0; i < recognFiles.length; i++) {
            File recognFile = recognFiles[i];
            File origFile = origFiles[i];
            double numOfChar = FileHelper.readFile(origFile).length();
            int dist = getLevenshteinDistance(recognFile, origFile);
            double accuracy = (numOfChar - dist) / numOfChar * 100;
            System.out.println("File # "+ i + " dist = " + dist + ", accuracy = " + accuracy);
        }
    }

    public static void refineRecognisedTextParts(File recognTextPartsFolder) throws IOException {
        File refinedFolder = recognTextPartsFolder.toPath().resolve("refined").toFile();
        FileUtils.forceMkdir(refinedFolder);
        String[] ext = {"txt"};
        List<File> crudeFiles = new ArrayList<>(FileUtils.listFiles(recognTextPartsFolder, ext, false));
        for (File crudeFile : crudeFiles) {
            File refinedFile = refinedFolder.toPath().resolve(crudeFile.getName()).toFile();
            String crudeText = FileHelper.readFile(crudeFile);
            String refinedText = refineRecognisedText(crudeText);
            FileHelper.saveFile(refinedFile, refinedText);
        }
    }

    private static String refineRecognisedText(String crudeText) {
        String refinedText = crudeText;
        // replace all CRLF
        refinedText = refinedText.replaceAll("\r\n", " ");
        // replace all CR
        refinedText = refinedText.replaceAll("\r", " ");
        // replace all LF
        refinedText = refinedText.replaceAll("\n", " ");
        // replace all tabs
        refinedText = refinedText.replaceAll("\t", " ");
        // replace all FF
        refinedText = refinedText.replaceAll("\f", "");
        // replace all double spaces
        refinedText = refinedText.replaceAll("  ", " ");
        // replace all double spaces
        refinedText = refinedText.replaceAll("  ", " ");
        // trim space
        refinedText = refinedText.trim();

        return refinedText;
    }

    public static void makeAccuracyReportScript(File reportsFolder, int textPartsNum) throws IOException {
        String[] engines = {"abby", "google", "tesseract"};
        String template = "accuracy %1$s %2$s %3$s\n";
        StringBuilder script = new StringBuilder();
        script.append("#!/bin/bash\n");
        for (String engine: engines) {
            for (int i = 1; i <= textPartsNum; i++) {
                String fileName = i + ".txt";
                String correctFile = "../original_text_parts/" + fileName;
                String recognFile = "../recognised_text_parts/" + engine + "/refined/" + fileName;
                String reportFile = engine + "/" + fileName;
                script.append(String.format(template, correctFile, recognFile, reportFile));
            }
            script.append(getAccSummCommand(engine, textPartsNum));
        }
        File scriptFile = reportsFolder.toPath().resolve("makeReports.sh").toFile();
        FileHelper.saveFile(scriptFile, script.toString());
    }

    private static String getAccSummCommand(String engine, int textPartsNum) {
        StringBuilder summReport = new StringBuilder();
        summReport.append("accsum ");
        for (int i = 1; i <= textPartsNum; i++) {
            summReport.append(engine + "/" +i + ".txt ");
        }
        summReport.append("> " + engine +"/summary.txt\n");
        return summReport.toString();
    }
}
