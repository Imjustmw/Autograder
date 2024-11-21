package autograder.utils;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import autograder.models.TestResult;

import java.io.FileOutputStream;
import java.util.List;

public class PDFGenerator {
    public static boolean generatePDF(String filePath, String headerContent, String content, List<TestResult> results) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath + "/results.pdf"));
            document.open();

            // Add Logo
            String imagePath = "src/main/java/autograder/resources/images/UWI-Logo.png";
            Image image = Image.getInstance(imagePath);
            image.setAlignment(Image.ALIGN_CENTER);
            image.scaleToFit(150, 150);
            image.setSpacingBefore(28f);
            image.setSpacingAfter(36f);
            document.add(image);

            // Create fonts
            String fontPath = "src/main/java/autograder/resources/fonts/Now.ttf";
            BaseFont nowFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font boldFont = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.COURIER, 10, Font.NORMAL);
            Font headerFont = new Font(nowFont, 13, Font.NORMAL);
            Font titleFont = new Font(nowFont, 15, Font.BOLD);
            Font cellFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            // Add title 
            Paragraph header = new Paragraph(headerContent, headerFont);
            header.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(header);

           
            Paragraph title = new Paragraph(content, titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(32f);
            document.add(title);

            // Add Body
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(60);

            // Add Headers
            
            PdfPCell cell1 = new PdfPCell(new Paragraph("CRITERIA", cellFont));
            PdfPCell cell2 = new PdfPCell(new Paragraph("MARK", cellFont));
            cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(cell1);
            table.addCell(cell2);

            // Add rows
            Paragraph feedbackParagraph = new Paragraph();

            for (TestResult result: results) {
                PdfPCell testNameCell = new PdfPCell(new Paragraph(result.getTestName(), cellFont));
                PdfPCell scoreCell = new PdfPCell(new Paragraph("" + result.getScore(), cellFont));
                scoreCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(testNameCell);
                table.addCell(scoreCell); // casting int to String
                if (!result.getFeedback().isEmpty()) {
                    feedbackParagraph.add(new Chunk(result.getTestName().toUpperCase() + ":\n", boldFont));
                    feedbackParagraph.add(new Chunk(result.getFeedback(), normalFont));
                }
            }

            table.setHorizontalAlignment(Paragraph.ALIGN_CENTER);
            table.setSpacingAfter(32f);
            document.add(table);

            // Add feedback
            Paragraph feedback = new Paragraph("Feedback", titleFont);
            feedback.setAlignment(Paragraph.ALIGN_CENTER);
            feedback.setSpacingAfter(18f);
            document.add(feedback);
            
            
            feedbackParagraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(feedbackParagraph);

            document.close();
        } catch (Exception e) {
            System.err.println("Error generating PDF: " + e.getMessage());
            return false;
        }
        return true;
    }
}