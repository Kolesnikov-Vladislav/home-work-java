import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import data.DataForJson;
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


public class CheckFilesTests extends Exception{
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
    private final ClassLoader cl = CheckFilesTests.class.getClassLoader();

    @Test
    void checkJsonStructure()  throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = cl.getResourceAsStream("JSONFile.json");
             InputStreamReader isr = new InputStreamReader(is)) {
            DataForJson dataForJson = mapper.readValue(isr, DataForJson.class);
            Assertions.assertEquals("Иванов Иван", dataForJson.fio, "ERROR FIO CLIENT");
            Assertions.assertEquals(1, dataForJson.gender, "ERROR MALE CLIENT");
            Assertions.assertEquals("123124125126", dataForJson.inn, "ERROR INN CLIENT");
            Assertions.assertEquals("1970-01-01T00:00:00.0000000+00:00", dataForJson.birthDate, "ERROR BIRTHDATE CLIENT");
            Assertions.assertEquals("88005553535", dataForJson.mainPhone, "ERROR MAINPHONE CLIENT");
            Assertions.assertEquals(null, dataForJson.additionalPhone, "ERROR ADDITIONALPHONE CLIENT");
            Assertions.assertEquals("ivanov@example.com", dataForJson.email, "ERROR EMAIL CLIENT");
            Assertions.assertEquals("Оставляет чаевые", dataForJson.comment, "ERROR COMMENT CLIENT");
            Assertions.assertEquals("4608999777", dataForJson.passport, "ERROR PASSPORT CLIENT");
            Assertions.assertEquals(null, dataForJson.docType, "ERROR DOCTYPE CLIENT");
            Assertions.assertEquals(null, dataForJson.nationality, "ERROR NATYONALITY CLIENT");
            Assertions.assertEquals(null, dataForJson.address, "ERROR ADRESS CLIENT");
        }
    }
    void checkFillingXlsxFile() throws Exception{
        try (InputStream is = cl.getResourceAsStream("AllFiles.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".xlsx")) {
                    XLS xls = new XLS(zis);
                    Assertions.assertTrue(xls.excel.getSheetAt(0).getRow(0).getCell(5).
                                    getStringCellValue().startsWith("xls"),
                            "oops xlsx pars error");
                }
            }
        }
    }

    @Test
    void checkFillingPdfFile() throws Exception{
        try (InputStream is = cl.getResourceAsStream("AllFiles.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".pdf")) {
                    PDF pdf = new PDF(zis);
                    Assertions.assertEquals("Typora", pdf.creator, "oops pdf pars error");
                }
            }
        }
    }

    @Test
    void checkFillingCsvFile() throws Exception{
        try (InputStream is = cl.getResourceAsStream("AllFiles.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            CSVReader csv = new CSVReader(new InputStreamReader(zis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".csv")) {
                    List<String[]> content = csv.readAll();
                    Assertions.assertArrayEquals(new String[]{"csv"}, content.get(4),
                            "csv parsed failed");
                }
            }
        }
    }

}
