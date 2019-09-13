package com.besheater.researches.examples;
// Imports the Google Cloud client library

import com.besheater.researches.App;
import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QuickStartGoogleOCR {
    public static void main(String... args) throws Exception {
        // Instantiates a client
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            // The path to the image file to annotate
            URL inputUrl = App.class.getResource("/wakeupcat.jpg");
            Paths.get(inputUrl.toURI());

            // Reads the image file into memory
            Path path = Paths.get(inputUrl.toURI());
            byte[] data = Files.readAllBytes(path);
            ByteString imgBytes = ByteString.copyFrom(data);

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            requests.add(request);

            // Performs label detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    annotation.getAllFields().forEach((k, v) ->
                            System.out.printf("%s : %s\n", k, v.toString()));
                }
            }
        }
    }

    public static void detectDocumentText(String filePath, PrintStream out) throws Exception,
            IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            client.close();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                TextAnnotation annotation = res.getFullTextAnnotation();
                for (Page page: annotation.getPagesList()) {
                    String pageText = "";
                    for (Block block : page.getBlocksList()) {
                        String blockText = "";
                        for (Paragraph para : block.getParagraphsList()) {
                            String paraText = "";
                            for (Word word: para.getWordsList()) {
                                String wordText = "";
                                for (Symbol symbol: word.getSymbolsList()) {
                                    wordText = wordText + symbol.getText();
                                    out.format("Symbol text: %s (confidence: %f)\n", symbol.getText(),
                                            symbol.getConfidence());
                                }
                                out.format("Word text: %s (confidence: %f)\n\n", wordText, word.getConfidence());
                                paraText = String.format("%s %s", paraText, wordText);
                            }
                            // Output Example using Paragraph:
                            out.println("\nParagraph: \n" + paraText);
                            out.format("Paragraph Confidence: %f\n", para.getConfidence());
                            blockText = blockText + paraText;
                        }
                        pageText = pageText + blockText;
                    }
                }
                out.println("\nComplete annotation:");
                out.println(annotation.getText());
            }
        }
    }
}