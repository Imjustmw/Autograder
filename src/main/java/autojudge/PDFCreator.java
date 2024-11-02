package autojudge;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

public class PDFCreator {

    String pdfPath;
    Document document;
    boolean isCreated = false;

    public PDFCreator(String path) {
        pdfPath = path;
    }

    public PDFCreator() {
        this("output.pdf");
    }

    public boolean CreatePDF() {
        try {
            // Initialize PDF writer
            PdfWriter writer = new PdfWriter(pdfPath);

            // Initialize PDF document
            PdfDocument pdf = new PdfDocument(writer);

            // Initialize document
            document = new Document(pdf);

        } 
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
            return false;
        }
        isCreated = true;
        return true;
    }

    public boolean  WriteParagraph(String info)
    {
        if(!isCreated)
            return false;
        try{
            document.add(new Paragraph(info));
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return  true;
    }

    public boolean AddTitle(String title)
    {
        if(!isCreated)
            return false;
        try {
               // Add a title with custom styling
                    Text titleText = new Text(title)
                    .setFontSize(20)                    // Set font size
                    .setBold()                          // Make it bold
                    .setFontColor(ColorConstants.BLACK); // Set color (optional)
            Paragraph p_title = new Paragraph(titleText)
                    .setTextAlignment(TextAlignment.CENTER); // Center-align the title
            document.add(p_title);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean Compile()
    {
        if(!isCreated)
            return false;
        try {
            document.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
