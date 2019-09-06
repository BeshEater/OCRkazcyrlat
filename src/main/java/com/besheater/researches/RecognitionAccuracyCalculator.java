package com.besheater.researches;

import org.apache.commons.text.similarity.LevenshteinDetailedDistance;
import org.apache.commons.text.similarity.LevenshteinResults;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

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
}
