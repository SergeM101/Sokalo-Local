// in src/main/java/com/sokalo/services/ReportGenerator.java
package com.sokalo.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;

import java.io.FileNotFoundException;
import java.time.LocalDate;

public class ReportGenerator {

    /**
     * Creates a PDF report from a JavaFX TableView.
     * @param tableView The table containing the data to report.
     * @param reportTitle The title of the report.
     */
    public static <T> void printReport(TableView<T> tableView, String reportTitle) {
        if (tableView.getItems().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Report Error", "Cannot generate an empty report.");
            return;
        }

        String filePath = reportTitle.replace(" ", "_") + "_" + LocalDate.now() + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);

            try (Document document = new Document(pdf)) {
                // Add Title and Date
                document.add(new Paragraph(reportTitle).setBold().setFontSize(18));
                document.add(new Paragraph("Generated on: " + LocalDate.now()));
                document.add(new Paragraph("\n"));

                // Create a PDF table
                int numColumns = tableView.getVisibleLeafColumns().size();
                Table pdfTable = new Table(numColumns);

                // Add table headers
                for (TableColumnBase<T, ?> col : tableView.getVisibleLeafColumns()) {
                    pdfTable.addHeaderCell(col.getText());
                }

                // Add data rows from the TableView
                for (T item : tableView.getItems()) {
                    for (TableColumnBase<T, ?> col : tableView.getVisibleLeafColumns()) {
                        String cellData = (col.getCellData(item) != null) ? col.getCellData(item).toString() : "";
                        pdfTable.addCell(cellData);
                    }
                }

                document.add(pdfTable);
                showAlert(Alert.AlertType.INFORMATION, "Report Generated", "Report saved successfully as:\n" + filePath);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "File Error", "Could not create the report file.");
        }
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}