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
            
            //2. első elem hozzáadása pl. céges logo
            //az itextpdf-ből importált Image osztály-ba!!! beletesszük a kép file-unkat
            Image image1 = null;
            try{
                image1 = Image.getInstance(getClass().getResource("/lake_and_house.jpg"));
            } catch (Exception ex){
                System.out.println("Valami baj van a kép file-lal!");
                System.out.println(ex);
            }
            
            //átméretezés
            image1.scaleToFit(200, 150);
            //beállítjuk a helyét
            image1.setAbsolutePosition(250f, 750f);
            //hozzáadjuk a dok-hoz
            document.add(image1);
            
            //3. hozzáadhatunk egy bekezdést: benne üres sorok + esetleg egy szöveg, betűtípus
            document.add(new Paragraph("\n\n\n\n", FontFactory.getFont("betutipus", BaseFont.IDENTITY_H, BaseFont.EMBEDDED)));
            
            //T Á B L Á Z A T   Ö S S Z E Á L L Í T Á S A
            
            float[] columnWidths = {2, 3, 3, 5, 3}; //relatív oszlopszélességek megadása tömbben
            // táblázat létrehozása a megadott oszlopszélességekkel
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100); //100% széles legyen, töltse ki a teret
            
            // címsor elkészítése
            PdfPCell cell = new PdfPCell(new Phrase("Kontakt adatok"));
            cell.setBackgroundColor(GrayColor.GRAYWHITE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(5); // hány oszlop széles legyen (mind az öt oszlop felett terjedjen ki)
            table.addCell(cell);
            
            // a fejléc elkészítése
            table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell("Azonosító");
            table.addCell("Vezetéknév");
            table.addCell("Keresztnév");
            table.addCell("E-mail cím");
            table.addCell("Telefonszám");
            //beállítjuk hány fejlécsor legyen
            table.setHeaderRows(1);
            
            // a táblázat törzsének elkészítése
            // innentől beállítom, hogy a hozzáadandó cellák (az adatokkal) milyenek legyenek
            table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
            // kell egy ciklus, ami a sorok cellaadatait rakja össze, annyiszor, ahány adatsor van
            for (int i = 0; i < data.size(); i++) {
                int id = i + 1;
                table.addCell("" + id);
                table.addCell(data.get(i).getLastName());
                table.addCell(data.get(i).getFirstName());
                table.addCell(data.get(i).getEmail());
                table.addCell(data.get(i).getPhoneNum());
            }
            
            // a táblázatot hozzáadjuk a dokumentumhoz
            document.add(table);
            
            // hozzáadunk egy darab valamit (aláírást)
            Chunk postScript = new Chunk("\n\n\n A ContactsData alkalmazás által létrehozva");
            Paragraph base = new Paragraph(postScript);
            document.add(base);
            
        } catch (DocumentException dex) {
            dex.printStackTrace();
        } catch (FileNotFoundException fex){
            fex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        document.close();
    }
    
}
