/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contactsdata;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.collections.ObservableList;

/**
 *
 * @author Felhasználó
 */
public class PDFGeneration {
    
    public void generatePDF(String fileName, ObservableList<Contact> data){
        Document document = new Document();
        try {
            
            //1. kell egy pdfWriter, aminek adunk egy dokum-t és egy outputStream-et(kimenő file neve)
            PdfWriter.getInstance(document, new FileOutputStream(fileName + ".pdf"));
            document.open();
            
            //első elem pl. céges logo
            //az itextpdf-ből importált Image osztály-ba!!! beletesszük a kép file-unkat
            Image image1 = Image.getInstance(getClass().getResource("/lake_and_house.jpg"));
            //átméretezés
            image1.scaleToFit(200, 150);
            //beállítjuk a helyét
            image1.setAbsolutePosition(150f, 750f);
            //hozzáadjuk a dok-hoz
            document.add(image1);
            
            //hozzáadhatunk egy bekezdést: benne üres sorok + esetleg egy szöveg, betűtípus
            document.add(new Paragraph("\n\n\n\n", FontFactory.getFont("betutipus", BaseFont.IDENTITY_H, BaseFont.EMBEDDED)));
            
            //Táblázat
            
            float[] columnWidths = {3, 3, 5, 3}; //relatív oszlopszélességek megadása tömbben
            // táblázat létrehozása a megadott oszlopszélességekkel
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100); //100% széles legyen, töltse ki a teret
            // fejléc
            PdfPCell cell = new PdfPCell(new Phrase("Kontakt adatok"));
            cell.setBackgroundColor(GrayColor.GRAYWHITE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(4); // hány oszlop széles legyen (mind a négy oszlop felett terjedjen ki
            table.addCell(cell);
            document.add(table);
            
            //ide jön a táblázat                                                   
            
            // hozzáadunk egy darab valamit (aláírást)
            Chunk postScript = new Chunk("\n\n\n A ContactsData alkalmazás által létrehozva");
            Paragraph base = new Paragraph(postScript);
            document.add(base);
            
        } catch (DocumentException dex) {
            dex.printStackTrace();
        } catch (FileNotFoundException fex){
            fex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        document.close();
    }
    
}
