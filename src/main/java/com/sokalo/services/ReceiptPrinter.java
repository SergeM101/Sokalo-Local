// in src/main/java/com/sokalo/util/ReceiptPrinter.java

package com.sokalo.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.sokalo.controller.POSController.SaleItemWrapper;
import com.sokalo.model.Sale;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReceiptPrinter {

    public static void printReceipt(Sale sale, List<SaleItemWrapper> items) {
        // Define the path and filename for the PDF
        String filePath = "receipt_" + sale.getSaleID() + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // --- Add content to the PDF ---
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            document.add(new Paragraph("SOKALO Store Receipt").setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(20));
            document.add(new Paragraph("Sale ID: " + sale.getSaleID()));
            document.add(new Paragraph("Date: " + sale.getSaleTime().format(formatter)));
            document.add(new Paragraph("----------------------------------"));

            // Add each item from the sale
            for (SaleItemWrapper item : items) {
                String line = String.format("%-20s x%d %.2f", item.getItemName(), item.getQuantitySold(), item.getSubtotal());
                document.add(new Paragraph(line));
            }

            document.add(new Paragraph("----------------------------------"));
            document.add(new Paragraph("Total: " + String.format("%.2f FCFA", sale.getTotalAmount())).setBold());
            document.add(new Paragraph("Thank you for your purchase!").setTextAlignment(TextAlignment.CENTER));

            document.close();
            System.out.println("Receipt generated successfully at: " + filePath);

        } catch (FileNotFoundException e) {
            System.err.println("Error generating PDF: " + e.getMessage());
        }
    }
}