package automation.library.common;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * utility class based of apache pdf extension
 */
public class PDFHelper {

    /**
     * return the PDF document for the input PFG file path
     */
    public static PDDocument getDoc(String path) {
        PDDocument doc = null;
        try {
            doc = PDDocument.load(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static PDDocument getDoc(byte[] contents) {
        PDDocument doc = null;
        try {
            doc = PDDocument.load(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * return PDFpage for the given PDF document input
     * @param doc input PDFDocument
     * @param num page number, index starts from 0
     */
    public static PDPage getPDFPage(PDDocument doc, int num) {
        return doc.getPage(num);
    }


    /**
     *  return PageTree og input PDF document object
     */
    public static PDPageTree getPDFPages(PDDocument doc) {
        return doc.getPages();
    }

    /**
     * returns the text of entire PDF document
     */
    public static String getPDFText(PDDocument doc) {
        String extractText = null;
        try {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            extractText = pdfStripper.getText(doc);
        } catch (IOException e) {
            e.printStackTrace();
            extractText = "error: extract not possible";
        }
        return extractText;
    }

    /**
     * return text of give pdf page range
     * @param startPage start page of PDF document (index start at 1)
     * @param endPage end page of PDF document
     */
    public static String getPDFText(PDDocument doc, int startPage, int...endPage) {
        String extractText = null;
        try {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setStartPage(startPage);
            if (endPage.length>0) {
                pdfStripper.setEndPage(endPage[0]);
            }else {
                pdfStripper.setEndPage(startPage);
            }
            extractText = pdfStripper.getText(doc);
        } catch (IOException e) {
            e.printStackTrace();
            extractText = "error: extract not possible";
        }

        return extractText;
    }

    /**
     * return array of string , separated by token provided
     * @param doc input PDF Document object
     * @param token separater/delimiter
     * @param startPage start page
     * @param endPage end page
     */
    public static String[] getPDFText(PDDocument doc, String token, int startPage, int... endPage) {
        String extractText = null;

        try {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setSortByPosition( true );
            pdfStripper.setStartPage(startPage);
            if (endPage.length > 0) {
                pdfStripper.setEndPage(endPage[0]);
            } else {
                pdfStripper.setEndPage(startPage);
            }

            extractText = pdfStripper.getText(doc);
        } catch (IOException var6) {
            var6.printStackTrace();
            extractText = "error: extract not possible";
        }

        return extractText.split(token);
    }

    /**
     * read the text form PDF document for given page number and area.
     * @param doc input PDF Document object
     * @param pageNum page number of PDF document (index start at 1)
     * @param rectangle area from where text has to be extracted
     */
    public static String getPDFText(PDDocument doc, int pageNum, Rectangle rectangle) {
        String extractText = null;
        try {
            PDPage page = doc.getPage(pageNum-1);
            PDFTextStripperByArea pdfStripper = new PDFTextStripperByArea();
            pdfStripper.setSortByPosition( true );
            pdfStripper.addRegion( "area", rectangle );
            pdfStripper.extractRegions( page );
            extractText = pdfStripper.getTextForRegion("area");
        } catch (IOException e) {
            e.printStackTrace();
            extractText = "error: extract not possible";
        }

        return extractText;
    }

    /**
     * helper method using java util scanner
     */
    public static String scanFile(String pathname) throws IOException {

        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int)file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");

        try {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + lineSeparator);
            }
            return fileContents.toString().trim();
        } finally {
            scanner.close();
        }
    }
}
