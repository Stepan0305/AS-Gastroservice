package com.timer.pdf.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.GrooveBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.timer.pdf.Activities.MainActivity;
import com.timer.pdf.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;


public class Generator {
    private static final DeviceRgb WHITE = new DeviceRgb(255, 255, 255);
    private static final DeviceRgb BLACK = new DeviceRgb(0, 0, 0);
    static String currentName = "default";
    public static File generate(DataKeeper keeper) {
        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File folder = new File(path + File.separator + "GeneratedFiles");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            currentName = getFileName();
            File file = null;
            if (success) {
                file = new File(path + "/GeneratedFiles/" + currentName);
            } else {
                file = new File(path , currentName);
            }
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(writer);
            PageSize a4 = PageSize.A4;
            pdfDocument.setDefaultPageSize(a4);
            Document document = new Document(pdfDocument);
            Table header = new Table(new float[]{(float) (a4.getWidth() * 0.7), (float) (a4.getWidth() * 0.3)});
            Cell headerCell = new Cell();
            headerCell.setBorder(new GrooveBorder(WHITE, 0));
            header.addCell(headerCell);
            Image img = new Image(ImageDataFactory.create(path + "/logo.png"));
            img.setHeight(45);
            headerCell = new Cell();
            headerCell.setPadding(25);
            headerCell.setBorder(new GrooveBorder(WHITE, 0));
            headerCell.add(img);
            header.addCell(headerCell);
            document.add(header);
            Table table1 = new Table(new float[]{a4.getWidth() / 2, a4.getWidth() / 2});
            Stream.of("Unsere Daten:", "Kundendaten:")
                    .forEach(el -> {
                        Cell cell = new Cell();
                        cell.setPadding(10);
                        cell.setBorder(new GrooveBorder(WHITE, 0));
                        cell.add(new Paragraph(el));
                        table1.addCell(cell);
                    });
            Stream.of(keeper.getOurData(), keeper.getClientData())
                    .forEach(el -> {
                        Cell cell = new Cell();
                        cell.setPadding(10);
                        cell.setBorder(new GrooveBorder(BLACK, 2));
                        cell.add(new Paragraph(el));
                        table1.addCell(cell);
                    });
            Stream.of("Auftrags-Nr: " + keeper.getOrderNum(), "Datum: " + new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()))
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
            table2Cell.add(new Paragraph("Arbeitsbeschreibung:"));
            table2.addCell(table2Cell);

            table2Cell = new Cell();
            table2Cell.setPadding(10);
            table2Cell.setBorder(new GrooveBorder(BLACK, 2));
            table2Cell.add(new Paragraph(keeper.getWorkDone()));
            table2.addCell(table2Cell);


            document.add(table2);


            Table table3 = new Table(new float[]{a4.getWidth() / 2, a4.getWidth() / 4, a4.getWidth() / 4});
            Stream.of("Arbeitsaufwand / Verarbeitetes Material", "Bezeichnung", "Menge")
                    .forEach(el -> {
                        Cell cell = new Cell();
                        cell.setPadding(10);
                        cell.setBorder(new GrooveBorder(WHITE, 0));
                        cell.add(new Paragraph(el));
                        table3.addCell(cell);
                    });
            for (Part p : keeper.getParts()) {
                Stream.of(p.getName(), p.getNumber(), p.getCount())
                        .forEach(el -> {
                            Cell cell = new Cell();
                            cell.setPadding(10);
                            cell.setBorder(new GrooveBorder(BLACK, 2));
                            cell.add(new Paragraph(el));
                            table3.addCell(cell);
                        });
            }
            document.add(table3);

            Table table4 = new Table(new float[]{a4.getWidth() / 2, a4.getWidth() / 2});
            table4.setMarginTop(10);
            String solution;
            if (DataKeeperKeeper.keeper.isFinished()) {
                solution = "JA";
            } else solution = "NEIN";
            Stream.of("Abgeschlossen: " + solution, "Ort/Datum: " + keeper.getTimePlace())
                    .forEach(el -> {
                        Cell cell = new Cell();
                        cell.setPadding(10);
                        cell.setPaddingBottom(25);
                        cell.setBorder(new GrooveBorder(BLACK, 2));
                        cell.add(new Paragraph(el));
                        table4.addCell(cell);
                    });
            Cell table4Cell = new Cell();
            table4Cell.setPadding(10);
            table4Cell.setBorder(new GrooveBorder(BLACK, 2));
            createFile(DataKeeperKeeper.keeper.getClientSignature(), path + "/client.png");
            ImageData data = ImageDataFactory.create(path + "/client.png");
            Image image = new Image(data);
            image.setMaxHeight(120);
            image.setMaxWidth(120);
            table4Cell.add(new Paragraph("Unterschrift Techniker:"));
            table4Cell.add(image);
            table4.addCell(table4Cell);

            table4Cell = new Cell();
            table4Cell.setPadding(10);
            table4Cell.setBorder(new GrooveBorder(BLACK, 2));
            createFile(DataKeeperKeeper.keeper.getOurSignature(), path + "/our.png");
            data = ImageDataFactory.create(path + "/our.png");
            image = new Image(data);
            image.setMaxHeight(120);
            image.setMaxWidth(120);
            table4Cell.add(new Paragraph("Unterschrift Kunde:"));
            table4Cell.add(image);
            table4.addCell(table4Cell);
            document.add(table4);

            Table table5 = new Table(new float[]{a4.getWidth() / 2, a4.getWidth() / 2});
            table5.setMarginTop(25);

            Cell table5Cell = new Cell();
            table5Cell.setPadding(10);
            table5Cell.setBorder(new GrooveBorder(WHITE, 0));
            table5Cell.add(new Paragraph(""));
            table5.addCell(table5Cell);

            table5Cell = new Cell();
            table5Cell.setBorder(new GrooveBorder(WHITE, 0));
            table5Cell.add(new Paragraph("Name Kunde:"));
            table5.addCell(table5Cell);

            table5Cell = new Cell();
            table5Cell.setPadding(10);
            table5Cell.setBorder(new GrooveBorder(WHITE, 0));
            table5Cell.add(new Paragraph(""));
            table5.addCell(table5Cell);

            table5Cell = new Cell();
            table5Cell.setBorder(new GrooveBorder(BLACK, 2));
            table5Cell.setPadding(10);
            table5Cell.add(new Paragraph(keeper.getClientName()));
            table5.addCell(table5Cell);

            document.add(table5);
            document.close();

            return file;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void createFile(Bitmap bitmap, String path) throws IOException {
        File f = new File(path);
        Log.d(path, bitmap.getAllocationByteCount() + "");
        //Convert bitmap to byte array

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
    }
    private static String getFileName(){
        String res = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss", Locale.getDefault()).format(new Date());

        res += ".pdf";
        return res;
    }
}
