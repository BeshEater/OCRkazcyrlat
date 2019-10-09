package com.besheater.researches;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ScannedLookGenerator {

    public static void makeBatFileForScannedPdfsGeneration(File pdfPartsFolder) throws IOException {
        List<File> files = getPdfFiles(pdfPartsFolder.listFiles());
        Collections.sort(files, FileHelper::numericFileNamesCompareAsc);
        FileUtils.forceMkdir(pdfPartsFolder.toPath().resolve("scanned").toFile());
        String command = "convert -density 200 %1$s -blur 0x0.8 -wave 2x256 -linear-stretch 5%%%%x0%%%%" +
                " -rotate 0.5 +noise Gaussian -attenuate 0.5 -format pdf -quality 85" +
                " -compress JPEG -colorspace gray scanned\\%1$s\n";
        StringBuilder batFileText = new StringBuilder();
        for (File file : files) {
            String fileName = file.getName();
            batFileText.append(String.format(command, fileName));
        }
        batFileText.append("PAUSE");
        File batFile = pdfPartsFolder.toPath().resolve("makeScannedPdf.bat").toFile();
        FileHelper.saveFile(batFile, batFileText.toString());
    }

    private static List<File> getPdfFiles(File[] files) {
        return Arrays.stream(files)
                .filter(file -> FileHelper.getFileExtension(file).equalsIgnoreCase("pdf"))
                .collect(Collectors.toList());
    }
}
