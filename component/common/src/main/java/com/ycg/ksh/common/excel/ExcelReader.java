/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-18 14:32:32
 */
package com.ycg.ksh.common.excel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;

import com.ycg.ksh.common.util.DateUtils;
import com.ycg.ksh.common.util.StringUtils;

/**
 * excel文件读取操作工具，基于poi之上
 * <p>
 * 
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2017-12-18 14:32:32
 */
public class ExcelReader implements java.lang.AutoCloseable {
    
    private static final String EMPTY_VALUE = "";
    
    private int                 startIndex;
    private int                 maxCellIndex;
    private Workbook            workbook;
    private Sheet               sheet;
    private InputStream         inputStream;
    
    public ExcelReader(InputStream is) throws Exception {
        inputStream = is;
        workbook = WorkbookFactory.create(inputStream);
        sheet = workbook.getSheetAt(0);
    }
    
    public void close() throws Exception {
        if(workbook != null) {
            workbook.close();
        }
        if(inputStream != null) {
            inputStream.close();
        }
    }
    /**
     * Excel读取 操作
     */
    public List<Object[]> readExcel() throws Exception {
        return readExcel(new ArrayList<Object[]>(sheet.getPhysicalNumberOfRows() - startIndex));
    }
    
    /**
     * Excel读取 操作
     */
    public List<Object[]> readExcel(List<Object[]> collection) throws Exception {
        /** 得到Excel的行数 */
        int totalRows = sheet.getLastRowNum() + 1;
        /** 循环Excel的行 */
        for (int rIndex = startIndex; rIndex < totalRows; rIndex++) {
            Row row = sheet.getRow(rIndex);
            if (row == null) {
                continue;
            }
            /** 得到该行的列数 */
            Object[] objects = new Object[maxCellIndex + 1];
            /** 循环Excel的列 */
            for (int cIndex = 0; cIndex < objects.length; cIndex++) {
                Cell cell = row.getCell(cIndex);
                if (null != cell) {
                    switch (cell.getCellTypeEnum()) {
                        case NUMERIC: // 数字
                            if (DateUtil.isCellDateFormatted(cell)) {
                                objects[cIndex] = cell.getDateCellValue();
                            } else {
                        		objects[cIndex] = NumberToTextConverter.toText(cell.getNumericCellValue());
                            }
                            break;
                        case STRING: // 字符串
                        	objects[cIndex] = cell.getStringCellValue();
                        	if (DateUtil.isCellDateFormatted(cell)) {
                                objects[cIndex] = DateUtils.formatDate(cell.getDateCellValue());
                            } 
                            break;
                        case BOOLEAN: // Boolean
                            objects[cIndex] = cell.getBooleanCellValue();
                            break;
                        case FORMULA: // 公式
                            objects[cIndex] = cell.getCellFormula();
                            break;
                        default:
                            objects[cIndex] = EMPTY_VALUE;
                            break;
                    }
                }
            }
            if(validate(objects)){
                collection.add(objects);
            }
        }
        return collection;
    }


    private boolean validate(Object[] objects){
        if(objects == null || objects.length <= 0){
            return false;
        }

        for (Object object : objects) {
            if(object != null && StringUtils.isNotBlank(object.toString())){
                return true;
            }
        }
        return false;
    }

    
    /**
     * getter method for startIndex
     * 
     * @return the startIndex
     */
    public int getStartIndex() {
        return startIndex;
    }
    
    /**
     * setter method for startIndex
     * 
     * @param startIndex the startIndex to set
     */
    public void setIndex(int startIndex, int maxCellIndex) {
        this.startIndex = startIndex;
        this.maxCellIndex = maxCellIndex;
    }
}
