package automation.library.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExcelHelper {

    static Logger log = LogManager.getLogger(ExcelHelper.class);
    /**
     * reads data from a given sheet in excel returning all rows and columns matching the supplied recordset (col A). if
     * no record set not provided then complete excel sheet data is returned
     */
    public static ArrayList<ArrayList<Object>> getDataAsArrayList(String filepath, String worksheet, String... recordSet)  {

        ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
        try {
            FileInputStream file = new FileInputStream(filepath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet(worksheet);
            int maxDataCount = 0;
            // Iterate through each rows one by one

            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                //Skip the first row beacause it will be header and also get column count
                if (row.getRowNum() == 0) {
                    maxDataCount = row.getLastCellNum();
                    //    continue;
                }

                //if row is empty then break the loop,do not go further
                if (isRowEmpty(row)) {
                    break;
                }

                ArrayList<Object> singleRows = new ArrayList<Object>();

                // For each row, iterate through all the columns

                for (int c = 0; c < maxDataCount; c++) {

                    Cell cell = row.getCell(c);

                    singleRows.add(getCellData(cell));

                }

                if (recordSet.length > 0) {
                    if(singleRows.get(0) !=null){
                        if (singleRows.get(0).toString().equalsIgnoreCase(recordSet[0])) {
                            data.add(singleRows);
                        }
                    }
                } else {
                    data.add(singleRows);
                }
            }

            file.close();

        } catch (Exception e) {
            log.error("exception processing file", e);
            e.printStackTrace();
        }
        return data;
    }

    /**
     * reads data from a given sheet in excel returning all rows and columns in hashed map. For respective row cell data,
     * first row respective column will be key. all row data will stored as value to unique key using col A and first column
     * of first row - [Cell AI value].[cell A[n] value]
     * Column A value need to be unique else will be overwritten, if no value in column A then it it will have null as key
     * @param filepath given path
     * @param worksheet given sheet
     * @return map list of given data sheet input
     */
    public static Map<String, LinkedHashMap<String, Object>> getDataAsMap(String filepath, String worksheet) {

        Map<String, LinkedHashMap<String, Object>> dataMap = new LinkedHashMap<String, LinkedHashMap<String, Object>>();
        LinkedHashMap<String, Object> mapTemp = null;

        String dataSetKey = null;
        Row headerRow = null;
        Object key = null;
        Object value = null;

        try {
            FileInputStream file = new FileInputStream(filepath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet(worksheet);

            int lastRow = sheet.getLastRowNum();


            int currentRowNum = 0;
            while (currentRowNum < lastRow) {
                if (isRowEmpty(sheet.getRow(currentRowNum))) {
                    currentRowNum++;
                    continue;
                }

                headerRow = sheet.getRow(currentRowNum);

                for (int j = currentRowNum + 1; j <= lastRow; j++) {
                    if (isRowEmpty(sheet.getRow(j))) {
                        currentRowNum = j + 1;
                        break;
                    }

                    dataSetKey = getCellData(sheet.getRow(headerRow.getRowNum()).getCell(0)) + "." + getCellData(sheet.getRow(sheet.getRow(j).getRowNum()).getCell(0));
                    mapTemp = new LinkedHashMap<String, Object>();
                    for (int cellCounter = 1; cellCounter < sheet.getRow(j).getLastCellNum(); cellCounter++) {

                        key = getCellData(sheet.getRow(headerRow.getRowNum()).getCell(cellCounter));
                        value = getCellData(sheet.getRow(j).getCell(cellCounter));

                        if (value != null) {
                            mapTemp.put(key.toString(), value);

                        }
                    }

                    currentRowNum++;
                    dataMap.put(dataSetKey, mapTemp);
                }
            }
            workbook.close();
            file.close();
        } catch (Exception e) {
            log.error("exception processing file", e);
            e.printStackTrace();
        }
        return dataMap;
    }

    private static Object getCellData(Cell cell) {
        Object obj = null;

        if (cell == null) return null;

        switch (cell.getCellType()) {
            case NUMERIC:
//            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    obj = (new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue()));
                } else {
                    obj = (cell.getNumericCellValue());
                }
                break;
            case STRING:
//            case Cell.CELL_TYPE_STRING:
                obj = (cell.getStringCellValue());
                break;
            case BLANK:
//            case Cell.CELL_TYPE_BLANK:
                obj = null;
                break;
            default:
                obj = (cell.getStringCellValue());
        }
        return obj;
    }

    private static boolean isRowEmpty(Row row) {

        if (row == null) return true;

        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
