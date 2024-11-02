package autojudge;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        // boolean isExtracted = FileExtractor.extractZipFile("/mnt/c/Users/Keith/documents/oop2_project/Object Oriented Programming Assignment 1 (816032626).zip", "/mnt/c/Users/Keith/documents/oop2_project/oop2");
        PDFCreator pdfdoc = new PDFCreator("/mnt/c/Users/Keith/documents/oop2_project/document.pdf");
        pdfdoc.CreatePDF();
        pdfdoc.AddTitle("Black People");
        pdfdoc.WriteParagraph("Daddy I love dad");
        pdfdoc.Compile();
      
    }
}
