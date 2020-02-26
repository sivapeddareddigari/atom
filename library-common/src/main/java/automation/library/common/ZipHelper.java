package automation.library.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * utility class to zip and unzip the files or directories
 */
public class ZipHelper {

    /**
     * To zip a set of files or directories to an archive:
     */
    public static void zipit(String sourcePath, String targetPath) throws IOException {

        FileOutputStream fos = new FileOutputStream(targetPath);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        File fileToZip = new File(sourcePath);

        zip(fileToZip, fileToZip.getName(), zipOut);
        zipOut.close();
        fos.close();
    }

    /**
     * To unzip an archive to a target directory
     */
    public static void unzipit(String sourcePath, String targetPath) throws IOException {

        File dir = new File(targetPath);
        if (!dir.exists()) dir.mkdirs();

        FileInputStream fis = new FileInputStream(sourcePath);
        ZipInputStream zipIn = new ZipInputStream(fis);

        unzip(targetPath, zipIn);
        zipIn.close();
        fis.close();
    }

    private static void zip(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {

        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            File[] files = fileToZip.listFiles();
            for (File file : files) {
                zip(file, fileName + "/" + file.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    private static void unzip(String targetPath, ZipInputStream zipIn) {

        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            ZipEntry ze = zipIn.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(targetPath + File.separator + fileName);
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zipIn.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zipIn.closeEntry();
                ze = zipIn.getNextEntry();
            }
            //close last ZipEntry
            zipIn.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}



