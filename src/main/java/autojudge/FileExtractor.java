package autojudge;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileExtractor
{
    public static boolean  extractZipFile(String zipFilePath, String outputDir)
    {
        File destDir = new File(outputDir);
        if(!destDir.exists())
        {
            destDir.mkdir();
        }

        try(ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))){
            ZipEntry entry = zipIn.getNextEntry();
            while(entry != null){
                File filePath = new File(outputDir,entry.getName());

                if(!entry.isDirectory()){
                    new File(filePath.getParent()).mkdirs();
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))){
                        byte[] buffer = new byte[4096];
                        int read;
                        while((read = zipIn.read(buffer)) != -1){
                            bos.write(buffer,0,read);
                        }
                    }
                }

                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}