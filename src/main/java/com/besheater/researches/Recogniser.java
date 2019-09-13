package com.besheater.researches;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Recogniser {

    private static final String tesseractFolderName = "tesseract";
    private static final String googleFolderName = "google";

    public static void makeTesseractBashScriptForRecogn(File inputImagesFolder, String lang) throws IOException {
        File recognTextFolder = inputImagesFolder.toPath().resolve(tesseractFolderName).toFile();
        FileUtils.forceMkdir(recognTextFolder);
        String command = "tesseract %1$s %2$s/%3$s -l %4$s --oem 1\n";
        StringBuilder text = new StringBuilder();
        text.append("#!/bin/bash\n");
        List<File> files = getSortedJpgImages(inputImagesFolder);
        for (File file : files) {
            System.out.println("File name = " + file.getName());
            String imageFileName = file.getName();
            String textFileName = FileHelper.getNameWithoutExtension(file);
            text.append(String.format(command, imageFileName, tesseractFolderName, textFileName, lang));
        }
        File scriptFile = inputImagesFolder.toPath().resolve("tesseractRecogn.sh").toFile();
        FileHelper.saveFile(scriptFile, text.toString());
    }

    public static void makeGoogleRecognisedParts(File inputImagesFolder) throws IOException {
        File recognTextFolder = inputImagesFolder.toPath().resolve(googleFolderName).toFile();
        FileUtils.forceMkdir(recognTextFolder);
        List<File> files = getSortedJpgImages(inputImagesFolder);
        for (File file : files) {
            String text = recogniseByGoogle(file);
            String recognFileName = FileHelper.getNameWithoutExtension(file) + ".txt";
            File recognFile = recognTextFolder.toPath().resolve(recognFileName).toFile();
            FileHelper.saveFile(recognFile, text);
        }
    }

    private static String recogniseByGoogle(File imageFile) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(imageFile));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            client.close();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    return null;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                TextAnnotation annotation = res.getFullTextAnnotation();
                return annotation.getText();
            }
        }
        return null;
    }

    private static List<File> getSortedJpgImages(File imagesFolder) {
        String[] extensions = {"jpg"};
        List<File> files = new ArrayList<>(FileUtils.listFiles(imagesFolder, extensions,false));
        files.sort(FileHelper::numericFileNamesCompareAsc);
        return files;
    }
}
