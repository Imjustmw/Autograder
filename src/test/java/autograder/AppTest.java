package autograder;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import autograder.models.TestResult;
import autograder.utils.*;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testTestResult()
    {
        TestResult result = new TestResult("JUnit Test");
        result.addScore(5);
        result.addScore(7);

        assertEquals("The addScore method should return 12", 12, result.getScore());
    }

    @Test
    public void testPDFGenerator(){
        boolean value = PDFGenerator.generatePDF("src\\\\main\\\\java\\\\autograder\\\\resources", "Header", "Content", null);
        assertEquals("This method should return true if  pdf was generated", true,value);
    }
   
    @Test
    public void  testFileCompiler(){
        boolean value = FileCompiler.compileJavaFiles("src\\main\\java\\autograder\\resources\\Matthew_Roodal_816024135_A1.zip");
        assertEquals("This method should return true if  file was extracted", true,value);
    }

}

