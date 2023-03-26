import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVReader;
import com.google.common.io.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class checkFilesTests extends Exception{
    @Test
    void watchFileTypeZip() throws Exception{

        File zipArchive = new File("src/test/resources/AllFiles.zip");
        try (InputStream is = new FileInputStream(zipArchive)) {
            byte[] zipByte = is.readAllBytes();
            String fillingZip = new String(zipByte, StandardCharsets.UTF_8);
            Assertions.assertTrue(fillingZip.contains("Google_table_CSV.csv")
                                        && fillingZip.contains("Google_table.xlsx")
                                        && fillingZip.contains("bonjur.pdf"));
        }
    }
    private final ClassLoader cl = checkFilesTests.class.getClassLoader();
    @Test
    void checkFillingAllFiles() throws Exception{
        try (InputStream is = cl.getResourceAsStream("AllFiles.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                switch (Files.getFileExtension(entry.getName())) {
                    case "xlsx" -> {
                        XLS xls = new XLS(zis);
                        Assertions.assertTrue(xls.excel.getSheetAt(0).getRow(0).getCell(5).
                                        getStringCellValue().startsWith("xls"),
                                         "oops xlsx pars error");
                    }
                    case "pdf" -> {
                        PDF pdf = new PDF(zis);
                        Assertions.assertEquals("Typora", pdf.creator , "oops pdf pars error");
                    }
                    case "csv" -> {
                        CSVReader csv = new CSVReader(new InputStreamReader(zis));
                        List<String[]> content = csv.readAll();
                        Assertions.assertArrayEquals(new String[]{"csv"}, content.get(4),
                                "csv parsed failed");
                    }
                }
            }
        }
    }

    @Test
    void checkJsonStructure()  throws Exception {
        Gson body = new Gson();
        try (InputStream is = cl.getResourceAsStream("JSONFile.json");
             InputStreamReader isr = new InputStreamReader(is)) {
            JsonObject jsonObject = body.fromJson(isr, JsonObject.class);
            Assertions.assertEquals("Иванов Иван", jsonObject.get("fio").getAsString());
        }
    }
}
