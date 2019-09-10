package com.besheater.researches;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

public class PdfFileHelper {

    public static void printPdfFile(File wordFile, File pdfFile) throws IOException {
        convertToPdf(wordFile, pdfFile);
    }

    private static void convertToPdf(File wordFile, File pdfFile) throws IOException {
        XWPFDocument document = new XWPFDocument(new FileInputStream(wordFile));
        PdfOptions options = PdfOptions.create();
        OutputStream out = new FileOutputStream(pdfFile);
        PdfConverter.getInstance().convert(document, out, options);
    }
}
