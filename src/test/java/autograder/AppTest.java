package autograder;

import java.util.List;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import autograder.models.TestResult;
import autograder.utils.FileExtractor;
import autograder.utils.PDFGenerator;

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
        boolean value = PDFGenerator.generatePDF("src/main/java/autograder/resources", "Header", "Content", List.of());
        assertEquals("This method should return true if  pdf was generated", true,value);
    }
   
    @Test
    public void  testFileCompiler(){
        boolean value = false;
        try{
            value = FileExtractor.extractZip("src/main/java/autograder/resources/Matthew_Roodal_816024135_A1.zip","src/main/java/autograder/resources/");
        }
        catch(Exception e){
            System.out.println("Error executing File extractor");
        }
        assertEquals("This method should return true if  file was extracted", true,value);
    }
}

