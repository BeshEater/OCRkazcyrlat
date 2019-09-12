package com.besheater.researches;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Recogniser {

    private static final String tesseractFolderName = "tesseract";

    public static void makeTesseractBashScriptForRecogn(File inputImagesFolder, String lang) throws IOException {
        File recognTextFolder = inputImagesFolder.toPath().resolve(tesseractFolderName).toFile();
        String command = "tesseract %1$s %2$s/%3$s -l %4$s --oem 1\n";
        StringBuilder text = new StringBuilder();
        text.append("#!/bin/bash\n");
        File[] files = inputImagesFolder.listFiles();
        Arrays.sort(files, FileHelper::numericFileNamesCompareAsc);
        for (File file : files) {
            String imageFileName = file.getName();
            String textFileName = FileHelper.getNameWithoutExtension(file);
            text.append(String.format(command, imageFileName, tesseractFolderName, textFileName, lang));
        }
        File scriptFile = inputImagesFolder.toPath().resolve("tesseractRecogn.sh").toFile();
        FileHelper.saveFile(scriptFile, text.toString());
        FileUtils.forceMkdir(recognTextFolder);
    }
}
