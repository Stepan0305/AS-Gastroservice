package com.timer.pdf.Models;

import android.os.Environment;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.GrooveBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.stream.Stream;

public class Generator {
    private static final DeviceRgb WHITE = new DeviceRgb(255, 255, 255);
    private static final DeviceRgb BLUE = new DeviceRgb(0, 0, 255);
    public static File generate(DataKeeper keeper, String time) {
        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File file = new File(path, "letter.pdf");
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(writer);
            PageSize a4 = PageSize.A4;
            pdfDocument.setDefaultPageSize(a4);
            Document document = new Document(pdfDocument);
            Table table1 = new Table(new float[]{a4.getWidth()/2, a4.getWidth()/2});
            Stream.of("Unsere Daten:", "Kundendaten:")
                    .forEach(el -> {
                        Cell cell = new Cell();
                        cell.setPadding(10);
                        cell.setBorder(new GrooveBorder(WHITE, 0));
                        cell.add(new Paragraph(el));
                        table1.addCell(cell);
                    });
            Stream.of("*Company data*", keeper.getClientData())
                    .forEach(el -> {
                        Cell cell = new Cell();
                        cell.setPadding(10);
                        cell.setBorder(new GrooveBorder(BLUE, 2));
                        cell.add(new Paragraph(el));
                        table1.addCell(cell);
                    });
            Stream.of("Auftrags-Nr:", "Datum:")
                    .forEach(el -> {
                        Cell cell = new Cell();
                        cell.setPadding(10);
                        cell.setBorder(new GrooveBorder(WHITE, 0));
                        cell.add(new Paragraph(el));
                        table1.addCell(cell);
                    });
            document.add(table1);

            Table table2 = new Table(new float[]{a4.getWidth()});
            Cell table2Cell = new Cell();
            table2Cell.setPadding(10);
            table2Cell.setBorder(new GrooveBorder(WHITE, 0));
            table2Cell.add(new Paragraph("Arbeitsbeschreibung, Materialverbrauch:"));
            table2.addCell(table2Cell);

            table2Cell = new Cell();
            table2Cell.setPadding(10);
            table2Cell.setBorder(new GrooveBorder(BLUE, 2));
            table2Cell.add(new Paragraph(keeper.getWorkDone()));
            table2.addCell(table2Cell);

            table2Cell = new Cell();
            table2Cell.setPadding(10);
            table2Cell.setBorder(new GrooveBorder(WHITE, 0));
            table2Cell.add(new Paragraph("Geräteeinsatz"));
            table2.addCell(table2Cell);

            table2Cell = new Cell();
            table2Cell.setPadding(10);
            table2Cell.setBorder(new GrooveBorder(WHITE, 0));
            table2Cell.add(new Paragraph("Arbeitszeit: " + time));
            table2.addCell(table2Cell);

            document.add(table2);


            Table table3 = new Table(new float[]{a4.getWidth()/2, a4.getWidth()/4, a4.getWidth()/4});
            Stream.of("Name, Berufsbezeichnung", "Arbeits-std", "Fahrzeit-Std")
                    .forEach(el -> {
                        Cell cell = new Cell();
                        cell.setPadding(10);
                        cell.setBorder(new GrooveBorder(WHITE, 0));
                        cell.add(new Paragraph(el));
                        table3.addCell(cell);
                    });
            for (Part p: keeper.getParts()){
                Stream.of(p.getName(), p.getNumber(), p.getCount())
                        .forEach(el -> {
                            Cell cell = new Cell();
                            cell.setPadding(10);
                            cell.setBorder(new GrooveBorder(BLUE, 2));
                            cell.add(new Paragraph(el));
                            table3.addCell(cell);
                        });
            }
            document.add(table3);

            Table table4 = new Table(new float[]{a4.getWidth()/2, a4.getWidth()/2});
            table4.setMarginTop(10);
            Stream.of("Abgeschlossen: JA", "Ort/Datum", "Aufgestellt: Unterschrift", "Anerkannt: Unterschrift")
                    .forEach(el -> {
                        Cell cell = new Cell();
                        cell.setPadding(10);
                        cell.setPaddingBottom(25);
                        cell.setBorder(new GrooveBorder(BLUE, 2));
                        cell.add(new Paragraph(el));
                        table4.addCell(cell);
                    });
            document.add(table4);
            document.close();
            return file;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
