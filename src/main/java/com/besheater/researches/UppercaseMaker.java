package com.besheater.researches;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class UppercaseMaker {

    public static void makeSomeWordsUppercase(File origTextFile, Locale locale, double uppercaseWordChance) throws IOException {
        String origText = FileHelper.readFile(origTextFile);
        String[] words = origText.split(" ");
        StringBuilder outputText = new StringBuilder();
        for (String word : words) {
            double rolled = ThreadLocalRandom.current().nextDouble();
            if (rolled < uppercaseWordChance) {
                word = word.toUpperCase(locale);
            }
            outputText.append(word);
            outputText.append(" ");
        }
        File newFile = origTextFile.toPath().resolveSibling("upperCase.txt").toFile();
        FileHelper.saveFile(newFile, outputText.toString());

    }
}
