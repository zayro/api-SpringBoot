package com.rest.api.example;

import java.io.FileInputStream;


import com.itextpdf.kernel.pdf.PdfDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
public class DocToPdfConverter {

    private static String ARCHIVO_DOC = "./src/main/resources/files/Documento.docx";
    private static String FOLDER_PDF = "./src/main/resources/files/ejemplo.pdf";
    public static void main(String[] args) {


        // convertDocToPdf(ARCHIVO_DOC, FOLDER_PDF);
        convertDocToPdfSimple(ARCHIVO_DOC, FOLDER_PDF);

    }


    public static void convertDocToPdf(String docFilePath, String pdfFilePath) {
        try {
            FileInputStream fis = new FileInputStream(docFilePath);
            XWPFDocument document = new XWPFDocument(fis);
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(pdfFilePath));
            Document doc = new Document(pdfDoc);
            for (int i = 0; i < document.getParagraphs().size(); i++) {
                doc.add(new Paragraph(document.getParagraphs().get(i).getText()));
            }
            pdfDoc.close();

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void convertDocToPdfSimple(String docFilePath, String pdfFilePath) {
        try {
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(docFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
            OutputStream outputStream = Files.newOutputStream(new File(pdfFilePath).toPath());
            StringBuilder stringBuilder = new StringBuilder();
            for (String paragraphText : lines) {
                stringBuilder.append(paragraphText + "\n");
            }
            outputStream.write(stringBuilder.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


